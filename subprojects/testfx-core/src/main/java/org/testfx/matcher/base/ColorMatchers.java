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

import java.util.Optional;
import javafx.scene.paint.Color;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.service.support.ColorMatcher;
import org.testfx.util.ColorUtils;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

public class ColorMatchers {

    /**
     * Creates a matcher that matches all {@link Color}s that are exactly equal to the given {@code color}.
     */
    @Factory
    public static Matcher<Color> isColor(Color color) {
        String descriptionText = "has color " + getColorText(color);
        return typeSafeMatcher(Color.class, descriptionText, ColorMatchers::getColorText,
            col -> col.equals(color));
    }

    /**
     * Creates a matcher that matches all {@link Color}s that match the given {@code color} with respect
     * to the given {@code colorMatcher}.
     * <p>
     * For example, to match colors using a custom matcher that considers two colors equal if they
     * have the same red components:
     * <pre>{@code
     * assertThat(Color.rgb(0.3, 0.2, 0.1), ColorMatchers.isColor(Color.rgb(0.3, 0.8, 0.7),
     *      (c1, c2) -> c1.getRed() == c2.getRed()));
     * }</pre>
     */
    @Factory
    public static Matcher<Color> isColor(Color color, ColorMatcher colorMatcher) {
        String descriptionText = "has color " + getColorText(color);
        return typeSafeMatcher(Color.class, descriptionText, ColorMatchers::getColorText,
            col -> colorMatcher.matchColors(col, color));
    }

    /**
     * Creates a matcher that matches all {@link Color}s that are exactly equal to the given JavaFX
     * named color {@code String}. The {@code namedColor} is <em>not</em> case sensitive.
     *
     * @throws AssertionError if the given named color {@code String} is not a JavaFX named color
     * @see <a href="https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor">JavaFX Named Colors</a>
     */
    @Factory
    public static Matcher<Color> isColor(String namedColor) {
        if (!ColorUtils.getNamedColor(namedColor).isPresent()) {
            throw new AssertionError("given color name: \"" + namedColor + "\" is not a named color\n" +
                    "See: https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor");
        }
        String descriptionText = "is \"" + namedColor + "\"";
        return typeSafeMatcher(Color.class, descriptionText, ColorMatchers::getColorText,
            color -> ColorUtils.getNamedColor(namedColor).map(col -> col.equals(color)).orElse(false));
    }

    /**
     * Creates a matcher that matches all {@link Color}s that are exactly equal to the given JavaFX
     * named {@code Color}.
     *
     * @throws AssertionError if the given named {@code Color} is not a JavaFX named color
     * @see <a href="https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor">JavaFX Named Colors</a>
     */
    @Factory
    public static Matcher<Color> hasClosestNamedColor(Color namedColor) {
        Optional<String> namedColorOptional = ColorUtils.getNamedColor(Integer.parseInt(
                namedColor.toString().substring(2, 8), 16));
        if (!namedColorOptional.isPresent()) {
            throw new AssertionError("given color: \"#" + namedColor.toString().substring(2, 8) +
                    "\" is not a named color\n" +
                    "See: https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor");
        }
        String descriptionText = "has closest named color " + getColorText(namedColor);
        return typeSafeMatcher(Color.class, descriptionText, color -> getColorText(color) +
                " which has closest named color: \"" + ColorUtils.getClosestNamedColor(
                        Integer.parseInt(color.toString().substring(2, 8), 16)) + "\"",
            color -> namedColor.equals(ColorUtils.getClosestNamedColor(color)));
    }

    /**
     * Creates a matcher that matches all {@link Color}s that have the given named color {@code String}
     * as their closest JavaFX named color. The {@code namedColor} is <em>not</em> case sensitive.
     *
     * @throws AssertionError if the given named color {@code String} is not a JavaFX named color
     * @see <a href="https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor">JavaFX Named Colors</a>
     */
    @Factory
    public static Matcher<Color> hasClosestNamedColor(String namedColor) {
        Optional<Color> namedColorOptional = ColorUtils.getNamedColor(namedColor);
        if (!namedColorOptional.isPresent()) {
            throw new AssertionError("given color name: \"" + namedColor + "\" is not a named color\n" +
                    "See: https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor");
        }
        String descriptionText = "has closest named color " + getColorText(namedColorOptional.get());
        return typeSafeMatcher(Color.class, descriptionText, color -> getColorText(color) +
                        " which has closest named color: \"" + ColorUtils.getClosestNamedColor(
                Integer.parseInt(color.toString().substring(2, 8), 16)) + "\"",
            color -> namedColorOptional.get().equals(ColorUtils.getClosestNamedColor(color)));
    }

    private static String getColorText(Color color) {
        String hex = color.toString().substring(2, 8);
        return ColorUtils.getNamedColor(Integer.parseInt(hex, 16)).map(s -> String.format("\"%s\" (#%s)", s, hex))
                .orElseGet(() -> "\"#" + hex + "\"");
    }

}
