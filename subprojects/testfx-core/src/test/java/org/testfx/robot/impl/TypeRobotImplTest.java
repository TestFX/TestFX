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

import javafx.scene.input.KeyCodeCombination;

import org.junit.Before;
import org.junit.Test;
import org.testfx.robot.KeyboardRobot;
import org.testfx.robot.SleepRobot;
import org.testfx.robot.TypeRobot;

import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.ALT;
import static javafx.scene.input.KeyCode.B;
import static javafx.scene.input.KeyCode.CONTROL;
import static javafx.scene.input.KeyCode.SHIFT;
import static javafx.scene.input.KeyCombination.ALT_DOWN;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;
import static javafx.scene.input.KeyCombination.SHIFT_DOWN;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public final class TypeRobotImplTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public TypeRobot typeRobot;

    public KeyboardRobot keyboardRobot;
    public SleepRobot sleepRobot;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setup() {
        keyboardRobot = mock(KeyboardRobot.class);
        sleepRobot = mock(SleepRobot.class);
        typeRobot = new TypeRobotImpl(keyboardRobot, sleepRobot);
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void push_with_key_for_A() {
        // when:
        typeRobot.push(A);

        // then:
        verify(keyboardRobot, times(1)).pressNoWait(eq(A));
        verify(keyboardRobot, times(1)).release(eq(A));
        verifyNoMoreInteractions(keyboardRobot);
    }

    @Test
    public void push_with_keys_for_ALT_A() {
        // when:
        typeRobot.push(ALT, A);

        // then:
        verify(keyboardRobot, times(1)).pressNoWait(eq(ALT), eq(A));
        verify(keyboardRobot, times(1)).release(eq(A), eq(ALT));
        verifyNoMoreInteractions(keyboardRobot);
    }

    @Test
    public void push_with_combination_for_ALT_A() {
        // when:
        typeRobot.push(new KeyCodeCombination(A, ALT_DOWN));

        // then:
        verify(keyboardRobot, times(1)).pressNoWait(eq(ALT), eq(A));
        verify(keyboardRobot, times(1)).release(eq(A), eq(ALT));
        verifyNoMoreInteractions(keyboardRobot);
    }

    @Test
    public void push_with_combination_for_CONTROL_SHIFT_A() {
        // when:
        typeRobot.push(new KeyCodeCombination(A, CONTROL_DOWN, SHIFT_DOWN));

        // then:
        verify(keyboardRobot, times(1)).pressNoWait(eq(SHIFT), eq(CONTROL), eq(A));
        verify(keyboardRobot, times(1)).release(eq(A), eq(CONTROL), eq(SHIFT));
        verifyNoMoreInteractions(keyboardRobot);
    }

    @Test
    public void type_with_keys_for_A() {
        // when:
        typeRobot.type(A);

        // then:
        verify(keyboardRobot, times(1)).pressNoWait(eq(A));
        verify(keyboardRobot, times(1)).release(eq(A));
        verifyNoMoreInteractions(keyboardRobot);
    }

    @Test
    public void type_with_keys_for_A_and_B() {
        // when:
        typeRobot.type(A, B);

        // then:
        verify(keyboardRobot, times(1)).pressNoWait(eq(A));
        verify(keyboardRobot, times(1)).release(eq(A));
        verify(keyboardRobot, times(1)).pressNoWait(eq(B));
        verify(keyboardRobot, times(1)).release(eq(B));
        verifyNoMoreInteractions(keyboardRobot);
    }

    @Test
    public void type_with_key_for_A_five_times() {
        // when:
        typeRobot.type(A, 5);

        // then:
        verify(keyboardRobot, times(5)).pressNoWait(eq(A));
        verify(keyboardRobot, times(5)).release(eq(A));
        verifyNoMoreInteractions(keyboardRobot);
    }

}
