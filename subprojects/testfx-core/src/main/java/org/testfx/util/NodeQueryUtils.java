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
package org.testfx.util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Labeled;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;
import javafx.stage.Window;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.hamcrest.Matcher;
import org.testfx.api.annotation.Unstable;

@Unstable
public final class NodeQueryUtils {

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    private NodeQueryUtils() {
        throw new UnsupportedOperationException();
    }

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static Set<Node> rootsOfWindows(Collection<Window> windows) {
        return rootOfWindow(Iterables.toArray(windows, Window.class));
    }

    public static Set<Node> rootOfWindow(Window... windows) {
        // TODO: is this set (toSet()) in order?
        return FluentIterable.from(ImmutableList.copyOf(windows))
            .<Node>transform((window) -> fromWindow(window))
            .toSet();
    }

    public static Set<Node> rootOfStage(Stage... stages) {
        return FluentIterable.from(ImmutableList.copyOf(stages))
            .<Node>transform((stage) -> fromStage(stage))
            .toSet();
    }

    public static Set<Node> rootOfScene(Scene... scenes) {
        return FluentIterable.from(ImmutableList.copyOf(scenes))
            .<Node>transform((scene) -> fromScene(scene))
            .toSet();
    }

    public static Set<Node> rootOfPopupControl(PopupControl... popupControls) {
        return FluentIterable.from(ImmutableList.copyOf(popupControls))
            .<Node>transform((popupControl) -> fromPopupControl(popupControl))
            .toSet();
    }

    public static Function<Node, Set<Node>> bySelector(String selector) {
        return (parentNode) -> lookupWithSelector(parentNode, selector);
    }

    public static Function<Node, Set<Node>> byPredicate(Predicate<Node> predicate) {
        return (parentNode) -> lookupWithPredicate(parentNode, predicate);
    }

    public static Function<Node, Set<Node>> byMatcher(Matcher<Node> matcher) {
        return byPredicate(matchesMatcher(matcher));
    }

    public static Function<Node, Set<Node>> byText(String text) {
        return byPredicate(hasText(text));
    }

    public static Predicate<Node> hasId(String id) {
        return (node) -> hasNodeId(node, id);
    }

    public static Predicate<Node> hasText(String text) {
        return (node) -> hasNodeText(node, text);
    }

    public static Predicate<Node> matchesMatcher(Matcher<Node> matcher) {
        return (node) -> matchesNodeMatcher(node, matcher);
    }

    public static Predicate<Node> isVisible() {
        return (node) -> isNodeVisible(node);
    }

    public static Function<Node, Set<Node>> combine(Function<Node, Set<Node>> function0,
                                                    Function<Node, Set<Node>> function1) {
        return (input) -> combine(input, ImmutableList.of(function0, function1));
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static Parent fromWindow(Window window) {
        //return window?.scene?.root
        return window.getScene().getRoot();
    }

    private static Parent fromStage(Stage stage) {
        //return stage?.scene?.root
        return stage.getScene().getRoot();
    }

    private static Parent fromScene(Scene scene) {
        //return scene?.root
        return scene.getRoot();
    }

    private static Parent fromPopupControl(PopupControl popupControl) {
        //return popupControl?.scene?.root
        return popupControl.getScene().getRoot();
    }

    private static Set<Node> lookupWithSelector(Node parentNode,
                                                String selector) {
        return parentNode.lookupAll(selector);
    }

    private static Set<Node> lookupWithPredicate(Node parentNode,
                                                 Predicate<Node> predicate) {
        Set<Node> resultNodes = Sets.newLinkedHashSet();
        if (applyPredicateSafely(predicate, parentNode)) {
            resultNodes.add(parentNode);
        }
        if (parentNode instanceof Parent) {
            List<Node> childNodes = ((Parent) parentNode).getChildrenUnmodifiable();
            for (Node childNode : childNodes) {
                resultNodes.addAll(lookupWithPredicate(childNode, predicate));
            }
        }
        return ImmutableSet.copyOf(resultNodes);
    }

    private static <T> boolean applyPredicateSafely(Predicate<T> predicate,
                                                    T input) {
        // TODO: Test cases with ClassCastException.
        try {
            return predicate.test(input);
        }
        catch (ClassCastException ignore) {
            return false;
        }
    }

    private static boolean hasNodeId(Node node,
                                       String id) {
        return Objects.equals(node.getId(), id);
    }

    private static boolean hasNodeText(Node node,
                                         String text) {
        // TODO: Test cases with node.getText() == null.
        if (node instanceof Labeled) {
            return Objects.equals(((Labeled) node).getText(), text);
        }
        else if (node instanceof TextInputControl) {
            return Objects.equals(((TextInputControl) node).getText(), text);
        }
        return false;
    }

    private static boolean matchesNodeMatcher(Node node,
                                                Matcher matcher) {
        // TODO: Test cases with ClassCastException.
        return matcher.matches(node);
    }

    @SuppressWarnings("deprecation")
    private static boolean isNodeVisible(Node node) {
        if (!node.isVisible() || !node.impl_isTreeVisible()) {
            return false;
        }
        else if (!isNodeWithinSceneBounds(node)) {
            return false;
        }
        return true;
    }

    private static boolean isNodeWithinSceneBounds(Node node) {
        Scene scene = node.getScene();
        Bounds nodeBounds = node.localToScene(node.getBoundsInLocal());
        return nodeBounds.intersects(0, 0, scene.getWidth(), scene.getHeight());
    }

    private static <T> Set<T> combine(T input,
                                      Collection<Function<T, Set<T>>> functions) {
        return FluentIterable.from(functions)
            .transformAndConcat((f) -> f.apply(input))
            .toSet();
    }

}
