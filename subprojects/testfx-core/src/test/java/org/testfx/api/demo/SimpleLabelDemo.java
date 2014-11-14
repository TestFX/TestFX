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

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
        // Register Stages.
        Stage primaryStage = FxLifecycle.registerPrimaryStage();
        Stage targetStage = FxLifecycle.registerTargetStage(() -> new Stage());

        // Setup, show and cleanup Application.
        Application demoApplication = FxLifecycle.setupApplication(SimpleLabelApplication.class);
        RunWaitUtils.sleep(3, TimeUnit.SECONDS);
        FxLifecycle.cleanupApplication(demoApplication);

        // Setup and show Scene.
        Scene demoScene = FxLifecycle.setupScene(() -> new SimpleLabelScene(300, 100));
        RunWaitUtils.sleep(3, TimeUnit.SECONDS);

        // Setup and show Scene Root.
        Parent demoSceneRoot = FxLifecycle.setupSceneRoot(() -> {
            Region sceneRoot = createSceneRoot(SimpleLabelDemo.class);
            sceneRoot.setPrefSize(300, 100);
            return sceneRoot;
        });
        RunWaitUtils.sleep(3, TimeUnit.SECONDS);

        // Setup and show Scene Root with FXML file.
        Parent fxmlSceneRoot = FxLifecycle.setupSceneRoot(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(SimpleLabelDemo.class.getResource("simpleLabel.fxml"));
            return uncheckException(() -> fxmlLoader.load());
        });
        RunWaitUtils.sleep(3, TimeUnit.SECONDS);

        // Cleanup Stages.
        RunWaitUtils.runLater(() -> targetStage.close());
        RunWaitUtils.runLater(() -> primaryStage.close());
    }

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static Region createEmptySceneRoot() {
        return new Region();
    }

    private static Region createSceneRoot(Class<?> cls) {
        return new StackPane(new Label(cls.getSimpleName()));
    }

    private static <T> T uncheckException(Callable<T> callable) {
        try {
            return callable.call();
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    //---------------------------------------------------------------------------------------------
    // STATIC CLASSES.
    //---------------------------------------------------------------------------------------------

    public static class SimpleLabelApplication extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            Region sceneRoot = createSceneRoot(getClass());
            Scene scene = new Scene(sceneRoot, 300, 100);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    public static class SimpleLabelScene extends Scene {
        public SimpleLabelScene(double width, double height) {
            super(createEmptySceneRoot(), width, height);
            Region sceneRoot = createSceneRoot(getClass());
            setRoot(sceneRoot);
        }
    }

}
