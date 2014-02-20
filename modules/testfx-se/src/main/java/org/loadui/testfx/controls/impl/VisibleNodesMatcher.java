package org.loadui.testfx.controls.impl;

import org.loadui.testfx.exceptions.NoNodesFoundException;
import javafx.scene.Node;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

import static org.loadui.testfx.GuiTest.find;

public class VisibleNodesMatcher extends TypeSafeMatcher<Object>
{
	public void describeTo( Description desc )
	{
		desc.appendText("visible");
	}

	@Factory
	public static Matcher<Object> visible()
	{
		return new VisibleNodesMatcher();
	}

	@Override
	public boolean matchesSafely( Object target )
	{
		if( target instanceof String )
		{
			try
			{
				Node node = find( (String) target );
				return node.isVisible();
			} catch( NoNodesFoundException e )
			{
				return false;
			}
		} else if( target instanceof Node )
		{
			return ((Node) target).isVisible();
		}
		return false;
	}
}
