package org.testfx.api.integration;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.framework.robot.FxRobot;
import org.loadui.testfx.framework.robot.impl.FxRobotImpl;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.api.FxLifecycle.registerPrimaryStage;
import static org.testfx.api.FxLifecycle.setupApplication;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isNotNull;
import static org.testfx.matcher.base.NodeMatchers.isNull;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class FxAssertDemoTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    FxRobot fx = new FxRobotImpl();

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupSpec() throws Exception {
        registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        setupApplication(DemoApplication.class);
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void nonExistingNode_is_null() {
        // expect:
        verifyThat("#nonExistingNode", isNull());
    }

    @Test
    public void button_is_not_null() {
        // expect:
        verifyThat("#button", isNotNull());
    }

    @Test
    public void button_is_enabled() {
        // expect:
        verifyThat("#button", isEnabled());
    }

    @Test
    public void button_is_visible() {
        // expect:
        verifyThat("#button", isVisible());
    }

    @Test
    public void button_has_label() {
        // when:
        fx.clickOn("#button");

        // then:
        verifyThat("#button", hasText("clicked!"));
    }

    //@Test
    //public void foo() {
    //    verifyThat("#button", nodeMatcher(TreeView.class, "is true", (TreeView input) -> true));
    //}

    //---------------------------------------------------------------------------------------------
    // TEST FIXTURES.
    //---------------------------------------------------------------------------------------------

    public static class DemoApplication extends Application {
        @Override
        public void start(Stage stage) {
            final Button button = new Button("click me!");
            button.setId("button");
            button.setOnAction(actionEvent -> button.setText("clicked!"));
            Scene scene = new Scene(button, 600, 400);
            stage.setScene(scene);
            stage.setTitle(this.getClass().getSimpleName());
            stage.show();
        }
    }

}
