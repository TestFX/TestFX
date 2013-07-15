package com.eviware.loadui.ui.fx.util.test.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import java.util.Collection;
import java.util.Set;

public class EmptyMatcher extends BaseMatcher
{
	public void describeTo( Description desc )
	{
		desc.appendText("empty");
	}

	@Factory
	public static Matcher<Set<?>> empty()
	{
		return new EmptyMatcher();
	}

	@Override
	public boolean matches( Object o )
	{
		return ((Collection<?>) o).isEmpty();
	}
}
