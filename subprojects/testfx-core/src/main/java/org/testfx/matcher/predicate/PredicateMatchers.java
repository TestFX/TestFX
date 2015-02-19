/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.testfx.matcher.predicate;

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
            @Override
            public void describeTo(Description description) {
                description.appendText(descriptionText);
            }

            @Override
            @SuppressWarnings("unchecked")
            public boolean matches(Object node) {
                return nodePredicate.apply((T) node);
            }

            @Override
            public void describeMismatch(Object node,
                                         Description description) {
                description.appendText("was ").appendValue(node);
            }
        };
    }

    public static <T extends Node> Matcher<T> nodeMatcher(final Class<T> expectedType,
                                                          final String descriptionText,
                                                          final Predicate<T> nodePredicate) {
        // This simply implements the null check, checks the type and then casts.
        return new TypeSafeMatcher<T>(expectedType) {
            @Override
            public void describeTo(Description description) {
                description.appendText(expectedType.getSimpleName());
                description.appendText(" ").appendText(descriptionText);
            }

            @Override
            protected boolean matchesSafely(T node) {
                return nodePredicate.apply(node);
            }

            @Override
            protected void describeMismatchSafely(Node node,
                                                  Description description) {
                description.appendText("was ").appendValue(node);
            }
        };
    }

}
