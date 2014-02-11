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
import javafx.stage.Stage;

import org.junit.Before;
import org.loadui.testfx.framework.app.StageSetupCallback;
import org.loadui.testfx.framework.junit.AppRobotTestBase;

public abstract class GuiTest extends AppRobotTestBase implements StageSetupCallback {

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setupGuiTest() throws Exception {
        setupApplication();
        setupStages(this);
    }

    // Runs in JavaFX Application Thread.
    public void setupStages(Stage primaryStage) {
        Parent sceneRootNode = getRootNode();
        Scene scene = new Scene(sceneRootNode, 600, 400);
        primaryStage.setScene(scene);
    }

    //---------------------------------------------------------------------------------------------
    // PROTECTED METHODS.
    //---------------------------------------------------------------------------------------------

    // Runs in JavaFX Application Thread.
    protected abstract Parent getRootNode();

}
