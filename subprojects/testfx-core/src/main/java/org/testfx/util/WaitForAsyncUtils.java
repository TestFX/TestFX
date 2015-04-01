/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.util;

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
import org.testfx.api.annotation.Unstable;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Unstable
public class WaitForAsyncUtils {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private final static long CONDITION_SLEEP_IN_MILLIS = 10;

    private final static long SEMAPHORE_SLEEP_IN_MILLIS = 10;

    private final static int SEMAPHORE_LOOPS_COUNT = 5;

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    // ASYNC METHODS.

    /**
     * Runs the given {@link Runnable} on a new {@link Thread} and returns a {@link Future} that
     * is set on finish or error.
     *
     * @param runnable the runnable
     * @return a future
     */
    public static Future<Void> async(Runnable runnable) {
        Callable<Void> callable = Executors.callable(runnable, null);
        return async(callable);
    }

    /**
     * Calls the given {@link Callable} on a new {@link Thread} and returns a {@link Future} that
     * is set on finish or error.
     *
     * @param callable the callable
     * @param <T> the callable type
     * @return a future
     */
    public static <T> Future<T> async(Callable<T> callable) {
        SettableFuture<T> future = SettableFuture.create();
        runOnThread(() -> callCallableAndSetFuture(callable, future));
        return future;
    }

    /**
     * Runs the given {@link Runnable} on the JavaFX Application Thread at some unspecified time
     * in the future and returns a {@link Future} that is set on finish or error.
     *
     * @param runnable the runnable
     * @return a future
     */
    public static Future<Void> asyncFx(Runnable runnable) {
        Callable<Void> callable = Executors.callable(runnable, null);
        return asyncFx(callable);
    }


    /**
     * Calls the given {@link Callable} on the JavaFX Application Thread at some unspecified time
     * in the future and returns a {@link Future} that is set on finish or error.
     *
     * @param callable the callable
     * @param <T> the callable type
     * @return a future
     */
    public static <T> Future<T> asyncFx(Callable<T> callable) {
        SettableFuture<T> future = SettableFuture.create();
        runOnFxThread(() -> callCallableAndSetFuture(callable, future));
        return future;
    }

    // WAIT-FOR METHODS.

    /**
     * Waits for given {@link Future} to be set (push) and returns {@code T}.
     * @param future the future
     * @param <T> the future type
     * @return a result
     */
    public static <T> T waitFor(Future<T> future) {
        try {
            return future.get();
        }
        catch (InterruptedException ignore) {
            return null;
        }
        catch (ExecutionException exception) {
            throw new RuntimeException(exception);
        }
    }

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
                                Future<T> future)
                         throws TimeoutException {
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
     * out with {@link TimeoutException}. The condition will be evaluated at least once. The method
     * will wait for the last condition to finish after a timeout.
     *
     * @param timeout the timeout
     * @param timeUnit the time unit
     * @param condition the condition
     * @throws TimeoutException
     */
    public static void waitFor(long timeout,
                               TimeUnit timeUnit,
                               Callable<Boolean> condition)
                        throws TimeoutException {
        Stopwatch stopwatch = Stopwatch.createStarted();
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
                               ObservableBooleanValue booleanValue)
                        throws TimeoutException {
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

    // WAIT-FOR-FX-EVENTS METHODS.

    /**
     * Waits for the event queue of JavaFX Application Thread to be completed, as well as any new
     * events triggered in it.
     */
    public static void waitForFxEvents() {
        waitForFxEvents(SEMAPHORE_LOOPS_COUNT);
    }

    /**
     * Waits the given {@code int} attempts for the event queue of JavaFX Application Thread to be
     * completed, as well as any new events triggered on it.
     *
     * @param attemptsCount the attempts
     */
    public static void waitForFxEvents(int attemptsCount) {
        for (int attempt = 0; attempt < attemptsCount; attempt++) {
            blockFxThreadWithSemaphore();
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

    // WAIT-FOR-ASYNC METHODS.

    /**
     * Runs the given {@link Runnable} on a new {@link Thread} and waits for it {@code long}
     * milliseconds to finish, otherwise times out with {@link TimeoutException}.
     *
     * @param millis the milliseconds
     * @param runnable the runnable
     */
    public static void waitForAsync(long millis,
                                    Runnable runnable) {
        Future<Void> future = async(runnable);
        waitForMillis(millis, future);
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
     */
    public static <T> T waitForAsync(long millis,
                                     Callable<T> callable) {
        Future<T> future = async(callable);
        return waitForMillis(millis, future);
    }

    /**
     * Runs the given {@link Runnable} on the JavaFX Application Thread at some unspecified time
     * in the future and waits for it {@code long} milliseconds to finish, otherwise times out with
     * {@link TimeoutException}.
     *
     * @param millis the milliseconds
     * @param runnable the runnable
     */
    public static void waitForAsyncFx(long millis,
                                      Runnable runnable) {
        Future<Void> future = asyncFx(runnable);
        waitForMillis(millis, future);
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
     */
    public static <T> T waitForAsyncFx(long millis,
                                       Callable<T> callable) {
        Future<T> future = asyncFx(callable);
        return waitForMillis(millis, future);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static <T> T waitForMillis(long millis,
                                       Future<T> future) {
        try {
            return waitFor(millis, MILLISECONDS, future);
        }
        catch (TimeoutException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void runOnThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(false);
        thread.start();
    }

    private static void runOnFxThread(Runnable runnable) {
        //Platform.runLater(runnable);
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        }
        else {
            Platform.runLater(runnable);
        }
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

    private static void blockFxThreadWithSemaphore() {
        Semaphore semaphore = new Semaphore(0);
        runOnFxThread(semaphore::release);
        try {
            semaphore.acquire();
        }
        catch (InterruptedException ignore) {}
    }

}
