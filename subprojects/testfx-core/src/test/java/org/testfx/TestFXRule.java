/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx;

import java.util.concurrent.Callable;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.testfx.api.FxToolkit;
import org.testfx.util.WaitForAsyncUtils;

public class TestFXRule extends TestWatcher {

    private static final long WAIT_MILLIS = 30000;
    public static boolean initialized;

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
