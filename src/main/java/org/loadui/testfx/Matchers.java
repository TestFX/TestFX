package org.loadui.testfx;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.loadui.testfx.matchers.HasLabelMatcher;
import org.loadui.testfx.matchers.HasLabelStringMatcher;

public class Matchers
{
	/**
	 * Matches any Labeled Node that has the given label.
	 *
	 * @deprecated Use {@link hasText(String) instead}.
	 */
	@Deprecated
	@Factory
	public static Matcher<Object> hasLabel( String label )
	{
		return new HasLabelStringMatcher( label );
	}

	/**
	 * Matches any Labeled Node that has a label that matches the given stringMatcher.
	 *
	 * @deprecated Use {@link hasText(Matcher<String>) instead}.
	 */
	@Deprecated
	@Factory
	public static Matcher<Object> hasLabel( Matcher<String> stringMatcher )
	{
		return new HasLabelMatcher( stringMatcher );
	}

	/**
	 * Matches any Labeled Node that has the given label.
	 *
	 * @param text
	 */
	@Factory
	public static Matcher<Object> hasText( String text )
	{
		return new HasLabelStringMatcher( text );
	}

	/**
	 * Matches any Labeled Node that has a label that matches the given stringMatcher.
	 *
	 * @param stringMatcher
	 */
	@Factory
	public static Matcher<Object> hasText( Matcher<String> stringMatcher )
	{
		return new HasLabelMatcher( stringMatcher );
	}
}
