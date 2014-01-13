package org.loadui.testfx.controls;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;
import org.loadui.testfx.exceptions.NoNodesFoundException;

import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.BACK_SPACE;
import static javafx.scene.input.KeyCode.CONTROL;
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

		TextInputControl textControl = ( TextInputControl )node;
        if( textControl.getLength() == 0 )
            return;

        GuiTest fx = new GuiTest() {
            @Override
            protected Parent getRootNode() {
                return null;
            }
        };

        fx.click( textControl );
        fx.push(CONTROL, A).push(BACK_SPACE);
	}
}
