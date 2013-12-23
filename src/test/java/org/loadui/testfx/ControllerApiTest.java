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
package org.loadui.testfx;

import com.google.common.base.Predicate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.layout.VBoxBuilder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.categories.TestFX;

import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.Commons.hasText;

@Category( TestFX.class )
public class ControllerApiTest extends GuiTest
{
	@Override
	protected Parent getRootNode()
	{
		return VBoxBuilder
				.create()
				.children( ButtonBuilder.create().id( "button1" ).text( "Button A" ).defaultButton(true).build(),
						ButtonBuilder.create().id( "button2" ).text( "Button B" ).build(),
						TextFieldBuilder.create().id( "text" ).build() ).build();
	}

	@Test
	public void shouldTypeString()
	{
		final String text = "H3llo W0rld";

		click( "#text" ).type( text );

		verifyThat("#text", hasText(text));
	}

	@Test
	public void shouldClickButton()
	{
		final Button button = find( "#button1" );
		button.setOnAction( new EventHandler<ActionEvent>()
		{
			@Override
			public void handle( ActionEvent actionEvent )
			{
				button.setText( "Was clicked" );
			}
		} );

		click( "Button A" );

		verifyThat( "#button1", hasText( "Was clicked" ) );
	}

    @Test
    public void shouldClickButton_usingLambda()
    {
        final Button button = find( "#button1" );
        button.setOnAction( new EventHandler<ActionEvent>()
        {
            @Override
            public void handle( ActionEvent actionEvent )
            {
                button.setText( "Was clicked" );
            }
        } );

        // In Java 8, this can be written as click( (Button b) -> b.isDefaultButton() )
        // To stay compatible with both Java 7 and 8, this test uses Java 7 code.
        click( new Predicate<Button>() {
            @Override
            public boolean apply(Button b) {
                return b.isDefaultButton();
            }
        } );

        verifyThat( "#button1", hasText( "Was clicked" ) );
    }
}
