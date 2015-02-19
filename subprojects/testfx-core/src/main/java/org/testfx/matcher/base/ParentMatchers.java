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
package org.testfx.matcher.base;

import javafx.scene.Parent;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.api.FxAssert;
import org.testfx.api.annotation.Unstable;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.query.NodeQuery;

import static org.testfx.matcher.base.BaseMatchers.baseMatcher;

@Unstable(reason = "needs more tests")
public class ParentMatchers {

    //---------------------------------------------------------------------------------------------
    // STATIC FACTORY METHODS.
    //---------------------------------------------------------------------------------------------

    @Factory
    public static Matcher<Parent> hasChild(String query) {
        NodeFinder nodeFinder = FxAssert.assertContext().getNodeFinder();
        return baseMatcher("Parent has child", (parent) -> {
            NodeQuery nodeQuery = nodeFinder.nodesFrom(parent);
            return nodeQuery.lookup(query).tryQueryFirst().isPresent();
        });
    }

    @Factory
    public static Matcher<Parent> hasChildren(int amount,
                                              String query) {
        NodeFinder nodeFinder = FxAssert.assertContext().getNodeFinder();
        return baseMatcher("Parent has children", (parent) -> {
            NodeQuery nodeQuery = nodeFinder.nodesFrom(parent);
            return nodeQuery.lookup(query).queryAll().size() == amount;
        });
    }

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static boolean hasChild(Parent parent) {
        return !parent.getChildrenUnmodifiable().isEmpty();
    }

    public static boolean hasChildren(int amount,
                                      Parent parent) {
        return parent.getChildrenUnmodifiable().size() == amount;
    }

}
