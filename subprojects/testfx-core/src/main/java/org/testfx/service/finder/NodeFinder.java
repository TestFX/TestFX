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
package org.testfx.service.finder;

import java.util.Set;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import com.google.common.base.Predicate;
import org.hamcrest.Matcher;
import org.testfx.service.query.NodeQuery;

public interface NodeFinder {

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    NodeQuery nodes();
    NodeQuery nodes(String query);
    <T extends Node> NodeQuery nodes(Predicate<T> predicate);
    NodeQuery nodes(Matcher<Object> matcher);

    NodeQuery nodesFrom(Node... parentNodes);
    NodeQuery nodesFrom(Set<Node> parentNodes);
    NodeQuery nodesFrom(NodeQuery nodeQuery);

    Node rootNode(Window window);
    Node rootNode(Scene scene);
    Node rootNode(Node node);

    //Node rootNode(Predicate<Window> predicate);
    //Node rootNode(int windowIndex);
    //Node rootNode(Pattern stageTitlePattern);
    //Node rootNode(String stageTitleRegexp);

}
