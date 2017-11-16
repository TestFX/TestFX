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
package org.testfx.framework.junit;

import java.util.concurrent.Callable;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.testfx.api.FxToolkit;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Optional JUit rule that can be used to ensure the JavaFX platform has
 * been initialized. Can also be used for retrying tests for example
 * if they are known to be flaky.
 * <p>
 * This rule can be used by adding the following annotated field to your
 * test classes:
 * <pre>{@code
 * @Rule
 * public TestFXRule testFXRule = new TestFXRule();
 * }</pre>
 * <p>
 * Developer's Note: TestFX uses this rule for its' own tests.
 */
public class TestFXRule extends TestWatcher {

    private final int retryCount;
    private static final long WAIT_MILLIS = 30000;
    private static boolean initialized;

    public TestFXRule() {
        this.retryCount = 0;
    }

    public TestFXRule(int retryCount) {
        this.retryCount = retryCount;
    }

    public Statement apply(Statement base, Description description) {
        return statement(base, description);
    }

    private Statement statement(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Throwable caughtThrowable = null;

                for (int i = 0; i < retryCount; i++) {
                    try {
                        base.evaluate();
                        return;
                    } catch (Throwable t) {
                        caughtThrowable = t;
                        System.err.println(description.getDisplayName() + ": run " + (i + 1) + " failed (retrying...)");
                    }
                }
                if (retryCount != 0) {
                    System.err.println(description.getDisplayName() + ": giving up after " + retryCount + " failures");
                }
                if (caughtThrowable != null) {
                    throw caughtThrowable;
                }
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

}
