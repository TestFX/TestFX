package org.loadui.testfx;

import javafx.scene.Node;
import org.hamcrest.Matcher;

public class Assertions
{
	public static void assertNodeExists( Matcher<?> matcher )
	{
		GuiTest.find( ( Matcher<Node> )matcher );
	}

	public static void assertNodeExists( String query )
	{
		GuiTest.find( query );
	}
}
