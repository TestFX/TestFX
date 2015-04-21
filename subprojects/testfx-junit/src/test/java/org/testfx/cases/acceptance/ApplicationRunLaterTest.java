package org.testfx.cases.acceptance;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

public class ApplicationRunLaterTest extends ApplicationTest {
    @Override
    public void init() throws Exception {
        FxToolkit.registerStage(() -> new Stage());
    }

    @Override
    public void start(Stage stage) {
        Button button = new Button("click me!");
        button.setOnAction((actionEvent) -> Platform.runLater(() -> button.setText("clicked!")));
        stage.setScene(new Scene(new StackPane(button), 100, 100));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    @Test
    public void should_contain_button() {
        verifyThat(".button", hasText("click me!"));
    }

    @Test
    public void should_click_on_button() throws Exception {
        clickOn(".button");
        waitForRunLater();
        verifyThat(".button", hasText("clicked!"));
    }
}
