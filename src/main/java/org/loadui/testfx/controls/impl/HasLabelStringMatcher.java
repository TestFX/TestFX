package org.loadui.testfx.controls.impl;

import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import static com.google.common.base.Preconditions.checkArgument;
import static org.loadui.testfx.GuiTest.find;

public class HasLabelStringMatcher extends TypeSafeMatcher<Object>
{
	private final String label;
    private String actualText;

    public HasLabelStringMatcher( String label )
	{
		this.label = label;
	}

	public void describeTo( Description desc )
	{
		desc.appendText( "Node should have label ");
        desc.appendValue( label );
	}

    @Override
    public void describeMismatchSafely( Object query, Description desc )
    {
        desc.appendText("Label was ");
        desc.appendValue( actualText );
    }


	@Override
	public boolean matchesSafely( Object target )
	{
		if( target instanceof String )
		{
            return nodeHasLabel( find( ( String )target ) );
		}
		else if( target instanceof Labeled )
		{
			return nodeHasLabel( ( Labeled )target );
		}
		return false;
	}

	private boolean nodeHasLabel( Node node )
	{
		checkArgument( node instanceof Labeled || node instanceof TextInputControl, "Target node must be Labeled or TextInputControl." );

        if( node instanceof Labeled )
		{
			Labeled labeled = ( Labeled )node;
            actualText = labeled.getText();
		}
        else
        {
		    TextInputControl textInput = ( TextInputControl )node;
            actualText = textInput.getText();
        }
        return label.equals(actualText);
	}
}