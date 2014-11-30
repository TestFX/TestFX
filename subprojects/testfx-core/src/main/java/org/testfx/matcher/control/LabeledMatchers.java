package org.testfx.matcher.control;

import javafx.scene.control.Labeled;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.matcher.predicate.PredicateMatchers;

public class LabeledMatchers {

    //---------------------------------------------------------------------------------------------
    // STATIC FACTORY METHODS.
    //---------------------------------------------------------------------------------------------

    @Factory
    public static Matcher<Labeled> hasText(final String text) {
        String descriptionText = "Labeled has text '" + text + "'";
        return PredicateMatchers.nodeMatcher(descriptionText, labeled -> hasText(labeled, text));
    }

    @Factory
    public static Matcher<Labeled> hasText(final Matcher<String> matcher) {
        return null;
    }

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static boolean hasText(Labeled labeled, String text) {
        return text.equals(lookupText(labeled));
    }

    public static String lookupText(Labeled labeled) {
        return labeled.getText();
    }

}
