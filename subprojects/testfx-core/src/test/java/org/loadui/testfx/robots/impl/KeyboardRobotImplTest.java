/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.loadui.testfx.robots.impl;

import javafx.scene.input.KeyCode;

import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.robots.KeyboardRobot;
import org.loadui.testfx.robots.ScreenRobot;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public final class KeyboardRobotImplTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    ScreenRobot screenRobot;
    KeyboardRobot keyboardRobot;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setup() {
        screenRobot = mock(ScreenRobot.class);
        keyboardRobot = new KeyboardRobotImpl(screenRobot);
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void press_with_keyCode_for_A() {
        // when:
        keyboardRobot.press(KeyCode.A);

        // then:
        verify(screenRobot, times(1)).pressKey(eq(KeyCode.A));
        verifyNoMoreInteractions(screenRobot);
    }

    @Test
    public void press_with_keyCodes_for_A_and_B() {
        // when:
        keyboardRobot.press(KeyCode.A, KeyCode.B);

        // then:
        verify(screenRobot, times(1)).pressKey(eq(KeyCode.A));
        verify(screenRobot, times(1)).pressKey(eq(KeyCode.B));
        verifyNoMoreInteractions(screenRobot);
    }

    @Test
    public void release_with_pressed_keyCode_for_A() {
        // given:
        keyboardRobot.press(KeyCode.A);
        reset(screenRobot);

        // when:
        keyboardRobot.release(KeyCode.A);

        // then:
        verify(screenRobot, times(1)).releaseKey(KeyCode.A);
        verifyNoMoreInteractions(screenRobot);
    }

    @Test
    public void release_with_pressed_keyCodes_for_A_and_B() {
        // given:
        keyboardRobot.press(KeyCode.A, KeyCode.B);
        reset(screenRobot);

        // when:
        keyboardRobot.release(KeyCode.B, KeyCode.A);

        // then:
        verify(screenRobot, times(1)).releaseKey(KeyCode.B);
        verify(screenRobot, times(1)).releaseKey(KeyCode.A);
        verifyNoMoreInteractions(screenRobot);
    }

    @Test
    public void release_with_unpressed_keyCode_for_A() {
        // when:
        keyboardRobot.release(KeyCode.A);

        // then:
        verify(screenRobot, times(0)).releaseKey(KeyCode.A);
        verifyNoMoreInteractions(screenRobot);
    }

    @Test
    public void releaseAll_with_pressed_keyCode_for_A() {
        // given:
        keyboardRobot.press(KeyCode.A);
        reset(screenRobot);

        // when:
        keyboardRobot.releaseAll();

        // then:
        verify(screenRobot, times(1)).releaseKey(KeyCode.A);
        verifyNoMoreInteractions(screenRobot);
    }

    @Test
    public void type_with_keyCode_for_A() {
        // when:
        keyboardRobot.type(KeyCode.A);

        // then:
        verify(screenRobot, times(1)).pressKey(eq(KeyCode.A));
        verify(screenRobot, times(1)).releaseKey(eq(KeyCode.A));
        verifyNoMoreInteractions(screenRobot);
    }

    @Test
    public void type_with_keyCodes_for_A_and_B() {
        // when:
        keyboardRobot.type(KeyCode.A, KeyCode.B);

        // then:
        verify(screenRobot, times(1)).pressKey(eq(KeyCode.A));
        verify(screenRobot, times(1)).releaseKey(eq(KeyCode.A));
        verify(screenRobot, times(1)).pressKey(eq(KeyCode.B));
        verify(screenRobot, times(1)).releaseKey(eq(KeyCode.B));
        verifyNoMoreInteractions(screenRobot);
    }

    @Test
    public void andType_with_keyCode_for_B_and_pressed_keyCode_for_A() {
        // given:
        keyboardRobot.press(KeyCode.A);
        reset(screenRobot);

        // when:
        keyboardRobot.andType(KeyCode.B);

        // then:
        verify(screenRobot, times(1)).pressKey(eq(KeyCode.B));
        verify(screenRobot, times(1)).releaseKey(eq(KeyCode.B));
        verify(screenRobot, times(1)).releaseKey(KeyCode.A);
        verifyNoMoreInteractions(screenRobot);
    }

}
