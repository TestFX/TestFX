package org.loadui.testfx;

import javafx.scene.Node;
import org.hamcrest.Matcher;

import static org.loadui.testfx.GuiTest.find;
import static org.junit.Assert.fail;

public class Assertions
{
	public static void assertNodeExists( Matcher<?> matcher )
	{
		if( GuiTest.find( (Matcher<Node>) matcher ) == null )
			fail("No node matches " + matcher);
	}

	public static void assertNodeExists( String query )
	{
		if( GuiTest.find( query ) == null )
			fail("No node matches '" + query +"'" );
	}
}
