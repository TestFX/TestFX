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

import javafx.scene.control.MenuItem;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

/**
 * TestFX matchers for {@link MenuItem} controls.
 */
public class MenuItemMatchers {

    private MenuItemMatchers() {}

    /**
     * Creates a matcher that matches all {@link MenuItem} objects
     * that have the given {@code text} as their text in MenuItem.
     *
     * @param text the {@code String} the matched MenuItems should have as their text
     */
    @Factory
    public static Matcher<MenuItem> hasText(String text) {
        String descriptionText = "has text \"" + text + "\"";
        return typeSafeMatcher(MenuItem.class, descriptionText,
            menuItem -> "\"" + menuItem.getText() + "\"",
            menuItem -> Objects.equals(text, menuItem.getText()));
    }

    /**
     * Creates a matcher that matches all {@link MenuItem} objects whose text matches the given matcher.
     *
     * @param matcher the {@code Matcher<String>} that the MenuItem text should match
     */
    @Factory
    public static Matcher<MenuItem> hasText(Matcher<String> matcher) {
        String descriptionText = "has " + matcher.toString();
        return typeSafeMatcher(MenuItem.class, descriptionText,
            menuItem -> "\"" + menuItem.getText() + "\"",
            menuItem -> matcher.matches(menuItem.getText()));
    }
}
