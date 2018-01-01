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
package org.testfx.cases.issue;

import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

@Ignore
public class GlassRobotClipboardBug {

    public static FxRobot fx;

    /*
    static {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
    }
    */

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
        fx = new FxRobot();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            ListView<String> listView = new ListView<>();
            listView.getItems().addAll("item.1", "item.2", "item.3");
            listView.setOnDragDetected(event -> {
                ClipboardContent content = new ClipboardContent();
                content.putString("item.1"); // <-- BUG
                Dragboard dragboard = listView.startDragAndDrop(TransferMode.MOVE);
                dragboard.setContent(content);
                event.consume();
            });
            return new HBox(listView);
        });
        FxToolkit.setupStage(Stage::show);
    }

    @Test(timeout = 2000)
    public void should_first_try() {
        // when:
        fx.drag("item.1");
        fx.dropTo("item.3");
    }

    @Test(timeout = 2000)
    public void should_second_try() {
        // when:
        fx.drag("item.1");
        fx.dropTo("item.3");
    }

    @Test(timeout = 2000)
    public void should_third_try() {
        // when:
        fx.drag("item.1");
        fx.dropTo("item.3");
    }

}
