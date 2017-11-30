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

import javafx.scene.paint.Color;

import org.assertj.core.api.AbstractAssert;
import org.testfx.matcher.base.ColorMatchers;
import org.testfx.service.support.ColorMatcher;

import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.assertions.impl.Adapter.fromInverseMatcher;
import static org.testfx.assertions.impl.Adapter.fromMatcher;

/**
 * Base class for all {@link javafx.scene.paint.Color} assertions.
 */
public class AbstractColorAssert<SELF extends AbstractColorAssert<SELF>> extends AbstractAssert<SELF, Color> {

    protected AbstractColorAssert(Color color, Class<?> selfType) {
        super(color, selfType);
    }

    /**
     * Verifies that the actual {@link javafx.scene.paint.Color} is exactly equal to the given {@code color}.
     *
     * @param color the given color to compare the actual color to
     * @return this assertion object
     */
    public SELF isColor(Color color) {
        assertThat(actual).is(fromMatcher(ColorMatchers.isColor(color)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.paint.Color} is not exactly equal to the given {@code color}.
     *
     * @param color the given color to compare the actual color to
     * @return this assertion object
     */
    public SELF isNotColor(Color color) {
        assertThat(actual).is(fromInverseMatcher(ColorMatchers.isColor(color)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.paint.Color} is matched by the given {@code color}
     * with respect to the given {@code colorMatcher}.
     * <p>
     * For example, to match colors using a custom matcher that considers two colors equal if they
     * have the same red components:
     * <pre>{@code
     * assertThat(Color.rgb(0.3, 0.2, 0.1)).isColor(Color.rgb(0.3, 0.8, 0.7),
     *      (c1, c2) -> c1.getRed() == c2.getRed());
     * }</pre>
     *
     * @param color the given color to compare the actual color to
     * @param colorMatcher the color matcher to use for comparison
     * @return this assertion object
     */
    public SELF isColor(Color color, ColorMatcher colorMatcher) {
        assertThat(actual).is(fromMatcher(ColorMatchers.isColor(color, colorMatcher)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.paint.Color} is not matched by the given {@code color}
     * with respect to the given {@code colorMatcher}.
     *
     * @param color the given color to compare the actual color to
     * @param colorMatcher the color matcher to use for comparison
     * @return this assertion object
     */
    public SELF isNotColor(Color color, ColorMatcher colorMatcher) {
        assertThat(actual).is(fromInverseMatcher(ColorMatchers.isColor(color, colorMatcher)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.paint.Color} is exactly equal to the given
     * named color.
     *
     * @param namedColor the given named color to compare the actual color to
     * @return this assertion object
     * @throws AssertionError if the given named {@code Color} is not a JavaFX named color
     * @see <a href="https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor">JavaFX Named Colors</a>
     */
    public SELF isColor(String namedColor) {
        assertThat(actual).is(fromMatcher(ColorMatchers.isColor(namedColor)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.paint.Color} is not exactly equal to the given
     * {@code namedColor}.
     *
     * @param namedColor the given named color to compare the actual color to
     * @return this assertion object
     * @throws AssertionError if the given named {@code Color} is not a JavaFX named color
     * @see <a href="https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor">JavaFX Named Colors</a>
     */
    public SELF isNotColor(String namedColor) {
        assertThat(actual).is(fromInverseMatcher(ColorMatchers.isColor(namedColor)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.paint.Color} has the given {@code namedColor} as their
     * closest named color. The {@code namedColor} is <em>not</em> case sensitive.
     *
     * @param namedColor the given named color to compare the actual color to
     * @return this assertion object
     * @throws AssertionError if the given named {@code Color} is not a JavaFX named color
     * @see <a href="https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor">JavaFX Named Colors</a>
     */
    public SELF hasClosestNamedColor(Color namedColor) {
        assertThat(actual).is(fromMatcher(ColorMatchers.hasClosestNamedColor(namedColor)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.paint.Color} does not have the given {@code namedColor} as their
     * closest named color. The {@code namedColor} is <em>not</em> case sensitive.
     *
     * @param namedColor the given named {@code Color} to compare the actual color to
     * @return this assertion object
     * @throws AssertionError if the given named {@code Color} is not a JavaFX named color
     * @see <a href="https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor">JavaFX Named Colors</a>
     */
    public SELF doesNotHaveClosestNamedColor(Color namedColor) {
        assertThat(actual).is(fromInverseMatcher(ColorMatchers.hasClosestNamedColor(namedColor)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.paint.Color} has the given {@code namedColor} as their
     * closest named color. The {@code namedColor} is <em>not</em> case sensitive.
     *
     * @param namedColor the given named color {@code String} to compare the actual color to
     * @return this assertion object
     * @throws AssertionError if the given named color is not a JavaFX named color
     * @see <a href="https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor">JavaFX Named Colors</a>
     */
    public SELF hasClosestNamedColor(String namedColor) {
        assertThat(actual).is(fromMatcher(ColorMatchers.hasClosestNamedColor(namedColor)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.paint.Color} does not have the given {@code namedColor} as their
     * closest named color. The {@code namedColor} is <em>not</em> case sensitive.
     *
     * @param namedColor the given named color {@code String} to compare the actual color to
     * @return this assertion object
     * @throws AssertionError if the given named coloris not a JavaFX named color
     * @see <a href="https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor">JavaFX Named Colors</a>
     */
    public SELF doesNotHaveClosestNamedColor(String namedColor) {
        assertThat(actual).is(fromInverseMatcher(ColorMatchers.hasClosestNamedColor(namedColor)));
        return myself;
    }
}
