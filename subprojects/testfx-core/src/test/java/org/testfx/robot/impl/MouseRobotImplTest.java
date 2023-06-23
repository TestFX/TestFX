/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2023 The TestFX Contributors
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

import javafx.scene.input.MouseButton;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.testfx.TestFXRule;
import org.testfx.robot.BaseRobot;
import org.testfx.robot.MouseRobot;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MouseRobotImplTest {

    @Rule(order = 0)
    public TestRule rule = new TestFXRule();

    @Rule(order = 1)
    public TestRule timeoutRule = Timeout.millis(3000);

    MouseRobot mouseRobot;
    BaseRobot baseRobot;

    @Before
    public void setup() {
        baseRobot = mock(BaseRobot.class);
        mouseRobot = new MouseRobotImpl(baseRobot);
    }

    @Test
    public void press_with_primary_button() {
        // when:
        mouseRobot.press(MouseButton.PRIMARY);

        // then:
        verify(baseRobot, times(1)).pressMouse(eq(MouseButton.PRIMARY));
        verifyNoMoreInteractions(baseRobot);
    }

    @Test
    public void press_with_primary_and_secondary_button() {
        // when:
        mouseRobot.press(MouseButton.PRIMARY, MouseButton.SECONDARY);

        // then:
        verify(baseRobot, times(1)).pressMouse(eq(MouseButton.PRIMARY));
        verify(baseRobot, times(1)).pressMouse(eq(MouseButton.SECONDARY));
        verifyNoMoreInteractions(baseRobot);
    }

    @Test
    public void press_with_no_buttons_should_press_primary_button() {
        // when:
        mouseRobot.press();

        // then:
        verify(baseRobot, times(1)).pressMouse(eq(MouseButton.PRIMARY));
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
        verifyNoMoreInteractions(baseRobot);
    }

    @Test
    public void release_with_unpressed_primary_button() {
        // when:
        mouseRobot.release(MouseButton.PRIMARY);

        // then:
        verify(baseRobot, times(0)).releaseMouse(MouseButton.PRIMARY);
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
        verifyNoMoreInteractions(baseRobot);
    }

}
