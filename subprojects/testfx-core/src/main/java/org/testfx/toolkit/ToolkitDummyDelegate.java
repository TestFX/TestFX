package org.testfx.toolkit;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ToolkitDummyDelegate extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle(getClass().getSimpleName());
    }
}
