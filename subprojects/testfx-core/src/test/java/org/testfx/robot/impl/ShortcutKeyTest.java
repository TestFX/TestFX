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
package org.testfx.robot.impl;

import java.util.Locale;
import java.util.concurrent.TimeoutException;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static javafx.scene.input.KeyCode.COMMAND;
import static javafx.scene.input.KeyCode.CONTROL;
import static javafx.scene.input.KeyCode.SHORTCUT;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Tests whether pressing {@code KeyCode.SHORTCUT} will convert into the OS-specific KeyCode (i.e.
 * {@code KeyCode.COMMAND} for Macs and {@code KeyCode.CONTROL} for everything else).
 */
public class ShortcutKeyTest extends FxRobot {

    private final KeyCode osSpecificShortcutKey;

    {
        String osName = System.getProperty("os.name").toLowerCase(Locale.US);
        osSpecificShortcutKey = osName.startsWith("mac") ? COMMAND : CONTROL;
    }

    private TextField field;
    private final String pressedText = "pressed";
    private final String releasedText = "released";

    @Before
    public void setup() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupStage(stage -> {
            field = new TextField();
            field.setOnKeyPressed(e -> {
                if (e.getCode().equals(osSpecificShortcutKey)) {
                    field.setText(pressedText);
                }
                e.consume();
            });
            field.setOnKeyReleased(e -> {
                if (e.getCode().equals(osSpecificShortcutKey)) {
                    field.setText(releasedText);
                }
                e.consume();
            });
            stage.setScene(new Scene(field));
            stage.show();
            field.requestFocus();
        });
    }

    @After
    public void cleanup() throws TimeoutException {
        // prevent hanging if test fails
        release(SHORTCUT, osSpecificShortcutKey);
    }

    @Test
    public void shortcut_keyCode_converts_to_OS_specific_keyCode_when_pressed() {
        // when:
        press(SHORTCUT);

        // then:
        assertThat(field.getText(), equalTo(pressedText));
    }

    @Test
    public void shortcut_keyCode_converts_to_OS_specific_keyCode_when_released() {
        // given:
        press(osSpecificShortcutKey);

        // and:
        assertThat(field.getText(), equalTo(pressedText));

        // when:
        release(SHORTCUT);

        // then:
        assertThat(field.getText(), equalTo(releasedText));
    }
}
