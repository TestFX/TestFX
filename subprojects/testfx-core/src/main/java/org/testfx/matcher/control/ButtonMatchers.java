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

import javafx.scene.control.Button;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

/**
 * TestFX matchers for {@link Button} controls.
 */
public class ButtonMatchers {

    private ButtonMatchers() {}

    /**
     * Creates a matcher that matches all {@link Button}s that are cancel buttons.
     */
    @Factory
    public static Matcher<Button> isCancelButton() {
        return typeSafeMatcher(Button.class, "is cancel button", Button::isCancelButton);
    }

    /**
     * Creates a matcher that matches all {@link Button}s that are default buttons.
     */
    @Factory
    public static Matcher<Button> isDefaultButton() {
        return typeSafeMatcher(Button.class, "is default button", Button::isDefaultButton);
    }
}
