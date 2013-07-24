package com.eviware.loadui.ui.fx.util.test.matchers;

import com.eviware.loadui.ui.fx.util.test.GuiTest;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

public class VisibleNodesMatcher extends TypeSafeMatcher<String>
{
	public void describeTo( Description desc )
	{
		desc.appendText("visible");
	}

	@Factory
	public static Matcher<String> visible()
	{
		return new VisibleNodesMatcher();
	}

	@Override
	public boolean matchesSafely( String domQuery )
	{
		return !GuiTest.findAll( domQuery ).isEmpty();
	}
}
