/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.loadui.testfx.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.application.Platform;
import javafx.beans.value.ObservableBooleanValue;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.SettableFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class InvokeWaitUtils {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    public final static long CONDITION_SLEEP_IN_MILLIS = 10;
    public final static long SEMAPHORE_SLEEP_IN_MILLIS = 10;
    public final static int SEMAPHORE_LOOPS_COUNT = 5;

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static Future invokeInThread(Callable<Void> callable) {
        SettableFuture<Void> future = SettableFuture.create();
        runInThread(() -> callCallableAndSetFuture(callable, future));
        return future;
    }

    public static Future invokeInThread(Runnable runnable) {
        Callable<Void> callable = Executors.callable(runnable, null);
        return invokeInThread(callable);
    }

    // Run on the JavaFX Application Thread at some unspecified time in the future.
    public static Future invokeInApplicationThread(Callable<Void> callable) {
        SettableFuture<Void> future = SettableFuture.create();
        runInApplicationThread(() -> callCallableAndSetFuture(callable, future));
        return future;
    }

    public static Future invokeInApplicationThread(Runnable runnable) {
        Callable<Void> callable = Executors.callable(runnable, null);
        return invokeInApplicationThread(callable);
    }

    public static void waitFor(Future future,
                               long timeout,
                               TimeUnit timeUnit) throws TimeoutException {
        try {
            future.get(timeout, timeUnit);
        }
        catch (InterruptedException ignore) {}
        catch (ExecutionException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void waitFor(Callable<Boolean> condition,
                               long timeout,
                               TimeUnit timeUnit) throws TimeoutException {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        while (true) {
            if (callCondition(condition)) {
                break;
            }
            sleep(CONDITION_SLEEP_IN_MILLIS, MILLISECONDS);
            if (stopwatch.elapsed(timeUnit) > timeout) {
                throw new TimeoutException();
            }
        }
    }

    public static void waitFor(ObservableBooleanValue booleanValue,
                               long timeout,
                               TimeUnit timeUnit) throws TimeoutException {
        if (booleanValue.get()) { return; }
        SettableFuture<Void> future = SettableFuture.create();
        booleanValue.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                future.set(null);
            }
        });
        waitFor(future, timeout, timeUnit);
    }

    public static void waitForApplicationThread() {
        waitForApplicationThread(SEMAPHORE_LOOPS_COUNT);
    }

    public static void waitForApplicationThread(int loopsCount) {
        for (int loop = 0; loop < loopsCount; loop++) {
            waitForSemaphore();
            sleep(SEMAPHORE_SLEEP_IN_MILLIS, MILLISECONDS);
        }
    }

    public static void sleep(long duration,
                             TimeUnit timeUnit) {
        try {
            Thread.sleep(timeUnit.toMillis(duration));
        }
        catch (InterruptedException ignore) {}
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static void runInThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    private static void runInApplicationThread(Runnable runnable) {
        Platform.runLater(runnable);
    }

    private static void callCallableAndSetFuture(Callable<Void> callable,
                                                 SettableFuture<Void> future) {
        try {
            callable.call();
            future.set(null);
        }
        catch (Throwable exception) {
            future.setException(exception);
        }
    }

    private static boolean callCondition(Callable<Boolean> condition) {
        try {
            return condition.call();
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void waitForSemaphore() {
        Semaphore semaphore = new Semaphore(0);
        runInApplicationThread(semaphore::release);
        try {
            semaphore.acquire();
        }
        catch (InterruptedException ignore) {}

    }

}
