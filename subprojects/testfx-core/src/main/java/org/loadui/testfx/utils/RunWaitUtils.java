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

    private final static long CONDITION_SLEEP_IN_MILLIS = 10;

    private final static long SEMAPHORE_SLEEP_IN_MILLIS = 10;

    private final static int SEMAPHORE_LOOPS_COUNT = 5;

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    // RUN METHODS.

    /**
     * Runs the given {@link Runnable} on a new {@link Thread} and returns a {@link Future} that
     * is set on finish or error.
     *
     * @param runnable the runnable
     * @return a future
     */
    public static Future<Void> runOutside(Runnable runnable) {
        Callable<Void> callable = Executors.callable(runnable, null);
        return callOutside(callable);
    }

    /**
     * Runs the given {@link Runnable} on the JavaFX Application Thread at some unspecified time
     * in the future and returns a {@link Future} that is set on finish or error.
     *
     * @param runnable the runnable
     * @return a future
     */
    public static Future<Void> runLater(Runnable runnable) {
        Callable<Void> callable = Executors.callable(runnable, null);
        return callLater(callable);
    }

    /**
     * Runs the given {@link Runnable} on a new {@link Thread} and waits for it {@code long}
     * milliseconds to finish, otherwise times out with {@link TimeoutException}.
     *
     * @param millis the milliseconds
     * @param runnable the runnable
     * @throws TimeoutException
     */
    public static void runOutsideAndWait(long millis,
                                         Runnable runnable) throws TimeoutException {
        Future<Void> future = runOutside(runnable);
        waitFor(millis, MILLISECONDS, future);
    }

    /**
     * Runs the given {@link Runnable} on the JavaFX Application Thread at some unspecified time
     * in the future and waits for it {@code long} milliseconds to finish, otherwise times out with
     * {@link TimeoutException}.
     *
     * @param millis the milliseconds
     * @param runnable the runnable
     * @throws TimeoutException
     */
    public static void runLaterAndWait(long millis,
                                       Runnable runnable) throws TimeoutException {
        Future<Void> future = runLater(runnable);
        waitFor(millis, MILLISECONDS, future);
    }

    // CALL METHODS.

    /**
     * Calls the given {@link Callable} on a new {@link Thread} and returns a {@link Future} that
     * is set on finish or error.
     *
     * @param callable the callable
     * @param <T> the callable type
     * @return a future
     */
    public static <T> Future<T> callOutside(Callable<T> callable) {
        SettableFuture<T> future = SettableFuture.create();
        runInThread(() -> callCallableAndSetFuture(callable, future));
        return future;
    }

    /**
     * Calls the given {@link Callable} on the JavaFX Application Thread at some unspecified time
     * in the future and returns a {@link Future} that is set on finish or error.
     *
     * @param callable the callable
     * @param <T> the callable type
     * @return a future
     */
    public static <T> Future<T> callLater(Callable<T> callable) {
        SettableFuture<T> future = SettableFuture.create();
        runInFxThread(() -> callCallableAndSetFuture(callable, future));
        return future;
    }

    /**
     * Calls the given {@link Callable} on a new {@link Thread}, waits for it {@code long}
     * milliseconds to finish and returns {@code T}, otherwise times out with
     * {@link TimeoutException}.
     *
     * @param millis the milliseconds
     * @param callable the callable
     * @param <T> the callable type
     * @return a result
     * @throws TimeoutException
     */
    public static <T> T callOutsideAndWait(long millis,
                                           Callable<T> callable) throws TimeoutException {
        Future<T> future = callOutside(callable);
        return waitFor(millis, MILLISECONDS, future);
    }

    /**
     * Calls the given {@link Callable} on the JavaFX Application Thread at some unspecified time
     * in the future, waits for it {@code long} milliseconds to finish and returns {@code T},
     * otherwise times out with {@link TimeoutException}.
     *
     * @param millis the milliseconds
     * @param callable the callable
     * @param <T> the callable type
     * @return a result
     * @throws TimeoutException
     */
    public static <T> T callLaterAndWait(long millis,
                                         Callable<T> callable) throws TimeoutException {
        Future<T> future = callLater(callable);
        return waitFor(millis, MILLISECONDS, future);
    }

    // WAIT METHODS.

    /**
     * Waits for given {@link Future} to be set (push) and returns {@code T}, otherwise times out
     * with {@link TimeoutException}.
     *
     * @param timeout the timeout
     * @param timeUnit the time unit
     * @param future the future
     * @param <T> the future type
     * @return a result
     * @throws TimeoutException
     */
    public static <T> T waitFor(long timeout,
                                TimeUnit timeUnit,
                                Future<T> future) throws TimeoutException {
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

    /**
     * Waits for given condition {@link Callable} to return (pull) {@code true}, otherwise times
     * out with {@link TimeoutException}.
     *
     * @param timeout the timeout
     * @param timeUnit the time unit
     * @param condition the condition
     * @throws TimeoutException
     */
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

    /**
     * Waits for given observable {@link ObservableBooleanValue} to return (push) {@code true},
     * otherwise times out with {@link TimeoutException}.
     *
     * @param timeout the timeout
     * @param timeUnit the time unit
     * @param booleanValue the observable
     * @throws TimeoutException
     */
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

    /**
     * Waits for the event queue of JavaFX Application Thread to be completed, as well as any new
     * events triggered in it.
     */
    public static void waitForFxEvents() {
        waitForFxEvents(SEMAPHORE_LOOPS_COUNT);
    }

    /**
     * Waits the given {@link int} attempts for the event queue of JavaFX Application Thread to be
     * completed, as well as any new events triggered on it.
     *
     * @param attemptsCount the attempts
     */
    public static void waitForFxEvents(int attemptsCount) {
        for (int attempt = 0; attempt < attemptsCount; attempt++) {
            waitForSemaphoreInFxThread();
            sleep(SEMAPHORE_SLEEP_IN_MILLIS, MILLISECONDS);
        }
    }

    // SLEEP METHODS.

    /**
     * Sleeps the given duration.
     *
     * @param duration the duration
     * @param timeUnit the time unit
     */
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
