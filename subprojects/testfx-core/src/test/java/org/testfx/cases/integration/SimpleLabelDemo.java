/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.cases.integration;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.testfx.api.FxToolkit;
import org.testfx.util.WaitForAsyncUtils;

public class SimpleLabelDemo {

    //---------------------------------------------------------------------------------------------
    // MAIN METHOD.
    //---------------------------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {
        // Register Stages.
        Stage primaryStage = FxToolkit.registerPrimaryStage();
        Stage otherStage = FxToolkit.registerStage(() -> new Stage());

        // Setup, show and cleanup Application.
        Application demoApplication = FxToolkit.setupApplication(SimpleLabelApplication.class);
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);
        FxToolkit.cleanupApplication(demoApplication);

        // Setup and show Scene.
        Scene demoScene = FxToolkit.setupScene(() -> new SimpleLabelScene(300, 100));
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);

        // Setup and show Scene Root.
        Parent demoSceneRoot = FxToolkit.setupSceneRoot(() -> {
            Region sceneRoot = createSceneRoot(SimpleLabelDemo.class);
            sceneRoot.setPrefSize(300, 100);
            return sceneRoot;
        });
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);

        // Setup and show Scene Root with FXML file.
        Parent fxmlSceneRoot = FxToolkit.setupSceneRoot(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(SimpleLabelDemo.class.getResource("res/simpleLabel.fxml"));
            return uncheckException(() -> fxmlLoader.load());
        });
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);

        // Cleanup Stages.
        WaitForAsyncUtils.asyncFx(() -> otherStage.close());
        WaitForAsyncUtils.asyncFx(() -> primaryStage.close());
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
