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
     * Sifts through stored nodes and keeps only those {@code Node}s that match the given matcher.
     *
     * @param matcher that determines which {@code Node}s to keep
     * @param <T> matcher type
     * @return itself for more method chaining
     */
    <T> NodeQuery match(Matcher<T> matcher);

    /**
     * Sifts through stored nodes and keeps only those {@code Node}s that pass the given predicate.
     *
     * @param predicate that indicates which {@code Node}s to keep
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
     * this query. If no nodes match this query then an {@link EmptyNodeQueryException} is thrown.
     * <p>
     * The determinism of this method relies on the determinism of {@link Node#lookupAll(String)},
     * for which the JavaDocs specifically state that the result is unordered. The current (9.0.4)
     * version of JavaFX happens to return the nodes in the order in which they are encountered whilst
     * traversing the scene graph but this could change in future versions of JavaFX. Thus if there are
     * multiple nodes matched by this query and you want a specific one it is advised not to use this
     * method and instead narrow the query so that only one node is matched.
     *
     * @param <T> the type that extends {@code Node}
     * @return the first node found that matches this query, if any
     * @throws EmptyNodeQueryException if no {@code TextFlow} nodes match this query
     */
    <T extends Node> T query();

    /**
     * Executes this {@code NodeQuery} and returns the first {@link Button} found that matches
     * this query. If no nodes match this query then an {@link EmptyNodeQueryException} is thrown.
     * <p>
     * The determinism of this method relies on the determinism of {@link Node#lookupAll(String)},
     * for which the JavaDocs specifically state that the result is unordered. The current (9.0.4)
     * version of JavaFX happens to return the nodes in the order in which they are encountered whilst
     * traversing the scene graph but this could change in future versions of JavaFX. Thus if there are
     * multiple nodes matched by this query and you want a specific one it is advised not to use this
     * method and instead narrow the query so that only one node is matched.
     *
     * @return the first {@code Button} found that matches this query, if any
     * @throws EmptyNodeQueryException if no {@code Button} nodes match this query
     */
    default Button queryButton() {
        return queryAs(Button.class);
    }

    /**
     * Executes this {@code NodeQuery} and returns the first {@link ComboBox} found that matches
     * this query. If no nodes match this query then an {@link EmptyNodeQueryException} is thrown.
     * <p>
     * The determinism of this method relies on the determinism of {@link Node#lookupAll(String)},
     * for which the JavaDocs specifically state that the result is unordered. The current (9.0.4)
     * version of JavaFX happens to return the nodes in the order in which they are encountered whilst
     * traversing the scene graph but this could change in future versions of JavaFX. Thus if there are
     * multiple nodes matched by this query and you want a specific one it is advised not to use this
     * method and instead narrow the query so that only one node is matched.
     *
     * @return the first {@code ComboBox} found that matches this query, if any
     * @throws EmptyNodeQueryException if no {@code ComboBox} nodes match this query
     */
    @SuppressWarnings("unchecked")
    default <T> ComboBox<T> queryComboBox() {
        return queryAs(ComboBox.class);
    }

    /**
     * Executes this {@code NodeQuery} and returns the first {@link Labeled} found that matches
     * this query. If no nodes match this query then an {@link EmptyNodeQueryException} is thrown.
     * <p>
     * The determinism of this method relies on the determinism of {@link Node#lookupAll(String)},
     * for which the JavaDocs specifically state that the result is unordered. The current (9.0.4)
     * version of JavaFX happens to return the nodes in the order in which they are encountered whilst
     * traversing the scene graph but this could change in future versions of JavaFX. Thus if there are
     * multiple nodes matched by this query and you want a specific one it is advised not to use this
     * method and instead narrow the query so that only one node is matched.
     *
     * @return the first {@code Labeled} found that matches this query, if any
     * @throws EmptyNodeQueryException if no {@code Labeled} nodes match this query
     */
    default Labeled queryLabeled() {
        return queryAs(Labeled.class);
    }

    /**
     * Executes this {@code NodeQuery} and returns the first {@link ListView} found that matches
     * this query. If no nodes match this query then an {@link EmptyNodeQueryException} is thrown.
     * <p>
     * The determinism of this method relies on the determinism of {@link Node#lookupAll(String)},
     * for which the JavaDocs specifically state that the result is unordered. The current (9.0.4)
     * version of JavaFX happens to return the nodes in the order in which they are encountered whilst
     * traversing the scene graph but this could change in future versions of JavaFX. Thus if there are
     * multiple nodes matched by this query and you want a specific one it is advised not to use this
     * method and instead narrow the query so that only one node is matched.
     *
     * @return the first {@code ListView} found that matches this query, if any
     * @throws EmptyNodeQueryException if no {@code ListView} nodes match this query
     */
    @SuppressWarnings("unchecked")
    default <T> ListView<T> queryListView() {
        return queryAs(ListView.class);
    }

    /**
     * Executes this {@code NodeQuery} and returns the first {@link Parent} found that matches
     * this query. If no nodes match this query then an {@link EmptyNodeQueryException} is thrown.
     * <p>
     * The determinism of this method relies on the determinism of {@link Node#lookupAll(String)},
     * for which the JavaDocs specifically state that the result is unordered. The current (9.0.4)
     * version of JavaFX happens to return the nodes in the order in which they are encountered whilst
     * traversing the scene graph but this could change in future versions of JavaFX. Thus if there are
     * multiple nodes matched by this query and you want a specific one it is advised not to use this
     * method and instead narrow the query so that only one node is matched.
     *
     * @return the first {@code Parent} found that matches this query, if any
     * @throws EmptyNodeQueryException if no {@code Parent} nodes match this query
     */
    default Parent queryParent() {
        return queryAs(Parent.class);
    }

    /**
     * Executes this {@code NodeQuery} and returns the first {@link TableView} found that matches
     * this query. If no nodes match this query then an {@link EmptyNodeQueryException} is thrown.
     * <p>
     * The determinism of this method relies on the determinism of {@link Node#lookupAll(String)},
     * for which the JavaDocs specifically state that the result is unordered. The current (9.0.4)
     * version of JavaFX happens to return the nodes in the order in which they are encountered whilst
     * traversing the scene graph but this could change in future versions of JavaFX. Thus if there are
     * multiple nodes matched by this query and you want a specific one it is advised not to use this
     * method and instead narrow the query so that only one node is matched.
     *
     * @return the first {@code TableView} found that matches this query, if any
     * @throws EmptyNodeQueryException if no {@code TableView} nodes match this query
     */
    @SuppressWarnings("unchecked")
    default <T> TableView<T> queryTableView() {
        return queryAs(TableView.class);
    }

    /**
     * Executes this {@code NodeQuery} and returns the first {@link Text} found that matches
     * this query. If no nodes match this query then an {@link EmptyNodeQueryException} is thrown.
     * <p>
     * The determinism of this method relies on the determinism of {@link Node#lookupAll(String)},
     * for which the JavaDocs specifically state that the result is unordered. The current (9.0.4)
     * version of JavaFX happens to return the nodes in the order in which they are encountered whilst
     * traversing the scene graph but this could change in future versions of JavaFX. Thus if there are
     * multiple nodes matched by this query and you want a specific one it is advised not to use this
     * method and instead narrow the query so that only one node is matched.
     *
     * @return the first {@code Text} found that matches this query, if any
     * @throws EmptyNodeQueryException if no {@code Text} nodes match this query
     */
    default Text queryText() {
        return queryAs(Text.class);
    }

    /**
     * Executes this {@code NodeQuery} and returns the first {@link TextFlow} found that matches
     * this query. If no nodes match this query then an {@link EmptyNodeQueryException} is thrown.
     * <p>
     * The determinism of this method relies on the determinism of {@link Node#lookupAll(String)},
     * for which the JavaDocs specifically state that the result is unordered. The current (9.0.4)
     * version of JavaFX happens to return the nodes in the order in which they are encountered whilst
     * traversing the scene graph but this could change in future versions of JavaFX. Thus if there are
     * multiple nodes matched by this query and you want a specific one it is advised not to use this
     * method and instead narrow the query so that only one node is matched.
     *
     * @return the first {@code TextFlow} found that matches this query, if any
     * @throws EmptyNodeQueryException if no {@code TextFlow} nodes match this query
     */
    default TextFlow queryTextFlow() {
        return queryAs(TextFlow.class);
    }

    /**
     * Executes this {@code NodeQuery} and returns the first {@link TextInputControl} found that matches
     * this query. If no nodes match this query then an {@link EmptyNodeQueryException} is thrown.
     * <p>
     * The determinism of this method relies on the determinism of {@link Node#lookupAll(String)},
     * for which the JavaDocs specifically state that the result is unordered. The current (9.0.4)
     * version of JavaFX happens to return the nodes in the order in which they are encountered whilst
     * traversing the scene graph but this could change in future versions of JavaFX. Thus if there are
     * multiple nodes matched by this query and you want a specific one it is advised not to use this
     * method and instead narrow the query so that only one node is matched.
     *
     * @return the first {@code TextInputControl} found that matches this query, if any
     * @throws EmptyNodeQueryException if no {@code TextInputControl} nodes match this query
     */
    default TextInputControl queryTextInputControl() {
        return queryAs(TextInputControl.class);
    }

    /**
     * Type-safe version of {@link #query()} that executes this {@code NodeQuery} and returns
     * the first {@code Node} found that matches this query. If no nodes match this query then
     * an {@link EmptyNodeQueryException} is thrown.
     * <p>
     * The determinism of this method relies on the determinism of {@link Node#lookupAll(String)},
     * for which the JavaDocs specifically state that the result is unordered. The current (9.0.4)
     * version of JavaFX happens to return the nodes in the order in which they are encountered whilst
     * traversing the scene graph but this could change in future versions of JavaFX. Thus if there are
     * multiple nodes matched by this query and you want a specific one it is advised not to use this
     * method and instead narrow the query so that only one node is matched.
     *
     * @param clazz the concrete sub-type of {@code Node} that should be returned by this query
     * so as to avoid extraneous casting when used inside an "assertThat" assertion
     * @param <T> the type that extends {@code Node}
     * @return the first node found that matches this query, if any
     * @throws EmptyNodeQueryException if no nodes match this query
     */
    <T extends Node> T queryAs(Class<T> clazz);

    /**
     * Executes this {@code NodeQuery} and returns an {@code Optional} that either contains
     * the first {@code Node} found that matches this query or nothing (e.g. {@link Optional#empty()}
     * returns {@literal true}) if no nodes match this query.
     * <p>
     * The determinism of this method relies on the determinism of {@link Node#lookupAll(String)},
     * for which the JavaDocs specifically state that the result is unordered. The current (9.0.4)
     * version of JavaFX happens to return the nodes in the order in which they are encountered whilst
     * traversing the scene graph but this could change in future versions of JavaFX. Thus if there are
     * multiple nodes matched by this query and you want a specific one it is advised not to use this
     * method and instead narrow the query so that only one node is matched.
     *
     * @param <T> the type that extends {@code Node}
     * @return the first node found or an empty {@code Optional} if the query does not match any nodes
     */
    <T extends Node> Optional<T> tryQuery();

    /**
     * Type-safe version of {@link #tryQuery()} that executes this {@code NodeQuery} and returns an
     * {@code Optional} that either contains the first {@code Node} found that matches this query or
     * nothing (e.g. {@link Optional#empty()} returns {@literal true}) if no nodes match this query.
     * <p>
     * The determinism of this method relies on the determinism of {@link Node#lookupAll(String)},
     * for which the JavaDocs specifically state that the result is unordered. The current (9.0.4)
     * version of JavaFX happens to return the nodes in the order in which they are encountered whilst
     * traversing the scene graph but this could change in future versions of JavaFX. Thus if there are
     * multiple nodes matched by this query and you want a specific one it is advised not to use this
     * method and instead narrow the query so that only one node is matched.
     *
     * @param clazz the concrete sub-type of {@code Node} that should be contained in the
     * {@code Optional} returned by this query so as to avoid extraneous casting when used inside
     * an "assertThat" assertion
     * @param <T> the type that extends {@code Node}
     * @return the first node found or an empty {@code Optional} if the query does not match any nodes
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
