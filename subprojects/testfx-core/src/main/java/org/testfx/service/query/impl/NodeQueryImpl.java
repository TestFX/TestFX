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
package org.testfx.service.query.impl;

import java.util.Collection;
import java.util.Set;
import javafx.scene.Node;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import org.hamcrest.Matcher;
import org.testfx.service.query.NodeQuery;

public class NodeQueryImpl implements NodeQuery {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final String CSS_ID_SELECTOR_PREFIX = "#";

    private static final String CSS_CLASS_SELECTOR_PREFIX = ".";

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private Set<Node> parentNodes = Sets.newLinkedHashSet();

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public NodeQuery from(Node... parentNodes) {
        this.parentNodes.addAll(ImmutableList.copyOf(parentNodes));
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
        FluentIterable<Node> query = FluentIterable.from(parentNodes);
        query = query.filter(Predicates.notNull());
        query = query.transformAndConcat(function);
        parentNodes = query.toSet();
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> NodeQuery select(Matcher<T> matcher) {
        FluentIterable<Node> query = FluentIterable.from(parentNodes);
        query = query.filter(NodeQueryUtils.matchesMatcher((Matcher<Node>) matcher));
        parentNodes = query.toSet();
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> NodeQuery select(Predicate<T> predicate) {
        FluentIterable<Node> query = FluentIterable.from(parentNodes);
        query = query.filter((Predicate<Node>) predicate);
        parentNodes = query.toSet();
        return this;
    }

    @Override
    public NodeQuery selectAt(int index) {
        FluentIterable<Node> query = FluentIterable.from(parentNodes);
        query = query.skip(index).limit(1);
        parentNodes = query.toSet();
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> T queryFirst() {
        FluentIterable<Node> query = FluentIterable.from(parentNodes);
        return (T) query.first().orNull();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> Optional<T> tryQueryFirst() {
        FluentIterable<Node> query = FluentIterable.from(parentNodes);
        return (Optional<T>) query.first();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Node> Set<T> queryAll() {
        FluentIterable<Node> query = FluentIterable.from(parentNodes);
        return (Set<T>) query.toSet();
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private boolean isCssSelector(String query) {
        return query.startsWith(CSS_ID_SELECTOR_PREFIX) ||
            query.startsWith(CSS_CLASS_SELECTOR_PREFIX);
    }

}
