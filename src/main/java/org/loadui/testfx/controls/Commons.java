package org.loadui.testfx.controls;

import com.google.common.base.Preconditions;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.controls.impl.HasLabelMatcher;
import org.loadui.testfx.controls.impl.HasLabelStringMatcher;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Commons
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

    public static Node nodeLabeledBy(String labelQuery)
    {
        Node foundNode = GuiTest.find(labelQuery);

        checkArgument(foundNode instanceof Label);
        Label label = ( Label )foundNode;
        Node labelFor = label.getLabelFor();
        checkNotNull(labelFor);
        return labelFor;
    }

    public static Node  nodeLabeledBy(Label label)
    {
        Node labelFor = label.getLabelFor();
        checkNotNull(labelFor);
        return labelFor;
    }
}
