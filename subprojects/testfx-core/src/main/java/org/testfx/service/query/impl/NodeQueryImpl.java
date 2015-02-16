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

import java.util.List;
import java.util.Set;
import javafx.scene.Node;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.testfx.service.query.NodeQuery;

import static com.google.common.base.Preconditions.checkNotNull;

public class NodeQueryImpl implements NodeQuery {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private Set<Node> parentNodes = Sets.newLinkedHashSet();

    private List<Selector<Node>> selectors = Lists.newArrayList();

    private List<Predicate<Node>> filters = Lists.newArrayList();

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public NodeQuery from(Set<Node> parentNodes) {
        checkNotNull(parentNodes, "parentNodes is null");
        this.parentNodes.addAll(parentNodes);
        return this;
    }

    @Override
    public NodeQuery from(Node... parentNodes) {
        checkNotNull(parentNodes, "parentNodes is null");
        this.parentNodes.addAll(ImmutableList.copyOf(parentNodes));
        return this;
    }

    @Override
    public NodeQuery lookup(Function<Node, Set<Node>> function) {
        checkNotNull(function, "function is null");
        this.selectors.add(new Selector<>(function));
        return this;
    }

    @Override
    public NodeQuery lookupAt(int index,
                              Function<Node, Set<Node>> function) {
        checkNotNull(function, "function is null");
        this.selectors.add(new Selector<>(function, index));
        return this;
    }

    @Override
    public NodeQuery childAt(int index) {
        this.selectors.add(new Selector<>(index));
        return this;
    }

    @Override
    public NodeQuery match(Predicate<Node> predicate) {
        checkNotNull(predicate, "predicate is null");
        this.filters.add(predicate);
        return this;
    }

    @Override
    public Set<Node> queryAll() {
        FluentIterable<Node> resultNodes = this.buildQuery(this.parentNodes);
        return resultNodes.toSet();
    }

    @Override
    public Optional<Node> queryFirst() {
        FluentIterable<Node> resultNodes = this.buildQuery(this.parentNodes);
        return resultNodes.first();
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private FluentIterable<Node> buildQuery(Set<Node> parentNodes) {
        FluentIterable<Node> query = FluentIterable.from(parentNodes);
        query = query.filter(Predicates.notNull());
        for (Selector<Node> selector : selectors) {
            query = applySelector(query, selector.function, selector.index);
        }
        for (Predicate<Node> filter : filters) {
            query = applyFilter(query, filter);
        }
        return query;
    }

    private FluentIterable<Node> applySelector(FluentIterable<Node> query,
                                               Function<Node, Set<Node>> function,
                                               Integer index) {
        if (function != null) {
            query = query.transformAndConcat(function);
        }
        if (index != null) {
            query = query.skip(index).limit(1);
        }
        return query;
    }

    private FluentIterable<Node> applyFilter(FluentIterable<Node> query,
                                             Predicate<Node> predicate) {
        query = query.filter(predicate);
        return query;
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC CLASSES.
    //---------------------------------------------------------------------------------------------

    private static class Selector<T> {
        public Function<T, Set<T>> function;
        public Integer index;

        public Selector(Function<T, Set<T>> function,
                        Integer index) {
            this.function = function;
            this.index = index;
        }

        public Selector(Function<T, Set<T>> function) {
            this(function, null);
        }

        public Selector(int index) {
            this(null, index);
        }
    }

}
