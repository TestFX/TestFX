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
package org.testfx.cases.integration;

import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.cases.TestCaseBase;
import org.testfx.framework.junit.TestFXRule;

import static org.testfx.assertions.api.Assertions.assertThat;

public class DialogTest extends TestCaseBase {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    @Before
    public void setup() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            Button openDialogButton = new Button("Open Dialog");
            openDialogButton.setId("openDialog");
            openDialogButton.setOnAction(event -> {
                Dialog<Boolean> dialog = new Dialog<>();
                dialog.setTitle("Dialog A");
                dialog.setHeaderText("Dialog A: Header Text");
                Button dialogAButton = new Button("Dialog A");
                dialog.getDialogPane().setContent(new StackPane(dialogAButton));
                dialog.show();
            });
            StackPane root = new StackPane(openDialogButton);
            root.setPrefSize(500, 500);
            return new StackPane(root);
        });
        FxToolkit.setupStage(Stage::show);
    }

    @Test
    @Ignore
    public void should_lookup_dialog() {
        clickOn("#openDialog");
        assertThat(targetWindow("Dialog A").lookup(".button").queryButton()).hasText("Dialog A");
    }
}
