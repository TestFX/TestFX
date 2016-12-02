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

import javafx.scene.input.MouseButton;

import org.junit.Before;
import org.junit.Test;
import org.testfx.robot.BaseRobot;
import org.testfx.robot.MouseRobot;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public final class MouseRobotImplTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public MouseRobot mouseRobot;

    public BaseRobot baseRobot;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setup() {
        baseRobot = mock(BaseRobot.class);
        mouseRobot = new MouseRobotImpl(baseRobot);
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void press_with_primary_button() {
        // when:
        mouseRobot.press(MouseButton.PRIMARY);

        // then:
        verify(baseRobot, times(1)).pressMouse(eq(MouseButton.PRIMARY));
        verify(baseRobot, times(1)).awaitEvents();
        verifyNoMoreInteractions(baseRobot);
    }

    @Test
    public void press_with_primary_and_secondary_button() {
        // when:
        mouseRobot.press(MouseButton.PRIMARY, MouseButton.SECONDARY);

        // then:
        verify(baseRobot, times(1)).pressMouse(eq(MouseButton.PRIMARY));
        verify(baseRobot, times(1)).pressMouse(eq(MouseButton.SECONDARY));
        verify(baseRobot, times(1)).awaitEvents();
        verifyNoMoreInteractions(baseRobot);
    }

    @Test
    public void press_with_no_buttons_should_press_primary_button() {
        // when:
        mouseRobot.press();

        // then:
        verify(baseRobot, times(1)).pressMouse(eq(MouseButton.PRIMARY));
        verify(baseRobot, times(1)).awaitEvents();
        verifyNoMoreInteractions(baseRobot);
    }

    @Test
    public void release_with_pressed_primary_button() {
        // given:
        mouseRobot.press(MouseButton.PRIMARY);
        reset(baseRobot);

        // when:
        mouseRobot.release(MouseButton.PRIMARY);

        // then:
        verify(baseRobot, times(1)).releaseMouse(MouseButton.PRIMARY);
        verify(baseRobot, times(1)).awaitEvents();
        verifyNoMoreInteractions(baseRobot);
    }

    @Test
    public void release_with_pressed_primary_and_secondary_button() {
        // given:
        mouseRobot.press(MouseButton.PRIMARY, MouseButton.SECONDARY);
        reset(baseRobot);

        // when:
        mouseRobot.release(MouseButton.PRIMARY, MouseButton.SECONDARY);

        // then:
        verify(baseRobot, times(1)).releaseMouse(MouseButton.PRIMARY);
        verify(baseRobot, times(1)).releaseMouse(MouseButton.SECONDARY);
        verify(baseRobot, times(1)).awaitEvents();
        verifyNoMoreInteractions(baseRobot);
    }

    @Test
    public void release_with_unpressed_primary_button() {
        // when:
        mouseRobot.release(MouseButton.PRIMARY);

        // then:
        verify(baseRobot, times(0)).releaseMouse(MouseButton.PRIMARY);
        verify(baseRobot, times(1)).awaitEvents();
        verifyNoMoreInteractions(baseRobot);
    }

    @Test
    public void release_with_no_buttons_should_release_pressed_buttons() {
        // given:
        mouseRobot.press(MouseButton.PRIMARY);
        reset(baseRobot);

        // when:
        mouseRobot.release();

        // then:
        verify(baseRobot, times(1)).releaseMouse(MouseButton.PRIMARY);
        verify(baseRobot, times(1)).awaitEvents();
        verifyNoMoreInteractions(baseRobot);
    }

}
