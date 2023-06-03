/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2023 The TestFX Contributors
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

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;
import javafx.application.Platform;
import javafx.scene.Scene;

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
            final FutureTask<R> task = new FutureTask<>(() -> function.apply(argument));

            Platform.runLater(task);

            try {
                return task.get();
                // note: no need to catch CancellationException, as we don't provide a way to cancel the task
            }
            catch (final ExecutionException e) {
                final Throwable cause = e.getCause();
                if (cause instanceof Error) {
                    throw (Error)cause;
                }
                // if it is no Error, it must be an Exception
                // (more precise, it must be a RuntimeException, as a Function cannot throw checked exceptions.
                // However, narrowing is not required here)
                throw (Exception)cause;
            }
            finally {
                task.cancel(false);
            }
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

    public static boolean waitForFxTicks(final Scene scene, final int nTicks) {
        return waitForFxTicks(scene, nTicks, -1);
    }

    public static boolean waitForFxTicks(final Scene scene, final int nTicks, final long timeoutMillis) { // NOPMD
        if (Platform.isFxApplicationThread()) {
            for (int i = 0; i < nTicks; i++) {
                Platform.requestNextPulse();
            }
            return true;
        }
        final Timer timer = new Timer("FXUtils-thread", true);
        final AtomicBoolean run = new AtomicBoolean(true);
        final AtomicInteger tickCount = new AtomicInteger(0);
        final Lock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();

        final Runnable tickListener = () -> {
            if (tickCount.incrementAndGet() >= nTicks) {
                lock.lock();
                try {

                    run.getAndSet(false);
                    condition.signal();
                }
                finally {
                    run.getAndSet(false);
                    lock.unlock();
                }
            }
            Platform.requestNextPulse();
        };

        lock.lock();
        try {
            FXUtils.runAndWait(() -> scene.addPostLayoutPulseListener(tickListener));
        }
        catch (final Exception e) {
            // cannot occur: tickListener is always non-null and
            // addPostLayoutPulseListener through 'runaAndWait' always executed in JavaFX thread
            // LOGGER..error(e, () -> "addPostLayoutPulseListener interrupted");
            e.printStackTrace();
        }
        try {
            Platform.requestNextPulse();
            if (timeoutMillis > 0) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // LOGGER..warn(() -> "FXUtils::waitForTicks(..) interrupted by timeout");

                        lock.lock();
                        try {
                            run.getAndSet(false);
                            condition.signal();
                        }
                        finally {
                            run.getAndSet(false);
                            lock.unlock();
                        }
                    }
                    }, timeoutMillis);
            }
            while (run.get()) {
                condition.await();
            }
        }
        catch (final InterruptedException e) {
            // LOGGER..error(e, () -> "await interrupted");
            e.printStackTrace();
        }
        finally {
            lock.unlock();
            timer.cancel();
        }
        try {
            FXUtils.runAndWait(() -> scene.removePostLayoutPulseListener(tickListener));
        }
        catch (final Exception e) {
            // cannot occur: tickListener is always non-null and
            // removePostLayoutPulseListener through 'runaAndWait' always executed in JavaFX thread
            // LOGGER..error(e, () -> "removePostLayoutPulseListener interrupted");
            e.printStackTrace();
        }

        return tickCount.get() >= nTicks;
    }
}
