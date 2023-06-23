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
package org.testfx.cases.acceptance;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.util.DebugUtils.informedErrorMessage;

public class ApplicationLaunchTest extends FxRobot {

    static Button button;

    public static class DemoApplication extends Application {
        @Override
        public void start(Stage stage) {
            button = new Button("click me!");
            button.setOnAction(actionEvent -> button.setText("clicked!"));
            stage.setScene(new Scene(new StackPane(button), 100, 100));
            stage.show();
            stage.setAlwaysOnTop(true);
        }
    }

    @BeforeEach
    void setup() throws Exception {
        ApplicationTest.launch(DemoApplication.class);
    }

    @AfterEach
    void cleanup() throws Exception {
        FxToolkit.cleanupStages();
    }

    @Test
    void should_contain_button() {
        // expect:
        assertThat(lookup(".button").queryButton()).hasText("click me!");
        assertThat(button).hasText("click me!");
        verifyThat(".button", hasText("click me!"), informedErrorMessage(this));
    }

    @Test
    void should_click_on_button() {
        // when:
        clickOn(".button");

        // then:
        assertThat(lookup(".button").queryButton()).hasText("clicked!");
        assertThat(button).hasText("clicked!");
        verifyThat(".button", hasText("clicked!"), informedErrorMessage(this));
    }

}
