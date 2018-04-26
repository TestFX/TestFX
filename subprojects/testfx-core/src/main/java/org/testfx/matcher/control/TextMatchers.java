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
package org.testfx.matcher.control;

import java.util.Objects;

import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

/**
 * TestFX matchers for {@link Text} nodes.
 */
public class TextMatchers {

    private TextMatchers() {}

    /**
     * Creates a matcher that matches all {@link Text}s whose text equals the given {@code text}.
     *
     * @param text the {@code String} the matched Texts should have as their text
     */
    @Factory
    public static Matcher<Text> hasText(String text) {
        String descriptionText = "has text \"" + text + "\"";
        return typeSafeMatcher(Text.class, descriptionText,
            textNode -> textNode.getClass().getSimpleName() + " with text: \"" + textNode.getText() + "\"",
            textNode -> Objects.equals(text, textNode.getText()));
    }

    /**
     * Creates a matcher that matches all {@link Text}s whose text matches the given {@code matcher}.
     *
     * @param matcher the {@code Matcher<String>} the Texts text should match
     */
    @Factory
    public static Matcher<Text> hasText(Matcher<String> matcher) {
        String descriptionText = "has " + matcher.toString();
        return typeSafeMatcher(Text.class, descriptionText,
            text -> text.getClass().getSimpleName() + " with text: \"" + text.getText() + "\"",
            textNode -> matcher.matches(textNode.getText()));
    }

    /**
     * Creates a matcher that matches all {@link Text}s that have the given {@code font}.
     *
     * @param font the {@code Font} that matched Texts should have as their font
     */
    @Factory
    public static Matcher<Text> hasFont(Font font) {
        String descriptionText = "has font " + toText(font);
        return typeSafeMatcher(Text.class, descriptionText,
            textNode -> textNode.getClass().getSimpleName() + " with font: " + toText(textNode.getFont()),
            textNode -> Objects.equals(font, textNode.getFont()));
    }

    /**
     * Creates a matcher that matches all {@link Text}s that have the given {@code smoothingType}
     * (either {@link FontSmoothingType#GRAY} or {@link FontSmoothingType#LCD}).
     *
     * @param smoothingType the {@code FontSmoothingType} that matched Texts should have
     */
    @Factory
    public static Matcher<Text> hasFontSmoothingType(FontSmoothingType smoothingType) {
        String descriptionText = "has font smoothing type: \"" + smoothingType + "\"";
        return typeSafeMatcher(Text.class, descriptionText,
            textNode -> textNode.getClass().getSimpleName() + " with font smoothing type: \"" +
                    textNode.getFontSmoothingType() + "\"",
            textNode -> Objects.equals(smoothingType, textNode.getFontSmoothingType()));
    }

    /**
     * Creates a matcher that matches all {@link Text}s that have strikethrough (that is, they
     * should be drawn with a line through them).
     *
     * @param strikethrough whether or not the matched Texts should have strikethrough
     */
    @Factory
    public static Matcher<Text> hasStrikethrough(boolean strikethrough) {
        String descriptionText = (strikethrough ? "has " : "does not have ") + "strikethrough";
        return typeSafeMatcher(Text.class, descriptionText,
            textNode -> textNode.getClass().getSimpleName() + (textNode.isStrikethrough() ?
                    " with " : " without ") + "strikethrough",
            textNode -> textNode.isStrikethrough() == strikethrough);
    }

    /**
     * Creates a matcher that matches all {@link Text}s that are underlined (that is, they
     * should be drawn with a line below them).
     *
     * @param underlined whether or not the matched Texts should be underlined
     */
    @Factory
    public static Matcher<Text> isUnderlined(boolean underlined) {
        String descriptionText = (underlined ? "is " : "is not ") + "underlined";
        return typeSafeMatcher(Text.class, descriptionText,
            textNode -> textNode.getClass().getSimpleName() + (textNode.isUnderline() ?
                    " with " : " without ") + "underline",
            textNode -> textNode.isUnderline() == underlined);
    }

    private static String toText(Font font) {
        return String.format("\"%s\" with family (\"%s\") and size (%.1f)", font.getName(),
                font.getFamily(), font.getSize());
    }

}

