/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2017 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.service.support;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import javafx.scene.Node;

import org.hamcrest.Matcher;
import org.testfx.api.annotation.Unstable;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Provides methods that will wait for a given node or value to meet a given condition by repeatedly testing that item
 * until the condition is met or the timeout is reached. When the timeout is reached, a {@link RuntimeException} is
 * thrown.
 */
@Unstable(reason = "needs more tests")
public class WaitUntilSupport {

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    /**
     * Waits until the provided node fulfills the given condition.
     *
     * @param node the node
     * @param condition the condition
     * @param timeoutInSeconds the number of seconds to wait before timing out. If the time out is reached, a
     *                         {@link RuntimeException} is thrown.
     */
    public <T extends Node> void waitUntil(final T node,
                                           final Predicate<T> condition,
                                           int timeoutInSeconds) {
        awaitCondition(() -> condition.test(node), timeoutInSeconds);
    }

    /**
     * Waits until the provided node fulfills the given condition.
     *
     * @param node the node
     * @param condition the condition
     */
    public void waitUntil(final Node node,
                          final Matcher<Object> condition,
                          int timeoutInSeconds) {
        awaitCondition(() -> condition.matches(node), timeoutInSeconds);
    }

    /**
     * Waits until the provided value fulfills the given condition.
     *
     * @param value the value
     * @param condition the condition
     * @param timeoutInSeconds the number of seconds to wait before timing out. If the time out is reached, a
     *                         {@link RuntimeException} is thrown.
     */
    public <T> void waitUntil(final T value,
                              final Matcher<? super T> condition,
                              int timeoutInSeconds) {
        awaitCondition(() -> condition.matches(value), timeoutInSeconds);
    }

    /**
     * Waits until the provided callable fulfills the given condition.
     *
     * @param callable the callable
     * @param condition the condition
     * @param timeoutInSeconds the number of seconds to wait before timing out. If the time out is reached, a
     *                         {@link RuntimeException} is thrown.
     */
    public <T> void waitUntil(final Callable<T> callable,
                              final Matcher<? super T> condition,
                              int timeoutInSeconds) {
        awaitCondition(() -> condition.matches(callable.call()), timeoutInSeconds);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private void awaitCondition(Callable<Boolean> condition, int timeoutInSeconds) {
        try {
            WaitForAsyncUtils.waitFor(timeoutInSeconds, TimeUnit.SECONDS, condition);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
