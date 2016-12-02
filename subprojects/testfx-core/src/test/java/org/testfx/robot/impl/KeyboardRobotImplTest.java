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

import org.junit.Before;
import org.junit.Test;
import org.testfx.robot.BaseRobot;
import org.testfx.robot.KeyboardRobot;

import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.B;

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

    public KeyboardRobot keyboardRobot;

    public BaseRobot baseRobot;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setup() {
        baseRobot = mock(BaseRobot.class);
        keyboardRobot = new KeyboardRobotImpl(baseRobot);
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void press_with_keyCode_for_A() {
        // when:
        keyboardRobot.press(A);

        // then:
        verify(baseRobot, times(1)).pressKeyboard(eq(A));
        verify(baseRobot, times(1)).awaitEvents();
        verifyNoMoreInteractions(baseRobot);
    }

    @Test
    public void press_with_keyCodes_for_A_and_B() {
        // when:
        keyboardRobot.press(A, B);

        // then:
        verify(baseRobot, times(1)).pressKeyboard(eq(A));
        verify(baseRobot, times(1)).pressKeyboard(eq(B));
        verify(baseRobot, times(1)).awaitEvents();
        verifyNoMoreInteractions(baseRobot);
    }

    @Test
    public void release_with_pressed_keyCode_for_A() {
        // given:
        keyboardRobot.press(A);
        reset(baseRobot);

        // when:
        keyboardRobot.release(A);

        // then:
        verify(baseRobot, times(1)).releaseKeyboard(A);
        verify(baseRobot, times(1)).awaitEvents();
        verifyNoMoreInteractions(baseRobot);
    }

    @Test
    public void release_with_pressed_keyCodes_for_A_and_B() {
        // given:
        keyboardRobot.press(A, B);
        reset(baseRobot);

        // when:
        keyboardRobot.release(B, A);

        // then:
        verify(baseRobot, times(1)).releaseKeyboard(B);
        verify(baseRobot, times(1)).releaseKeyboard(A);
        verify(baseRobot, times(1)).awaitEvents();
        verifyNoMoreInteractions(baseRobot);
    }

    @Test
    public void release_with_unpressed_keyCode_for_A() {
        // when:
        keyboardRobot.release(A);

        // then:
        verify(baseRobot, times(0)).releaseKeyboard(A);
        verify(baseRobot, times(1)).awaitEvents();
        verifyNoMoreInteractions(baseRobot);
    }

    @Test
    public void release_all_with_no_keyCodes_after_pressed_keyCode_for_A() {
        // given:
        keyboardRobot.press(A);
        reset(baseRobot);

        // when:
        keyboardRobot.release();

        // then:
        verify(baseRobot, times(1)).releaseKeyboard(A);
        verify(baseRobot, times(1)).awaitEvents();
        verifyNoMoreInteractions(baseRobot);
    }

}
