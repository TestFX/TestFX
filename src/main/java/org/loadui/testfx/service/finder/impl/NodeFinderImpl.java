package org.loadui.testfx.service.finder.impl;

import java.util.List;
import java.util.Set;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;
import javafx.stage.Window;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.hamcrest.Matcher;
import org.loadui.testfx.service.finder.NodeFinder;
import org.loadui.testfx.service.finder.NodeFinderException;
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
        List<Window> windows = windowFinder.listWindows();
        Window window = windows.get(windowNumber);
        target(window);
    }

    public void target(String stageTitleRegex) {
        List<Window> windows = this.windowFinder.listWindows();
        Window window = Iterables.find(windows, hasStageTitlePredicate(stageTitleRegex));
        target(window);
    }

    public void target(Scene scene) {
        Window window = scene.getWindow();
        target(window);
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
        List<Window> windows = windowFinder.listWindows();
        Window window = windows.get(windowNumber);
        return parent(window);
    }

    public Node parent(String stageTitleRegex) {
        List<Window> windows = windowFinder.listWindows();
        Window window = Iterables.find(windows, hasStageTitlePredicate(stageTitleRegex));
        return parent(window);
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
        assertNodesFound(visibleNodes, ERROR_NO_VISIBLE_NODES_FOUND);
        return visibleNodes;
    }

    private Set<Node> transformToResultNodes(List<Window> windows,
            Function<Node, Set<Node>> toResultNodesFunction) {
        Iterable<Node> rootNodes = Iterables.transform(windows, toRootNodeFunction());
        Iterable<Set<Node>> resultNodes = Iterables.transform(rootNodes, toResultNodesFunction);
        return ImmutableSet.copyOf(Iterables.concat(resultNodes));
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
        if (predicate.apply(parentNode)) {
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
                return isNodeLabel(node, label);
            }
        };
    }

    private Predicate<Window> hasStageTitlePredicate(final String stageTitleRegex) {
        return new Predicate<Window>() {
            @Override
            public boolean apply(Window window) {
                return window instanceof Stage &&
                    isStageTitle((Stage) window, stageTitleRegex);
            }
        };
    }

    private boolean isCssSelector(String query) {
        return query.startsWith(CSS_ID_SELECTOR_SYMBOL) ||
            query.startsWith(CSS_CLASS_SELECTOR_SYMBOL);
    }

    private boolean isStageTitle(Stage stage, String stageTitleRegex) {
        return stage.getTitle().matches(stageTitleRegex);
    }

    private boolean isNodeLabel(Node node, String label) {
        if (node instanceof Labeled) {
            return ((Labeled) node).getText().equals(label);
        }
        else if (node instanceof TextInputControl) {
            return ((TextInputControl) node).getText().equals(label);
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    private boolean isNodeVisible(Node node) {
        // TODO: !node.isVisible() || !node.impl_isTreeVisible()
        // TODO: isNodeWithinSceneBounds
        return node.isVisible() && node.impl_isTreeVisible();
    }

    private void assertNodesFound(Set<Node> resultNodes, String errorMessage) {
        if (resultNodes.isEmpty()) {
            throw new NodeFinderException(errorMessage);
        }
    }

}
