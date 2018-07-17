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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

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
import static org.testfx.util.DebugUtils.informedErrorMessage;

/**
 * Tests whether pressing {@code KeyCode.SHORTCUT} will convert into the OS-specific KeyCode (i.e.
 * {@code KeyCode.COMMAND} for Macs and {@code KeyCode.CONTROL} for everything else).
 */
public class ShortcutKeyTest extends FxRobot {

    @Rule
    public TestRule rule = RuleChain.outerRule(new TestFXRule()).around(Timeout.millis(3000));

    private VBox box;
    private TextField field;
    private TextField field1;
    private TextField field2;
    private final String initialText = "no action";
    private final String pressedText = "pressed";
    private final String releasedText = "released";
    private final String emptyText = "";

    @Before
    public void setup() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupStage(stage -> {
            box = new VBox();
            field = new TextField(initialText);
            field.setOnKeyPressed(e -> {
                // System.out.println(e.getCode().getName() + " " + e.isShortcutDown());
                // On macOS, depending on the system either KeyCode.META or KeyCode.COMMAND is reported see #589
                if (((e.getCode() == KeyCode.CONTROL)  || 
                        (e.getCode() == KeyCode.META) || 
                        (e.getCode() == KeyCode.COMMAND)
                        ) && e.isShortcutDown()) {
                    field.setText(pressedText);
                } else {
                    field.setText(e.getCode().toString());
                }
                e.consume();
            });
            field.setOnKeyReleased(e -> {
                // System.out.println(e.getCode().getName() + " " + e.isShortcutDown());
                // On macOS, depending on the system either KeyCode.META or KeyCode.COMMAND is reported see #589
                if (((e.getCode() == KeyCode.CONTROL)  || 
                        (e.getCode() == KeyCode.META) || 
                        (e.getCode() == KeyCode.COMMAND)
                        ) && !e.isShortcutDown()) {
                    field.setText(releasedText);
                } else {
                    field.setText(e.getCode().toString());
                }
                e.consume();
            });
            field1 = new TextField(initialText);
            field2 = new TextField(emptyText);
            box.getChildren().addAll(field, field1, field2);
            stage.setScene(new Scene(box));
            stage.show();
            field.requestFocus();
        });

        WaitForAsyncUtils.waitForFxEvents();
    }

    @After
    public void cleanup() {
        // prevent hanging if test fails
        release(SHORTCUT, KeyCode.A, KeyCode.C, KeyCode.V);
    }

    /**
     * Verifies that that the correct key is received and that the method KeyEvent.isShortcutDown works.
     */
    @Test
    public void shortcut_keyCode_converts_to_OS_specific_keyCode_when_pressed() {
        // when:
        press(KeyCode.SHORTCUT);

        // then:
        verifyThat(field.getText(), equalTo(pressedText), informedErrorMessage(this));
    }

    /**
     * Verifies that that the correct key is received and that the method KeyEvent.isShortcutDown works.
     */
    @Test
    public void shortcut_keyCode_converts_to_OS_specific_keyCode_when_released() { //fix 589, 590
        // given:
        press(KeyCode.SHORTCUT);

        // and:
        verifyThat(field.getText(), equalTo(pressedText), informedErrorMessage(this));

        // when:
        release(KeyCode.SHORTCUT);

        // then:
        verifyThat(field.getText(), equalTo(releasedText), informedErrorMessage(this));
    }
    
    /**
     * Test that the KeyCombinations do work (copy paste)
     */
    @Test
    public void shortcut_keyCode_copy_paste() {
        //given:
        verifyThat(field1.getText(), equalTo(initialText), informedErrorMessage(this));
        verifyThat(field2.getText(), equalTo(emptyText), informedErrorMessage(this));


        //when:
        clickOn(field1, MouseButton.PRIMARY);
        clickOn(field1, MouseButton.PRIMARY);
        robotContext().getTypeRobot().push(new KeyCodeCombination(KeyCode.A, KeyCombination.SHORTCUT_DOWN));
        robotContext().getTypeRobot().push(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN));
        clickOn(field2, MouseButton.PRIMARY);
        field2.requestFocus();
        robotContext().getTypeRobot().push(new KeyCodeCombination(KeyCode.V, KeyCombination.SHORTCUT_DOWN));
        
        //then:
        verifyThat(field2.getText(), equalTo(initialText), informedErrorMessage(this));
        
    }
    
}
