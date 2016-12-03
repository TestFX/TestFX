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
package org.testfx.framework.junit;

import java.util.Locale;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import org.junit.Test;

import static javafx.scene.input.KeyCode.COMMAND;
import static javafx.scene.input.KeyCode.CONTROL;
import static javafx.scene.input.KeyCode.SHORTCUT;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Tests whether pressing {@code KeyCode.SHORTCUT} will convert into the OS-specific KeyCode (i.e.
 * {@code KeyCode.COMMAND} for Macs and {@code KeyCode.CONTROL} for everything else).
 */
public class ShortcutKeyTest extends ApplicationTest {

    private final KeyCode osSpecificShortcutKey;

    {
        String osName = System.getProperty("os.name").toLowerCase(Locale.US);
        osSpecificShortcutKey = osName.startsWith("mac") ? COMMAND : CONTROL;
    }

    private TextField field;
    private final String pressedText = "pressed";
    private final String releasedText = "released";

    @Override
    public void start(Stage stage) throws Exception {
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
        stage.setScene(new Scene(field, 400, 400));
        stage.show();
        field.requestFocus();
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
