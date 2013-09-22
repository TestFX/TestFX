package org.loadui.testfx;

import org.loadui.testfx.matchers.HasLabelMatcher;
import org.loadui.testfx.matchers.HasLabelStringMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class Matchers
{
    /**
     * Matches any Labeled Node that has the given label.
     *
     * @param label
     */
	@Factory
	public static Matcher<Object> hasLabel( String label )
	{
		return new HasLabelStringMatcher( label );
	}

    /**
     * Matches any Labeled Node that has a label that matches the given stringMatcher.
     *
     * @param stringMatcher
     */
	@Factory
	public static Matcher<Object> hasLabel( Matcher<String> stringMatcher )
	{
		return new HasLabelMatcher( stringMatcher );
	}
}
