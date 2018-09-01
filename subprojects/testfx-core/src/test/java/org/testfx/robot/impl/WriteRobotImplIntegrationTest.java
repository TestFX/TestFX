package org.testfx.robot.impl;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxService;
import org.testfx.cases.InternalTestCaseBase;
import org.testfx.robot.BaseRobot;
import org.testfx.service.support.FiredEvents;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class WriteRobotImplIntegrationTest extends InternalTestCaseBase {

    WriteRobotImpl writeRobot;
    TextArea area;

    @Override
    public Node createComponent() {
        BaseRobot realBaseRobot = FxService.serviceContext().getBaseRobot();
        writeRobot = new WriteRobotImpl(new DelayedRobot(realBaseRobot, 500), robotContext().getSleepRobot(),
                FxService.serviceContext().getWindowFinder());
        WriteRobotImpl.CHAR_TO = 2000;
        area = new TextArea();
        area.setPrefColumnCount(100);
        area.setPrefRowCount(20);
        area.setId("text");
        return area;
    }

    @BeforeClass
    public static void setupAll() {
        WriteRobotImpl.debug = false;
        WriteRobotImpl.SLEEP_AFTER_CHARACTER_IN_MILLIS = 0;
    }

    @AfterClass
    public static void tearDown() {
        WriteRobotImpl.SLEEP_AFTER_CHARACTER_IN_MILLIS = WriteRobotImpl.SLEEP_AFTER_CHARACTER_IN_MILLIS_DEFAULT;
        WriteRobotImpl.debug = false;
        WriteRobotImpl.CHAR_TO = WriteRobotImpl.CHAR_TO_DEFAULT;
    }

    @Test
    public void writeCharTest() {
        // given:
        clickOn(getComponent(), MouseButton.PRIMARY);

        FiredEvents e = FiredEvents.beginStoringFiredEvents();
        long t = System.currentTimeMillis();
        try {
            // when:
            writeRobot.write('A');

            // then:
            assertThat(area.getText(), equalTo("A"));
            assertTrue("Delay wasn't long enough " + (System.currentTimeMillis() - t),
                    System.currentTimeMillis() - t > 500);
        } 
        finally {
            e.stopStoringFiredEvents();
        }
    }

    @Test
    public void writeCharTimeoutTest() {
        // given:
        clickOn(getComponent(), MouseButton.PRIMARY);

        FiredEvents e = FiredEvents.beginStoringFiredEvents();
        long t = System.currentTimeMillis();
        try {
            // when:
            WriteRobotImpl.CHAR_TO = 10;
            writeRobot.write('A');

            // then:
            assertTrue("Delay was too long " + (System.currentTimeMillis() - t), System.currentTimeMillis() - t < 450);
        } 
        finally {
            sleep(1000); // wait for events to happen...
            e.stopStoringFiredEvents();
        }
    }

    @Test
    public void writeStringTest() {
        // given:
        String test = "Aa";
        clickOn(getComponent(), MouseButton.PRIMARY);
        long t = System.currentTimeMillis();

        FiredEvents e = FiredEvents.beginStoringFiredEvents();
        try {
            // when:
            writeRobot.write(test);

            // then:
            assertThat(area.getText(), equalTo(test));
            assertTrue("Delay wasn't long enough " + (System.currentTimeMillis() - t),
                    System.currentTimeMillis() - t > 500);
        } 
        finally {
            e.stopStoringFiredEvents();
        }
    }

    @Test
    public void writeStringTOTest() {
        // given:
        String test = "Aa";
        clickOn(getComponent(), MouseButton.PRIMARY);
        long t = System.currentTimeMillis();

        FiredEvents e = FiredEvents.beginStoringFiredEvents();
        try {
            // when:
            WriteRobotImpl.CHAR_TO = 10;
            writeRobot.write(test);

            // then:
            assertTrue("Delay was too long " + (System.currentTimeMillis() - t), System.currentTimeMillis() - t < 450);
        } 
        finally {
            sleep(1000); // wait for events to happen...
            e.stopStoringFiredEvents();
        }
    }
}
