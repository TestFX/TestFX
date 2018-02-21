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

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testfx.assertions.api.Assertions.assertThat;

public class ButtonAssertTest extends FxRobot {

    Button button;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            button = new Button();
            return new StackPane(button);
        });
        FxToolkit.showStage();
    }

    @Test
    public void isCancelButton() {
        // given:
        button.setCancelButton(true);

        // then:
        assertThat(lookup(".button").queryAs(Button.class)).isCancelButton();
        assertThat((Button) lookup(".button").query()).isCancelButton();
        assertThat(button).isCancelButton();
    }

    @Test
    public void isCancelButton_fails() {
        assertThatThrownBy(() -> assertThat(button).isCancelButton())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Expected: Button is cancel button");
    }

    @Test
    public void isNotCancelButton() {
        assertThat(button).isNotCancelButton();
    }

    @Test
    public void isNotCancelButton_fails() {
        // given:
        button.setCancelButton(true);

        // then:
        assertThatThrownBy(() -> assertThat(button).isNotCancelButton())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Expected: Button is cancel button to be false");
    }

    @Test
    public void isDefaultButton() {
        // given:
        button.setDefaultButton(true);

        // then:
        assertThat(button).isDefaultButton();
    }

    @Test
    public void isDefaultButton_fails() {
        assertThatThrownBy(() -> assertThat(button).isDefaultButton())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Expected: Button is default button\n");
    }

    @Test
    public void isNotDefaultButton() {
        assertThat(button).isNotDefaultButton();
    }

    @Test
    public void isNotDefaultButton_fails() {
        // given:
        button.setDefaultButton(true);

        // then:
        assertThatThrownBy(() -> assertThat(button).isNotDefaultButton())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Expected: Button is default button to be false\n");
    }
}
