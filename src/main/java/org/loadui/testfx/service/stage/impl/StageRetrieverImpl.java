/*
 * Copyright 2013 SmartBear Software
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
package org.loadui.testfx.service.stage.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.loadui.testfx.service.stage.SceneProvider;
import org.loadui.testfx.service.stage.StageRetriever;
import org.loadui.testfx.utils.FXTestUtils;
import org.loadui.testfx.utils.FxLauncherUtils;

public class StageRetrieverImpl implements StageRetriever {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    static final String STAGE_TITLE = "";
    static final Integer SCENE_RETRIEVE_TIMEOUT_SECONDS = 25;
    static final Integer STAGE_SETUP_TIMEOUT_SECONDS = 10;

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private Stage primaryStage = null;

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public Stage retrieve() throws Throwable {
        // TODO: Document what happens if the next line is missing.
        try {
            primaryStage = FxLauncherUtils.launchOnce(SCENE_RETRIEVE_TIMEOUT_SECONDS,
                TimeUnit.SECONDS);
        }
        catch (TimeoutException exception) {
            throw new RuntimeException(exception);
        }
        return primaryStage;
    }

    public Stage retrieveWithScene(final SceneProvider sceneProvider) throws Throwable {
        final Stage stage = this.retrieve();
        // TODO: Document what happens if the next line is missing.
        FXTestUtils.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                Scene scene = setupScene(sceneProvider);
                setupStage(stage, scene);
            }
        }, STAGE_SETUP_TIMEOUT_SECONDS);
        return stage;
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Scene setupScene(SceneProvider sceneProvider) {
        Parent sceneRootNode = sceneProvider.setupSceneRoot();
        return sceneProvider.setupScene(sceneRootNode);
    }

    private void setupStage(Stage stage, Scene scene) {
        stage.setTitle(STAGE_TITLE);
        stage.setScene(scene);
    }

}
