/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2023 The TestFX Contributors
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
package org.testfx.service.query.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.scene.Node;

import org.hamcrest.Matcher;
import org.testfx.service.query.EmptyNodeQueryException;
import org.testfx.service.query.NodeQuery;
import org.testfx.util.NodeQueryUtils;

public class NodeQueryImpl implements NodeQuery {

    private static final String CSS_ID_SELECTOR_PREFIX = "#";
    private static final String CSS_CLASS_SELECTOR_PREFIX = ".";

    private Set<Node> parentNodes = new LinkedHashSet<>();
    private final List<String> queryDescriptors = new ArrayList<>();

    @Override
    public NodeQuery from(Node... parentNodes) {
        this.parentNodes.addAll(Arrays.asList(parentNodes));
        queryDescriptors.add("from nodes: " + Arrays.toString(parentNodes));
        return this;
    }

    @Override
    public NodeQuery from(Collection<Node> parentNodes) {
        this.parentNodes.addAll(parentNodes);
        queryDescriptors.add("from nodes: " + parentNodes);
        return this;
    }

    @Override
    public NodeQuery lookup(String query) {
        Function<Node, Set<Node>> queryFunction = isCssSelector(query) ?
            NodeQueryUtils.bySelector(query) : NodeQueryUtils.byText(query);
        lookup(queryFunction);
        queryDescriptors.add("lookup by " + (isCssSelector(query) ? "selector" : "text") + ": \"" + query + "\"");
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> NodeQuery lookup(Matcher<T> matcher) {
        lookup(NodeQueryUtils.byMatcher((Matcher<Node>) matcher));
        queryDescriptors.add("lookup by matcher: \"" + matcher + "\"");
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> NodeQuery lookup(Predicate<T> predicate) {
        lookup(NodeQueryUtils.byPredicate((Predicate<Node>) predicate));
        queryDescriptors.add("lookup by predicate: \"" + predicate + "\"");
        return this;
    }

    @Override
    public NodeQuery lookup(Function<Node, Set<Node>> function) {
        // surely there's a better way to do the following
        parentNodes = parentNodes.stream()
            .map(function)
            .reduce((nodes, nodes2) -> {
                Set<Node> set = new LinkedHashSet<>(nodes);
                set.addAll(nodes2);
                return set;
            }).orElseGet(LinkedHashSet::new);
        queryDescriptors.add("lookup by function: \"" + function + "\"");
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> NodeQuery match(Matcher<T> matcher) {
        parentNodes = parentNodes.stream()
            .filter(NodeQueryUtils.matchesMatcher((Matcher<Node>) matcher))
            .collect(Collectors.toCollection(LinkedHashSet::new));
        queryDescriptors.add("matching by matcher: " + matcher);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> NodeQuery match(Predicate<T> predicate) {
        parentNodes = parentNodes.stream()
            .filter((Predicate<Node>) predicate)
            .collect(Collectors.toCollection(LinkedHashSet::new));
        queryDescriptors.add("matching by predicate: " + predicate);
        return this;
    }

    @Override
    public NodeQuery nth(int index) {
        parentNodes = parentNodes.stream()
            .skip(index)
            .limit(1)
            .collect(Collectors.toCollection(LinkedHashSet::new));
        queryDescriptors.add("fetching the " + ordinal(index) + " node");
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> T query() {
        if (parentNodes.isEmpty()) {
            throw new EmptyNodeQueryException("there is no node in the scene-graph matching the query: " + this);
        } else {
            return (T) parentNodes.iterator().next();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> T queryAs(Class<T> clazz) {
        if (parentNodes.stream().noneMatch(node -> clazz.isAssignableFrom(node.getClass()))) {
            throw new EmptyNodeQueryException("there is no node in the scene-graph matching the query: " + this);
        } else {
            return (T) parentNodes.iterator().next();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> Optional<T> tryQuery() {
        if (parentNodes.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of((T) parentNodes.iterator().next());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> Optional<T> tryQueryAs(Class<T> clazz) {
        if (parentNodes.stream().noneMatch(node -> clazz.isAssignableFrom(node.getClass()))) {
            return Optional.empty();
        } else {
            return Optional.of((T) parentNodes.iterator().next());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> Set<T> queryAll() {
        return (Set<T>) new LinkedHashSet<>(parentNodes);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> Set<T> queryAllAs(Class<T> clazz) {
        return (Set<T>) new LinkedHashSet<>(parentNodes);
    }

    @Override
    public String toString() {
        if (queryDescriptors.isEmpty()) {
            return "the empty NodeQuery";
        }
        return "NodeQuery: " + String.join(",\n", queryDescriptors);
    }

    private static boolean isCssSelector(String query) {
        return query.startsWith(CSS_ID_SELECTOR_PREFIX) ||
            query.startsWith(CSS_CLASS_SELECTOR_PREFIX);
    }

    /**
     * https://stackoverflow.com/a/6810409/3634630
     */
    private static String ordinal(int i) {
        String[] sufixes = new String[] {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];
        }
    }
}
