/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2024 The TestFX Contributors
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
package org.testfx.framework.junit5.utils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;
import javafx.application.Platform;

// import org.junit.platform.commons.logging.Logger;
// import org.junit.platform.commons.logging.LoggerFactory;

/**
 * Small tool to execute/call JavaFX GUI-related code from potentially non-JavaFX thread (equivalent to old:
 * SwingUtilities.invokeLater(...) ... invokeAndWait(...) tools)
 */
public final class FXUtils {
    // private static final Logger LOGGER = LoggerFactory.getLogger(FXUtils.class);

    public static void assertJavaFxThread() {
        if (!Platform.isFxApplicationThread()) {
            throw new IllegalStateException("access JavaFX from non-JavaFX thread - please fix");
        }
    }

    /**
     * If you run into any situation where all of your scenes end, the thread managing all of this will just peter out.
     * To prevent this from happening, add this line:
     */
    public static void keepJavaFxAlive() {
        Platform.setImplicitExit(false);
    }

    /**
     * Invokes a Runnable in JFX Thread and waits while it's finished. Like SwingUtilities.invokeAndWait does for EDT.
     *
     * @param function Runnable function that should be executed within the JavaFX thread
     * @throws Exception if a exception is occurred in the run method of the Runnable
     */
    public static void runAndWait(final Runnable function) throws Exception {
        runAndWait("runAndWait(Runnable)", t -> {
            function.run();
            return "FXUtils::runAndWait - null Runnable return";
        });
    }

    /**
     * Invokes a Runnable in JFX Thread and waits while it's finished. Like SwingUtilities.invokeAndWait does for EDT.
     *
     * @param function Supplier function that should be executed within the JavaFX thread
     * @param <R> generic for return type
     * @return function result of type R
     * @throws Exception if a exception is occurred in the run method of the Runnable
     */
    public static <R> R runAndWait(final Supplier<R> function) throws Exception {
        return runAndWait("runAndWait(Supplier<R>)", t -> function.get());
    }

    /**
     * Invokes a Runnable in JFX Thread and waits while it's finished. Like SwingUtilities.invokeAndWait does for EDT.
     *
     * @param argument function argument
     * @param function transform function that should be executed within the JavaFX thread
     * @param <T> generic for argument type
     * @param <R> generic for return type
     * @return function result of type R
     * @throws Exception if a exception is occurred in the run method of the Runnable
     */
    public static <T, R> R runAndWait(final T argument, final Function<T, R> function) throws Exception {
        if (Platform.isFxApplicationThread()) {
            return function.apply(argument);
        } else {
            final AtomicBoolean runCondition = new AtomicBoolean(true);
            final Lock lock = new ReentrantLock();
            final Condition condition = lock.newCondition();
            final ExceptionWrapper throwableWrapper = new ExceptionWrapper();

            final RunnableWithReturn<R> run = new RunnableWithReturn<>(() -> {
                R returnValue = null;
                lock.lock();
                try {
                    returnValue = function.apply(argument);
                }
                catch (final Exception e) {
                    throwableWrapper.t = e;
                }
                finally {
                    try {
                        runCondition.set(false);
                        condition.signal();
                    }
                    finally {
                        runCondition.set(false);
                        lock.unlock();
                    }
                }
                return returnValue;
            });
            lock.lock();
            try {
                Platform.runLater(run);
                while (runCondition.get()) {
                    condition.await();
                }
                if (throwableWrapper.t != null) {
                    throw throwableWrapper.t;
                }
            }
            finally {
                lock.unlock();
            }
            return run.getReturnValue();
        }
    }

    public static void runFX(final Runnable run) {
        FXUtils.keepJavaFxAlive();
        if (Platform.isFxApplicationThread()) {
            run.run();
        } else {
            Platform.runLater(run);
        }
    }

    private static class ExceptionWrapper {
        private Exception t;
    }

    private static class RunnableWithReturn<R> implements Runnable {
        private final Supplier<R> internalRunnable;
        private final Object lock = new Object();
        private R returnValue;

        public RunnableWithReturn(final Supplier<R> run) {
            internalRunnable = run;
        }

        public R getReturnValue() {
            synchronized (lock) {
                return returnValue;
            }
        }

        @Override
        public void run() {
            synchronized (lock) {
                returnValue = internalRunnable.get();
            }
        }
    }
}
