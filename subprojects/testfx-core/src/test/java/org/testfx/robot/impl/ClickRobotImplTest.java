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

import javafx.scene.input.MouseButton;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.robot.ClickRobot;
import org.testfx.robot.Motion;
import org.testfx.robot.MouseRobot;
import org.testfx.robot.MoveRobot;
import org.testfx.robot.SleepRobot;
import org.testfx.service.query.PointQuery;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ClickRobotImplTest {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    ClickRobot clickRobot;
    MouseRobot mouseRobot;
    MoveRobot moveRobot;
    SleepRobot sleepRobot;

    @Before
    public void setup() {
        mouseRobot = mock(MouseRobot.class);
        moveRobot = mock(MoveRobot.class);
        sleepRobot = mock(SleepRobot.class);
        clickRobot = new ClickRobotImpl(mouseRobot, moveRobot, sleepRobot);
    }

    @Test
    public void clickOn_with_primary_mouse_button() {
        // when:
        clickRobot.clickOn(MouseButton.PRIMARY);

        // then:
        verify(mouseRobot, times(1)).pressNoWait(eq(MouseButton.PRIMARY));
        verify(mouseRobot, times(1)).release(eq(MouseButton.PRIMARY));
        verifyNoMoreInteractions(mouseRobot);
        verifyZeroInteractions(moveRobot, sleepRobot);
    }

    @Test
    public void clickOn_with_primary_and_secondary_mouse_buttons() {
        // when:
        clickRobot.clickOn(MouseButton.PRIMARY, MouseButton.SECONDARY);

        // then:
        verify(mouseRobot, times(1)).pressNoWait(
            eq(MouseButton.PRIMARY), eq(MouseButton.SECONDARY)
        );
        verify(mouseRobot, times(1)).release(
            eq(MouseButton.PRIMARY), eq(MouseButton.SECONDARY)
        );
        verifyNoMoreInteractions(mouseRobot);
        verifyZeroInteractions(moveRobot, sleepRobot);
    }

    @Test
    public void clickOn_with_pointQuery_and_primary_button() {
        // given:
        PointQuery pointQuery = mock(PointQuery.class);

        // when:
        clickRobot.clickOn(pointQuery, MouseButton.PRIMARY);

        // then:
        verify(moveRobot, times(1)).moveTo(eq(pointQuery), eq(Motion.DEFAULT));
        verify(mouseRobot, times(1)).pressNoWait(eq(MouseButton.PRIMARY));
        verify(mouseRobot, times(1)).release(eq(MouseButton.PRIMARY));
        verifyNoMoreInteractions(moveRobot, mouseRobot);
        verifyZeroInteractions(sleepRobot);
    }

    @Test
    public void doubleClickOn_with_primary_mouse_button() {
        // when:
        clickRobot.doubleClickOn(MouseButton.PRIMARY);

        // then:
        verify(mouseRobot, times(2)).pressNoWait(eq(MouseButton.PRIMARY));
        verify(mouseRobot, times(2)).release(eq(MouseButton.PRIMARY));
        verify(sleepRobot, times(1)).sleep(anyLong());
        verifyNoMoreInteractions(mouseRobot, sleepRobot);
        verifyZeroInteractions(moveRobot);
    }

    @Test
    public void doubleClickOn_with_primary_and_secondary_mouse_buttons() {
        // when:
        clickRobot.doubleClickOn(MouseButton.PRIMARY, MouseButton.SECONDARY);

        // then:
        verify(mouseRobot, times(2)).pressNoWait(
            eq(MouseButton.PRIMARY), eq(MouseButton.SECONDARY)
        );
        verify(mouseRobot, times(2)).release(
            eq(MouseButton.PRIMARY), eq(MouseButton.SECONDARY)
        );
        verify(sleepRobot, times(1)).sleep(anyLong());
        verifyNoMoreInteractions(mouseRobot, sleepRobot);
        verifyZeroInteractions(moveRobot);
    }

    @Test
    public void doubleClickOn_with_pointQuery_and_primary_button() {
        // given:
        PointQuery pointQuery = mock(PointQuery.class);

        // when:
        clickRobot.doubleClickOn(pointQuery, MouseButton.PRIMARY);

        // then:
        verify(moveRobot, times(1)).moveTo(eq(pointQuery), eq(Motion.DEFAULT));
        verify(mouseRobot, times(2)).pressNoWait(eq(MouseButton.PRIMARY));
        verify(mouseRobot, times(2)).release(eq(MouseButton.PRIMARY));
        verify(sleepRobot, times(1)).sleep(anyLong());
        verifyNoMoreInteractions(moveRobot, mouseRobot, sleepRobot);
    }

}
