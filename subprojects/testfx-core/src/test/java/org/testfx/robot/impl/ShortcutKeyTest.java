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
package org.testfx.robot.impl;

import java.util.concurrent.TimeoutException;

import javafx.scene.Scene;
import javafx.scene.control.TextField;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.util.WaitForAsyncUtils;

import static javafx.scene.input.KeyCode.SHORTCUT;

import static org.hamcrest.CoreMatchers.equalTo;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.robot.impl.KeyboardRobotImpl.OS_SPECIFIC_SHORTCUT;
import static org.testfx.util.DebugUtils.informedErrorMessage;

/**
 * Tests whether pressing {@code KeyCode.SHORTCUT} will convert into the OS-specific KeyCode (i.e.
 * {@code KeyCode.COMMAND} for Macs and {@code KeyCode.CONTROL} for everything else).
 */
public class ShortcutKeyTest extends FxRobot {

    @Rule
    public TestRule rule = RuleChain.outerRule(new TestFXRule()).around(Timeout.millis(3000));

    private TextField field;
    private final String pressedText = "pressed";
    private final String releasedText = "released";

    @Before
    public void setup() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupStage(stage -> {
            field = new TextField("not pressed");
            field.setOnKeyPressed(e -> {
                if (e.getCode().equals(OS_SPECIFIC_SHORTCUT)) {
                    field.setText(pressedText);
                }
                else {
                    field.setText(e.getCode().toString());
                }
                e.consume();
            });
            field.setOnKeyReleased(e -> {
                if (e.getCode().equals(OS_SPECIFIC_SHORTCUT)) {
                    field.setText(releasedText);
                }
                else {
                    field.setText(e.getCode().toString());
                }
                e.consume();
            });
            stage.setScene(new Scene(field));
            stage.show();
            field.requestFocus();
        });

        WaitForAsyncUtils.waitForFxEvents();
    }

    @After
    public void cleanup() {
        // prevent hanging if test fails
        release(SHORTCUT, OS_SPECIFIC_SHORTCUT);
    }

    @Test
    public void shortcut_keyCode_converts_to_OS_specific_keyCode_when_pressed() {
        // when:
        press(SHORTCUT);

        // then:
        verifyThat(field.getText(), equalTo(pressedText), informedErrorMessage(this));
    }

    @Test
    public void shortcut_keyCode_converts_to_OS_specific_keyCode_when_released() {
        // given:
        press(OS_SPECIFIC_SHORTCUT);

        // and:
        verifyThat(field.getText(), equalTo(pressedText), informedErrorMessage(this));

        // when:
        release(SHORTCUT);

        // then:
        verifyThat(field.getText(), equalTo(releasedText), informedErrorMessage(this));
    }
}
