package org.loadui.testfx.robots.impl;

import javafx.geometry.Point2D;

import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.robots.MoveRobot;
import org.loadui.testfx.robots.ScreenRobot;
import org.loadui.testfx.service.query.PointQuery;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public final class MoveRobotImplTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    ScreenRobot screenRobot;
    MoveRobot moveRobot;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setup() {
        screenRobot = mock(ScreenRobot.class);
        moveRobot = new MoveRobotImpl(screenRobot);
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void moveTo_a_point_within_10_pixels() {
        // given:
        Point2D sourcePoint = new Point2D(0, 0);
        given(screenRobot.retrieveMouse()).willReturn(sourcePoint);

        // and:
        Point2D targetPoint = new Point2D(10, 0);
        PointQuery pointQuery = mock(PointQuery.class);
        given(pointQuery.query()).willReturn(targetPoint);

        // when:
        moveRobot.moveTo(pointQuery);

        // then:
        for (double x = 0.0; x <= 9.0; x++) {
            verify(screenRobot, times(1)).moveMouse(new Point2D(x, 0));
        }
        verify(screenRobot, times(2)).moveMouse(new Point2D(10, 0));
        verify(screenRobot, times(2)).awaitEvents();
    }

    @Test
    public void moveTo_a_point_within_1000_pixels() {
        // given:
        Point2D sourcePoint = new Point2D(0, 0);
        given(screenRobot.retrieveMouse()).willReturn(sourcePoint);

        // and:
        Point2D targetPoint = new Point2D(1000, 0);
        PointQuery pointQuery = mock(PointQuery.class);
        given(pointQuery.query()).willReturn(targetPoint);

        // when:
        moveRobot.moveTo(pointQuery);

        // then:
        verify(screenRobot, times(200)).moveMouse(not(eq(targetPoint)));
        verify(screenRobot, times(2)).moveMouse(targetPoint);
        verify(screenRobot, times(2)).awaitEvents();
    }

    @Test
    public void moveTo_should_move_to_moved_target_point() {
        // given:
        Point2D sourcePoint = new Point2D(0, 0);
        given(screenRobot.retrieveMouse()).willReturn(sourcePoint);

        // and:
        Point2D targetPoint = new Point2D(10, 0);
        Point2D movedTargetPoint = new Point2D(20, 0);
        PointQuery pointQuery = mock(PointQuery.class);
        given(pointQuery.query()).willReturn(targetPoint, movedTargetPoint);

        // when:
        moveRobot.moveTo(pointQuery);

        // then:
        verify(screenRobot, times(1)).moveMouse(targetPoint);
        verify(screenRobot, times(1)).moveMouse(movedTargetPoint);
        verify(screenRobot, times(2)).awaitEvents();
    }

    @Test
    public void moveBy_a_distance_of_10_pixels() {
        // given:
        Point2D sourcePoint = new Point2D(0, 0);
        given(screenRobot.retrieveMouse()).willReturn(sourcePoint);

        // when:
        Point2D targetPoint = new Point2D(10, 0);
        moveRobot.moveBy(10, 0);

        // then:
        verify(screenRobot, times(10)).moveMouse(not(eq(targetPoint)));
        verify(screenRobot, times(1)).moveMouse(targetPoint);
        verify(screenRobot, times(1)).awaitEvents();
    }

    @Test
    public void moveBy_a_distance_of_1000_pixels() {
        // given:
        Point2D sourcePoint = new Point2D(0, 0);
        given(screenRobot.retrieveMouse()).willReturn(sourcePoint);

        // when:
        Point2D targetPoint = new Point2D(1000, 0);
        moveRobot.moveBy(1000, 0);

        // then:
        verify(screenRobot, times(200)).moveMouse(not(eq(targetPoint)));
        verify(screenRobot, times(1)).moveMouse(targetPoint);
        verify(screenRobot, times(1)).awaitEvents();
    }

}
