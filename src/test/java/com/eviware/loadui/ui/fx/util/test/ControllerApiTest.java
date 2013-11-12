/*
 * Copyright 2013 SmartBear Software
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the Licence for the specific language governing permissions and limitations
 * under the Licence.
 */
package com.eviware.loadui.ui.fx.util.test;

import static org.loadui.testfx.Assertions.assertNodeExists;
import static org.loadui.testfx.GuiTest.find;
import static org.loadui.testfx.GuiTest.showNodeInStage;
import static org.loadui.testfx.Matchers.hasLabel;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import org.loadui.testfx.FXScreenController;
import org.loadui.testfx.FXTestUtils;
import org.loadui.testfx.GlassScreenController;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;
import javafx.application.Application;
import javafx.scene.SceneBuilder;
import javafx.scene.control.*;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.common.util.concurrent.SettableFuture;

@Category( TestFX.class )
public class ControllerApiTest
{
	private static final GuiTest controller = new GuiTest();
    private static VBox root = VBoxBuilder
            .create()
            .children(ButtonBuilder.create().id("button1").text("Button A").build(),
                    ButtonBuilder.create().id("button2").text("Button B").build(),
                    TextFieldBuilder.create().id("text").build()).build();

	@BeforeClass
	public static void createWindow() throws Throwable
	{
        System.out.println("-1 -1  -1");
        showNodeInStage(root);
	}

	@Test
	public void shouldTypeString()
	{
		final String text = "H3llo W0rld";
        TextField textField = find( "#text" );

        Platform.runLater( new Runnable() {
            @Override
            public void run() {
                controller.click("#text").type( text );
            }
        } );

        controller.sleep(3000);

		assertThat( textField.getText(), is( text ) );
	}

}
