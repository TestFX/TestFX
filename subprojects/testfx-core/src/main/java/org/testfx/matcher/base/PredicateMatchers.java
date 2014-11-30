package org.testfx.matcher.base;

import javafx.scene.Node;

import com.google.common.base.Predicate;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class PredicateMatchers {

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static <T extends Node> Matcher<T> nodeMatcher(final String descriptionText,
                                                          final Predicate<T> nodePredicate) {
        return new BaseMatcher<T>() {
            public void describeTo(Description description) {
                description.appendText(descriptionText);
            }

            @SuppressWarnings("unchecked")
            public boolean matches(Object node) {
                return nodePredicate.apply((T) node);
            }

            public void describeMismatch(Object node, Description description) {
                description.appendText("was ").appendValue(node);
            }
        };
    }

    public static <T extends Node> Matcher<T> nodeMatcher(final Class<T> expectedType,
                                                          final String descriptionText,
                                                          final Predicate<T> nodePredicate) {
        // This simply implements the null check, checks the type and then casts.
        return new TypeSafeMatcher<T>(expectedType) {
            public void describeTo(Description description) {
                description.appendText(expectedType.getSimpleName());
                description.appendText(" ").appendText(descriptionText);
            }

            protected boolean matchesSafely(T node) {
                return nodePredicate.apply(node);
            }

            protected void describeMismatchSafely(Node node, Description mismatchDescription) {
                mismatchDescription.appendText("was ").appendValue(node);
            }
        };
    }

}
