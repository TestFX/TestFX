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
package org.testfx.matcher.control;

import javafx.scene.control.Labeled;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.matcher.predicate.PredicateMatchers;

public class LabeledMatchers {

    //---------------------------------------------------------------------------------------------
    // STATIC FACTORY METHODS.
    //---------------------------------------------------------------------------------------------

    @Factory
    public static Matcher<Labeled> hasText(final String text) {
        String descriptionText = "Labeled has text '" + text + "'";
        return PredicateMatchers.nodeMatcher(descriptionText, labeled -> hasText(labeled, text));
    }

    @Factory
    public static Matcher<Labeled> hasText(final Matcher<String> matcher) {
        return null;
    }

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static boolean hasText(Labeled labeled, String text) {
        return text.equals(lookupText(labeled));
    }

    public static String lookupText(Labeled labeled) {
        return labeled.getText();
    }

}
