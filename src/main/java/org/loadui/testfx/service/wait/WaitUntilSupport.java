package org.loadui.testfx.service.wait;

import java.util.concurrent.Callable;
import javafx.scene.Node;

import com.google.common.base.Predicate;
import org.hamcrest.Matcher;
import org.loadui.testfx.utils.TestUtils;

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
        TestUtils.awaitCondition(waitCallable, timeoutInSeconds);
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
        TestUtils.awaitCondition(waitCallable, timeoutInSeconds);
    }

    public <T> void waitUntil(final T value, final Matcher<? super T> condition,
                              int timeoutInSeconds) {
        Callable<Boolean> waitCallable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return condition.matches(value);
            }
        };
        TestUtils.awaitCondition(waitCallable, timeoutInSeconds);
    }

    public <T> void waitUntil(final Callable<T> callable, final Matcher<? super T> condition,
                              int timeoutInSeconds) {
        Callable<Boolean> waitCallable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return condition.matches(callable.call());
            }
        };
        TestUtils.awaitCondition(waitCallable, timeoutInSeconds);
    }

}
