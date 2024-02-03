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
package org.testfx;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class Repeat implements TestRule {

    private final int count;

    public Repeat(int count) {
        this.count = count;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {

            @Override
            @SuppressWarnings("synthetic-access")
            public void evaluate() throws Throwable {
                Throwable caughtThrowable = null;
                int failuresCount = 0;
                for (int i = 0; i < count; i++) {
                    try {
                        base.evaluate();
                    }
                    catch (Throwable t) {
                        caughtThrowable = t;
                        System.err.println(description.getDisplayName() + ": run " + (i + 1) + " failed:");
                        t.printStackTrace();
                        ++failuresCount;
                    }
                }
                if (caughtThrowable == null) {
                    return;
                }
                String message = description.getDisplayName();
                message += ": failures " + failuresCount;
                message += " out of " + count;
                message += " tries. See last throwable as the cause.";
                throw new AssertionError(message, caughtThrowable);
            }
        };
    }
}
