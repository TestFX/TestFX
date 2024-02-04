/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2024 The TestFX Contributors
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

import javafx.css.Styleable;

import org.assertj.core.api.AbstractAssert;
import org.testfx.matcher.base.StyleableMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testfx.assertions.impl.Adapter.fromInverseMatcher;
import static org.testfx.assertions.impl.Adapter.fromMatcher;

/**
 * Base class for all {@link javafx.css.Styleable} assertions.
 */
public class AbstractStyleableAssert<SELF extends AbstractStyleableAssert<SELF>>
        extends AbstractAssert<SELF, Styleable> {

    protected AbstractStyleableAssert(Styleable actual, Class<?> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link javafx.css.Styleable} has the given type selector.
     *
     * @param typeSelector the given type selector to compare the actual type selector to
     * @return this assertion object
     */
    public SELF hasTypeSelector(String typeSelector) {
        assertThat(actual).is(fromMatcher(StyleableMatchers.hasTypeSelector(typeSelector)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.css.Styleable} does not have the given type selector.
     *
     * @param typeSelector the given type selector to compare the actual type selector to
     * @return this assertion object
     */
    public SELF doesNotHaveTypeSelector(String typeSelector) {
        assertThat(actual).is(fromInverseMatcher(StyleableMatchers.hasTypeSelector(typeSelector)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.css.Styleable} has the given CSS id.
     *
     * @param id the given CSS id to compare the actual CSS id to
     * @return this assertion object
     */
    public SELF hasId(String id) {
        assertThat(actual).is(fromMatcher(StyleableMatchers.hasId(id)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.css.Styleable} does not have the given CSS id.
     *
     * @param id the given CSS id to compare the actual CSS id to
     * @return this assertion object
     */
    public SELF doesNotHaveId(String id) {
        assertThat(actual).is(fromInverseMatcher(StyleableMatchers.hasId(id)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.css.Styleable} has the given CSS style.
     *
     * @param style the given CSS style to compare the actual CSS style to
     * @return this assertion object
     */
    public SELF hasStyle(String style) {
        assertThat(actual).is(fromMatcher(StyleableMatchers.hasStyle(style)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.css.Styleable} does not have the given CSS style.
     *
     * @param style the given CSS style to compare the actual CSS style to
     * @return this assertion object
     */
    public SELF doesNotHaveStyle(String style) {
        assertThat(actual).is(fromInverseMatcher(StyleableMatchers.hasStyle(style)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.css.Styleable} has the given parent.
     *
     * @param styleableParent the given parent to compare the actual styleable parent to
     * @return this assertion object
     */
    public SELF hasStyleableParent(Styleable styleableParent) {
        assertThat(actual).is(fromMatcher(StyleableMatchers.hasStyleableParent(styleableParent)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.css.Styleable} does not have the given parent.
     *
     * @param styleableParent the given parent to compare the actual styleable parent to
     * @return this assertion object
     */
    public SELF doesNotHaveStyleableParent(Styleable styleableParent) {
        assertThat(actual).is(fromInverseMatcher(StyleableMatchers.hasStyleableParent(styleableParent)));
        return myself;
    }
}
