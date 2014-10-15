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
import org.loadui.testfx.robots.SleepRobot;
import org.loadui.testfx.robots.TypeRobot;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public final class TypeRobotImplTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    ScreenRobot screenRobot;
    KeyboardRobot keyboardRobot;
    SleepRobot sleepRobot;
    TypeRobot typeRobot;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setup() {
        screenRobot = mock(ScreenRobot.class);
        keyboardRobot = new KeyboardRobotImpl(screenRobot);
        sleepRobot = mock(SleepRobot.class);
        typeRobot = new TypeRobotImpl(keyboardRobot, sleepRobot);
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void type_with_keyCode_for_A() {
        // when:
        typeRobot.type(KeyCode.A);

        // then:
        verify(screenRobot, times(1)).pressKey(eq(KeyCode.A));
        verify(screenRobot, times(1)).releaseKey(eq(KeyCode.A));
        verifyNoMoreInteractions(screenRobot);
    }

    @Test
    public void type_with_keyCodes_for_A_and_B() {
        // when:
        typeRobot.type(KeyCode.A, KeyCode.B);

        // then:
        verify(screenRobot, times(1)).pressKey(eq(KeyCode.A));
        verify(screenRobot, times(1)).releaseKey(eq(KeyCode.A));
        verify(screenRobot, times(1)).pressKey(eq(KeyCode.B));
        verify(screenRobot, times(1)).releaseKey(eq(KeyCode.B));
        verifyNoMoreInteractions(screenRobot);
    }

    @Test
    public void andType_with_keyCode_for_B_with_held_keyCode_for_A() {
        // given:
        typeRobot.hold(KeyCode.A);
        reset(screenRobot);

        // when:
        typeRobot.andType(KeyCode.B);

        // then:
        verify(screenRobot, times(1)).pressKey(eq(KeyCode.B));
        verify(screenRobot, times(1)).releaseKey(eq(KeyCode.B));
        verify(screenRobot, times(1)).releaseKey(KeyCode.A);
        verifyNoMoreInteractions(screenRobot);
    }

}
