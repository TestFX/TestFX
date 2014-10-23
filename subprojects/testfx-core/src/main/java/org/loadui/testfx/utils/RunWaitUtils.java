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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.SettableFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class RunWaitUtils {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    public final static long CONDITION_SLEEP_IN_MILLIS = 10;
    public final static long SEMAPHORE_SLEEP_IN_MILLIS = 10;
    public final static int SEMAPHORE_LOOPS_COUNT = 5;

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    // RUN METHODS.

    public static Future<Void> runOutside(Runnable runnable) {
        Callable<Void> callable = Executors.callable(runnable, null);
        return callOutside(callable);
    }

    public static Future<Void> runLater(Runnable runnable) {
        Callable<Void> callable = Executors.callable(runnable, null);
        return callLater(callable);
    }

    public static void runOutsideAndWait(long millis,
                                         Runnable runnable) throws TimeoutException {
        Callable<Void> callable = Executors.callable(runnable, null);
        Future<Void> future = callOutside(callable);
        waitFor(millis, MILLISECONDS, future);
    }

    public static void runLaterAndWait(long millis,
                                       Runnable runnable) throws TimeoutException {
        Callable<Void> callable = Executors.callable(runnable, null);
        Future<Void> future = callLater(callable);
        waitFor(millis, MILLISECONDS, future);
    }

    // INVOKE-IN METHODS.

    public static <T> Future<T> callOutside(Callable<T> callable) {
        SettableFuture<T> future = SettableFuture.create();
        runInThread(() -> callCallableAndSetFuture(callable, future));
        return future;
    }

    // Run on the JavaFX Application Thread at some unspecified time in the future.
    public static <T> Future<T> callLater(Callable<T> callable) {
        SettableFuture<T> future = SettableFuture.create();
        runInFxThread(() -> callCallableAndSetFuture(callable, future));
        return future;
    }

    public static <T> T callOutsideAndWait(long millis,
                                           Callable<T> callable) throws TimeoutException {
        Future<T> future = callOutside(callable);
        return waitFor(millis, MILLISECONDS, future);
    }

    public static <T> T callLaterAndWait(long millis,
                                         Callable<T> callable) throws TimeoutException {
        Future<T> future = callLater(callable);
        return waitFor(millis, MILLISECONDS, future);
    }

    // WAIT-FOR METHODS.

    public static <V> V waitFor(long timeout,
                                TimeUnit timeUnit,
                                Future<V> future) throws TimeoutException {
        try {
            return future.get(timeout, timeUnit);
        }
        catch (InterruptedException ignore) {
            return null;
        }
        catch (ExecutionException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void waitFor(long timeout,
                               TimeUnit timeUnit,
                               Callable<Boolean> condition) throws TimeoutException {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        while (!callConditionAndReturnResult(condition)) {
            sleep(CONDITION_SLEEP_IN_MILLIS, MILLISECONDS);
            if (stopwatch.elapsed(timeUnit) > timeout) {
                throw new TimeoutException();
            }
        }
    }

    public static void waitFor(long timeout,
                               TimeUnit timeUnit,
                               ObservableBooleanValue booleanValue) throws TimeoutException {
        SettableFuture<Void> future = SettableFuture.create();
        ChangeListener<Boolean> changeListener = (observable, oldValue, newValue) -> {
            if (newValue) {
                future.set(null);
            }
        };
        booleanValue.addListener(changeListener);
        if (!booleanValue.get()) {
            waitFor(timeout, timeUnit, future);
        }
        booleanValue.removeListener(changeListener);
    }

    public static void waitForFxEvents() {
        waitForFxEvents(SEMAPHORE_LOOPS_COUNT);
    }

    // Attempts to wait for events in the JavaFX event thread to complete, as well as any new
    // events triggered by them.
    public static void waitForFxEvents(int loopsCount) {
        for (int loop = 0; loop < loopsCount; loop++) {
            waitForSemaphoreInFxThread();
            sleep(SEMAPHORE_SLEEP_IN_MILLIS, MILLISECONDS);
        }
    }

    // SLEEP METHODS.

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
        Thread thread = new Thread(runnable);
        thread.setDaemon(false);
        thread.start();
    }

    private static void runInFxThread(Runnable runnable) {
        Platform.runLater(runnable);
    }

    private static <T> void callCallableAndSetFuture(Callable<T> callable,
                                                     SettableFuture<T> future) {
        try {
            future.set(callable.call());
        }
        catch (Throwable exception) {
            future.setException(exception);
        }
    }

    private static boolean callConditionAndReturnResult(Callable<Boolean> condition) {
        try {
            return condition.call();
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void waitForSemaphoreInFxThread() {
        Semaphore semaphore = new Semaphore(0);
        runInFxThread(semaphore::release);
        try {
            semaphore.acquire();
        }
        catch (InterruptedException ignore) {}
    }

}
