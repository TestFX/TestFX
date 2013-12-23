package org.loadui.testfx.controls.impl;

import javafx.scene.Node;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.loadui.testfx.exceptions.NoNodesFoundException;

import static org.loadui.testfx.GuiTest.find;

/**
 * @author renato
 */
public class EnabledMatcher extends TypeSafeMatcher<Object>
{
	private boolean shouldBeDisabled;

	private EnabledMatcher( boolean shouldBeEnabled )
	{
		this.shouldBeDisabled = shouldBeEnabled;
	}

	@Override
	public void describeTo( Description desc )
	{
		desc.appendText( "Node should be " + ( shouldBeDisabled ? "Dis" : "En" ) + "abled" );
	}

	@Factory
	public static Matcher<Object> enabled()
	{
		return new EnabledMatcher( false );
	}

	@Factory
	public static Matcher<Object> disabled()
	{
		return new EnabledMatcher( true );
	}

	@Override
	public boolean matchesSafely( Object target )
	{
		if( target instanceof String )
		{
			try
			{
				Node node = find( ( String )target );
				return node.isDisabled() == shouldBeDisabled;
			}
			catch( NoNodesFoundException e )
			{
				return false;
			}
		}
		else if( target instanceof Node )
		{
			return ( ( Node )target ).isDisabled() == shouldBeDisabled;
		}
		return false;
	}

}
