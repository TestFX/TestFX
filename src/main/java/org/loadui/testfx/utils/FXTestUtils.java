/*
 * Copyright 2013 SmartBear Software
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
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.application.Platform;

import com.google.common.util.concurrent.SettableFuture;

public class FXTestUtils {

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    private FXTestUtils() {
        throw new UnsupportedOperationException();
    }

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static void awaitCondition(Callable<Boolean> condition) {
        awaitCondition(condition, 5);
    }

    public static void awaitCondition(Callable<Boolean> condition,
                                      int timeoutInSeconds) {
        long timeout = System.currentTimeMillis() + timeoutInSeconds * 1000;
        try {
            while (!condition.call()) {
                Thread.sleep(10);
                if (System.currentTimeMillis() > timeout) {
                    throw new TimeoutException();
                }
            }
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Attempts to wait for events in the JavaFX event thread to complete, as well as any new
     * events triggered by them.
     */
    public static void awaitEvents() {
        try {
            for (int loopIndex = 0; loopIndex < 5; loopIndex++) {
                final Semaphore semaphore = new Semaphore(0);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        semaphore.release();
                    }
                });
                semaphore.acquire();
                try {
                    Thread.sleep(10);
                }
                catch (InterruptedException ignore) {}
            }
        }
        catch (Throwable exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Runs the given Callable in the JavaFX thread, waiting for it to complete before returning.
     * Also attempts to wait for any other JavaFX events that may have been queued in the Callable
     * to complete. If any Exception is thrown during execution of the Callable, that exception
     * will be re-thrown from invokeAndWait.
     *
     * @param task
     * @param timeoutInSeconds
     * @throws Throwable
     */
    public static void invokeAndWait(final Callable<?> task,
                                     int timeoutInSeconds) throws Exception {
        final SettableFuture<Void> future = SettableFuture.create();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    task.call();
                    future.set(null);
                }
                catch (Throwable e) {
                    future.setException(e);
                }
            }
        });

        try {
            future.get(timeoutInSeconds, TimeUnit.SECONDS);
            awaitEvents();
        }
        catch (ExecutionException exception) {
            if (exception.getCause() instanceof Exception) {
                throw (Exception) exception.getCause();
            }
            else {
                throw exception;
            }
        }
    }

    /**
     * @param task
     * @param timeoutInSeconds
     * @throws Throwable
     * @see FXTestUtils#invokeAndWait(Callable, int)
     */
    public static void invokeAndWait(final Runnable task,
                                     int timeoutInSeconds) throws Exception {
        invokeAndWait(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                task.run();

                return null;
            }
        }, timeoutInSeconds);
    }

}
