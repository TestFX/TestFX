package org.testfx.matcher.control;

import javafx.scene.control.TextInputControl;

import com.google.common.base.Predicate;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.matcher.base.PredicateMatchers;

public class TextInputControlMatchers {

    //---------------------------------------------------------------------------------------------
    // MATCHER METHODS.
    //---------------------------------------------------------------------------------------------

    @Factory
    public static Matcher<TextInputControl> hasText(final String text) {
        String descriptionText = "TextInputControl has text '" + text + "'";
        return PredicateMatchers.nodeMatcher(descriptionText, new Predicate<TextInputControl>() {
            @Override
            public boolean apply(TextInputControl textInputControl) {
                return hasText(textInputControl, text);
            }
        });
    }

    //---------------------------------------------------------------------------------------------
    // CONDITION METHODS.
    //---------------------------------------------------------------------------------------------

    public static boolean hasText(TextInputControl textInputControl, String text) {
        return text.equals(lookupText(textInputControl));
    }

    public static String lookupText(TextInputControl textInputControl) {
        return textInputControl.getText();
    }

}
