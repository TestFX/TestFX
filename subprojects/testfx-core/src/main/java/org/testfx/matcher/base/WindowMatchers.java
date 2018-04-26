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

import javafx.stage.Window;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import static org.testfx.matcher.base.GeneralMatchers.baseMatcher;

/**
 * TestFX matchers for {@link Window} instances.
 */
public class WindowMatchers {

    private WindowMatchers() {}

    /**
     * A matcher checking if a window is currently showing.
     *
     * @return the matcher
     */
    @Factory
    public static Matcher<Window> isShowing() {
        return baseMatcher("Window is showing", Window::isShowing);
    }

    /**
     * A matcher checking if a window is currently not showing.
     *
     * @return the matcher
     */
    @Factory
    public static Matcher<Window> isNotShowing() {
        return baseMatcher("Window is not showing", window -> !window.isShowing());
    }

    /**
     * A matcher checking if a window currently has the focus.
     *
     * @return the matcher
     */
    @Factory
    public static Matcher<Window> isFocused() {
        return baseMatcher("Window is focused", Window::isFocused);
    }

    /**
     * A matcher checking if a window is currently not focused.
     *
     * @return the matcher
     */
    @Factory
    public static Matcher<Window> isNotFocused() {
        return baseMatcher("Window is not focused", window -> !window.isFocused());
    }

}
