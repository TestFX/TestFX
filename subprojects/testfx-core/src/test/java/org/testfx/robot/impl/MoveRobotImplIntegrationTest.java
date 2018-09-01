package org.testfx.robot.impl;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.cases.InternalTestCaseBase;
import org.testfx.robot.Motion;
import org.testfx.robot.MoveRobot;
import org.testfx.service.query.PointQuery;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MoveRobotImplIntegrationTest extends InternalTestCaseBase {

    private MoveRobot moveRobot;
    private List<Point2D> points;

    @Override
    public Node createComponent() {
        moveRobot = new MoveRobotImpl(robotContext().getBaseRobot(), 
                robotContext().getMouseRobot(), 
                robotContext().getSleepRobot());
        points = new ArrayList<>();
        Pane p = new Pane();
        p.setPrefSize(400, 400);
        p.setOnMouseMoved(e -> points.add(new Point2D(e.getX(), e.getY())));
        return p;
    }

    @BeforeClass
    public static void setupAll() {
        MoveRobotImpl.SLEEP_AFTER_MOVEMENT_STEP_IN_MILLIS = 0;
    }
    
    @AfterClass
    public static void resetAll() {
        MoveRobotImpl.SLEEP_AFTER_MOVEMENT_STEP_IN_MILLIS = MoveRobotImpl.SLEEP_AFTER_MOVEMENT_STEP_IN_MILLIS_DEFAULT;
    }

    @Test
    public void moveTo_a_point_with_motion_DIRECT() {
        
        // given:
        Point2D sourcePoint = point(getTestStage()).atPosition(0, 0).atOffset(new Point2D(10, 10)).query();
        PointQuery targetPoint = point(getTestStage()).atPosition(0, 0).atOffset(new Point2D(310, 310));
        robotContext().getMouseRobot().move(sourcePoint);
        points.clear();
        
        // when:
        moveRobot.moveTo(targetPoint, Motion.DIRECT);

        // then:
        assertTrue("Didn't receive enough events expected 10, but was " + points.size(), points.size() >= 10);
    }

    @Test
    public void moveTo_a_point_with_motion_HORIZONTAL_FIRST() {
        // given:
        Point2D sourcePoint = point(getTestStage()).atPosition(0, 0).atOffset(new Point2D(10, 10)).query();
        PointQuery targetPoint = point(getTestStage()).atPosition(0, 0).atOffset(new Point2D(310, 310));
        robotContext().getMouseRobot().move(sourcePoint);
        points.clear();
        
        // when:
        moveRobot.moveTo(targetPoint, Motion.HORIZONTAL_FIRST);

        // then:
        assertTrue("Didn't receive enough events expected 10, but was " + points.size(), points.size() >= 10);
        assertThat("Didn't move horizontal first", points.get(0).getY(), equalTo(points.get(1).getY()));
    }

    @Test
    public void moveTo_a_point_with_motion_VERTICAL_FIRST() {
        // given:
        Point2D sourcePoint = point(getTestStage()).atPosition(0, 0).atOffset(new Point2D(10, 10)).query();
        PointQuery targetPoint = point(getTestStage()).atPosition(0, 0).atOffset(new Point2D(310, 310));
        robotContext().getMouseRobot().move(sourcePoint);
        points.clear();
        
        // when:
        moveRobot.moveTo(targetPoint, Motion.VERTICAL_FIRST);

        // then:
        assertTrue("Didn't receive enough events expected 10, but was " + points.size(), points.size() >= 10);
        assertThat("Didn't move horizontal first", points.get(0).getX(), equalTo(points.get(1).getX()));
    }


}
