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
package org.testfx.framework.junit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Callable;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.testfx.api.FxToolkit;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Optional JUit rule that can be used to ensure the JavaFX platform has
 * been initialized before UI tests are run. The rule can also be used
 * for retrying flaky tests.
 * <p>
 * The rule can be used by adding a {@code @Rule} annotated field to your
 * test class:
 * <pre>{@code
 * public class MyTest extends ApplicationTest {
 *     {@literal @}Rule public TestFXRule testFXRule = new TestFXRule();
 *
 *     @Test
 *     public void myTest() {
 *         // ...
 *     }
 * }
 * }</pre>
 * <p>
 * Developer's Note: TestFX uses this rule for its' own tests.
 */
public class TestFXRule extends TestWatcher {

    private final int retryCount;
    private static final long WAIT_MILLIS = 30000;
    private boolean initialized;
    private int currentAttempt;
    private Throwable[] errors = new Throwable[0];

    public TestFXRule() {
        this.retryCount = 1;
    }

    public TestFXRule(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override public Statement apply(final Statement base, final Description description) {

        errors = new Throwable[retryCount];

        return new Statement() {
            @Override public void evaluate() throws Throwable {
                while (currentAttempt < retryCount) {
                    try {
                        base.evaluate();
                        return;
                    } catch (Throwable t) {
                        if (t.toString().startsWith("org.junit.AssumptionViolatedException")) {
                            // In this case we should not propagate the error because violated assumptions
                            // are a normal part of testing on various configurations such as:
                            // assumeThat(System.getenv("TRAVIS_OS_NAME"), is(not(equalTo("osx"))));
                            return;
                        }
                        errors[currentAttempt] = t;
                        currentAttempt++;
                        Thread.sleep(1000);
                    }
                }
                throw RetryException.from(errors);
            }
        };
    }

    @Override
    protected void starting(Description description) {
        if (!initialized) {
            Callable<Boolean> waitForFXThread = () -> {
                for (int i = 0; i < WAIT_MILLIS / 250; i++) {
                    if (FxToolkit.isFXApplicationThreadRunning()) {
                        return true;
                    } else {
                        Thread.sleep(250);
                    }
                }
                return false;
            };

            initialized = WaitForAsyncUtils.waitForAsync(WAIT_MILLIS, waitForFXThread);
            if (!initialized) {
                throw new RuntimeException("JavaFX platform was not initialized in time");
            }
        }
    }

    private static class RetryException extends RuntimeException {
        private RetryException(String message) {
            super(message);
        }

        private static RetryException from(Throwable[] errors) {
            final StringBuilder msg = new StringBuilder("Invoked methods still failed after " +
                    errors.length + " attempts.");
            for (int i = 0; i < errors.length; i++) {
                final Throwable error = errors[i];
                msg.append('\n');
                msg.append("Attempt #").append(i).append(" threw exception:");
                msg.append(stackTraceAsString(error));
            }
            return new RetryException(msg.toString());
        }

        private static String stackTraceAsString(Throwable t) {
            final StringWriter errors = new StringWriter();
            t.printStackTrace(new PrintWriter(errors));
            return errors.toString();
        }
    }
}
