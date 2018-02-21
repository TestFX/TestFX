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
package org.testfx.matcher.base;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;

import static org.hamcrest.MatcherAssert.assertThat;

public class WindowMatchersTest extends FxRobot {

    @Rule
    public TestRule rule = RuleChain.outerRule(new TestFXRule()).around(exception = ExpectedException.none());
    public ExpectedException exception;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Test
    public void isNotShowing() throws Exception {
        Window window = FxToolkit.setupFixture((Callable<Stage>) Stage::new);
        assertWithCleanup(() -> assertThat(window, WindowMatchers.isNotShowing()));
    }

    @Test
    public void isNotFocused() throws Exception {
        Window window = FxToolkit.setupFixture((Callable<Stage>) Stage::new);
        assertWithCleanup(() -> assertThat(window, WindowMatchers.isNotFocused()));
    }

    @Test
    public void isShowing() throws Exception {
        Window window = FxToolkit.setupFixture(() -> {
            Stage stage = new Stage();
            stage.show();
            return stage;
        });
        assertWithCleanup(() -> assertThat(window, WindowMatchers.isShowing()));
    }

    private void assertWithCleanup(Runnable runnable) throws TimeoutException {
        try {
            runnable.run();
        }
        finally {
            FxToolkit.cleanupStages();
        }
    }

    @Test
    @Ignore("See https://github.com/TestFX/TestFX/pull/284 for details")
    public void isFocused() throws Exception {
        Window window = FxToolkit.setupFixture(() -> {
            Stage stage = new Stage();
            stage.show();
            stage.requestFocus();
            return stage;
        });

        assertThat(window, WindowMatchers.isFocused());
    }
}
