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
package org.testfx.api;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import javafx.scene.Node;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.testfx.matcher.base.GeneralMatchers;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.query.NodeQuery;

/**
 * All TestFX tests should use {@link #verifyThat(Node, Matcher, Function)} when writing tests,
 * so that the developer can use {@link org.testfx.util.DebugUtils} to provide additional info
 * as to why a test failed.
 */
public final class FxAssert {

    private static FxAssertContext context;

    public static <T> void verifyThat(T value, Matcher<? super T> matcher) {
        verifyThatImpl("", value, matcher, Function.identity());
    }

    public static <T> void verifyThat(T value,
                                      Matcher<? super T> matcher,
                                      Function<StringBuilder, StringBuilder> errorMessageMapper) {
        verifyThatImpl("", value, matcher, errorMessageMapper);
    }

    public static <T extends Node> void verifyThat(T node, Matcher<T> nodeMatcher) {
        verifyThatImpl("", node, nodeMatcher, Function.identity());
    }

    public static <T extends Node> void verifyThat(T node,
                                                   Matcher<T> nodeMatcher,
                                                   Function<StringBuilder, StringBuilder> errorMessageMapper) {
        verifyThatImpl("", node, nodeMatcher, errorMessageMapper);
    }

    public static <T extends Node> void verifyThatIter(Iterable<T> nodes, Matcher<Iterable<T>> nodesMatcher) {
        verifyThatImpl("", nodes, nodesMatcher, Function.identity());
    }

    public static <T extends Node> void verifyThatIter(Iterable<T> nodes,
                                                       Matcher<Iterable<T>> nodesMatcher,
                                                       Function<StringBuilder, StringBuilder> errorMessageMapper) {
        verifyThatImpl("", nodes, nodesMatcher, errorMessageMapper);
    }

    public static <T extends Node> void verifyThat(String nodeQuery, Matcher<T> nodeMatcher) {
        verifyThatImpl("", toNode(nodeQuery), nodeMatcher, Function.identity());
    }

    public static <T extends Node> void verifyThat(String nodeQuery,
                                                   Matcher<T> nodeMatcher,
                                                   Function<StringBuilder, StringBuilder> errorMessageMapper) {
        verifyThatImpl("", toNode(nodeQuery), nodeMatcher, errorMessageMapper);
    }

    public static <T extends Node> void verifyThatIter(String nodeQuery,
                                                       Matcher<Iterable<T>> nodesMatcher) {
        verifyThatImpl("", toNodeSet(nodeQuery), nodesMatcher, Function.identity());
    }

    public static <T extends Node> void verifyThatIter(String nodeQuery,
                                                       Matcher<Iterable<T>> nodesMatcher,
                                                       Function<StringBuilder, StringBuilder> errorMessageMapper) {
        verifyThatImpl("", toNodeSet(nodeQuery), nodesMatcher, errorMessageMapper);
    }

    public static <T extends Node> void verifyThat(NodeQuery nodeQuery, Matcher<T> nodeMatcher) {
        verifyThatImpl("", nodeQuery.query(), nodeMatcher, Function.identity());
    }

    public static <T extends Node> void verifyThat(NodeQuery nodeQuery,
                                                   Matcher<T> nodeMatcher,
                                                   Function<StringBuilder, StringBuilder> errorMessageMapper) {
        verifyThatImpl("", nodeQuery.query(), nodeMatcher, errorMessageMapper);
    }

    public static <T extends Node> void verifyThatIter(NodeQuery nodeQuery,
                                                       Matcher<Iterable<T>> nodesMatcher) {
        verifyThatImpl("", nodeQuery.queryAll(), nodesMatcher, Function.identity());
    }

    public static <T extends Node> void verifyThatIter(NodeQuery nodeQuery,
                                                       Matcher<Iterable<T>> nodesMatcher,
                                                       Function<StringBuilder, StringBuilder> errorMessageMapper) {
        verifyThatImpl("", nodeQuery.queryAll(), nodesMatcher, errorMessageMapper);
    }

    public static <T extends Node> void verifyThat(T node, Predicate<T> nodePredicate) {
        verifyThatImpl("", node, toNodeMatcher(nodePredicate), Function.identity());
    }

    public static <T extends Node> void verifyThat(T node,
                                                   Predicate<T> nodePredicate,
                                                   Function<StringBuilder, StringBuilder> errorMessageMapper) {
        verifyThatImpl("", node, toNodeMatcher(nodePredicate), errorMessageMapper);
    }

    public static <T extends Node> void verifyThat(String nodeQuery, Predicate<T> nodePredicate) {
        verifyThatImpl("", toNode(nodeQuery), toNodeMatcher(nodePredicate), Function.identity());
    }

    public static <T extends Node> void verifyThat(String nodeQuery,
                                                   Predicate<T> nodePredicate,
                                                   Function<StringBuilder, StringBuilder> errorMessageMapper) {
        verifyThatImpl("", toNode(nodeQuery), toNodeMatcher(nodePredicate), errorMessageMapper);
    }

    public static <T extends Node> void verifyThat(NodeQuery nodeQuery, Predicate<T> nodePredicate) {
        verifyThatImpl("", nodeQuery.query(), toNodeMatcher(nodePredicate), Function.identity());
    }

    public static <T extends Node> void verifyThat(NodeQuery nodeQuery,
                                                   Predicate<T> nodePredicate,
                                                   Function<StringBuilder, StringBuilder> errorMessageMapper) {
        verifyThatImpl("", nodeQuery.query(), toNodeMatcher(nodePredicate), errorMessageMapper);
    }

    public static FxAssertContext assertContext() {
        if (context == null) {
            context = new FxAssertContext();
        }
        return context;
    }

    /**
     * Allow developer to debug a failed test (e.g. the state of the stage or node, which keys were pressed, etc.)
     *
     * @see org.testfx.util.DebugUtils
     */
    private static <T> void verifyThatImpl(String reason, T value, Matcher<? super T> matcher,
                                           Function<StringBuilder, StringBuilder> errorMessageMapper) {
        try {
            MatcherAssert.assertThat(reason, value, matcher);
        }
        catch (AssertionError error) {
            // TODO: make assertion throw more reliable.
            StringBuilder sb = new StringBuilder(error.getMessage());
            throw new AssertionError(errorMessageMapper.apply(sb));
        }
    }

    private static <T extends Node> T toNode(String nodeQuery) {
        NodeFinder nodeFinder = assertContext().getNodeFinder();
        return nodeFinder.lookup(nodeQuery).query();
    }

    private static <T extends Node> Set<T> toNodeSet(String nodeQuery) {
        NodeFinder nodeFinder = assertContext().getNodeFinder();
        return nodeFinder.lookup(nodeQuery).queryAll();
    }

    private static <T extends Node> Matcher<T> toNodeMatcher(Predicate<T> nodePredicate) {
        return GeneralMatchers.baseMatcher("applies on Predicate", nodePredicate);
    }

}
