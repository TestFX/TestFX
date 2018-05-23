/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2018 The TestFX Contributors
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
package org.testfx.util;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class WaitForAsyncUtilsTest {

    @Rule
    public TestRule rule = Timeout.millis(10000);

    @Test
    public void async_callable() throws Exception {
        // when:
        Future<String> future = WaitForAsyncUtils.async(() -> "foo");

        // then:
        Thread.sleep(10);
        assertThat(future.get(), CoreMatchers.is("foo"));
        waitForThreads(future);
    }

    @Test
    public void async_callable_with_sleep() throws Exception {
        // when:
        Future<String> future = WaitForAsyncUtils.async(() -> {
            Thread.sleep(50);
            return "foo";
        });

        // then:
        assertThat(future.isDone(), CoreMatchers.is(false));
        WaitForAsyncUtils.sleep(250, MILLISECONDS);
        assertThat(future.get(), CoreMatchers.is("foo"));
        waitForThreads(future);
    }

    @Test
    public void async_callable_with_exception() throws Throwable {
        // given:
        WaitForAsyncUtils.printException = false;
        Callable<Void> callable = () -> {
            throw new UnsupportedOperationException();
        };
        WaitForAsyncUtils.clearExceptions();

        // when:
        Future<Void> future = WaitForAsyncUtils.async(callable);

        // then:
        waitForException(future);
        assertThatThrownBy(() -> {
            WaitForAsyncUtils.checkException();
            WaitForAsyncUtils.waitFor(50, MILLISECONDS, future);
            waitForThreads(future);
        }).isExactlyInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void clearExceptionsTest() throws Throwable {
        // given:
        WaitForAsyncUtils.printException = false;
        Callable<Void> callable = () -> {
            throw new UnsupportedOperationException();
        };
        WaitForAsyncUtils.clearExceptions();

        // when:
        Future<Void> future = WaitForAsyncUtils.async(callable);

        // then:
        waitForException(future);
        WaitForAsyncUtils.clearExceptions();
        WaitForAsyncUtils.checkException();
        waitForThreads(future);
    }

    @Test
    public void autoCheckExceptionTest() {
        // given:
        WaitForAsyncUtils.printException = false;
        WaitForAsyncUtils.autoCheckException = true;
        Callable<Void> callable = () -> {
            throw new UnsupportedOperationException();
        };
        WaitForAsyncUtils.clearExceptions();

        // when:
        Future<Void> future = WaitForAsyncUtils.async(callable);
        waitForThreads(future);

        // then:
        try {
            future = WaitForAsyncUtils.async(callable);
            fail("No exception thrown by autoCheck");
        }
        catch (Throwable e) {
            if (!(e instanceof UnsupportedOperationException)) {
                throw e;
            }
        }

        WaitForAsyncUtils.clearExceptions();
        waitForThreads(future);
    }

    @Test
    public void unhandledExceptionTest() throws Throwable {
        for (int i = 0; i < 20; i++) {
            // given:
            WaitForAsyncUtils.printException = false;
            Callable<Void> callable = () -> {
                throw new NullPointerException("unhandledExceptionTest");
            };
            WaitForAsyncUtils.clearExceptions();

            // when:
            Future<Void> future = WaitForAsyncUtils.async(callable);
            try {
                WaitForAsyncUtils.waitFor(50, MILLISECONDS, future);
                fail("No exception thrown");
            }
            catch (Throwable exception) {
                if (!exception.getMessage().contains("unhandledExceptionTest")) {
                    fail("Another exception was thrown: " + exception.getMessage());
                }
            }

            // then:
            WaitForAsyncUtils.printException = true;
            try {
                WaitForAsyncUtils.checkException();
            }
            catch (Throwable exception) {
                if (exception.getMessage().contains("unhandledExceptionTest")) {
                    fail("Handled exception not removed from stack");
                } else {
                    fail("Another exception was thrown: " + exception.getMessage());
                }
            }
            WaitForAsyncUtils.clearExceptions();
            waitForException(future);
        }
    }

    @Test
    public void waitFor_with_future() throws Exception {
        // when:
        Future<Void> future = WaitForAsyncUtils.async(() -> null);

        // then:
        WaitForAsyncUtils.waitFor(50, MILLISECONDS, future);
        waitForThreads(future);
    }

    @Test
    public void waitFor_with_future_with_sleep() {
        // when:
        Future<Void> future = WaitForAsyncUtils.async(() -> {
            Thread.sleep(250);
            return null;
        });

        // then:
        assertThatThrownBy(() -> WaitForAsyncUtils.waitFor(50, MILLISECONDS, future))
                .isExactlyInstanceOf(TimeoutException.class);
        waitForThreads(future);
    }

    @Test
    public void waitFor_with_future_cancelled() {
        // given:
        WaitForAsyncUtils.printException = false;

        // when:
        Future<Void> future = WaitForAsyncUtils.async(() -> {
            Thread.sleep(250);
            return null;
        });
        try {
            Thread.sleep(50);
        }
        catch (Exception ignore) {
        }
        future.cancel(true);
        waitForThreads(future);

        // then:
        try { // only thrown if really interrupted (need to be started first)
            WaitForAsyncUtils.checkException();
            fail("checkException didn't detect Exception");
        }
        catch (Throwable ignore) {
        }

        assertThatThrownBy(() -> WaitForAsyncUtils.waitFor(50, MILLISECONDS, future))
                .isExactlyInstanceOf(CancellationException.class);
    }

    @Test
    public void waitFor_with_booleanCallable() throws Exception {
        WaitForAsyncUtils.waitFor(250, MILLISECONDS, () -> true);
    }

    @Test
    public void waitFor_with_booleanCallable_with_sleep() throws Exception {
        WaitForAsyncUtils.waitFor(250, MILLISECONDS, () -> {
            Thread.sleep(50);
            return true;
        });
    }

    @Test
    public void waitFor_with_booleanCallable_with_false() {
        assertThatThrownBy(() -> WaitForAsyncUtils.waitFor(250, MILLISECONDS, () -> false))
                .isExactlyInstanceOf(TimeoutException.class);
    }

    @Test
    public void waitFor_with_booleanCallable_with_exception() {
        assertThatThrownBy(() -> WaitForAsyncUtils.waitFor(250, MILLISECONDS, () -> {
            throw new UnsupportedOperationException();
        })).hasCauseExactlyInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void waitFor_with_booleanValue() throws Exception {
        // given:
        BooleanProperty property = new SimpleBooleanProperty(false);

        // when:
        WaitForAsyncUtils.async(() -> {
            Thread.sleep(50);
            property.set(true);
            return null;
        });

        // then:
        WaitForAsyncUtils.waitFor(250, MILLISECONDS, property);
    }

    @Test
    public void waitFor_with_booleanValue_with_false() {
        // given:
        BooleanProperty property = new SimpleBooleanProperty(false);

        // when:
        WaitForAsyncUtils.async(() -> {
            Thread.sleep(50);
            property.set(false);
            return null;
        });

        // then:
        assertThatThrownBy(() -> WaitForAsyncUtils.waitFor(250, MILLISECONDS, property))
                .isExactlyInstanceOf(TimeoutException.class);
    }

    @Test
    public void daemonThreads() throws Exception {
        final Future<Thread> future = WaitForAsyncUtils.async(Thread::currentThread);
        final Thread thread = future.get();
        assertThat(thread.isDaemon(), CoreMatchers.is(true));
    }

    void waitForException(Future<?> f) throws InterruptedException {
        Thread.sleep(50);
        assertTrue(f.isDone());
    }

    void waitForThreads(Future<?> f) {
        while (!f.isDone()) {
            try {
                Thread.sleep(1);
            }
            catch (Exception ignore) {
            }
        }
        try {
            Thread.sleep(50);
        }
        catch (Exception ignore) {
        }
    }

}
