/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2018 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.matcher.base;

import java.util.function.Function;
import java.util.function.Predicate;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class GeneralMatchers {

    private GeneralMatchers() {}

    /**
     * Creates a matcher that matches when the given {@code predicate} returns {@literal true}.
     *
     * @param descriptionText describes what the matcher tries to match. This is used to
     * explain what happened when a match fails.
     * @param predicate the predicate that the passed-in object must pass (i.e. predicate.apply(object)
     * returns {@literal true}) to match
     */
    @Factory
    public static <T> Matcher<T> baseMatcher(final String descriptionText,
                                             final Predicate<T> predicate) {
        return new BaseMatcher<T>() {
            @Override
            public void describeTo(Description description) {
                description.appendText(descriptionText);
            }

            @Override
            @SuppressWarnings("unchecked")
            public boolean matches(Object object) {
                return predicate.test((T) object);
            }

            @Override
            public void describeMismatch(Object object, Description description) {
                description.appendText("was ").appendValue(object);
            }
        };
    }

    /**
     * Creates a matcher that matches when the passed-in object is not {@literal null}, is an instance of
     * the given type, and the given {@code predicate} returns {@literal true} when that object is passed into it.
     *
     * @param expectedType the class that the passed-in object must be an instance of to match
     * @param descriptionText describes what the matcher tries to match. This is used to
     * explain what happened when a match fails.
     * @param predicate the predicate that the passed-in object must pass (i.e. predicate.apply(object)
     * returns {@literal true}) to match
     */
    @Factory
    public static <S, T extends S> Matcher<S> typeSafeMatcher(final Class<T> expectedType,
                                                              final String descriptionText,
                                                              final Predicate<T> predicate) {
        return new TypeSafeMatcher<S>(expectedType) {
            @Override
            public void describeTo(Description description) {
                description.appendText(expectedType.getSimpleName());
                description.appendText(" ").appendText(descriptionText);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected boolean matchesSafely(S object) {
                return predicate.test((T) object);
            }

            @Override
            protected void describeMismatchSafely(S object, Description description) {
                description.appendText("was ").appendValue(object);
            }
        };
    }

    /**
     * Creates a matcher that matches when the passed-in object is not {@literal null}, is an instance of
     * the given type, and the given {@code predicate} returns {@literal true} when that object is passed into it.
     *
     * @param expectedType the class that the passed-in object must be an instance of to match
     * @param descriptionText describes what the matcher tries to match. This is used to explain what happened
     * when a match fails.
     * @param describeActual a {@link Function} which takes as input the "actual" object and produces
     * a String that clearly describes the "actual" object. This is used for producing a human-readable description
     * of the difference between the "expected" and "actual" if the {@code Matcher} fails
     * @param predicate the predicate that the passed-in object must pass (i.e. predicate.apply(object)
     * returns {@literal true}) to match
     */
    @Factory
    public static <S, T extends S> Matcher<S> typeSafeMatcher(final Class<T> expectedType,
                                                              final String descriptionText,
                                                              final Function<S, String> describeActual,
                                                              final Predicate<T> predicate) {
        return new TypeSafeMatcher<S>(expectedType) {
            @Override
            public void describeTo(Description description) {
                description.appendText(expectedType.getSimpleName());
                description.appendText(" ").appendText(descriptionText);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected boolean matchesSafely(S object) {
                return predicate.test((T) object);
            }

            @Override
            protected void describeMismatchSafely(S object,
                                                  Description description) {
                description.appendText("was ").appendText(describeActual.apply(object));
            }
        };
    }
}
