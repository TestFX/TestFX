package org.loadui.testfx;

import org.loadui.testfx.matchers.HasLabelMatcher;
import org.loadui.testfx.matchers.HasLabelStringMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class Matchers
{
	@Factory
	public static Matcher<Object> hasLabel( String label )
	{
		return new HasLabelStringMatcher( label );
	}

	@Factory
	public static Matcher<Object> hasLabel( Matcher<String> stringMatcher )
	{
		return new HasLabelMatcher( stringMatcher );
	}
}
