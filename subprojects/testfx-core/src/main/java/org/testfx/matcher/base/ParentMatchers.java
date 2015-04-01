/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.matcher.base;

import javafx.scene.Node;
import javafx.scene.Parent;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.api.annotation.Unstable;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

@Unstable(reason = "needs more tests")
public class ParentMatchers {

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    @Factory
    @Unstable(reason = "is missing apidocs")
    public static Matcher<Node> hasChild() {
        String descriptionText = "has child";
        return typeSafeMatcher(Parent.class, descriptionText, node -> hasChild(node));
    }

    @Factory
    @Unstable(reason = "is missing apidocs")
    public static Matcher<Node> hasChildren(int amount) {
        String descriptionText = "has " + amount + " children";
        return typeSafeMatcher(Parent.class, descriptionText, node -> hasChildren(amount, node));
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static boolean hasChild(Parent parent) {
        return !parent.getChildrenUnmodifiable().isEmpty();
    }

    private static boolean hasChildren(int amount,
                                       Parent parent) {
        return parent.getChildrenUnmodifiable().size() == amount;
    }

}
