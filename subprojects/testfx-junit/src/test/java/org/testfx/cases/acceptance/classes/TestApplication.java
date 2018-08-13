package org.testfx.cases.acceptance.classes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TestApplication extends Application {

    public TestApplication.AppState state = AppState.CREATED;
    public MouseEvent btnEvent;

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (state != AppState.INITIALIZED) {
            throw new RuntimeException(
                    "LifeCycle Exception. Cannot go from " + state.name() + " to " + AppState.STARTED.name());
        }
        state = AppState.STARTED;

        // Parameters
        Parameters param = this.getParameters();
        String initialText = "InitialText";
        if (param.getRaw().size() > 0) {
            initialText = param.getRaw().get(0);
        }

        primaryStage.setTitle("MyTestApp");
        TextField tf = new TextField(initialText);
        tf.setId("myTestAppTF");
        Button btn = new Button();
        btn.setText("button");
        btn.setId("myTestAppBtn");
        btn.setOnAction(e -> {
            tf.setText("clickedText");
        });
        btn.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> btnEvent = e);

        HBox root = new HBox();
        root.getChildren().addAll(btn, tf);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        if (state != AppState.CREATED) {
            throw new RuntimeException(
                    "LifeCycle Exception. Cannot go from " + state.name() + " to " + AppState.INITIALIZED.name());
        }
        state = AppState.INITIALIZED;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (state != AppState.STARTED) {
            throw new RuntimeException(
                    "LifeCycle Exception. Cannot go from " + state.name() + " to " + AppState.STOPPED.name());
        }
        state = AppState.STOPPED;
    }

    public static enum AppState {
        CREATED, INITIALIZED, STARTED, STOPPED;
    }

}