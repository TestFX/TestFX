package org.testfx.robot.impl;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.testfx.api.FxService;
import org.testfx.cases.InternalTestCaseBase;
import org.testfx.robot.BaseRobot;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MouseRobotIntegrationTest extends InternalTestCaseBase {

    MouseRobotImpl mouseRobot;
    BaseRobot realBaseRobot;
    Button area;
    double scroll;

    @Override
    public Node createComponent() {
        realBaseRobot = FxService.serviceContext().getBaseRobot();
        mouseRobot = new MouseRobotImpl(new DelayedRobot(realBaseRobot, 500));
        MouseRobotImpl.MOUSE_TO = 2000;
        area = new Button("Init");
        area.setPrefSize(400, 400);
        area.setId("button");
        area.setOnMousePressed(e -> area.setText(
                "pressed " + e.isPrimaryButtonDown() + " " + e.isMiddleButtonDown() + " " + e.isSecondaryButtonDown()));
        area.setOnMouseReleased(e -> area.setText("released " + e.isPrimaryButtonDown() + " " + e.isMiddleButtonDown() +
                " " + e.isSecondaryButtonDown()));
        scroll = 0;
        area.setOnScroll(e -> {
            scroll = e.getDeltaY();
        });
        return area;
    }

    @After
    public void after() {
        mouseRobot.release();
        sleep(1000);
    }

    @AfterClass
    public static void tearDown() {
        MouseRobotImpl.MOUSE_TO = MouseRobotImpl.MOUSE_TO_DEFAULT;
    }

    @Test
    public void pressReleaseTest() {
        long t = System.currentTimeMillis();

        // when
        mouseRobot.press(MouseButton.PRIMARY, MouseButton.SECONDARY);
        try {

            // then
            assertTrue("Operation seems timed out " + (System.currentTimeMillis() - t), 
                    System.currentTimeMillis() - t < 1000);
            assertThat(area.getText(), equalTo("pressed true false true"));

        } 
        finally {
            t = System.currentTimeMillis();
            
            // when
            mouseRobot.release(MouseButton.PRIMARY, MouseButton.SECONDARY);
        }
        // then
        assertTrue("Operation seems timed out " + (System.currentTimeMillis() - t), 
                System.currentTimeMillis() - t < 1000);
        assertThat(area.getText(), equalTo("released false false false"));
    }

    @Test
    public void mouseMoveTest() {
        moveTo(new Point2D(20, 20));
        sleep(500);
        assertThat(realBaseRobot.retrieveMouse(), not(equalTo(new Point2D(400, 400))));
        long t = System.currentTimeMillis();

        // when
        mouseRobot.move(new Point2D(400, 400));

        // then
        assertTrue("Operation seems timed out " + (System.currentTimeMillis() - t), 
                System.currentTimeMillis() - t < 1000);
        assertThat(realBaseRobot.retrieveMouse(), equalTo(new Point2D(400, 400)));
    }
    @Test
    public void mouseMoveRoundingUpTest() {
        moveTo(new Point2D(20, 20));
        sleep(500);
        assertThat(realBaseRobot.retrieveMouse(), not(equalTo(new Point2D(400, 400))));
        long t = System.currentTimeMillis();

        // when
        mouseRobot.move(new Point2D(400.9, 400.9));

        // then
        assertTrue("Operation seems timed out " + (System.currentTimeMillis() - t), 
                System.currentTimeMillis() - t < 1000);
    }
    @Test
    public void mouseMoveRoundingDownTest() {
        moveTo(new Point2D(20, 20));
        sleep(500);
        assertThat(realBaseRobot.retrieveMouse(), not(equalTo(new Point2D(400, 400))));
        long t = System.currentTimeMillis();

        // when
        mouseRobot.move(new Point2D(400.1, 400.1));

        // then
        assertTrue("Operation seems timed out " + (System.currentTimeMillis() - t), 
                System.currentTimeMillis() - t < 1000);
    }

    @Test
    public void scrollTest() {
        long t = System.currentTimeMillis();

        // when
        mouseRobot.scroll(5);

        // then
        assertTrue("Operation seems timed out " + (System.currentTimeMillis() - t), 
                System.currentTimeMillis() - t < 1000);
        assertTrue(scroll != 0);
    }

}
