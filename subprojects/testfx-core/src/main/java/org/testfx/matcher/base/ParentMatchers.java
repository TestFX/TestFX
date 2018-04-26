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

import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

/**
 * TestFX matchers for {@link Parent} nodes.
 */
public class ParentMatchers {

    private ParentMatchers() {}

    /**
     * Creates a matcher that matches all {@link Parent}s that have at least one child.
     */
    @Factory
    public static Matcher<Parent> hasChild() {
        String descriptionText = "has at least one child";
        return typeSafeMatcher(Parent.class, descriptionText,
            parent -> toText(parent.getChildrenUnmodifiable()),
            parent -> !parent.getChildrenUnmodifiable().isEmpty());
    }

    /**
     * Creates a matcher that matches all {@link Parent}s that have exactly {@code amount} children.
     */
    @Factory
    public static Matcher<Parent> hasChildren(int amount) {
        String descriptionText = "has exactly " + amount + " children";
        return typeSafeMatcher(Parent.class, descriptionText,
            parent -> toText(parent.getChildrenUnmodifiable()),
            parent -> parent.getChildrenUnmodifiable().size() == amount);
    }

    private static String toText(ObservableList<Node> children) {
        if (children.isEmpty()) {
            return "empty (contained no children)";
        } else {
            return '[' + children.stream().map(node -> node.getClass().getSimpleName())
                    .collect(Collectors.joining(", ")) + ']' +
                    " (which has " + children.size() + ' ' + (children.size() == 1 ? "child" : "children") + ')';
        }
    }

}
