package org.testfx.robot.impl;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxService;
import org.testfx.api.FxTiming;
import org.testfx.cases.InternalTestCaseBase;
import org.testfx.robot.BaseRobot;
import org.testfx.service.support.FiredEvents;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ClickRobotIntegrationTest extends InternalTestCaseBase {

    ClickRobotImpl clickRobot;
    BaseRobot realBaseRobot;
    Button area;

    @Override
    public Node createComponent() {
        realBaseRobot = FxService.serviceContext().getBaseRobot();
        clickRobot = new ClickRobotImpl(new MouseRobotImpl(new DelayedRobot(realBaseRobot, 500)),
                robotContext().getMoveRobot(), robotContext().getSleepRobot());
        ClickRobotImpl.CLICK_TO = 2000;
        area = new Button("Init");
        area.setPrefSize(400, 400);
        area.setId("button");
        area.setOnMouseClicked(e -> area.setText("clicked " + e.getClickCount()));
        return area;
    }
    
    @BeforeClass
    public static void setupAll() {
        ClickRobotImpl.SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS = 0;
    }

    @AfterClass
    public static void tearDown() {
        FxTiming.reset();
    }

    @Test
    public void clickTest() {
        // given:
        FiredEvents e = FiredEvents.beginStoringFiredEvents();
        long t = System.currentTimeMillis();
        try {

            // when:
            clickRobot.clickOn(MouseButton.PRIMARY);

            // then:
            t = System.currentTimeMillis() - t;
            assertThat(area.getText(), equalTo("clicked 1"));
            assertTrue("Delay wasn't long enough " + t, t > 500);
            assertTrue("Delay was too long enough " + t, t < 1500);
        } 
        finally {
            e.stopStoringFiredEvents();
        }
    }

    @Test
    public void clickTimeoutTest() {
        // given:
        FiredEvents e = FiredEvents.beginStoringFiredEvents();
        long t = System.currentTimeMillis();
        try {

            // when:
            ClickRobotImpl.CLICK_TO = 10;
            clickRobot.clickOn(MouseButton.PRIMARY);

            // then:
            t = System.currentTimeMillis() - t;
            assertTrue("Delay was too long " + t, t < 450);
        } 
        finally {
            sleep(1000); // wait for events to happen...
            e.stopStoringFiredEvents();
        }
    }

    @Test
    public void doubleClickTest() {
        // given:
        FiredEvents e = FiredEvents.beginStoringFiredEvents();
        long t = System.currentTimeMillis();
        try {

            // when:
            clickRobot.doubleClickOn(MouseButton.PRIMARY);

            // then:
            t = System.currentTimeMillis() - t;
            assertThat(area.getText(), equalTo("clicked 2"));
            assertTrue("Delay wasn't long enough " + t, t > 500);
            assertTrue("Delay was too long enough " + t, t < 1500);
        } 
        finally {
            e.stopStoringFiredEvents();
        }
    }

    @Test
    public void doubleClickTimeoutTest() {
        // given:
        FiredEvents e = FiredEvents.beginStoringFiredEvents();
        long t = System.currentTimeMillis();
        try {

            // when:
            ClickRobotImpl.CLICK_TO = 10;
            clickRobot.doubleClickOn(MouseButton.PRIMARY);

            // then:
            t = System.currentTimeMillis() - t;
            assertTrue("Delay was too long " + t, t < 450);
        } 
        finally {
            sleep(1000); // wait for events to happen...
            e.stopStoringFiredEvents();
        }
    }

}
