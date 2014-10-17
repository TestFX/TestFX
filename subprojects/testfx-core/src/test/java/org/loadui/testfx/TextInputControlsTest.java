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
package org.loadui.testfx;

import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.junit.Test;

import static javafx.scene.input.KeyCode.TAB;
import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.Commons.hasText;
import static org.loadui.testfx.controls.TextInputControls.clearTextIn;

public class TextInputControlsTest extends GuiTest {

    public static final String TEXT_FIELD = ".text-field";

  @Override
  protected Parent getRootNode() {
    return  new VBox(new TextField());
  }

    @Test
    public void shouldClearText() {
        clickOn(TEXT_FIELD).write("Some text");
        verifyThat(TEXT_FIELD, hasText("Some text"));

        push(TAB); // To change focus from the TextField.

        clearTextIn(TEXT_FIELD);
        verifyThat(TEXT_FIELD, hasText(""));
    }

}
