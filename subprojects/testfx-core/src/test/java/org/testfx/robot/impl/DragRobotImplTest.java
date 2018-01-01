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
import org.testfx.robot.DragRobot;
import org.testfx.robot.MouseRobot;
import org.testfx.robot.MoveRobot;
import org.testfx.service.query.PointQuery;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class DragRobotImplTest {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    DragRobot dragRobot;
    MouseRobot mouseRobot;
    MoveRobot moveRobot;

    @Before
    public void setup() {
        mouseRobot = mock(MouseRobot.class);
        moveRobot = mock(MoveRobot.class);
        dragRobot = new DragRobotImpl(mouseRobot, moveRobot);
    }

    @Test
    public void drag_with_primary_button() {
        // when:
        dragRobot.drag(MouseButton.PRIMARY);

        // then:
        verify(mouseRobot, times(1)).press(eq(MouseButton.PRIMARY));
        verifyNoMoreInteractions(mouseRobot);
        verifyZeroInteractions(moveRobot);
    }

    @Test
    public void drag_with_pointQuery_and_primary_button() {
        // given:
        PointQuery pointQuery = mock(PointQuery.class);

        // when:
        dragRobot.drag(pointQuery, MouseButton.PRIMARY);

        // then:
        verify(mouseRobot, times(1)).press(eq(MouseButton.PRIMARY));
        verify(moveRobot, times(1)).moveTo(eq(pointQuery));
        verifyNoMoreInteractions(mouseRobot);
    }

    @Test
    public void drop_with_dragged_with_primary_button() {
        // given:
        dragRobot.drag(MouseButton.PRIMARY);
        reset(mouseRobot, moveRobot);

        // when:
        dragRobot.drop();

        // then:
        verify(mouseRobot, times(1)).release();
        verifyNoMoreInteractions(mouseRobot);
        verifyZeroInteractions(moveRobot);
    }

    @Test
    public void dropTo_pointQuery_with_dragged_with_primary_button() {
        // given:
        PointQuery pointQuery = mock(PointQuery.class);

        // and:
        dragRobot.drag(MouseButton.PRIMARY);
        reset(mouseRobot, moveRobot);

        // when:
        dragRobot.dropTo(pointQuery);

        // then:
        verify(moveRobot, times(1)).moveTo(eq(pointQuery));
        verify(mouseRobot, times(1)).release();
        verifyNoMoreInteractions(moveRobot, mouseRobot);
    }

    @Test
    public void dropTo_x_and_y_with_dragged_with_primary_button() {
        // given:
        dragRobot.drag(MouseButton.PRIMARY);
        reset(mouseRobot, moveRobot);

        // when:
        dragRobot.dropBy(10.0, 20.0);

        // then:
        verify(moveRobot, times(1)).moveBy(eq(10.0), eq(20.0));
        verify(mouseRobot, times(1)).release();
        verifyNoMoreInteractions(moveRobot, mouseRobot);
    }

}
