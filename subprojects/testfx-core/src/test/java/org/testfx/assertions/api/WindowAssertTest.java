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
package org.testfx.assertions.api;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import javafx.stage.Stage;
import javafx.stage.Window;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testfx.assertions.api.Assertions.assertThat;

public class WindowAssertTest extends FxRobot {
    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @After
    public void cleanup() throws TimeoutException {
        FxToolkit.cleanupStages();
    }

    @Test
    public void isShowing() throws Exception {
        // given:
        Window window = FxToolkit.setupFixture(() -> {
            Stage stage = new Stage();
            stage.show();
            return stage;
        });

        // then:
        assertThat(window).isShowing();
    }

    @Test
    public void isShowing_fails() throws Exception {
        // given:
        Window window = FxToolkit.setupFixture((Callable<Stage>) Stage::new);

        // then:
        assertThatThrownBy(() -> assertThat(window).isShowing())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Expected: Window is showing");
    }

    @Test
    public void isNotShowing() throws Exception {
        // given:
        Window window = FxToolkit.setupFixture((Callable<Stage>) Stage::new);

        // then:
        assertThat(window).isNotShowing();
    }

    @Test
    public void isNotShowing_fails() throws Exception {
        // given:
        Window window = FxToolkit.setupFixture(() -> {
            Stage stage = new Stage();
            stage.show();
            return stage;
        });

        // then:
        assertThatThrownBy(() -> assertThat(window).isNotShowing())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Expected: Window is not showing");
    }

    @Test
    public void isNotFocused() throws Exception {
        // given:
        Window window = FxToolkit.setupFixture((Callable<Stage>) Stage::new);

        // then:
        assertThat(window).isNotFocused();
    }
}
