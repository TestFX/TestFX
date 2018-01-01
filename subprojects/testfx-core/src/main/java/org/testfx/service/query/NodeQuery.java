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
package org.testfx.service.query;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import javafx.scene.Node;

import org.hamcrest.Matcher;

public interface NodeQuery {

    /**
     * Stores all given {@code parentNodes} within this NodeQuery.
     *
     * @param parentNodes the parentNodes to store
     * @return itself
     */
    NodeQuery from(Node... parentNodes);

    /**
     * Stores all given {@code parentNodes} within this NodeQuery.
     *
     * @param parentNodes the parentNodes to store
     * @return itself
     */
    NodeQuery from(Collection<Node> parentNodes);

    /**
     * Sifts through stored nodes by their id ("#id"), their class (".class"), or the text it has ("text"),
     * depending on the query used, and keeps only those {@code Node}s that meet the query.
     *
     * @param query the query to use
     * @return itself
     */
    NodeQuery lookup(String query);

    /**
     * Sifts through stored nodes and keeps only those {@code Node}s that match the given matcher.
     *
     * @param matcher the matcher used to determine which {@code Node}s to keep and which to remove
     * @param <T> matcher type
     * @return itself
     */
    <T> NodeQuery lookup(Matcher<T> matcher);

    /**
     * Sifts through stored nodes and keeps only those {@code Node}s that pass the given {@code predicate}.
     *
     * @param predicate the predicate used to determine which {@code Node}s to keep and which to remove.
     * @param <T> type that extends {@code Node}
     * @return itself
     */
    <T extends Node> NodeQuery lookup(Predicate<T> predicate);

    /**
     * Sifts through stored nodes and uses {@code function} to determine which nodes to keep and which to remove.
     *
     * @param function that returns the {@code Node}s to keep
     * @return itself
     */
    NodeQuery lookup(Function<Node, Set<Node>> function);

    /**
     * Sifts through stored nodes and removes all {@code Node}s that match the given matcher.
     *
     * @param matcher that determines which {@code Node}s to remove
     * @param <T> matcher type
     * @return itself
     */
    <T> NodeQuery match(Matcher<T> matcher);

    /**
     * Sifts through stored nodes and removes all {@code Node}s that pass the given predicate.
     *
     * @param predicate that indicates which {@code Node}s to remove
     * @param <T> predicate type
     * @return itself
     */
    <T extends Node> NodeQuery match(Predicate<T> predicate);

    /**
     * Keeps the nth {@code Node} in stored nodes and removes all others.
     *
     * @param index within the collection of {@code Node}s
     * @return itself
     */
    NodeQuery nth(int index);

    /**
     *
     * @param <T> type that extends {@code Node}
     * @return the first node found or null.
     */
    <T extends Node> T query();

    /**
     *
     * @param <T> type that extends {@code Node}
     * @return the first node found or null
     */
    <T extends Node> Optional<T> tryQuery();

    /**
     *
     * @param <T> type that extends {@code Node}
     * @return all nodes found
     */
    <T extends Node> Set<T> queryAll();

}
