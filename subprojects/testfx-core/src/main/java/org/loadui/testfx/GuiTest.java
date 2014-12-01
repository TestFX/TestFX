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
package org.loadui.testfx;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import com.google.common.base.Predicate;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.loadui.testfx.framework.app.StageSetupCallback;
import org.loadui.testfx.framework.junit.AppRobotTestBase;
import org.loadui.testfx.service.finder.NodeFinder;
import org.loadui.testfx.service.finder.WindowFinder;
import org.loadui.testfx.service.support.CaptureSupport;
import org.loadui.testfx.service.support.WaitUntilSupport;

import static org.junit.Assume.assumeFalse;
import static org.testfx.api.FxService.serviceContext;

public abstract class GuiTest extends AppRobotTestBase implements StageSetupCallback {

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static <T extends Window> T targetWindow(T window) {
        windowFinder.target(window);
        return window;
    }

    public static List<Window> getWindows() {
        return windowFinder.listWindows();
    }

    public static Window getWindowByIndex(int windowIndex) {
        return windowFinder.window(windowIndex);
    }

    public static Stage findStageByTitle(String stageTitleRegex) {
        return (Stage) windowFinder.window(stageTitleRegex);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> T find(String query) {
        return (T) nodeFinder.node(query);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> Set<T> findAll(String query) {
        return (Set<T>) nodeFinder.nodes(query);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> T find(Predicate<T> predicate) {
        return (T) nodeFinder.node((Predicate<Node>) predicate);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> T find(Matcher<Object> matcher) {
        return (T) nodeFinder.node(matcher);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> T find(String query, Node parent) {
        return (T) nodeFinder.node(query, parent);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> Set<T> findAll(Predicate<T> predicate, Node parent) {
        return (Set<T>) nodeFinder.nodes((Predicate<Node>) predicate);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> Set<T> findAll(Matcher<Object> matcher, Node parent) {
        return (Set<T>) nodeFinder.nodes(matcher);
    }

    public static boolean exists(String nodeQuery) {
        return find(nodeQuery) != null;
    }

    /**
     * Returns a Callable that calculates the number of nodes that matches the given query.
     *
     * @param nodeQuery a CSS query or the label of a node.
     * @return
     */
    public static Callable<Integer> numberOf(final String nodeQuery) {
        return () -> findAll(nodeQuery).size();
    }

    public static <T extends Node> void waitUntil(T node, Predicate<T> condition) {
        waitUntil(node, condition, 15);
    }

    public static <T extends Node> void waitUntil(T node, Predicate<T> condition,
                                                  int timeoutInSeconds) {
        waitUntilSupport.waitUntil(node, condition, timeoutInSeconds);
    }

    public static void waitUntil(Node node, Matcher<Object> condition) {
        waitUntil(node, condition, 15);
    }

    public static void waitUntil(Node node, Matcher<Object> condition, int timeoutInSeconds) {
        waitUntilSupport.waitUntil(node, condition, timeoutInSeconds);
    }

    public static <T> void waitUntil(T value, Matcher<? super T> condition) {
        waitUntil(value, condition, 15);
    }

    public static <T> void waitUntil(T value, Matcher<? super T> condition, int timeoutInSeconds) {
        waitUntilSupport.waitUntil(value, condition, timeoutInSeconds);
    }

    public static <T> void waitUntil(Callable<T> callable, Matcher<? super T> condition) {
        waitUntil(callable, condition, 15);
    }

    public static <T> void waitUntil(Callable<T> callable, Matcher<? super T> condition,
                                     int timeoutInSeconds) {
        waitUntilSupport.waitUntil(callable, condition, timeoutInSeconds);
    }

    public static File captureScreenshot() {
        File captureFile = new File("screenshot" + new Date().getTime() + ".png");
        captureSupport.capturePrimaryScreenToFile(captureFile);
        return captureFile;
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC FIELDS.
    //---------------------------------------------------------------------------------------------

    private static final WindowFinder windowFinder = serviceContext().getWindowFinder();
    private static final NodeFinder nodeFinder = serviceContext().getNodeFinder();

    private static final WaitUntilSupport waitUntilSupport =
        serviceContext().getWaitUntilSupport();
    private static final CaptureSupport captureSupport = serviceContext().getCaptureSupport();

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void checkHeadless() {
        assumeFalse(
                "Cannot run JavaFX in headless environment",
                GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance());
    }

    @Before
    public void setupGuiTest() throws Exception {
        setupApplication();
        setupStages(this);
    }

    // Runs in JavaFX Application Thread.
    public void setupStages(Stage primaryStage) {
        Parent sceneRootNode = getRootNode();
        Scene scene = new Scene(sceneRootNode);
        primaryStage.setScene(scene);
    }

    //---------------------------------------------------------------------------------------------
    // PROTECTED METHODS.
    //---------------------------------------------------------------------------------------------

    // Runs in JavaFX Application Thread.
    protected abstract Parent getRootNode();

}
