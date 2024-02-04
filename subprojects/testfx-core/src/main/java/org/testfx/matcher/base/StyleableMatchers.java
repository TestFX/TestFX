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
package org.testfx.matcher.base;

import java.util.Objects;
import javafx.css.Styleable;

import org.hamcrest.Matcher;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

/**
 * TestFX matchers for {@link Styleable} objects.
 */
public class StyleableMatchers {

    private StyleableMatchers() {}

    /**
     * Creates a matcher that matches all {@link Styleable} objects that have the given {@code typeSelector} as
     * their type selector.
     *
     * @param typeSelector the {@code String} the matched Styleables should have as their type selector
     */
    public static Matcher<Styleable> hasTypeSelector(String typeSelector) {
        String descriptionText = "has type selector \"" + typeSelector + "\"";
        return typeSafeMatcher(Styleable.class, descriptionText,
            styleable -> "\"" + styleable.getTypeSelector() + "\"",
            styleable -> Objects.equals(typeSelector, styleable.getTypeSelector()));
    }

    /**
     * Creates a matcher that matches all {@link Styleable} objects that have the given {@code id} as
     * their CSS id.
     *
     * @param id the {@code String} the matched Styleables should have as their CSS id
     */
    public static Matcher<Styleable> hasId(String id) {
        String descriptionText = "has CSS id \"" + id + "\"";
        return typeSafeMatcher(Styleable.class, descriptionText,
            styleable -> "\"" + styleable.getId() + "\"",
            styleable -> Objects.equals(id, styleable.getId()));
    }

    /**
     * Creates a matcher that matches all {@link Styleable} objects that have the given {@code style} as
     * their CSS style.
     *
     * @param style the {@code String} the matched Styleables should have as their CSS style
     */
    public static Matcher<Styleable> hasStyle(String style) {
        String descriptionText = "has CSS style \"" + style + "\"";
        return typeSafeMatcher(Styleable.class, descriptionText,
            styleable -> "\"" + styleable.getStyle() + "\"",
            styleable -> Objects.equals(style, styleable.getStyle()));
    }

    /**
     * Creates a matcher that matches all {@link Styleable} objects that have the given {@code parent} as
     * their styleable parent.
     *
     * @param styleableParent the {@code String} the matched Styleables should have as their styleable parent
     */
    public static Matcher<Styleable> hasStyleableParent(Styleable styleableParent) {
        String descriptionText = "has styleable parent \"" + styleableParent + "\"";
        return typeSafeMatcher(Styleable.class, descriptionText,
            styleable -> "\"" + styleable.getStyleableParent() + "\"",
            styleable -> Objects.equals(styleableParent, styleable.getStyleableParent()));
    }
}
