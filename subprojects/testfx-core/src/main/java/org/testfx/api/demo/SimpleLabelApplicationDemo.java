package org.testfx.api.demo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.loadui.testfx.utils.RunWaitUtils;
import org.testfx.api.FxLifecycle;

public class SimpleLabelApplicationDemo {

    //---------------------------------------------------------------------------------------------
    // MAIN METHOD.
    //---------------------------------------------------------------------------------------------

    public static void main(String[] args) throws TimeoutException {
        Stage primaryStage = FxLifecycle.launchPrimaryStage();
        Application demoApplication = FxLifecycle.setupApplication(SimpleLabelApplication.class);

        RunWaitUtils.sleep(3, TimeUnit.SECONDS);

        FxLifecycle.cleanupApplication(demoApplication);
        RunWaitUtils.runLaterAndWait(1000, () -> primaryStage.close());
    }

    //---------------------------------------------------------------------------------------------
    // STATIC CLASSES.
    //---------------------------------------------------------------------------------------------

    public static class SimpleLabelApplication extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            StackPane sceneRoot = new StackPane(new Label(getClass().getSimpleName()));
            Scene scene = new Scene(sceneRoot, 300, 100);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

}
