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
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.testfx.util.WaitForAsyncUtils;

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
        try {
            WaitForAsyncUtils.waitFor(timeoutInSeconds, TimeUnit.SECONDS, condition);
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
        WaitForAsyncUtils.waitForFxEvents();
    }

    /**
     * Runs the given Callable in the JavaFX thread, waiting for it to complete before returning.
     * Also attempts to wait for any other JavaFX events that may have been queued in the Callable
     * to complete. If any Exception is thrown during execution of the Callable, that exception
     * will be re-thrown from invokeAndWait.
     *
     * @param task
     * @param timeoutInSeconds
     * @throws Exception
     */
    public static void invokeAndWait(final Callable<?> task,
                                     int timeoutInSeconds) throws Exception {
        Future<?> future = WaitForAsyncUtils.asyncFx(task);
        WaitForAsyncUtils.waitFor(timeoutInSeconds, TimeUnit.SECONDS, future);
        WaitForAsyncUtils.waitForFxEvents();
    }

    /**
     * @param task
     * @param timeoutInSeconds
     * @throws Exception
     * @see FXTestUtils#invokeAndWait(Callable, int)
     */
    public static void invokeAndWait(final Runnable task,
                                     int timeoutInSeconds) throws Exception {
        invokeAndWait(Executors.callable(task), timeoutInSeconds);
    }

}
