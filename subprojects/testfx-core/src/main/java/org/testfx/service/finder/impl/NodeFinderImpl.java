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
package org.testfx.service.finder.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import org.hamcrest.Matcher;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.finder.WindowFinder;
import org.testfx.service.query.NodeQuery;
import org.testfx.service.query.impl.NodeQueryImpl;
import org.testfx.util.NodeQueryUtils;

public class NodeFinderImpl implements NodeFinder {

    private final WindowFinder windowFinder;

    public NodeFinderImpl(WindowFinder windowFinder) {
        this.windowFinder = windowFinder;
    }

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
        return new NodeQueryImpl().from(rootsOfWindows());
    }

    @Override
    public NodeQuery from(Node... parentNodes) {
        return new NodeQueryImpl().from(parentNodes);
    }

    @Override
    public NodeQuery from(Collection<Node> parentNodes) {
        return new NodeQueryImpl().from(parentNodes);
    }

    @Override
    public NodeQuery from(NodeQuery nodeQuery) {
        return new NodeQueryImpl().from(nodeQuery.queryAll());
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

    private Set<Node> rootsOfWindows() {
        List<Window> windows = windowFinder.listTargetWindows();
        return NodeQueryUtils.rootsOfWindows(windows);
    }

}
