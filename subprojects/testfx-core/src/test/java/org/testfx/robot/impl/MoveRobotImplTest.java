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

import javafx.geometry.Point2D;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.robot.BaseRobot;
import org.testfx.robot.Motion;
import org.testfx.robot.MouseRobot;
import org.testfx.robot.MoveRobot;
import org.testfx.robot.SleepRobot;
import org.testfx.service.query.PointQuery;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MoveRobotImplTest {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    private MoveRobot moveRobot;
    private BaseRobot baseRobot;
    private MouseRobot mouseRobot;
    private SleepRobot sleepRobot;

    @Before
    public void setup() {
        baseRobot = mock(BaseRobot.class);
        mouseRobot = mock(MouseRobot.class);
        sleepRobot = mock(SleepRobot.class);
        moveRobot = new MoveRobotImpl(baseRobot, mouseRobot, sleepRobot);
    }

    @Test
    public void moveTo_a_point_with_motion_DIRECT() {
        // given:
        Point2D sourcePoint = new Point2D(0, 0);
        given(baseRobot.retrieveMouse()).willReturn(sourcePoint);

        // and:
        Point2D targetPoint = new Point2D(300, 100);
        PointQuery pointQuery = mock(PointQuery.class);
        given(pointQuery.query()).willReturn(targetPoint);

        // when:
        moveRobot.moveTo(pointQuery, Motion.DIRECT);

        // then:
        verify(mouseRobot, times(199)).moveNoWait(
                argThat(argument -> sourcePoint.getY() != argument.getY() || targetPoint.getX() != argument.getX()));
        verify(mouseRobot, times(2)).move(targetPoint);
    }

    @Test
    public void moveTo_a_point_with_motion_HORIZONTAL_FIRST() {
        // given:
        Point2D sourcePoint = new Point2D(0, 0);
        given(baseRobot.retrieveMouse()).willReturn(sourcePoint);

        // and:
        Point2D targetPoint = new Point2D(300, 100);
        PointQuery pointQuery = mock(PointQuery.class);
        given(pointQuery.query()).willReturn(targetPoint);

        // when:
        moveRobot.moveTo(pointQuery, Motion.HORIZONTAL_FIRST);

        // then:
        verify(mouseRobot, times(199)).moveNoWait(
                argThat(argument -> sourcePoint.getY() == argument.getY() || targetPoint.getX() == argument.getX()));
        verify(mouseRobot, times(2)).move(targetPoint);
    }

    @Test
    public void moveTo_a_point_with_motion_VERTICAL_FIRST() {
        // given:
        Point2D sourcePoint = new Point2D(0, 0);
        given(baseRobot.retrieveMouse()).willReturn(sourcePoint);

        // and:
        Point2D targetPoint = new Point2D(300, 100);
        PointQuery pointQuery = mock(PointQuery.class);
        given(pointQuery.query()).willReturn(targetPoint);

        // when:
        moveRobot.moveTo(pointQuery, Motion.VERTICAL_FIRST);

        // then:
        verify(mouseRobot, times(199)).moveNoWait(
                argThat(argument -> sourcePoint.getX() == argument.getX() || targetPoint.getY() == argument.getY()));
        verify(mouseRobot, times(2)).move(targetPoint);
    }

    @Test
    public void moveTo_a_point_within_10_pixels() {
        // given:
        Point2D sourcePoint = new Point2D(0, 0);
        given(baseRobot.retrieveMouse()).willReturn(sourcePoint);

        // and:
        Point2D targetPoint = new Point2D(10, 0);
        PointQuery pointQuery = mock(PointQuery.class);
        given(pointQuery.query()).willReturn(targetPoint);

        // when:
        moveRobot.moveTo(pointQuery);

        // then:
        for (double x = 1.0; x <= 9.0; x++) {
            verify(mouseRobot, times(1)).moveNoWait(new Point2D(x, 0));
        }
        verify(mouseRobot, times(2)).move(new Point2D(10, 0));
    }

    @Test
    public void moveTo_a_point_within_1000_pixels() {
        // given:
        Point2D sourcePoint = new Point2D(0, 0);
        given(baseRobot.retrieveMouse()).willReturn(sourcePoint);

        // and:
        Point2D targetPoint = new Point2D(1000, 0);
        PointQuery pointQuery = mock(PointQuery.class);
        given(pointQuery.query()).willReturn(targetPoint);

        // when:
        moveRobot.moveTo(pointQuery);

        // then:
        verify(mouseRobot, times(199)).moveNoWait(not(eq(targetPoint)));
        verify(mouseRobot, times(2)).move(targetPoint);
    }

    @Test
    public void moveTo_should_move_to_moved_target_point() {
        // given:
        Point2D sourcePoint = new Point2D(0, 0);
        given(baseRobot.retrieveMouse()).willReturn(sourcePoint);

        // and:
        Point2D targetPoint = new Point2D(10, 0);
        Point2D movedTargetPoint = new Point2D(20, 0);
        PointQuery pointQuery = mock(PointQuery.class);
        given(pointQuery.query()).willReturn(targetPoint, movedTargetPoint);

        // when:
        moveRobot.moveTo(pointQuery);

        // then:
        verify(mouseRobot, times(1)).move(targetPoint);
        verify(mouseRobot, times(1)).move(movedTargetPoint);
    }

    @Test
    public void moveBy_a_distance_of_10_pixels() {
        // given:
        Point2D sourcePoint = new Point2D(0, 0);
        given(baseRobot.retrieveMouse()).willReturn(sourcePoint);

        // when:
        Point2D targetPoint = new Point2D(10, 0);
        moveRobot.moveBy(10, 0);

        // then:
        verify(mouseRobot, times(9)).moveNoWait(not(eq(targetPoint)));
        verify(mouseRobot, times(1)).move(targetPoint);
    }

    @Test
    public void moveBy_a_distance_of_1000_pixels() {
        // given:
        Point2D sourcePoint = new Point2D(0, 0);
        given(baseRobot.retrieveMouse()).willReturn(sourcePoint);

        // when:
        Point2D targetPoint = new Point2D(1000, 0);
        moveRobot.moveBy(1000, 0);

        // then:
        verify(mouseRobot, times(199)).moveNoWait(not(eq(targetPoint)));
        verify(mouseRobot, times(1)).move(targetPoint);
    }

}
