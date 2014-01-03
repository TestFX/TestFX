package org.loadui.testfx.controls;

import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import org.loadui.testfx.exceptions.NoNodesFoundException;

import static org.loadui.testfx.GuiTest.find;

public class TextInputControls
{
	public static void clearTextIn(TextInputControl textInputControl)
	{
		textInputControl.clear();
	}

	public static void clearTextIn(String textInputQuery)
	{
		Node node = find(textInputQuery);
		if (!(node instanceof TextInputControl)) {
			throw new NoNodesFoundException(textInputQuery + " selected " + node + " which is not a TextInputControl!");
		}

		TextInputControl t = ( TextInputControl )node;
		t.clear();
	}
}
