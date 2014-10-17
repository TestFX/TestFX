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
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.robots.KeyboardRobot;
import org.loadui.testfx.robots.SleepRobot;
import org.loadui.testfx.robots.TypeRobot;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public final class TypeRobotImplTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    KeyboardRobot keyboardRobot;
    SleepRobot sleepRobot;
    TypeRobot typeRobot;

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
        typeRobot.push(KeyCode.A);

        // then:
        verify(keyboardRobot, times(1)).press(eq(KeyCode.A));
        verify(keyboardRobot, times(1)).release(eq(KeyCode.A));
        verifyNoMoreInteractions(keyboardRobot);
    }

    @Test
    public void push_with_keys_for_ALT_A() {
        // when:
        typeRobot.push(KeyCode.ALT, KeyCode.A);

        // then:
        verify(keyboardRobot, times(1)).press(eq(KeyCode.ALT), eq(KeyCode.A));
        verify(keyboardRobot, times(1)).release(eq(KeyCode.A), eq(KeyCode.ALT));
        verifyNoMoreInteractions(keyboardRobot);
    }

    @Test
    public void push_with_combination_for_ALT_A() {
        // when:
        typeRobot.push(new KeyCodeCombination(KeyCode.A, KeyCombination.ALT_DOWN));

        // then:
        verify(keyboardRobot, times(1)).press(eq(KeyCode.ALT), eq(KeyCode.A));
        verify(keyboardRobot, times(1)).release(eq(KeyCode.A), eq(KeyCode.ALT));
        verifyNoMoreInteractions(keyboardRobot);
    }

    @Test
    public void push_with_combination_for_CONTROL_SHIFT_A() {
        // when:
        typeRobot.push(new KeyCodeCombination(KeyCode.A,
            KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

        // then:
        verify(keyboardRobot, times(1)).press(
            eq(KeyCode.SHIFT), eq(KeyCode.CONTROL), eq(KeyCode.A));
        verify(keyboardRobot, times(1)).release(
            eq(KeyCode.A), eq(KeyCode.CONTROL), eq(KeyCode.SHIFT));
        verifyNoMoreInteractions(keyboardRobot);
    }

    @Test
    public void type_with_keys_for_A() {
        // when:
        typeRobot.type(KeyCode.A);

        // then:
        verify(keyboardRobot, times(1)).press(eq(KeyCode.A));
        verify(keyboardRobot, times(1)).release(eq(KeyCode.A));
        verifyNoMoreInteractions(keyboardRobot);
    }

    @Test
    public void type_with_keys_for_A_and_B() {
        // when:
        typeRobot.type(KeyCode.A, KeyCode.B);

        // then:
        verify(keyboardRobot, times(1)).press(eq(KeyCode.A));
        verify(keyboardRobot, times(1)).release(eq(KeyCode.A));
        verify(keyboardRobot, times(1)).press(eq(KeyCode.B));
        verify(keyboardRobot, times(1)).release(eq(KeyCode.B));
        verifyNoMoreInteractions(keyboardRobot);
    }

    @Test
    public void type_with_key_for_A_five_times() {
        // when:
        typeRobot.type(KeyCode.A, 5);

        // then:
        verify(keyboardRobot, times(5)).press(eq(KeyCode.A));
        verify(keyboardRobot, times(5)).release(eq(KeyCode.A));
        verifyNoMoreInteractions(keyboardRobot);
    }

}
