package org.loadui.testfx;

import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBoxBuilder;
import org.junit.Test;

import static javafx.scene.input.KeyCode.TAB;
import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.Commons.hasText;
import static org.loadui.testfx.controls.TextInputControls.clearTextIn;

public class TextInputControlsTest extends GuiTest
{

	public static final String TEXT_FIELD = ".text-field";

	@Override
	protected Parent getRootNode()
	{
		TextField textField = new TextField();
		return VBoxBuilder
				.create()
				.children( textField, new TextField() ).build();
	}

	@Test
	public void shouldClearText()
	{
		click( TEXT_FIELD ).type( "Some text" );
		verifyThat( TEXT_FIELD, hasText("Some text") );

        push(TAB); // To change focus from the TextField.

		clearTextIn( TEXT_FIELD );
		verifyThat( TEXT_FIELD, hasText("") );
	}
}
