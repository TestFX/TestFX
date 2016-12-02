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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.layout.VBoxBuilder;

import org.junit.Test;

import static org.loadui.testfx.Assertions.verifyThat;

// verifyThat supports lambda expressions, but to keep Java 7 compatibility, we don't use them here.
public class AssertionsTest extends GuiTest {

  @Test
  public void shouldSupportPredicatesForQueries() {
    verifyThat("Button A", Button::isDefaultButton);
  }

  @Test
  public void shouldSupportPredicatesForQueries2() {
    try {
      verifyThat("Button A", Button::isCancelButton);
    }
    catch (AssertionError e) {
      return;
    }
    throw new AssertionError("verifyThat should have failed.");
  }

  @Test
  public void shouldSupportPredicatesForNodes() {
    Button b = find("Button A");

    verifyThat(b, Button::isDefaultButton);
  }

  @Override
  protected Parent getRootNode() {
    return VBoxBuilder
        .create()
        .children(ButtonBuilder.create().id("button1").text("Button A").defaultButton(true).build(),
            ButtonBuilder.create().id("button2").text("Button B").build(),
            TextFieldBuilder.create().id("text").build()).build();
  }

}
