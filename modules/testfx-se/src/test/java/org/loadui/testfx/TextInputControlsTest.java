package org.loadui.testfx;

import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBoxBuilder;
import org.junit.Test;
import org.loadui.testfx.controls.Commons;
import org.loadui.testfx.controls.TextInputControls;

import static javafx.scene.input.KeyCode.TAB;

public class TextInputControlsTest extends GuiTest
{

	public static final String TEXT_FIELD = ".text-field";

	@Override
	protected Parent getRootNode()
	{
		TextField textField = new TextField();
		return VBoxBuilder
				.create()
				.children( textField ).build();
	}

	@Test
	public void shouldClearText()
	{
		click( TEXT_FIELD ).type( "Some text" );
		Assertions.verifyThat(TEXT_FIELD, Commons.hasText("Some text"));

		push(TAB); // To change focus from the TextField.

		TextInputControls.clearTextIn(TEXT_FIELD);
		Assertions.verifyThat(TEXT_FIELD, Commons.hasText(""));
	}
}
