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

public class TypeRobotIntegrationTest extends InternalTestCaseBase {

    TypeRobotImpl typeRobot;
    WindowFinder windowFinder;
    TextArea area;

    @BeforeClass
    public static void setupAll() {
        TypeRobotImpl.SLEEP_AFTER_KEY_CODE_IN_MILLIS = 0;
    }

    @AfterClass
    public static void tearDown() {
        TypeRobotImpl.SLEEP_AFTER_KEY_CODE_IN_MILLIS = TypeRobotImpl.SLEEP_AFTER_KEY_CODE_IN_MILLIS_DEFAULT;
    }

    @Before
    public void setup() throws Exception {
    }
    
    @Override
    public Node createComponent() {
        // waiting for events to happen -> real robot required
        typeRobot = new TypeRobotImpl(robotContext().getKeyboardRobot(), robotContext().getSleepRobot());
        area = new TextArea();
        area.setPrefColumnCount(100);
        area.setPrefRowCount(20);
        area.setId("text");
        return area;
    }

    @Test
    public void write_char() {
        // given:
        clickOn(MouseButton.PRIMARY);

        // when:
        typeRobot.push(KeyCode.A, KeyCode.B);

        // then:
        assertThat(area.getText(), equalTo("ab"));
    }

}
