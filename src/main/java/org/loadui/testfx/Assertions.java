package org.loadui.testfx;

import org.hamcrest.Matcher;

public class Assertions
{
	public static void assertNodeExists( Matcher<?> matcher )
	{
		GuiTest.find( ( Matcher<Object> )matcher );
	}

	public static void assertNodeExists( String query )
	{
		GuiTest.find( query );
	}
}
