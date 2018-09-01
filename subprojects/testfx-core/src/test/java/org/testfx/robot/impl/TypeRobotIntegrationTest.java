package org.testfx.robot.impl;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.cases.InternalTestCaseBase;
import org.testfx.service.finder.WindowFinder;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TypeRobotIntegrationTest extends InternalTestCaseBase {

    TypeRobotImpl typeRobot;
    WindowFinder windowFinder;
    TextArea area;

    @BeforeClass
    public static void setupAll() {
        KeyboardRobotImpl.KEYBOARD_TO = 2000;
        TypeRobotImpl.SLEEP_AFTER_KEY_CODE_IN_MILLIS = 0;
    }

    @AfterClass
    public static void tearDown() {
        KeyboardRobotImpl.KEYBOARD_TO = KeyboardRobotImpl.KEYBOARD_DEFAULT_TO;
        TypeRobotImpl.SLEEP_AFTER_KEY_CODE_IN_MILLIS = TypeRobotImpl.SLEEP_AFTER_KEY_CODE_IN_MILLIS_DEFAULT;
    }

    @Before
    public void setup() throws Exception {
    }
    
    @Override
    public Node createComponent() {
        KeyboardRobotImpl.KEYBOARD_TO = 2000;
        // waiting for events to happen -> real robot required
        typeRobot = new TypeRobotImpl(robotContext().getKeyboardRobot(), robotContext().getSleepRobot());
        typeRobot = new TypeRobotImpl(new KeyboardRobotImpl(new DelayedRobot(robotContext().getBaseRobot(), 500)), 
                robotContext().getSleepRobot());
        area = new TextArea();
        area.setPrefColumnCount(100);
        area.setPrefRowCount(20);
        area.setId("text");
        return area;
    }

    @Test
    public void pushTest() {
        // given:
        clickOn(MouseButton.PRIMARY);
        long t = System.currentTimeMillis();

        // when:
        typeRobot.push(KeyCode.A, KeyCode.B);

        // then:
        t = System.currentTimeMillis() - t;
        assertThat(area.getText(), equalTo("ab"));
        assertTrue("Delay wasn't long enough " + t, t > 1000); //two keys
        assertTrue("Delay was too long enough " + t, t < 3000); 
    }

    @Test
    public void pushTimeoutTest() {
        KeyboardRobotImpl.KEYBOARD_TO = 50;
        // given:
        clickOn(MouseButton.PRIMARY);
        long t = System.currentTimeMillis();

        // when:
        typeRobot.push(KeyCode.A, KeyCode.B);

        // then:
        t = System.currentTimeMillis() - t;
        try {
            assertTrue("Timeout didn't work " + t, t < 500);  //two keys
        }
        finally {
            sleep(1000); // wait for keys to be pressed
        }
    }
}
