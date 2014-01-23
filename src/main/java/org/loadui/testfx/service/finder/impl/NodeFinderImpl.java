/*
 * Copyright 2013 SmartBear Software
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
package org.loadui.testfx.service.finder.impl;

import java.util.List;
import java.util.Set;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.stage.Window;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.hamcrest.Matcher;
import org.loadui.testfx.exceptions.NoNodesFoundException;
import org.loadui.testfx.exceptions.NoNodesVisibleException;
import org.loadui.testfx.service.finder.NodeFinder;
import org.loadui.testfx.service.finder.WindowFinder;

public class NodeFinderImpl implements NodeFinder {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    static final String CSS_ID_SELECTOR_SYMBOL = "#";
    static final String CSS_CLASS_SELECTOR_SYMBOL = ".";

    static final String ERROR_NO_NODES_FOUND = "No matching nodes were found.";
    static final String ERROR_NO_VISIBLE_NODES_FOUND =
        "Matching nodes were found, but none of them are visible.";

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private WindowFinder windowFinder;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public NodeFinderImpl(WindowFinder windowFinder) {
        this.windowFinder = windowFinder;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public void target(Window window) {
        windowFinder.setLastTargetWindow(window);
    }

    public void target(int windowNumber) {
        target(windowFinder.window(windowNumber));
    }

    public void target(String stageTitleRegex) {
        target(windowFinder.window(stageTitleRegex));
    }

    public void target(Scene scene) {
        target(windowFinder.window(scene));
    }

    public Node node(String query) {
        Function<Node, Set<Node>> toResultNodesFunction;
        if (isCssSelector(query)) {
            toResultNodesFunction = fromNodeCssSelectorFunction(query);
        }
        else {
            Predicate<Node> nodeLabelPredicate = hasNodeLabelPredicate(query);
            toResultNodesFunction = fromNodesPredicateFunction(nodeLabelPredicate);
        }
        List<Window> orderedWindows = windowFinder.listOrderedWindows();
        Set<Node> resultNodes = nodesImpl(orderedWindows, toResultNodesFunction);
        return Iterables.getFirst(resultNodes, null);
    }

    public Set<Node> nodes(String query) {
        Function<Node, Set<Node>> toResultNodesFunction;
        if (isCssSelector(query)) {
            toResultNodesFunction = fromNodesCssSelectorFunction(query);
        }
        else {
            Predicate<Node> nodeLabelPredicate = hasNodeLabelPredicate(query);
            toResultNodesFunction = fromNodesPredicateFunction(nodeLabelPredicate);
        }
        List<Window> orderedWindows = windowFinder.listOrderedWindows();
        return nodesImpl(orderedWindows, toResultNodesFunction);
    }

    public Node node(Predicate<Node> predicate) {
        Set<Node> resultNodes = nodes(predicate);
        return Iterables.getFirst(resultNodes, null);
    }

    public Set<Node> nodes(Predicate<Node> predicate) {
        List<Window> windows = windowFinder.listWindows();
        Function<Node, Set<Node>> toResultNodesFunction = fromNodesPredicateFunction(predicate);
        return nodesImpl(windows, toResultNodesFunction);
    }

    public Node node(Matcher<Object> matcher) {
        Set<Node> resultNodes = nodes(matcher);
        return Iterables.getFirst(resultNodes, null);
    }

    public Set<Node> nodes(Matcher<Object> matcher) {
        Predicate<Node> nodeMatcherPredicate = toNodeMatcherPredicate(matcher);
        return nodes(nodeMatcherPredicate);
    }

    public Node parent(Window window) {
        return window.getScene().getRoot();
    }

    public Node parent(int windowNumber) {
        return parent(windowFinder.window(windowNumber));
    }

    public Node parent(String stageTitleRegex) {
        return parent(windowFinder.window(stageTitleRegex));
    }

    public Node parent(Scene scene) {
        return scene.getRoot();
    }

    public Node node(String query, Node parentNode) {
        Set<Node> resultNodes = nodes(query, parentNode);
        return Iterables.getFirst(resultNodes, null);
    }

    public Set<Node> nodes(String query, Node parentNode) {
        // TODO: Filter visible nodes and allow label queries.
        target(parentNode.getScene().getWindow());
        return findNodesInParent(query, parentNode);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE TRANSFORM METHODS.
    //---------------------------------------------------------------------------------------------

    private Set<Node> nodesImpl(List<Window> windows,
            Function<Node, Set<Node>> toResultNodesFunction) {
        Set<Node> resultNodes = transformToResultNodes(windows, toResultNodesFunction);
        assertNodesFound(resultNodes, ERROR_NO_NODES_FOUND);

        Set<Node> visibleNodes = Sets.filter(resultNodes, isNodeVisiblePredicate());
        assertNodesVisible(visibleNodes, ERROR_NO_VISIBLE_NODES_FOUND);
        return visibleNodes;
    }

    private Set<Node> transformToResultNodes(List<Window> windows,
            Function<Node, Set<Node>> toResultNodesFunction) {
        //return FluentIterable.from(windows)
        //    .transform(toRootNodeFunction())
        //    .transformAndConcat(toResultNodesFunction)
        //    .filter(Predicates.notNull())
        //    .toSet();
        Iterable<Node> rootNodes = Iterables.transform(windows, toRootNodeFunction());
        Iterable<Set<Node>> resultNodeSets = Iterables.transform(rootNodes, toResultNodesFunction);
        Iterable<Node> resultNodes = Iterables.concat(resultNodeSets);
        return ImmutableSet.copyOf(Iterables.filter(resultNodes, Predicates.notNull()));
    }

    private Function<Window, Node> toRootNodeFunction() {
        return new Function<Window, Node>() {
            @Override
            public Node apply(Window window) {
                if (window != null && window.getScene() != null) {
                    return window.getScene().getRoot();
                }
                return null;
            }
        };
    }

    private Function<Node, Set<Node>> fromNodeCssSelectorFunction(final String selector) {
        return new Function<Node, Set<Node>>() {
            @Override
            public Set<Node> apply(Node rootNode) {
                return Sets.newHashSet(findNodeInParent(selector, rootNode));
            }
        };
    }

    private Function<Node, Set<Node>> fromNodesCssSelectorFunction(final String selector) {
        return new Function<Node, Set<Node>>() {
            @Override
            public Set<Node> apply(Node rootNode) {
                return findNodesInParent(selector, rootNode);
            }
        };
    }

    private Function<Node, Set<Node>> fromNodesPredicateFunction(final Predicate<Node> predicate) {
        return new Function<Node, Set<Node>>() {
            @Override
            public Set<Node> apply(Node rootNode) {
                return findNodesInParent(predicate, rootNode);
            }
        };
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE BACKEND METHODS.
    //---------------------------------------------------------------------------------------------

    private Node findNodeInParent(String selector, Node parentNode) {
        return parentNode.lookup(selector);
    }

    private Set<Node> findNodesInParent(String selector, Node parentNode) {
        return parentNode.lookupAll(selector);
    }

    private Set<Node> findNodesInParent(Predicate<Node> predicate, Node parentNode) {
        Set<Node> resultNodes = Sets.newLinkedHashSet();
        if (applyPredicateOnNode(predicate, parentNode)) {
            resultNodes.add(parentNode);
        }
        if (parentNode instanceof Parent) {
            List<Node> childNodes = ((Parent) parentNode).getChildrenUnmodifiable();
            for (Node childNode : childNodes) {
                resultNodes.addAll(findNodesInParent(predicate, childNode));
            }
        }
        return ImmutableSet.copyOf(resultNodes);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE HELPER METHODS.
    //---------------------------------------------------------------------------------------------

    private boolean applyPredicateOnNode(Predicate<Node> predicate, Node node) {
        // TODO: Test cases with ClassCastException.
        try {
            return predicate.apply(node);
        }
        catch (ClassCastException exception) {
            return false;
        }
    }

    private Predicate<Node> toNodeMatcherPredicate(final Matcher<Object> matcher) {
        return new Predicate<Node>() {
            @Override
            public boolean apply(Node node) {
                return matcher.matches(node);
            }
        };
    }

    private Predicate<Node> isNodeVisiblePredicate() {
        return new Predicate<Node>() {
            @Override
            public boolean apply(Node node) {
                return isNodeVisible(node);
            }
        };
    }

    private Predicate<Node> hasNodeLabelPredicate(final String label) {
        return new Predicate<Node>() {
            @Override
            public boolean apply(Node node) {
                return hasNodeLabel(node, label);
            }
        };
    }

    private boolean isCssSelector(String query) {
        return query.startsWith(CSS_ID_SELECTOR_SYMBOL) ||
            query.startsWith(CSS_CLASS_SELECTOR_SYMBOL);
    }

    private boolean hasNodeLabel(Node node, String label) {
        // TODO: Test cases with node.getText() == null.
        if (node instanceof Labeled) {
            return label.equals(((Labeled) node).getText());
        }
        else if (node instanceof TextInputControl) {
            return label.equals(((TextInputControl) node).getText());
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    private boolean isNodeVisible(Node node) {
        if (!node.isVisible() || !node.impl_isTreeVisible()) {
            return false;
        }
        return isNodeWithinSceneBounds(node);
    }

    private boolean isNodeWithinSceneBounds(Node node) {
        Scene scene = node.getScene();
        Bounds nodeBounds = node.localToScene(node.getBoundsInLocal());
        return nodeBounds.intersects(0, 0, scene.getWidth(), scene.getHeight());
    }

    private void assertNodesFound(Set<Node> resultNodes, String errorMessage) {
        // TODO: Save screenshot on exception.
        if (resultNodes.isEmpty()) {
            throw new NoNodesFoundException(errorMessage);
        }
    }

    private void assertNodesVisible(Set<Node> resultNodes, String errorMessage) {
        // TODO: Save screenshot on exception.
        if (resultNodes.isEmpty()) {
            throw new NoNodesVisibleException(errorMessage);
        }
    }

}
