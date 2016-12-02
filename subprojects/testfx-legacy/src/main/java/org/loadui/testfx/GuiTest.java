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
package org.loadui.testfx;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.loadui.testfx.exceptions.NoNodesFoundException;
import org.loadui.testfx.exceptions.NoNodesVisibleException;
import org.loadui.testfx.exceptions.NodeQueryException;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.finder.NodeFinderException;
import org.testfx.service.finder.WindowFinder;
import org.testfx.service.support.CaptureSupport;
import org.testfx.service.support.WaitUntilSupport;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.Assume.assumeFalse;
import static org.testfx.api.FxService.serviceContext;

public abstract class GuiTest extends FxRobot {

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static <T extends Window> T _targetWindow(T window) {
        WINDOW_FINDER.targetWindow(window);
        return window;
    }

    public static List<Window> getWindows() {
        return WINDOW_FINDER.listTargetWindows();
    }

    public static Window getWindowByIndex(int windowIndex) {
        return WINDOW_FINDER.window(windowIndex);
    }

    public static Stage findStageByTitle(String stageTitleRegex) {
        return (Stage) WINDOW_FINDER.window(stageTitleRegex);
    }

    public static <T extends Node> T find(String query) {
        try {
            return NODE_FINDER.lookup(query).query();
        }
        catch (NodeFinderException exception) {
            throw buildNodeQueryException(exception);
        }
    }

    public static <T extends Node> Set<T> findAll(String query) {
        try {
            return NODE_FINDER.lookup(query).queryAll();
        }
        catch (NodeFinderException exception) {
            throw buildNodeQueryException(exception);
        }
    }

    public static <T extends Node> T find(Predicate<T> predicate) {
        try {
            return NODE_FINDER.lookup(predicate).query();
        }
        catch (NodeFinderException exception) {
            throw buildNodeQueryException(exception);
        }
    }

    public static <T extends Node> T find(Matcher<Object> matcher) {
        try {
            return NODE_FINDER.lookup(matcher).query();
        }
        catch (NodeFinderException exception) {
            throw buildNodeQueryException(exception);
        }
    }

    public static <T extends Node> T find(String query, Node parent) {
        try {
            return NODE_FINDER.from(parent).lookup(query).query();
        }
        catch (NodeFinderException exception) {
            throw buildNodeQueryException(exception);
        }
    }

    public static <T extends Node> Set<T> findAll(Predicate<T> predicate, Node parent) {
        try {
            return NODE_FINDER.from(parent).lookup(predicate).queryAll();
        }
        catch (NodeFinderException exception) {
            throw buildNodeQueryException(exception);
        }
    }

    public static <T extends Node> Set<T> findAll(Matcher<Object> matcher, Node parent) {
        try {
            return NODE_FINDER.from(parent).lookup(matcher).queryAll();
        }
        catch (NodeFinderException exception) {
            throw buildNodeQueryException(exception);
        }
    }

    public static boolean exists(String nodeQuery) {
        try {
            return find(nodeQuery) != null;
        }
        catch (NodeFinderException exception) {
            throw buildNodeQueryException(exception);
        }
    }

    /**
     * Returns a Callable that calculates the number of nodes that matches the given query.
     *
     * @param nodeQuery a CSS query or the label of a node.
     */
    public static Callable<Integer> numberOf(final String nodeQuery) {
        return () -> findAll(nodeQuery).size();
    }

    public static <T extends Node> void waitUntil(T node, Predicate<T> condition) {
        waitUntil(node, condition, 15);
    }

    public static <T extends Node> void waitUntil(T node, Predicate<T> condition,
                                                  int timeoutInSeconds) {
        WAIT_UNTIL_SUPPORT.waitUntil(node, condition, timeoutInSeconds);
    }

    public static void waitUntil(Node node, Matcher<Object> condition) {
        waitUntil(node, condition, 15);
    }

    public static void waitUntil(Node node, Matcher<Object> condition, int timeoutInSeconds) {
        WAIT_UNTIL_SUPPORT.waitUntil(node, condition, timeoutInSeconds);
    }

    public static <T> void waitUntil(T value, Matcher<? super T> condition) {
        waitUntil(value, condition, 15);
    }

    public static <T> void waitUntil(T value, Matcher<? super T> condition, int timeoutInSeconds) {
        WAIT_UNTIL_SUPPORT.waitUntil(value, condition, timeoutInSeconds);
    }

    public static <T> void waitUntil(Callable<T> callable, Matcher<? super T> condition) {
        waitUntil(callable, condition, 15);
    }

    public static <T> void waitUntil(Callable<T> callable, Matcher<? super T> condition,
                                     int timeoutInSeconds) {
        WAIT_UNTIL_SUPPORT.waitUntil(callable, condition, timeoutInSeconds);
    }

    public static File captureScreenshot() {
        Path captureFile = Paths.get("screenshot" + new Date().getTime() + ".png");
        Image captureImage = CAPTURE_SUPPORT.captureRegion(Screen.getPrimary().getBounds());
        CAPTURE_SUPPORT.saveImage(captureImage, captureFile);
        return captureFile.toFile();
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static NodeQueryException buildNodeQueryException(NodeFinderException exception) {
        switch (exception.getErrorType()) {
            case NO_NODES_FOUND:
                throw new NoNodesFoundException(exception.getMessage());
            case NO_VISIBLE_NODES_FOUND:
                throw new NoNodesVisibleException(exception.getMessage());
            default:
                throw new AssertionError("Unhandled NodeFinderException.");
        }
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC FIELDS.
    //---------------------------------------------------------------------------------------------

    private static final WindowFinder WINDOW_FINDER = serviceContext().getWindowFinder();
    private static final NodeFinder NODE_FINDER = serviceContext().getNodeFinder();
    private static final WaitUntilSupport WAIT_UNTIL_SUPPORT = serviceContext().getWaitUntilSupport();
    private static final CaptureSupport CAPTURE_SUPPORT = serviceContext().getCaptureSupport();

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void internalSetupSpec() {
        assumeFalse(
            "Cannot run JavaFX in headless environment",
            GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance()
        );
    }

    @Before
    public void internalSetup() throws Exception {
        targetWindow(FxToolkit.registerPrimaryStage());
        FxToolkit.setupSceneRoot(this::getRootNode);
        WaitForAsyncUtils.waitForFxEvents();
        FxToolkit.setupStage((stage) -> {
            stage.show();
            stage.toBack();
            stage.toFront();
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    //---------------------------------------------------------------------------------------------
    // PROTECTED METHODS.
    //---------------------------------------------------------------------------------------------

    // Runs in JavaFX Application Thread.
    protected abstract Parent getRootNode();

}
