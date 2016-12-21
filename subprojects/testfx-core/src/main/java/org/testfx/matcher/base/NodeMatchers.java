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

import static org.testfx.matcher.base.GeneralMatchers.baseMatcher;

/**
 * TestFX matchers for {@link Node}.
 */
@Unstable(reason = "needs more tests")
public class NodeMatchers {

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    /**
     * Creates a matcher that matches everything ({@link Matcher#matches(Object) matches(Object)} always
     * returns true.
     */
    @Factory
    @Unstable(reason = "maybe find a better method name")
    public static Matcher<Node> anything() {
        return baseMatcher("anything", node -> true);
    }

    /**
     * Creates a matcher that matches all null {@link Node}s.
     */
    @Factory
    public static Matcher<Node> isNull() {
        return baseMatcher("Node is null", node -> isNull(node));
    }

    /**
     * Creates a matcher that matches all non-null {@link Node}s.
     */
    @Factory
    public static Matcher<Node> isNotNull() {
        return baseMatcher("Node is not null", node -> !isNull(node));
    }

    /**
     * Creates a matcher that matches all visible {@link Node}s.
     */
    @Factory
    public static Matcher<Node> isVisible() {
        return baseMatcher("Node is visible", node -> isVisible(node));
    }

    /**
     * Creates a matcher that matches all invisible {@link Node}s.
     */
    @Factory
    public static Matcher<Node> isInvisible() {
        return baseMatcher("Node is invisible", node -> !isVisible(node));
    }

    /**
     * Creates a matcher that matches all enabled {@link Node}s (i.e. {@link Node#isDisabled()} returns false).
     */
    @Factory
    public static Matcher<Node> isEnabled() {
        return baseMatcher("Node is enabled", node -> isEnabled(node));
    }

    /**
     * Creates a matcher that matches all disabled {@link Node}s (i.e. {@link Node#isDisabled()} returns true).
     */
    @Factory
    public static Matcher<Node> isDisabled() {
        return baseMatcher("Node is disabled", node -> !isEnabled(node));
    }

    /**
     * Creates a matcher that matches all {@link javafx.scene.control.Labeled}, {@link TextInputControl},
     * and {@link Text} objects that have the given {@code string}.
     */
    @Factory
    public static Matcher<Node> hasText(String string) {
        String descriptionText = "Node has text \"" + string + "\"";
        return baseMatcher(descriptionText, node -> hasText(node, string));
    }

    /**
     * Creates a matcher that matches all {@link javafx.scene.control.Labeled}, {@link TextInputControl},
     * and {@link Text} objects whose {@code text} matches the given {@code matcher}.
     */
    @Factory
    public static Matcher<Node> hasText(Matcher<String> matcher) {
        String descriptionText = "Node has " + matcher.toString();
        return baseMatcher(descriptionText, node -> hasText(node, matcher));
    }

    /**
     * Creates a matcher that matches all {@link Node}s that have at least one node that is found via
     * {@link org.testfx.service.query.NodeQuery#lookup(String)}.
     */
    @Factory
    public static Matcher<Node> hasChild(String query) {
        String descriptionText = "Node has child \"" + query + "\"";
        return baseMatcher(descriptionText, node -> hasChild(node, query));
    }

    /**
     * Creates a matcher that matches all {@link Node}s that have exactly {@code amount} nodes that are found via
     * {@link org.testfx.service.query.NodeQuery#lookup(String)}.
     */
    @Factory
    public static Matcher<Node> hasChildren(int amount,
                                            String query) {
        String descriptionText = "Node has " + amount + " children \"" + query + "\"";
        return baseMatcher(descriptionText, node -> hasChildren(node, amount, query));
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
        NodeQuery nodeQuery = nodeFinder.from(node);
        return !nodeQuery.lookup(query).queryAll().isEmpty();
    }

    private static boolean hasChildren(Node node,
                                       int amount,
                                       String query) {
        NodeFinder nodeFinder = FxAssert.assertContext().getNodeFinder();
        NodeQuery nodeQuery = nodeFinder.from(node);
        return nodeQuery.lookup(query).queryAll().size() == amount;
    }

}
