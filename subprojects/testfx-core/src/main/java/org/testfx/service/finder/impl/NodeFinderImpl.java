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
package org.testfx.service.finder.impl;

import java.util.List;
import java.util.Set;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import com.google.common.base.Predicate;
import org.hamcrest.Matcher;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.finder.WindowFinder;
import org.testfx.service.query.NodeQuery;
import org.testfx.service.query.NodeQueryFactory;
import org.testfx.service.query.impl.NodeQueryFactoryImpl;
import org.testfx.service.query.impl.NodeQueryUtils;

public class NodeFinderImpl implements NodeFinder {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private WindowFinder windowFinder;

    private NodeQueryFactory nodeQueryFactory;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public NodeFinderImpl(WindowFinder windowFinder) {
        this.windowFinder = windowFinder;
        this.nodeQueryFactory = new NodeQueryFactoryImpl();
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public NodeQuery nodes() {
        return nodeQueryFactory.build().from(rootsOfWindows());
    }

    @Override
    public NodeQuery nodes(String query) {
        return nodes().lookup(query);
    }

    @Override
    public <T extends Node> NodeQuery nodes(Predicate<T> predicate) {
        return nodes().lookup(predicate);
    }

    @Override
    public NodeQuery nodes(Matcher<Object> matcher) {
        return nodes().lookup(matcher);
    }

    @Override
    public NodeQuery nodesFrom(Node... parentNodes) {
        return nodeQueryFactory.build().from(parentNodes);
    }

    @Override
    public NodeQuery nodesFrom(Set<Node> parentNodes) {
        return nodeQueryFactory.build().from(parentNodes);
    }

    @Override
    public NodeQuery nodesFrom(NodeQuery nodeQuery) {
        return nodeQueryFactory.build().from(nodeQuery.queryAll());
    }

    @Override
    public Node rootNode(Window window) {
        return window.getScene().getRoot();
    }

    @Override
    public Node rootNode(Scene scene) {
        return scene.getRoot();
    }

    @Override
    public Node rootNode(Node node) {
        return node.getScene().getRoot();
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Set<Node> rootsOfWindows() {
        List<Window> windows = windowFinder.listOrderedWindows();
        return NodeQueryUtils.rootsOfWindows(windows);
    }

}
