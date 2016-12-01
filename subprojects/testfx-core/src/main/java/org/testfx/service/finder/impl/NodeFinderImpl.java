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
package org.testfx.service.finder.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import org.hamcrest.Matcher;
import org.testfx.api.annotation.Unstable;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.finder.WindowFinder;
import org.testfx.service.query.NodeQuery;
import org.testfx.service.query.NodeQueryFactory;
import org.testfx.service.query.impl.NodeQueryFactoryImpl;
import org.testfx.util.NodeQueryUtils;

@Unstable
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
    public NodeQuery lookup(String query) {
        return fromAll().lookup(query);
    }

    @Override
    public <T> NodeQuery lookup(Matcher<T> matcher) {
        return fromAll().lookup(matcher);
    }

    @Override
    public <T extends Node> NodeQuery lookup(Predicate<T> predicate) {
        return fromAll().lookup(predicate);
    }

    @Override
    public NodeQuery fromAll() {
        return nodeQueryFactory.build().from(rootsOfWindows());
    }

    @Override
    public NodeQuery from(Node... parentNodes) {
        return nodeQueryFactory.build().from(parentNodes);
    }

    @Override
    public NodeQuery from(Collection<Node> parentNodes) {
        return nodeQueryFactory.build().from(parentNodes);
    }

    @Override
    public NodeQuery from(NodeQuery nodeQuery) {
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
        List<Window> windows = windowFinder.listTargetWindows();
        return NodeQueryUtils.rootsOfWindows(windows);
    }

}
