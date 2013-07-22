package com.eviware.loadui.ui.fx.util.test.matchers;

import javafx.scene.Node;
import javafx.scene.control.Labeled;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

import static com.eviware.loadui.ui.fx.util.test.TestFX.find;
import static com.google.common.base.Preconditions.checkArgument;

public class HasLabelMatcher extends TypeSafeMatcher<Object>
{
	private final String label;

	public HasLabelMatcher( String label )
	{
		this.label = label;
	}

	public void describeTo( Description desc )
	{
		desc.appendText( "Node should have label " + label );
	}

	@Factory
	public static Matcher<Object> hasLabel( String label )
	{
		return new HasLabelMatcher( label );
	}

	@Override
	public boolean matchesSafely( Object target )
	{
		if( target instanceof String )
		{
			return nodeHasLabel( find( (String) target ) );
		} else if( target instanceof Labeled )
		{
			return nodeHasLabel( (Labeled) target );
		} else
		{
			throw new IllegalArgumentException( "Argument must be a String or a Node." );
		}
	}

	private boolean nodeHasLabel( Node node )
	{
		Labeled labeled = (Labeled) node;
		return label.equals( labeled.getText() );
	}
}