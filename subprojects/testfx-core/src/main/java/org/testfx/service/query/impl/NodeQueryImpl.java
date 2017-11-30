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
            .collect(Collectors.toSet());
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> NodeQuery match(Predicate<T> predicate) {
        parentNodes = parentNodes.stream()
            .filter((Predicate<Node>) predicate)
            .collect(Collectors.toSet());
        return this;
    }

    @Override
    public NodeQuery nth(int index) {
        parentNodes = parentNodes.stream()
            .skip(index)
            .limit(1)
            .collect(Collectors.toSet());
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> T query() {
        return (T) parentNodes.stream().findFirst().orElse(null);
    }

    @Override
    public Button queryButton() {
        return (Button) parentNodes.stream().findFirst().orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ComboBox<T> queryComboBox() {
        return (ComboBox<T>) parentNodes.stream().findFirst().orElse(null);
    }

    @Override
    public Labeled queryLabeled() {
        return (Labeled) parentNodes.stream().findFirst().orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ListView<T> queryListView() {
        return (ListView<T>) parentNodes.stream().findFirst().orElse(null);
    }

    @Override
    public Parent queryParent() {
        return (Parent) parentNodes.stream().findFirst().orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> TableView<T> queryTableView() {
        return (TableView<T>) parentNodes.stream().findFirst().orElse(null);
    }

    @Override
    public Text queryText() {
        return (Text) parentNodes.stream().findFirst().orElse(null);
    }

    @Override
    public TextFlow queryTextFlow() {
        return (TextFlow) parentNodes.stream().findFirst().orElse(null);
    }

    @Override
    public TextInputControl queryTextInputControl() {
        return (TextInputControl) parentNodes.stream().findFirst().orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> T queryAs(Class<T> clazz) {
        return (T) parentNodes.stream().findFirst().orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> Optional<T> tryQuery() {
        return (Optional<T>) parentNodes.stream().findFirst();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> Optional<T> tryQueryAs(Class<T> clazz) {
        return (Optional<T>) parentNodes.stream().findFirst();
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
