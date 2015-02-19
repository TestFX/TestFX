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
package org.testfx.api;

import javafx.scene.Node;

import com.google.common.base.Predicate;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.testfx.api.annotation.Unstable;
import org.testfx.matcher.base.BaseMatchers;
import org.testfx.service.finder.NodeFinder;

@Unstable(reason = "class was recently added")
public class FxAssert {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final String EMPTY_STRING = "";

    //---------------------------------------------------------------------------------------------
    // STATIC FIELDS.
    //---------------------------------------------------------------------------------------------

    private static FxAssertContext context;

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static <T extends Node> void verifyThat(String reason,
                                                   T node,
                                                   Matcher<T> nodeMatcher) {
        verifyThatImpl(reason, node, nodeMatcher);
    }

    public static <T extends Node> void verifyThat(T node,
                                                   Matcher<T> nodeMatcher) {
        verifyThatImpl(emptyReason(), node, nodeMatcher);
    }

    public static <T extends Node> void verifyThat(String reason,
                                                   String nodeQuery,
                                                   Matcher<T> nodeMatcher) {
        verifyThatImpl(reason, toNode(nodeQuery), nodeMatcher);
    }

    public static <T extends Node> void verifyThat(String nodeQuery,
                                                   Matcher<T> nodeMatcher) {
        verifyThatImpl(emptyReason(), toNode(nodeQuery), nodeMatcher);
    }

    public static <T extends Node> void verifyThat(String reason,
                                                   T node,
                                                   Predicate<T> nodePredicate) {
        verifyThatImpl(reason, node, toNodeMatcher(nodePredicate));
    }

    public static <T extends Node> void verifyThat(T node,
                                                   Predicate<T> nodePredicate) {
        verifyThatImpl(emptyReason(), node, toNodeMatcher(nodePredicate));
    }

    public static FxAssertContext assertContext() {
        if (context == null) {
            context = new FxAssertContext();
        }
        return context;
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static <T extends Node> void verifyThatImpl(String reason,
                                                        T node,
                                                        Matcher<T> nodeMatcher) {
        try {
            MatcherAssert.assertThat(reason, node, nodeMatcher);
        }
        catch (AssertionError error) {
            // TODO: Save screenshot to file.
            throw new AssertionError(error.getMessage());
        }
    }

    private static String emptyReason() {
        return EMPTY_STRING;
    }

    private static <T extends Node> T toNode(String nodeQuery) {
        NodeFinder nodeFinder = assertContext().getNodeFinder();
        return nodeFinder.nodes(nodeQuery).queryFirst();
    }

    private static <T extends Node> Matcher<T> toNodeMatcher(Predicate<T> nodePredicate) {
        return BaseMatchers.baseMatcher("applies on Predicate", nodePredicate);
    }

}
