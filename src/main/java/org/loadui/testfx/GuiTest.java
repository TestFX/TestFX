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
package org.loadui.testfx;

import javafx.scene.Parent;
import javafx.scene.Scene;

import org.junit.Before;
import org.loadui.testfx.framework.FxRobot;
import org.loadui.testfx.service.stage.SceneProvider;
import org.loadui.testfx.service.stage.StageRetriever;
import org.loadui.testfx.service.stage.impl.StageRetrieverImpl;

public abstract class GuiTest extends FxRobot implements SceneProvider {

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public GuiTest() {
        super();
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setupStage() throws Throwable {
        StageRetriever stageRetriever = new StageRetrieverImpl();
        stageRetriever.retrieveWithScene(this);
    }

    protected abstract Parent getRootNode();

    // Runs in JavaFX Application Thread.
    public Scene setupScene(Parent sceneRootNode) {
        return new Scene(sceneRootNode, 600, 400);
    }

    // Runs in JavaFX Application Thread.
    public Parent setupSceneRoot() {
        return getRootNode();
    }

}
