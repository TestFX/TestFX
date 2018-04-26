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
import javafx.scene.control.TextInputControl;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

/**
 * TestFX matchers for {@link TextInputControl} controls.
 */
public class TextInputControlMatchers {

    private TextInputControlMatchers() {}

    /**
     * Creates a matcher that matches all {@link TextInputControl}s that have text equal to the given {@code text}.
     *
     * @param text the {@code String} the matched TextInputControls should have as their text
     */
    @Factory
    public static Matcher<TextInputControl> hasText(String text) {
        String descriptionText = "has text \"" + text + "\"";
        return typeSafeMatcher(TextInputControl.class, descriptionText,
            textInputControl -> textInputControl.getClass().getSimpleName() + " with text: \"" +
                    textInputControl.getText() + "\"",
            textInputControl -> Objects.equals(text, textInputControl.getText()));
    }

    /**
     * Creates a matcher that matches all {@link TextInputControl}s that have text that matches the given
     * {@code matcher}.
     *
     * @param matcher the {@code Matcher<String>} the TextInputControls text should match
     */
    @Factory
    public static Matcher<TextInputControl> hasText(Matcher<String> matcher) {
        String descriptionText = "has " + matcher.toString();
        return typeSafeMatcher(TextInputControl.class, descriptionText,
            textInputControl -> textInputControl.getClass().getSimpleName() + " with text: \"" +
                    textInputControl.getText() + "\"",
            textInputControl -> matcher.matches(textInputControl.getText()));
    }

}
