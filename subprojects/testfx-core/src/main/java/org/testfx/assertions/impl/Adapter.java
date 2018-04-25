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
package org.testfx.assertions.impl;

import org.assertj.core.api.Condition;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

/**
 * Provides static utility methods for converting a hamcrest matcher to an AssertJ
 * condition. We use this internally in the implementation of AssertJ assertions.
 * <p>
 * This class is not part of the TestFX API and is not expected to be useful outside
 * of our specific context.
 */
public class Adapter {

    public static <T> Condition<? super T> fromInverseMatcher(Matcher<? extends T> matcher) {
        return adaptMatcher(matcher, true);
    }

    public static <T> Condition<? super T> fromMatcher(Matcher<? extends T> matcher) {
        return adaptMatcher(matcher, false);
    }

    private static <T> Condition<? super T> adaptMatcher(Matcher<? extends T> matcher, boolean invert) {
        return new Condition<T>() {
            @Override
            public boolean matches(T t) {

                boolean matches = matcher.matches(t);
                if (invert) {
                    matches = !matches;
                }
                if (!matches) {
                    StringDescription reasonDescription = new StringDescription();
                    matcher.describeTo(reasonDescription);
                    // This is hacky, we essentially short-circuit AssertJ exception throwing in order to
                    // use the same error messages as Hamcrest matchers without having to call "overridingErrorMessage"
                    // at each "assertThat" call site.
                    throw new AssertionError(getErrorMessage(matcher, t, invert));
                }
                return true;
            }
        };
    }

    private static <T> String getErrorMessage(Matcher<? extends T> matcher, T actual, boolean invert) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        errorMessageBuilder.append("Expected: ");
        StringDescription reasonDescription = new StringDescription();
        matcher.describeTo(reasonDescription);
        errorMessageBuilder.append(reasonDescription.toString())
                .append(invert ? " to be false" : "")
                .append("\n     but: ")
                .append(getActual(matcher, actual));
        return errorMessageBuilder.toString();
    }

    private static <T> String getActual(Matcher<? extends T> matcher, T actual) {
        String actualStr = actual.toString();
        if (matcher instanceof TypeSafeMatcher) {
            TypeSafeMatcher typeSafeMatcher = (TypeSafeMatcher) matcher;
            StringDescription valueDescription = new StringDescription();
            typeSafeMatcher.describeMismatch(actual, valueDescription);
            actualStr = valueDescription.toString();
        }
        return actualStr;
    }
}
