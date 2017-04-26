/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2017 The TestFX Contributors
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

import java.util.function.Predicate;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.testfx.api.annotation.Unstable;

@Unstable(reason = "requires more testing; likely to be replaced by a builder")
public class GeneralMatchers {

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    /**
     * Creates a matcher that matches when the given {@code predicate} returns true.
     *
     * @param descriptionText describes what the matcher tries to match. Only appears when a match fails.
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
            public void describeMismatch(Object object,
                                         Description description) {
                description.appendText("was ").appendValue(object);
            }
        };
    }

    /**
     * Creates a matcher that matches when the passed-in object is not null, is an instance of the given type,
     * and the given {@code predicate} returns true when that object is passed into it.
     *
     * @param descriptionText describes what the matcher tries to match. Only appears when a match fails.
     */
    @Factory
    public static <S, T extends S> Matcher<S> typeSafeMatcher(final Class<T> expectedType,
                                                              final String descriptionText,
                                                              final Predicate<T> predicate) {
        // This simply implements the null check, checks the type and then casts.
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
                description.appendText("was ").appendValue(object);
            }
        };
    }

}
