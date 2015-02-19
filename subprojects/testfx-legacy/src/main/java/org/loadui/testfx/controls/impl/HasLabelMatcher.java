/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.loadui.testfx.controls.impl;

import javafx.scene.control.Labeled;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.loadui.testfx.GuiTest.find;

public class HasLabelMatcher extends TypeSafeMatcher<Object> {

    private final Matcher<String> matcher;

    public HasLabelMatcher(Matcher<String> matcher) {
        this.matcher = matcher;
    }

    public void describeTo(Description desc) {
        desc.appendText("Node should match " + matcher);
    }

    @Override
    public boolean matchesSafely(Object target) {
        if (target instanceof String) {
            return matcher.matches(((Labeled) find((String) target)).getText());
        }
        else if (target instanceof Labeled) {
            return matcher.matches(((Labeled) target).getText());
        }
        return false;
    }

}