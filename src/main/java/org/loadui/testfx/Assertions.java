package org.loadui.testfx;

import com.google.common.base.Predicate;
import javafx.scene.Node;
import org.hamcrest.Matcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.loadui.testfx.GuiTest.find;
import static org.loadui.testfx.utils.FXTestUtils.releaseButtons;

public class Assertions
{
	@SuppressWarnings( "unchecked" )
	public static void assertNodeExists( Matcher<?> matcher )
	{
		find((Matcher<Object>) matcher);
	}

	public static void assertNodeExists( String query )
	{
		find(query);
	}

	public static <T> void verifyThat( T value, Matcher<? super T> matcher )
	{
		verifyThat( "", value, matcher );
	}

	public static <T> void verifyThat( String reason, T value, Matcher<? super T> matcher )
	{
		try
		{
			assertThat( reason, value, matcher );
		}
		catch( AssertionError e )
		{
            releaseButtons();
			throw new AssertionError( e.getMessage() + " Screenshot saved as " + GuiTest.captureScreenshot().getAbsolutePath() , e );
		}
	}

    public static <T extends Node> void verifyThat( String query, Predicate<T> predicate )
    {
            T node = find( query );
            if(! predicate.apply( node ) )
            {
                releaseButtons();
                throw new AssertionError( "Predicate failed for query '" + query + "'. Screenshot saved as " + GuiTest.captureScreenshot().getAbsolutePath() );
            }
    }

    public static <T extends Node> void verifyThat( T node, Predicate<T> predicate )
    {
            if(! predicate.apply( node ) )
            {
                releaseButtons();
                throw new AssertionError( "Predicate failed for '" + node + "'. Screenshot saved as " + GuiTest.captureScreenshot().getAbsolutePath() );
            }
    }
}
