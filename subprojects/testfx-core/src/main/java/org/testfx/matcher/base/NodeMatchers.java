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
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Text;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.api.FxAssert;
import org.testfx.api.annotation.Unstable;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.matcher.control.TextMatchers;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.query.NodeQuery;

import static org.testfx.matcher.base.BaseMatchers.baseMatcher;

@Unstable(reason = "needs more tests")
public class NodeMatchers {

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    @Factory
    public static Matcher<Node> anything() {
        return baseMatcher("anything", node -> true);
    }

    @Factory
    public static Matcher<Node> isNull() {
        return baseMatcher("Node is null", node -> isNull(node));
    }

    @Factory
    public static Matcher<Node> isNotNull() {
        return baseMatcher("Node is not null", node -> !isNull(node));
    }

    @Factory
    public static Matcher<Node> isVisible() {
        return baseMatcher("Node is visible", node -> isVisible(node));
    }

    @Factory
    public static Matcher<Node> isInvisible() {
        return baseMatcher("Node is invisible", node -> !isVisible(node));
    }

    @Factory
    public static Matcher<Node> isEnabled() {
        return baseMatcher("Node is enabled", node -> isEnabled(node));
    }

    @Factory
    public static Matcher<Node> isDisabled() {
        return baseMatcher("Node is disabled", node -> !isEnabled(node));
    }

    @Factory
    public static Matcher<Node> hasText(String text) {
        String descriptionText = "Node has text \"" + text + "\"";
        return baseMatcher(descriptionText, node -> hasText(node, text));
    }

    @Factory
    public static Matcher<Node> hasText(Matcher<String> textMatcher) {
        String descriptionText = "Node has " + textMatcher.toString();
        return baseMatcher(descriptionText, node -> hasText(node, textMatcher));
    }

    @Factory
    public static Matcher<Node> hasChild(String query) {
        String descriptionText = "Node has child \"" + query + "\"";
        return baseMatcher(descriptionText, (node) -> hasChild(node, query));
    }

    @Factory
    public static Matcher<Node> hasChildren(int amount,
                                            String query) {
        String descriptionText = "Node has " + amount + " children \"" + query + "\"";
        return baseMatcher(descriptionText, (node) -> hasChildren(node, amount, query));
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static boolean isNull(Node node) {
        return node == null;
    }

    private static boolean isVisible(Node node) {
        return node.isVisible();
    }

    private static boolean isEnabled(Node node) {
        return !node.isDisabled();
    }

    private static boolean hasText(Node node,
                                   String string) {
        if (node instanceof Labeled) {
            return LabeledMatchers.hasText(string).matches(node);
        }
        else if (node instanceof TextInputControl) {
            return TextInputControlMatchers.hasText(string).matches(node);
        }
        else if (node instanceof Text) {
            return TextMatchers.hasText(string).matches(node);
        }
        return false;
    }

    private static boolean hasText(Node node,
                                   Matcher<String> matcher) {
        if (node instanceof Labeled) {
            return LabeledMatchers.hasText(matcher).matches(node);
        }
        else if (node instanceof TextInputControl) {
            return TextInputControlMatchers.hasText(matcher).matches(node);
        }
        else if (node instanceof Text) {
            return TextMatchers.hasText(matcher).matches(node);
        }
        return false;
    }

    private static boolean hasChild(Node node,
                                    String query) {
        NodeFinder nodeFinder = FxAssert.assertContext().getNodeFinder();
        NodeQuery nodeQuery = nodeFinder.nodesFrom(node);
        return !nodeQuery.lookup(query).queryAll().isEmpty();
    }

    private static boolean hasChildren(Node node,
                                       int amount,
                                       String query) {
        NodeFinder nodeFinder = FxAssert.assertContext().getNodeFinder();
        NodeQuery nodeQuery = nodeFinder.nodesFrom(node);
        return nodeQuery.lookup(query).queryAll().size() == amount;
    }

}
