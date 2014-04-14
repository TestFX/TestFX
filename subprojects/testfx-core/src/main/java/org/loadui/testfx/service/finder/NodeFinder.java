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
package org.loadui.testfx.service.finder;

import java.util.Set;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import com.google.common.base.Predicate;
import org.hamcrest.Matcher;

public interface NodeFinder {
    public Node      node(String query);
    public Set<Node> nodes(String query);
    public Node      node(Predicate<Node> predicate);
    public Set<Node> nodes(Predicate<Node> predicate);
    public Node      node(Matcher<Object> matcher);
    public Set<Node> nodes(Matcher<Object> matcher);

    public Node      parent(Window window);
    public Node      parent(int windowIndex);
    public Node      parent(String stageTitleRegex);
    public Node      parent(Scene scene);

    public Node      node(String query, Node parentNode);
    public Set<Node> nodes(String query, Node parentNode);
}
