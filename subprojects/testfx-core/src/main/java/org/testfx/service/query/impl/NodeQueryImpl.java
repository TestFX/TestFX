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
    public NodeQuery lookup(Function<Node, Set<Node>> selector) {
        checkNotNull(selector, "selector is null");
        this.selectors.add(new Selector<>(selector));
        return this;
    }

    @Override
    public NodeQuery lookupAt(int index,
                              Function<Node, Set<Node>> selector) {
        checkNotNull(selector, "selector is null");
        this.selectors.add(new Selector<>(selector, index));
        return this;
    }

    @Override
    public NodeQuery match(Predicate<Node> filter) {
        checkNotNull(filter, "filter is null");
        this.filters.add(filter);
        return this;
    }

//    public NodeQuery peek(Predicate<Node> consumer) {
//        checkNotNull(consumer, "consumer is null");
//        Predicate<Node> filter = (node) -> { consumer.apply(node); return true; };
//        this.filters.add(filter);
//        return this;
//    }

    @Override
    public NodeQuery throwIfEmpty() {
        throw new UnsupportedOperationException();
        //return this;
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
        FluentIterable<Node> query = FluentIterable.from(parentNodes).filter(Predicates.notNull());
        query = this.applySelectors(query, this.selectors);
        query = this.applyFilters(query, this.filters);
        return query;
    }

    private FluentIterable<Node> applySelectors(FluentIterable<Node> query,
                                                List<Selector<Node>> selectors) {
        for (Selector<Node> selector : selectors) {
            query = query.transformAndConcat(selector.selector);
            if (selector.index != null) {
                query = query.skip(selector.index).limit(1);
            }
        }
        return query;
    }

    private FluentIterable<Node> applyFilters(FluentIterable<Node> query,
                                              List<Predicate<Node>> filters) {
        for (Predicate<Node> filter : filters) {
            query = query.filter(filter);
        }
        return query;
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC CLASSES.
    //---------------------------------------------------------------------------------------------

    private static class Selector<T> {
        public Function<T, Set<T>> selector;
        public Integer index;

        public Selector(Function<T, Set<T>> selector,
                        Integer index) {
            this.selector = selector;
            this.index = index;
        }

        public Selector(Function<T, Set<T>> selector) {
            this(selector, null);
        }
    }

}
