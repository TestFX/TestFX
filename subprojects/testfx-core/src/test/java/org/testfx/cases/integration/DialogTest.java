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

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.layout.StackPane;

import org.junit.Ignore;
import org.junit.Test;
import org.testfx.cases.InternalTestCaseBase;

import static org.testfx.assertions.api.Assertions.assertThat;

public class DialogTest extends InternalTestCaseBase {


    @Override
    public Node createComponent() {
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
    }
    

    @Test
    @Ignore("Flaky")
    public void should_lookup_dialog() {
        clickOn("#openDialog");
        //target window hits the right window (always)
        /*robotContext().getWindowFinder().targetWindow("Dialog A");
        Window w=robotContext().getWindowFinder().targetWindow();
        String s=((Stage)w).getTitle();
        System.out.println(s); */
        assertThat(targetWindow("Dialog A").lookup(".button").queryButton()).hasText("Dialog A");
    }
    
}
