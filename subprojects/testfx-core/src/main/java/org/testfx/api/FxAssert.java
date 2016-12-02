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
package org.testfx.api;

import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.function.Predicate;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.stage.Screen;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.testfx.api.annotation.Unstable;
import org.testfx.matcher.base.GeneralMatchers;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.query.NodeQuery;

@Unstable(reason = "method signatures need fine-tuning")
public final class FxAssert {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final String EMPTY_STRING = "";

    private static final String ERROR_TIMESTAMP_PATTERN = "yyyyMMdd.HHmmss.SSS";

    //---------------------------------------------------------------------------------------------
    // STATIC FIELDS.
    //---------------------------------------------------------------------------------------------

    private static FxAssertContext context;

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    // ASSERTIONS: SUPER MATCHER.

    @Unstable(reason = "is missing apidocs; might be removed due to complications with generics")
    public static <T> void verifyThat(T value,
                                      Matcher<? super T> matcher) {
        verifyThatImpl(emptyReason(), value, matcher);
    }

    // ASSERTIONS: {NODE, NODES} + MATCHER.

    @Unstable(reason = "is missing apidocs")
    public static <T extends Node> void verifyThat(T node,
                                                   Matcher<T> nodeMatcher) {
        verifyThatImpl(emptyReason(), node, nodeMatcher);
    }

    @Unstable(reason = "is missing apidocs; might change to simplify iterable handling")
    public static <T extends Node> void verifyThatIter(Iterable<T> nodes,
                                                       Matcher<Iterable<T>> nodesMatcher) {
        verifyThatImpl(emptyReason(), nodes, nodesMatcher);
    }

    // ASSERTIONS: STRING QUERY + MATCHER.

    @Unstable(reason = "is missing apidocs")
    public static <T extends Node> void verifyThat(String nodeQuery,
                                                   Matcher<T> nodeMatcher) {
        verifyThatImpl(emptyReason(), toNode(nodeQuery), nodeMatcher);
    }

    @Unstable(reason = "is missing apidocs; might change to simplify iterable handling")
    public static <T extends Node> void verifyThatIter(String nodeQuery,
                                                       Matcher<Iterable<T>> nodesMatcher) {
        verifyThatImpl(emptyReason(), toNodeSet(nodeQuery), nodesMatcher);
    }

    // ASSERTIONS: NODE QUERY + MATCHER.

    @Unstable(reason = "is missing apidocs")
    public static <T extends Node> void verifyThat(NodeQuery nodeQuery,
                                                   Matcher<T> nodeMatcher) {
        verifyThatImpl(emptyReason(), toNode(nodeQuery), nodeMatcher);
    }

    @Unstable(reason = "is missing apidocs; might change to simplify iterable handling")
    public static <T extends Node> void verifyThatIter(NodeQuery nodeQuery,
                                                       Matcher<Iterable<T>> nodesMatcher) {
        verifyThatImpl(emptyReason(), toNodeSet(nodeQuery), nodesMatcher);
    }

    // ASSERTIONS: {NODE, STRING QUERY, NODE QUERY} + PREDICATE.

    @Unstable(reason = "is missing apidocs; might change if typing causes trouble")
    public static <T extends Node> void verifyThat(T node,
                                                   Predicate<T> nodePredicate) {
        verifyThatImpl(emptyReason(), node, toNodeMatcher(nodePredicate));
    }

    @Unstable(reason = "is missing apidocs; might change if typing causes trouble")
    public static <T extends Node> void verifyThat(String nodeQuery,
                                                   Predicate<T> nodePredicate) {
        verifyThatImpl(emptyReason(), toNode(nodeQuery), toNodeMatcher(nodePredicate));
    }

    @Unstable(reason = "is missing apidocs; might change if typing causes trouble")
    public static <T extends Node> void verifyThat(NodeQuery nodeQuery,
                                                   Predicate<T> nodePredicate) {
        verifyThatImpl(emptyReason(), toNode(nodeQuery), toNodeMatcher(nodePredicate));
    }

    // INTERNAL CONTEXT.

    @Unstable(reason = "is missing apidocs")
    public static FxAssertContext assertContext() {
        if (context == null) {
            context = new FxAssertContext();
        }
        return context;
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static <T> void verifyThatImpl(String reason,
                                           T value,
                                           Matcher<? super T> matcher) {
        try {
            MatcherAssert.assertThat(reason, value, matcher);
        }
        catch (AssertionError error) {
            // TODO: make error capture and assertion throw more reliable.
//            captureErrorScreenshot();
            throw new AssertionError(error.getMessage());
        }
    }

    private static String emptyReason() {
        return EMPTY_STRING;
    }

    private static <T extends Node> T toNode(String nodeQuery) {
        NodeFinder nodeFinder = assertContext().getNodeFinder();
        return toNode(nodeFinder.lookup(nodeQuery));
    }

    private static <T extends Node> Set<T> toNodeSet(String nodeQuery) {
        NodeFinder nodeFinder = assertContext().getNodeFinder();
        return toNodeSet(nodeFinder.lookup(nodeQuery));
    }

    private static <T extends Node> T toNode(NodeQuery nodeQuery) {
        return nodeQuery.query();
    }

    private static <T extends Node> Set<T> toNodeSet(NodeQuery nodeQuery) {
        return nodeQuery.queryAll();
    }

    private static <T extends Node> Matcher<T> toNodeMatcher(Predicate<T> nodePredicate) {
        return GeneralMatchers.baseMatcher("applies on Predicate", nodePredicate);
    }

    private static void captureErrorScreenshot() {
        ZonedDateTime errorDateTime = ZonedDateTime.now();

        Rectangle2D primaryScreenRegion = Screen.getPrimary().getBounds();
        Image errorImage = assertContext().getCaptureSupport().captureRegion(primaryScreenRegion);

        String errorTimestamp = formatErrorTimestamp(errorDateTime, ERROR_TIMESTAMP_PATTERN);
        String errorImageFilename = "testfx-" + errorTimestamp + ".png";

        assertContext().getCaptureSupport().saveImage(errorImage, Paths.get(errorImageFilename));
    }

    private static String formatErrorTimestamp(ZonedDateTime dateTime,
                                               String dateTimePattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        return dateTime.format(formatter);
    }

//    private static ZonedDateTime toZuluTime(ZonedDateTime dateTime) {
//        return dateTime.withZoneSameInstant(ZoneId.of(/* UTC */ "Z"));
//    }

}
