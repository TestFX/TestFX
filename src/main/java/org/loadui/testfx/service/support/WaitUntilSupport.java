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
package org.loadui.testfx.service.support;

import java.util.concurrent.Callable;
import javafx.scene.Node;

import com.google.common.base.Predicate;
import org.hamcrest.Matcher;
import org.loadui.testfx.utils.FXTestUtils;

public class WaitUntilSupport {

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public <T extends Node> void waitUntil(final T node, final Predicate<T> condition,
                                           int timeoutInSeconds) {
        Callable<Boolean> waitCallable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return condition.apply(node);
            }
        };
        awaitCondition(waitCallable, timeoutInSeconds);
    }

    /**
     * Waits until the provided node fulfills the given condition.
     *
     * @param node the node
     * @param condition the condition
     */
    public void waitUntil(final Node node, final Matcher<Object> condition,
                          int timeoutInSeconds) {
        Callable<Boolean> waitCallable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return condition.matches(node);
            }
        };
        awaitCondition(waitCallable, timeoutInSeconds);
    }

    public <T> void waitUntil(final T value, final Matcher<? super T> condition,
                              int timeoutInSeconds) {
        Callable<Boolean> waitCallable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return condition.matches(value);
            }
        };
        awaitCondition(waitCallable, timeoutInSeconds);
    }

    public <T> void waitUntil(final Callable<T> callable, final Matcher<? super T> condition,
                              int timeoutInSeconds) {
        Callable<Boolean> waitCallable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return condition.matches(callable.call());
            }
        };
        awaitCondition(waitCallable, timeoutInSeconds);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private void awaitCondition(Callable<Boolean> condition, int timeoutInSeconds) {
        FXTestUtils.awaitCondition(condition, timeoutInSeconds);
    }

}
