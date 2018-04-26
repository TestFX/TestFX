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
package org.testfx.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Labeled;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.hamcrest.Matcher;

import static org.testfx.internal.JavaVersionAdapter.isNotVisible;

public final class NodeQueryUtils {

    private NodeQueryUtils() {}

    /**
     * Returns a set of the given windows' scenes' root nodes.
     */
    public static Set<Node> rootsOfWindows(Collection<Window> windows) {
        return windows.stream()
                .map(NodeQueryUtils::fromWindow)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a set of the given windows' scenes' root nodes.
     */
    public static Set<Node> rootOfWindow(Window... windows) {
        // TODO: is this set (toSet()) in order?
        return Arrays.stream(windows)
                .map(NodeQueryUtils::fromWindow)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a set of the given stages' scenes' root nodes.
     */
    public static Set<Node> rootOfStage(Stage... stages) {
        return Arrays.stream(stages)
                .map(NodeQueryUtils::fromStage)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a set of the given scenes' root nodes.
     */
    public static Set<Node> rootOfScene(Scene... scenes) {
        return Arrays.stream(scenes)
                .map(NodeQueryUtils::fromScene)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a set of the given popup controls' scenes' root nodes.
     */
    public static Set<Node> rootOfPopupControl(PopupControl... popupControls) {
        return Arrays.stream(popupControls)
                .map(NodeQueryUtils::fromPopupControl)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a function that calls {@link Node#lookup(String)} on each given node.
     */
    public static Function<Node, Set<Node>> bySelector(String selector) {
        return parentNode -> lookupWithSelector(parentNode, selector);
    }

    /**
     * Returns a function that returns a {@code Set} of all {@code Node}s that pass the given {@code predicate}.
     */
    public static Function<Node, Set<Node>> byPredicate(Predicate<Node> predicate) {
        return parentNode -> lookupWithPredicate(parentNode, predicate);
    }

    /**
     * Returns a function that returns a {@code Set} of all {@code Node}s that match the given {@code matcher}.
     */
    public static Function<Node, Set<Node>> byMatcher(Matcher<Node> matcher) {
        return byPredicate(matchesMatcher(matcher));
    }

    /**
     * Returns a function that returns a {@code Set} of all {@link javafx.scene.control.Label}s,
     * {@link TextInputControl}s, or any of their subclasses that have the given {@code text}.
     */
    public static Function<Node, Set<Node>> byText(String text) {
        return byPredicate(hasText(text));
    }

    /**
     * Returns a predicate that returns true if the node's id equals the given {@code id}.
     */
    public static Predicate<Node> hasId(String id) {
        return node -> Objects.equals(node.getId(), id);
    }

    /**
     * Returns a predicate that returns true if the node is a {@link javafx.scene.control.Label},
     * {@link TextInputControl}, or any of their subclasses whose text equals the given {@code text}.
     */
    public static Predicate<Node> hasText(String text) {
        return node -> hasNodeText(node, text);
    }

    /**
     * Returns a predicate that returns true if the given node matches the given {@code matcher}.
     */
    public static Predicate<Node> matchesMatcher(Matcher<Node> matcher) {
        return node -> matchesNodeMatcher(node, matcher);
    }

    /**
     * Returns a predicate that returns true if the given node is visible, the given tree is visible, or the
     * node's local bounds are within its scene's bounds
     */
    public static Predicate<Node> isVisible() {
        return NodeQueryUtils::isNodeVisible;
    }

    /**
     * Returns a function that returns a {@code Set} of all {@code Node}s that maps the given node by {@code function0}
     * and then by {@code function1)}.
     */
    public static Function<Node, Set<Node>> combine(Function<Node, Set<Node>> function0,
                                                    Function<Node, Set<Node>> function1) {
        List<Function<Node, Set<Node>>> functions = new ArrayList<>();
        functions.add(function0);
        functions.add(function1);
        return input -> combine(input, functions);
    }

    private static Parent fromWindow(Window window) {
        return window.getScene().getRoot();
    }

    private static Parent fromStage(Stage stage) {
        return stage.getScene().getRoot();
    }

    private static Parent fromScene(Scene scene) {
        return scene.getRoot();
    }

    private static Parent fromPopupControl(PopupControl popupControl) {
        return popupControl.getScene().getRoot();
    }

    private static Set<Node> lookupWithSelector(Node parentNode, String selector) {
        return parentNode.lookupAll(selector);
    }

    private static Set<Node> lookupWithPredicate(Node parentNode, Predicate<Node> predicate) {
        Set<Node> resultNodes = new LinkedHashSet<>();
        if (applyPredicateSafely(predicate, parentNode)) {
            resultNodes.add(parentNode);
        }
        if (parentNode instanceof Parent) {
            List<Node> childNodes = ((Parent) parentNode).getChildrenUnmodifiable();
            for (Node childNode : childNodes) {
                resultNodes.addAll(lookupWithPredicate(childNode, predicate));
            }
        }
        return Collections.unmodifiableSet(resultNodes);
    }

    private static <T> boolean applyPredicateSafely(Predicate<T> predicate, T input) {
        // TODO: Test cases with ClassCastException.
        try {
            return predicate.test(input);
        }
        catch (ClassCastException ignore) {
            return false;
        }
    }

    private static boolean hasNodeText(Node node, String text) {
        // TODO: Test cases with node.getText() == null.
        if (node instanceof Labeled) {
            return Objects.equals(((Labeled) node).getText(), text);
        }
        else if (node instanceof TextInputControl) {
            return Objects.equals(((TextInputControl) node).getText(), text);
        }
        return false;
    }

    private static boolean matchesNodeMatcher(Node node, Matcher matcher) {
        // TODO: Test cases with ClassCastException.
        return matcher.matches(node);
    }

    @SuppressWarnings("deprecation")
    private static boolean isNodeVisible(Node node) {
        return !isNotVisible(node) && isNodeWithinSceneBounds(node);
    }

    private static boolean isNodeWithinSceneBounds(Node node) {
        Scene scene = node.getScene();
        Bounds nodeBounds = node.localToScene(node.getBoundsInLocal());
        return nodeBounds.intersects(0, 0, scene.getWidth(), scene.getHeight());
    }

    private static <T> Set<T> combine(T input, Collection<Function<T, Set<T>>> functions) {
        return functions.stream()
                .map(f -> f.apply(input))
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
