package org.loadui.testfx;

import org.hamcrest.Matcher;

import static org.hamcrest.MatcherAssert.assertThat;

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

	public static <T> void verifyThat(T value, Matcher<T> matcher)
	{
		try
		{
			assertThat( value, matcher );
		} catch( AssertionError e )
		{
			throw new AssertionError( "Screenshot saved as " + GuiTest.captureScreenshot().getAbsolutePath() , e );
		}
	}
}
