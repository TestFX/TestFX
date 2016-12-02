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
package org.testfx.service.query;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import javafx.scene.Node;

import org.hamcrest.Matcher;

public interface NodeQuery {

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    NodeQuery from(Node... parentNodes);
    NodeQuery from(Collection<Node> parentNodes);

    NodeQuery lookup(String query);
    <T> NodeQuery lookup(Matcher<T> matcher);
    <T extends Node> NodeQuery lookup(Predicate<T> predicate);
    NodeQuery lookup(Function<Node, Set<Node>> function);

    <T> NodeQuery match(Matcher<T> matcher);
    <T extends Node> NodeQuery match(Predicate<T> predicate);
    NodeQuery nth(int index);

    <T extends Node> T query();
    <T extends Node> Optional<T> tryQuery();
    <T extends Node> Set<T> queryAll();

}
