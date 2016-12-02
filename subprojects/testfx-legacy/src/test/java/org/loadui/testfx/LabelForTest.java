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
package org.loadui.testfx;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.PasswordFieldBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.layout.GridPane;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.categories.TestFX;

import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.Commons.hasText;
import static org.loadui.testfx.controls.Commons.nodeLabeledBy;

@Category(TestFX.class)
public class LabelForTest extends GuiTest {

    @Override
    protected Parent getRootNode() {
        TextField uText = TextFieldBuilder.create().id("uname").build();
        TextField pText = PasswordFieldBuilder.create().id("pword").build();
        Label u = LabelBuilder.create().text("User name").labelFor(uText).build();
        Label p = LabelBuilder.create().text("Password").labelFor(pText).build();

        GridPane grid = new GridPane();
        grid.add(u, 0, 0);
        grid.add(uText, 1, 0);
        grid.add(p, 0, 1);
        grid.add(pText, 1, 1);
        return grid;
    }

    @Test
    public void shouldClickButton() {
        clickOn(nodeLabeledBy("User name")).write("Steve");
        clickOn(nodeLabeledBy("Password")).write("duke4ever");

        verifyThat("#pword", hasText("duke4ever"));
    }

}
