package org.testfx.matcher.base;

import javafx.scene.Node;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.testfx.matcher.predicate.PredicateMatchers;

public class NodeMatchers {

    //---------------------------------------------------------------------------------------------
    // STATIC FACTORY METHODS.
    //---------------------------------------------------------------------------------------------

    @Factory
    public static Matcher<Node> isNull() {
        return PredicateMatchers.nodeMatcher("Node is null", node -> isNull(node));
    }

    @Factory
    public static Matcher<Node> isNotNull() {
        return PredicateMatchers.nodeMatcher("Node is not null", node -> !isNull(node));
    }

    @Factory
    public static Matcher<Node> isVisible() {
        return PredicateMatchers.nodeMatcher("Node is visible", node -> isVisible(node));
    }

    @Factory
    public static Matcher<Node> isInvisible() {
        return PredicateMatchers.nodeMatcher("Node is invisible", node -> !isVisible(node));
    }

    @Factory
    public static Matcher<Node> isEnabled() {
        return PredicateMatchers.nodeMatcher("Node is enabled", node -> isEnabled(node));
    }

    @Factory
    public static Matcher<Node> isDisabled() {
        return PredicateMatchers.nodeMatcher("Node is disabled", node -> !isEnabled(node));
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
