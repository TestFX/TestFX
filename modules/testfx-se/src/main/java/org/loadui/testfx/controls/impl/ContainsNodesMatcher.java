package org.loadui.testfx.controls.impl;

import org.loadui.testfx.GuiTest;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

public class ContainsNodesMatcher extends TypeSafeMatcher<String>
{
	final int numberOf;
	final String domQuery;

	public ContainsNodesMatcher( String domQuery )
	{
		this(-1, domQuery);
	}

	public ContainsNodesMatcher( int numberOf, String domQuery )
	{
		this.numberOf = numberOf;
		this.domQuery = domQuery;
	}

	public void describeTo( Description desc )
	{
		desc.appendText("contains");
	}

	@Factory
	public static Matcher<String> contains( String domQuery )
	{
		return new ContainsNodesMatcher(domQuery);
	}

	@Factory
	public static Matcher<String> contains( int numberOf, String domQuery )
	{
		return new ContainsNodesMatcher(numberOf, domQuery);
	}

	@Override
	public boolean matchesSafely( String domRoot )
	{
		if( numberOf >= 0 )
		{
			return GuiTest.findAll( domRoot + " " + domQuery ).size() == numberOf;
		}
		return !GuiTest.findAll( domRoot + " " + domQuery ).isEmpty();
	}
}
