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
package org.testfx.service.finder;

import java.util.Collection;
import java.util.function.Predicate;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import org.hamcrest.Matcher;
import org.testfx.service.query.NodeQuery;

public interface NodeFinder {

    /**
     * Returns a {@link NodeQuery} that stores all the root nodes that meet the given query
     *
     * @see NodeQuery#lookup(String)
     */
    NodeQuery lookup(String query);

    /**
     * Returns a {@link NodeQuery} that stores all the root nodes that match the given matcher.
     *
     * @see NodeQuery#lookup(Matcher)
     */
    <T> NodeQuery lookup(Matcher<T> matcher);

    /**
     * Returns a {@link NodeQuery} that stores all the root nodes that pass the given predicate
     *
     * @see NodeQuery#lookup(Predicate)
     */
    <T extends Node> NodeQuery lookup(Predicate<T> predicate);

    /**
     * Returns a {@link NodeQuery} that stores all the root nodes of all windows via
     * {@link WindowFinder#listTargetWindows()}
     */
    NodeQuery fromAll();

    /**
     * Returns a {@link NodeQuery} that stores the given parentNodes
     */
    NodeQuery from(Node... parentNodes);

    /**
     * Returns a {@link NodeQuery} that stores the given parentNodes collection.
     */
    NodeQuery from(Collection<Node> parentNodes);

    /**
     * Returns a new {@link NodeQuery} that stores all the parentNodes from the given nodeQuery (essentially,
     * it creates a copy/clone).
     */
    NodeQuery from(NodeQuery nodeQuery);

    /**
     * Returns the window's scene's root node.
     */
    Node rootNode(Window window);

    /**
     * Returns the scene's root node
     */
    Node rootNode(Scene scene);

    /**
     * Returns the node's scene's root node
     */
    Node rootNode(Node node);

    //Node rootNode(Predicate<Window> predicate);
    //Node rootNode(int windowIndex);
    //Node rootNode(Pattern stageTitlePattern);
    //Node rootNode(String stageTitleRegexp);

}
