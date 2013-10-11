package org.loadui.testfx;

import org.hamcrest.Matcher;

import static org.junit.Assert.fail;

public class Assertions
{
	public static void assertNodeExists( Matcher<?> matcher )
	{
		if( GuiTest.find( ( Matcher<Object> )matcher ) == null )
			fail( "No node matches " + matcher );
	}

	public static void assertNodeExists( String query )
	{
		if( GuiTest.find( query ) == null )
			fail( "No node matches '" + query + "'" );
	}


}
