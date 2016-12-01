/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
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

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    NodeQuery lookup(String query);
    <T> NodeQuery lookup(Matcher<T> matcher);
    <T extends Node> NodeQuery lookup(Predicate<T> predicate);

    NodeQuery fromAll();
    NodeQuery from(Node... parentNodes);
    NodeQuery from(Collection<Node> parentNodes);
    NodeQuery from(NodeQuery nodeQuery);

    Node rootNode(Window window);
    Node rootNode(Scene scene);
    Node rootNode(Node node);

    //Node rootNode(Predicate<Window> predicate);
    //Node rootNode(int windowIndex);
    //Node rootNode(Pattern stageTitlePattern);
    //Node rootNode(String stageTitleRegexp);

}
