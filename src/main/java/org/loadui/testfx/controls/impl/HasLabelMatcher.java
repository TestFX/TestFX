package org.loadui.testfx.controls.impl;

import javafx.scene.control.Labeled;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

import static org.loadui.testfx.GuiTest.find;

public class HasLabelMatcher extends TypeSafeMatcher<Object>
{
	private final Matcher<String> matcher;

	public HasLabelMatcher( Matcher<String> matcher )
	{
		this.matcher = matcher;
	}

	public void describeTo( Description desc )
	{
		desc.appendText( "Node should match " + matcher );
	}

	@Override
	public boolean matchesSafely( Object target )
	{
		if( target instanceof String )
		{
			return matcher.matches(((Labeled) find( (String) target )).getText() );
		} else if( target instanceof Labeled )
		{
			return matcher.matches( ((Labeled) target).getText() );
		}
		return false;
	}
}