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

import javafx.scene.Node;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import static org.testfx.matcher.predicate.PredicateMatchers.nodeMatcher;

public class NodeMatchers {

    //---------------------------------------------------------------------------------------------
    // STATIC FACTORY METHODS.
    //---------------------------------------------------------------------------------------------

    @Factory
    public static Matcher<Node> isNull() {
        return nodeMatcher("Node is null", node -> isNull(node));
    }

    @Factory
    public static Matcher<Node> isNotNull() {
        return nodeMatcher("Node is not null", node -> !isNull(node));
    }

    @Factory
    public static Matcher<Node> isVisible() {
        return nodeMatcher("Node is visible", node -> isVisible(node));
    }

    @Factory
    public static Matcher<Node> isInvisible() {
        return nodeMatcher("Node is invisible", node -> !isVisible(node));
    }

    @Factory
    public static Matcher<Node> isEnabled() {
        return nodeMatcher("Node is enabled", node -> isEnabled(node));
    }

    @Factory
    public static Matcher<Node> isDisabled() {
        return nodeMatcher("Node is disabled", node -> !isEnabled(node));
    }

    @Factory
    public static Matcher<Node> hasText(String text) {
        return null;
    }

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static boolean isNull(Node node) {
        return node == null;
    }

    public static boolean isVisible(Node node) {
        return node.isVisible();
    }

    public static boolean isEnabled(Node node) {
        return !node.isDisabled();
    }

}
