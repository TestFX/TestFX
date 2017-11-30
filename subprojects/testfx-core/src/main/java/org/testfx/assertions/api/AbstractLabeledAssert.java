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
package org.testfx.assertions.api;

import javafx.scene.control.Labeled;

import org.hamcrest.Matcher;
import org.testfx.matcher.control.LabeledMatchers;

import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.assertions.impl.Adapter.fromInverseMatcher;
import static org.testfx.assertions.impl.Adapter.fromMatcher;

/**
 * Base class for all {@link javafx.scene.control.Labeled} assertions.
 */
public class AbstractLabeledAssert<SELF extends AbstractLabeledAssert<SELF>> extends AbstractParentAssert<SELF> {

    protected AbstractLabeledAssert(Labeled actual, Class<?> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.Labeled} has exactly the given {@code text}.
     *
     * @param text the given text to compare the actual text to
     * @return this assertion object
     */
    public SELF hasText(String text) {
        assertThat(actual).is(fromMatcher(LabeledMatchers.hasText(text)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.Labeled} does not have exactly the given {@code text}.
     *
     * @param text the given text to compare the actual text to
     * @return this assertion object
     */
    public SELF doesNotHaveText(String text) {
        assertThat(actual).is(fromInverseMatcher(LabeledMatchers.hasText(text)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.Labeled} is matched by the given {@code matcher}.
     *
     * @param matcher the {@code String} matcher to test the actual text with
     * @return this assertion object
     */
    public SELF hasText(Matcher<String> matcher) {
        assertThat(actual).is(fromMatcher(LabeledMatchers.hasText(matcher)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.Labeled} is not matched by the given {@code matcher}.
     *
     * @param matcher the {@code String} matcher to test the actual text with
     * @return this assertion object
     */
    public SELF doesNotHaveText(Matcher<String> matcher) {
        assertThat(actual).is(fromInverseMatcher(LabeledMatchers.hasText(matcher)));
        return myself;
    }
}
