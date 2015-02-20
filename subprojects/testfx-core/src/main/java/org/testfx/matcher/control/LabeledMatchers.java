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

import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.control.Labeled;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.api.annotation.Unstable;

import static org.testfx.matcher.base.BaseMatchers.typeSafeMatcher;

@Unstable(reason = "needs more tests")
public class LabeledMatchers {

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    @Factory
    public static Matcher<Node> hasText(String string) {
        String descriptionText = "has text \"" + string + "\"";
        return typeSafeMatcher(Labeled.class, descriptionText, node -> hasText(node, string));
    }

    @Factory
    public static Matcher<Node> hasText(Matcher<String> matcher) {
        String descriptionText = "has " + matcher.toString();
        return typeSafeMatcher(Labeled.class, descriptionText, node -> hasText(node, matcher));
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static boolean hasText(Labeled labeled,
                                   String string) {
        return Objects.equals(string, lookupText(labeled));
    }

    private static boolean hasText(Labeled labeled,
                                   Matcher<String> matcher) {
        return matcher.matches(lookupText(labeled));
    }

    private static String lookupText(Labeled labeled) {
        return labeled.getText();
    }

}
