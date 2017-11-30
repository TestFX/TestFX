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

public interface NodeQuery {

    /**
     * Stores all given {@code parentNodes} within this NodeQuery.
     *
     * @param parentNodes the parentNodes to store
     * @return itself for more method chaining
     */
    NodeQuery from(Node... parentNodes);

    /**
     * Stores all given {@code parentNodes} within this NodeQuery.
     *
     * @param parentNodes the parentNodes to store
     * @return itself for more method chaining
     */
    NodeQuery from(Collection<Node> parentNodes);

    /**
     * Sifts through stored nodes by their id ("#id"), their class (".class"), or the text it has ("text"),
     * depending on the query used, and keeps only those {@code Node}s that meet the query.
     *
     * @param query the query to use
     * @return itself for more method chaining
     */
    NodeQuery lookup(String query);

    /**
     * Sifts through stored nodes and keeps only those {@code Node}s that match the given matcher.
     *
     * @param matcher the matcher used to determine which {@code Node}s to keep and which to remove
     * @param <T> matcher type
     * @return itself for more method chaining
     */
    <T> NodeQuery lookup(Matcher<T> matcher);

    /**
     * Sifts through stored nodes and keeps only those {@code Node}s that pass the given {@code predicate}.
     *
     * @param predicate the predicate used to determine which {@code Node}s to keep and which to remove.
     * @param <T> type that extends {@code Node}
     * @return itself for more method chaining
     */
    <T extends Node> NodeQuery lookup(Predicate<T> predicate);

    /**
     * Sifts through stored nodes and uses {@code function} to determine which nodes to keep and which to remove.
     *
     * @param function that returns the {@code Node}s to keep
     * @return itself for more method chaining
     */
    NodeQuery lookup(Function<Node, Set<Node>> function);

    /**
     * Sifts through stored nodes and removes all {@code Node}s that match the given matcher.
     *
     * @param matcher that determines which {@code Node}s to remove
     * @param <T> matcher type
     * @return itself for more method chaining
     */
    <T> NodeQuery match(Matcher<T> matcher);

    /**
     * Sifts through stored nodes and removes all {@code Node}s that pass the given predicate.
     *
     * @param predicate that indicates which {@code Node}s to remove
     * @param <T> predicate type
     * @return itself for more method chaining
     */
    <T extends Node> NodeQuery match(Predicate<T> predicate);

    /**
     * Keeps the nth {@code Node} in stored nodes and removes all others.
     *
     * @param index within the collection of {@code Node}s
     * @return itself for more method chaining
     */
    NodeQuery nth(int index);

    /**
     * Executes this {@code NodeQuery} and returns the first {@code Node} found that matches
     * this query or {@literal null} if no nodes match this query.
     *
     * @param <T> the type that extends {@code Node}
     * @return the first node found or {@literal null}
     */
    <T extends Node> T query();

    /**
     * Executes this {@code NodeQuery} and returns the first {@link Button} found that matches
     * this query or {@literal null} if no buttons match this query.
     *
     * @return the first {@code Button} found or {@literal null}
     */
    Button queryButton();

    /**
     * Executes this {@code NodeQuery} and returns the first {@link ComboBox} found that matches
     * this query or {@literal null} if no combo-boxes match this query.
     *
     * @return the first {@code ComboBox} found or {@literal null}
     */
    <T> ComboBox<T> queryComboBox();

    /**
     * Executes this {@code NodeQuery} and returns the first {@link Labeled} found that matches
     * this query or {@literal null} if no labeleds match this query.
     *
     * @return the first {@code Labeled} found or {@literal null}
     */
    Labeled queryLabeled();

    /**
     * Executes this {@code NodeQuery} and returns the first {@link ListView} found that matches
     * this query or {@literal null} if no list views match this query.
     *
     * @return the first {@code ListView} found or {@literal null}
     */
    <T> ListView<T> queryListView();

    /**
     * Executes this {@code NodeQuery} and returns the first {@link Parent} found that matches
     * this query or {@literal null} if no parents match this query.
     *
     * @return the first {@code Parent} found or {@literal null}
     */
    Parent queryParent();

    /**
     * Executes this {@code NodeQuery} and returns the first {@link TableView} found that matches
     * this query or {@literal null} if no table views match this query.
     *
     * @return the first {@code TableView} found or {@literal null}
     */
    <T> TableView<T> queryTableView();

    /**
     * Executes this {@code NodeQuery} and returns the first {@link Text} found that matches
     * this query or {@literal null} if no texts match this query.
     *
     * @return the first {@code Text} found or {@literal null}
     */
    Text queryText();

    /**
     * Executes this {@code NodeQuery} and returns the first {@link TextFlow} found that matches
     * this query or {@literal null} if no text flows match this query.
     *
     * @return the first {@code TextFlow} found or {@literal null}
     */
    TextFlow queryTextFlow();

    /**
     * Executes this {@code NodeQuery} and returns the first {@link TextInputControl} found that matches
     * this query or {@literal null} if no text input controls match this query.
     *
     * @return the first {@code TextInputControl} found or {@literal null}
     */
    TextInputControl queryTextInputControl();

    /**
     * Type-safe version of {@link #query()} that executes this {@code NodeQuery} and returns
     * the first {@code Node} found that matches this query or {@literal null} if no nodes
     * match this query.
     *
     * @param clazz the concrete sub-type of {@code Node} that should be returned by this query
     * so as to avoid extraneous casting when used inside an "assertThat" assertion
     * @param <T> the type that extends {@code Node}
     * @return the first node found or {@literal null}
     */
    <T extends Node> T queryAs(Class<T> clazz);

    /**
     * Executes this {@code NodeQuery} and returns an {@code Optional} that either contains
     * the first {@code Node} found that matches this query or nothing (e.g. {@link Optional#empty()}
     * returns {@literal true}) if no nodes match this query.
     *
     * @param <T> the type that extends {@code Node}
     * @return the first node found or {@literal null}
     */
    <T extends Node> Optional<T> tryQuery();

    /**
     * Type-safe version of {@link #tryQuery()} that executes this {@code NodeQuery} and returns an
     * {@code Optional} that either contains the first {@code Node} found that matches this query or
     * nothing (e.g. {@link Optional#empty()} returns {@literal true}) if no nodes match this query.
     *
     * @param clazz the concrete sub-type of {@code Node} that should be contained in the
     * {@code Optional} returned by this query so as to avoid extraneous casting when used inside
     * an "assertThat" assertion
     * @param <T> the type that extends {@code Node}
     * @return the first node found or {@literal null}
     */
    <T extends Node> Optional<T> tryQueryAs(Class<T> clazz);

    /**
     * Executes this {@code NodeQuery} and returns the {@code Set} of all the {@code Node}s that
     * match this query. If no nodes match this query, the empty set is returned.
     *
     * @param <T> the type that extends {@code Node}
     * @return the set of nodes that match this query
     */
    <T extends Node> Set<T> queryAll();

    /**
     * Type-safe version of {@link #queryAll()} that executes this {@code NodeQuery} and returns
     * the {@code Set} of all the {@code Node}s that match this query. If no nodes match this query,
     * the empty set is returned.
     *
     * @param clazz the concrete sub-type of {@code Node} the set of which should be returned by
     * this query so as to avoid extraneous casting when used inside an "assertThat" assertion
     * @param <T> the type that extends {@code Node}
     * @return the set of nodes that match this query
     */
    <T extends Node> Set<T> queryAllAs(Class<T> clazz);

}
