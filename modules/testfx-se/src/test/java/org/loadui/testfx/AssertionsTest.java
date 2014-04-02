package org.loadui.testfx;

import com.google.common.base.Predicate;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.layout.VBoxBuilder;
import org.junit.Test;

import static org.loadui.testfx.Assertions.verifyThat;

// verifyThat supports lambda expressions, but to keep Java 7 compatibility, we don't use them here.
public class AssertionsTest extends GuiTest {

    @Test
    public void shouldSupportPredicatesForQueries()
    {
        verifyThat("Button A", new Predicate<Button>() {
            @Override
            public boolean apply(Button b) {
                return b.isDefaultButton();
            }
        });
    }

    @Test
    public void shouldSupportPredicatesForQueries2()
    {
        try {
            verifyThat("Button A", new Predicate<Button>() {
                @Override
                public boolean apply(Button b) {
                    return b.isCancelButton();
                }
            });
        } catch (AssertionError e)
        {
            return;
        }
        throw new AssertionError("verifyThat should have failed.");
    }

    @Test
    public void shouldSupportPredicatesForNodes()
    {
        Button b = find("Button A");

        verifyThat(b, new Predicate<Button>() {
            @Override
            public boolean apply(Button b) {
                return b.isDefaultButton();
            }
        });
    }

    @Override
    protected Parent getRootNode()
    {
        return VBoxBuilder
                .create()
                .children( ButtonBuilder.create().id( "button1" ).text( "Button A" ).defaultButton(true).build(),
                        ButtonBuilder.create().id( "button2" ).text( "Button B" ).build(),
                        TextFieldBuilder.create().id( "text" ).build() ).build();
    }
}
