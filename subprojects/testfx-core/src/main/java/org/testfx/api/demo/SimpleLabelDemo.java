/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.testfx.api.demo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.loadui.testfx.utils.RunWaitUtils;
import org.testfx.api.FxLifecycle;

public class SimpleLabelDemo {

    //---------------------------------------------------------------------------------------------
    // MAIN METHOD.
    //---------------------------------------------------------------------------------------------

    public static void main(String[] args) throws TimeoutException {
        // Setup Stage.
        Stage primaryStage = FxLifecycle.setupPrimaryStage();
        Stage targetStage = FxLifecycle.setupTargetStage(FxLifecycle.setup(() -> new Stage()));

        // Setup, show and cleanup Application.
        Application demoApplication = FxLifecycle.setupApplication(SimpleLabelApplication.class);
        RunWaitUtils.sleep(3, TimeUnit.SECONDS);
        FxLifecycle.cleanupApplication(demoApplication);

        // Setup and show Scene.
        Scene demoScene = FxLifecycle.setupScene(() -> new SimpleLableScene(new Region(), 300, 100));
        RunWaitUtils.sleep(3, TimeUnit.SECONDS);

        // Cleanup Stage.
        RunWaitUtils.runLaterAndWait(1000, () -> targetStage.close());
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

    public static class SimpleLableScene extends Scene {
        public SimpleLableScene(Parent root, double width, double height) {
            super(root, width, height);
            StackPane sceneRoot = new StackPane(new Label(getClass().getSimpleName()));
            setRoot(sceneRoot);
        }
    }

}
