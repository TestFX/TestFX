package org.testfx.robot.impl;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxService;
import org.testfx.api.FxTiming;
import org.testfx.cases.InternalTestCaseBase;
import org.testfx.robot.BaseRobot;
import org.testfx.service.support.FiredEvents;

import static javafx.scene.input.KeyCode.A;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class KeyboardRobotImplIntegrationTest extends InternalTestCaseBase {

    KeyboardRobotImpl keyboardRobot;
    TextArea area;

    @Override
    public Node createComponent() {
        BaseRobot realBaseRobot = FxService.serviceContext().getBaseRobot();
        keyboardRobot = new KeyboardRobotImpl(new DelayedRobot(realBaseRobot, 500));
        KeyboardRobotImpl.KEYBOARD_TO = 2000;
        area = new TextArea();
        area.setPrefColumnCount(100);
        area.setPrefRowCount(20);
        area.setId("text");
        return area;
    }

    @BeforeClass
    public static void init() {
        KeyboardRobotImpl.debugKeys = false;
    }
    
    @AfterClass
    public static void tearDown() {
        FxTiming.reset();
        KeyboardRobotImpl.debugKeys = false;
    }

    @After
    public void after() {
        keyboardRobot.release();
        sleep(1000);
    }


    ///////// integration tests to verify timing ////////////
    @Test
    public void delayedPressTest() {
        // given:
        clickOn(area, MouseButton.PRIMARY);
        FiredEvents events = FiredEvents.beginStoringFiredEvents();
        long t = System.currentTimeMillis();

        // when:
        keyboardRobot.press(A);
        // sleep(2000);

        // then:
        try {
            t = System.currentTimeMillis() - t;
            assertTrue("Returned before event",
                    events.hasEvent(e -> e instanceof KeyEvent && ((KeyEvent) e).getCode() == KeyCode.A));
            assertTrue("Delay too short " + t, t > 450);
            assertTrue("Delay too long " + t, t < 1500);
        } 
        finally {
            events.stopStoringFiredEvents();
        }
    }

    @Test
    public void delayedPressDelTest() {
        // given:
        clickOn(area, MouseButton.PRIMARY);
        FiredEvents events = FiredEvents.beginStoringFiredEvents();
        long t = System.currentTimeMillis();

        // when:
        keyboardRobot.press(KeyCode.BACK_SPACE);
        // sleep(2000);

        // then:
        try {
            t = System.currentTimeMillis() - t;
            assertTrue("Returned before event",
                    events.hasEvent(e -> e instanceof KeyEvent && ((KeyEvent) e).getCode() == KeyCode.BACK_SPACE));
            assertTrue("Delay too short " + t, t > 450);
            assertTrue("Delay too long " + t, t < 1500);
        } 
        finally {
            events.stopStoringFiredEvents();
        }
    }

    @Test
    public void delayedReleaseTest() {
        // given:
        clickOn(area, MouseButton.PRIMARY);
        keyboardRobot.press(A);
        FiredEvents events = FiredEvents.beginStoringFiredEvents();
        long t = System.currentTimeMillis();

        // when:
        keyboardRobot.release(A); // needs to be pressed before
        // sleep(2000);

        // then:
        try {
            t = System.currentTimeMillis() - t;
            assertTrue("Returned before event",
                events.hasEvent(e -> e instanceof KeyEvent && ((KeyEvent) e).getCode() == KeyCode.A));
            assertTrue("Not enough events " + events.getEvents().size(), events.hasEvents(
                s -> s.filter(e -> (e instanceof KeyEvent) && ((KeyEvent) e).getCode() == KeyCode.A).count() >= 1));
            assertTrue("Delay too short " + t, t > 450);
            assertTrue("Delay too long " + t, t < 1500);
        } 
        finally {
            events.stopStoringFiredEvents();
        }
    }

    @Test
    public void delayedPressReleaseTest() {
        // given:
        clickOn(area, MouseButton.PRIMARY);
        FiredEvents events = FiredEvents.beginStoringFiredEvents();

        // when:
        keyboardRobot.press(A);
        keyboardRobot.release(A);
        // sleep(2000);

        // then:
        try {
            assertTrue("Returned before event",
                events.hasEvent(e -> e instanceof KeyEvent && ((KeyEvent) e).getCode() == KeyCode.A));
            assertTrue("Not enough events " + events.getEvents().size(), events.hasEvents(
                s -> s.filter(e -> (e instanceof KeyEvent) && ((KeyEvent) e).getCode() == KeyCode.A).count() >= 2));
            assertThat(area.getText(), equalTo("a"));
        } 
        finally {
            events.stopStoringFiredEvents();
        }
    }

    @Test
    public void delayedTimeoutTest() {
        // given:
        clickOn(area, MouseButton.PRIMARY);
        FiredEvents events = FiredEvents.beginStoringFiredEvents();
        KeyboardRobotImpl.KEYBOARD_TO = 10;
        long t = System.currentTimeMillis();

        // when:
        keyboardRobot.press(A); // >500ms
        keyboardRobot.release(A); // >500ms
        // sleep(2000);

        // then:
        try {
            assertTrue("Timeout seems not to work", System.currentTimeMillis() - t < 400);
            // events aren't guaranteed...
        } 
        finally {
            sleep(2000); // events should still be processed...
            events.stopStoringFiredEvents();
        }
    }

    @Test
    public void delayedKeyCombinationPressReleaseTest() {
        // Results may vary across platforms, as modifier might be handled
        // differently...
        // given:
        clickOn(area, MouseButton.PRIMARY);
        FiredEvents events = FiredEvents.beginStoringFiredEvents();

        // when:
        keyboardRobot.press(KeyCode.SHORTCUT, A);
        keyboardRobot.release(A, KeyCode.SHORTCUT);

        // then:
        try {
            assertTrue("Returned before event",
                    events.hasEvent(e -> e instanceof KeyEvent && ((KeyEvent) e).getCode() == KeyCode.A));
            // shortcut doesn't send typed on mac
            assertTrue("Not enough events " + events.getEvents().size(),
                    events.hasEvents(s -> s.filter(e -> e instanceof KeyEvent).count() >= 5));
        } 
        finally {
            events.stopStoringFiredEvents();
        }
    }
}
