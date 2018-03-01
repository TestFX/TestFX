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
package org.testfx.service.query.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import org.hamcrest.Matcher;
import org.testfx.api.annotation.Unstable;
import org.testfx.service.query.NodeQuery;
import org.testfx.util.NodeQueryUtils;

@Unstable
public class NodeQueryImpl implements NodeQuery {

    private static final String CSS_ID_SELECTOR_PREFIX = "#";
    private static final String CSS_CLASS_SELECTOR_PREFIX = ".";

    private Set<Node> parentNodes = new LinkedHashSet<>();

    @Override
    public NodeQuery from(Node... parentNodes) {
        this.parentNodes.addAll(Arrays.asList(parentNodes));
        return this;
    }

    @Override
    public NodeQuery from(Collection<Node> parentNodes) {
        this.parentNodes.addAll(parentNodes);
        return this;
    }

    @Override
    public NodeQuery lookup(String query) {
        Function<Node, Set<Node>> queryFunction = isCssSelector(query) ?
            NodeQueryUtils.bySelector(query) : NodeQueryUtils.byText(query);
        lookup(queryFunction);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> NodeQuery lookup(Matcher<T> matcher) {
        lookup(NodeQueryUtils.byMatcher((Matcher<Node>) matcher));
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> NodeQuery lookup(Predicate<T> predicate) {
        lookup(NodeQueryUtils.byPredicate((Predicate<Node>) predicate));
        return this;
    }

    @Override
    public NodeQuery lookup(Function<Node, Set<Node>> function) {
        // surely there's a better way to do the following
        parentNodes = parentNodes.stream()
            .filter(Objects::nonNull)
            .map(function)
            .reduce((nodes, nodes2) -> {
                Set<Node> set = new LinkedHashSet<>(nodes);
                set.addAll(nodes2);
                return set;
            }).orElseGet(LinkedHashSet::new);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> NodeQuery match(Matcher<T> matcher) {
        parentNodes = parentNodes.stream()
            .filter(NodeQueryUtils.matchesMatcher((Matcher<Node>) matcher))
            .collect(Collectors.toCollection(LinkedHashSet::new));
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> NodeQuery match(Predicate<T> predicate) {
        parentNodes = parentNodes.stream()
            .filter((Predicate<Node>) predicate)
            .collect(Collectors.toCollection(LinkedHashSet::new));
        return this;
    }

    @Override
    public NodeQuery nth(int index) {
        parentNodes = parentNodes.stream()
            .skip(index)
            .limit(1)
            .collect(Collectors.toCollection(LinkedHashSet::new));
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> T query() {
        if (parentNodes.isEmpty()) {
            return null;
        } else {
            return (T) parentNodes.iterator().next();
        }
    }

    @Override
    public Button queryButton() {
        if (parentNodes.stream().noneMatch(node -> Button.class.isAssignableFrom(node.getClass()))) {
            return null;
        } else {
            return (Button) parentNodes.iterator().next();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ComboBox<T> queryComboBox() {
        if (parentNodes.stream().noneMatch(node -> ComboBox.class.isAssignableFrom(node.getClass()))) {
            return null;
        } else {
            return (ComboBox<T>) parentNodes.iterator().next();
        }
    }

    @Override
    public Labeled queryLabeled() {
        if (parentNodes.stream().noneMatch(node -> Labeled.class.isAssignableFrom(node.getClass()))) {
            return null;
        } else {
            return (Labeled) parentNodes.iterator().next();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ListView<T> queryListView() {
        if (parentNodes.stream().noneMatch(node -> ListView.class.isAssignableFrom(node.getClass()))) {
            return null;
        } else {
            return (ListView<T>) parentNodes.iterator().next();
        }
    }

    @Override
    public Parent queryParent() {
        if (parentNodes.stream().noneMatch(node -> Parent.class.isAssignableFrom(node.getClass()))) {
            return null;
        } else {
            return (Parent) parentNodes.iterator().next();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> TableView<T> queryTableView() {
        if (parentNodes.stream().noneMatch(node -> TableView.class.isAssignableFrom(node.getClass()))) {
            return null;
        } else {
            return (TableView<T>) parentNodes.iterator().next();
        }
    }

    @Override
    public Text queryText() {
        if (parentNodes.stream().noneMatch(node -> Text.class.isAssignableFrom(node.getClass()))) {
            return null;
        } else {
            return (Text) parentNodes.iterator().next();
        }
    }

    @Override
    public TextFlow queryTextFlow() {
        if (parentNodes.stream().noneMatch(node -> TextFlow.class.isAssignableFrom(node.getClass()))) {
            return null;
        } else {
            return (TextFlow) parentNodes.iterator().next();
        }
    }

    @Override
    public TextInputControl queryTextInputControl() {
        if (parentNodes.stream().noneMatch(node -> TextInputControl.class.isAssignableFrom(node.getClass()))) {
            return null;
        } else {
            return (TextInputControl) parentNodes.iterator().next();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> T queryAs(Class<T> clazz) {
        if (parentNodes.stream().noneMatch(node -> clazz.isAssignableFrom(node.getClass()))) {
            return null;
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

    private boolean isCssSelector(String query) {
        return query.startsWith(CSS_ID_SELECTOR_PREFIX) ||
            query.startsWith(CSS_CLASS_SELECTOR_PREFIX);
    }

}
