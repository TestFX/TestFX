package org.testfx.framework.junit.integration;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxLifecycle;
import org.testfx.framework.junit.FxRobotTestBase;

import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.Commons.hasText;

public class FxRobotApplicationTest extends FxRobotTestBase {

    //---------------------------------------------------------------------------------------------
    // FIXTURES.
    //---------------------------------------------------------------------------------------------

    public static class DemoApplication extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            Button button = new Button("click me!");
            button.setOnAction((actionEvent) -> button.setText("clicked!"));
            primaryStage.setScene(new Scene(new StackPane(button), 100, 100));
            primaryStage.show();
        }
    }

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setup() throws Exception {
        FxLifecycle.setupApplication(DemoApplication.class);
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void should_contain_button() {
        // expect:
        verifyThat(".button", hasText("click me!"));
    }

    @Test
    public void should_click_on_button() {
        // when:
        clickOn(".button");

        // then:
        verifyThat(".button", hasText("clicked!"));
    }

}
