/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2018 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.cases;

import java.util.List; //checkstyle needs one line between javafx and any group before it

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import org.junit.BeforeClass;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

public abstract class TestCaseBase extends FxRobot {

    // Provide some background component for the basic test, that can fetch
    // some key and mouse events...
    @BeforeClass
    public static void baseSetupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupStage(stage -> {
            Region region = new Region();
            List.class.getName(); //and we need to use that package (check style)
            String bg = "-fx-background-color: magenta;";
            region.setStyle(bg);
    
            VBox box = new VBox(region);
            box.setPadding(new Insets(10));
            box.setSpacing(10);
            VBox.setVgrow(region, Priority.ALWAYS);
    
            StackPane sceneRoot = new StackPane(box);
            Scene scene = new Scene(sceneRoot, 300, 100);
            stage.setScene(scene);
            stage.show();
        });
    }

}
