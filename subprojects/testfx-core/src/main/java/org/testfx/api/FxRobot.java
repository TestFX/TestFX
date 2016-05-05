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

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseButton;
import javafx.stage.Screen;
import javafx.stage.Window;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.hamcrest.Matcher;
import org.testfx.api.annotation.Unstable;
import org.testfx.service.locator.PointLocator;
import org.testfx.service.query.BoundsQuery;
import org.testfx.service.query.NodeQuery;
import org.testfx.service.query.PointQuery;
import org.testfx.service.support.Capture;
import org.testfx.util.BoundsQueryUtils;

import static org.testfx.util.NodeQueryUtils.isVisible;
import static org.testfx.util.WaitForAsyncUtils.asyncFx;
import static org.testfx.util.WaitForAsyncUtils.waitFor;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@Unstable(reason = "class was recently added")
public class FxRobot implements FxRobotInterface {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final FxRobotContext context;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    @Unstable(reason = "is missing apidocs")
    public FxRobot() {
        context = new FxRobotContext();
        context.setPointPosition(Pos.CENTER);
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR ROBOT CONTEXT.
    //---------------------------------------------------------------------------------------------

    @Unstable(reason = "is missing apidocs")
    public FxRobotContext robotContext() {
        return context;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR WINDOW TARGETING.
    //---------------------------------------------------------------------------------------------

    @Override
    @Unstable(reason = "is missing apidocs")
    public Window targetWindow() {
        return context.getWindowFinder().targetWindow();
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot targetWindow(Window window) {
        context.getWindowFinder().targetWindow(window);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot targetWindow(Predicate<Window> predicate) {
        context.getWindowFinder().targetWindow(predicate);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot targetWindow(int windowNumber) {
        context.getWindowFinder().targetWindow(windowNumber);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot targetWindow(String stageTitleRegex) {
        context.getWindowFinder().targetWindow(stageTitleRegex);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot targetWindow(Pattern stageTitlePattern) {
        context.getWindowFinder().targetWindow(stageTitlePattern);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot targetWindow(Scene scene) {
        context.getWindowFinder().targetWindow(scene);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot targetWindow(Node node) {
        context.getWindowFinder().targetWindow(node);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR WINDOW LOOKUP.
    //---------------------------------------------------------------------------------------------

    @Override
    @Unstable(reason = "is missing apidocs")
    public List<Window> listWindows() {
        return context.getWindowFinder().listWindows();
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public List<Window> listTargetWindows() {
        return context.getWindowFinder().listTargetWindows();
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public Window window(Predicate<Window> predicate) {
        return context.getWindowFinder().window(predicate);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public Window window(int windowIndex) {
        return context.getWindowFinder().window(windowIndex);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public Window window(String stageTitleRegex) {
        return context.getWindowFinder().window(stageTitleRegex);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public Window window(Pattern stageTitlePattern) {
        return context.getWindowFinder().window(stageTitlePattern);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public Window window(Scene scene) {
        return context.getWindowFinder().window(scene);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public Window window(Node node) {
        return context.getWindowFinder().window(node);
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR NODE LOOKUP.
    //---------------------------------------------------------------------------------------------

    @Override
    @Unstable(reason = "is missing apidocs")
    public NodeQuery fromAll() {
        return context.getNodeFinder().fromAll();
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public NodeQuery from(Node... parentNodes) {
        return context.getNodeFinder().from(parentNodes);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public NodeQuery from(Collection<Node> parentNodes) {
        return context.getNodeFinder().from(parentNodes);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public NodeQuery from(NodeQuery nodeQuery) {
        return context.getNodeFinder().from(nodeQuery);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public NodeQuery lookup(String query) {
        return context.getNodeFinder().lookup(query);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public <T extends Node> NodeQuery lookup(Matcher<T> matcher) {
        return context.getNodeFinder().lookup(matcher);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public <T extends Node> NodeQuery lookup(Predicate<T> predicate) {
        return context.getNodeFinder().lookup(predicate);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public Node rootNode(Window window) {
        return context.getNodeFinder().rootNode(window);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public Node rootNode(Scene scene) {
        return context.getNodeFinder().rootNode(scene);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public Node rootNode(Node node) {
        return context.getNodeFinder().rootNode(node);
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR BOUNDS LOCATION.
    //---------------------------------------------------------------------------------------------

    @Override
    public BoundsQuery bounds(double minX,
                              double minY,
                              double width,
                              double height) {
        return () -> BoundsQueryUtils.bounds(minX, minY, width, height);
    }

    @Override
    public BoundsQuery bounds(Point2D point) {
        return () -> BoundsQueryUtils.bounds(point);
    }

    @Override
    public BoundsQuery bounds(Bounds bounds) {
        return () -> bounds;
    }

    @Override
    public BoundsQuery bounds(Node node) {
        return () -> BoundsQueryUtils.boundsOnScreen(node);
    }

    @Override
    public BoundsQuery bounds(Scene scene) {
        return () -> BoundsQueryUtils.boundsOnScreen(BoundsQueryUtils.bounds(scene), scene);
    }

    @Override
    public BoundsQuery bounds(Window window) {
        return () -> BoundsQueryUtils.boundsOnScreen(BoundsQueryUtils.bounds(window), window);
    }

    @Override
    public BoundsQuery bounds(String query) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Node> BoundsQuery bounds(Matcher<T> matcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Node> BoundsQuery bounds(Predicate<T> predicate) {
        throw new UnsupportedOperationException();
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT POSITION.
    //---------------------------------------------------------------------------------------------

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot targetPos(Pos pointPosition) {
        context.setPointPosition(pointPosition);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT LOCATION.
    //---------------------------------------------------------------------------------------------

    @Override
    @Unstable(reason = "is missing apidocs")
    public PointQuery point(double x,
                            double y) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        return pointLocator.point(new Point2D(x, y)).atPosition(pointPosition);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public PointQuery point(Point2D point) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        return pointLocator.point(point).atPosition(pointPosition);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public PointQuery point(Bounds bounds) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        return pointLocator.point(bounds).atPosition(pointPosition);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public PointQuery point(Node node) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        targetWindow(node.getScene().getWindow());
        return pointLocator.point(node).atPosition(pointPosition);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public PointQuery point(Scene scene) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        targetWindow(scene.getWindow());
        return pointLocator.point(scene).atPosition(pointPosition);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public PointQuery point(Window window) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        targetWindow(window);
        return pointLocator.point(window).atPosition(pointPosition);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public PointQuery point(String query) {
        NodeQuery nodeQuery = lookup(query);
        Node node = queryNode(nodeQuery, "the query \"" + query + "\"");
        return point(node).atPosition(context.getPointPosition());
    }

    @Override
    @Unstable(reason = "is missing apidocs; might change to accept all objects")
    public <T extends Node> PointQuery point(Matcher<T> matcher) {
        NodeQuery nodeQuery = lookup(matcher);
        Node node = queryNode(nodeQuery, "the matcher \"" + matcher.toString() + "\"");
        return point(node).atPosition(context.getPointPosition());
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public <T extends Node> PointQuery point(Predicate<T> predicate) {
        NodeQuery nodeQuery = lookup(predicate);
        Node node = queryNode(nodeQuery, "the predicate");
        return point(node).atPosition(context.getPointPosition());
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT OFFSET.
    //---------------------------------------------------------------------------------------------

    @Override
    @Unstable(reason = "is missing apidocs")
    public PointQuery offset(Point2D point,
                             double offsetX,
                             double offsetY) {
        return point(point).atOffset(offsetX, offsetY);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public PointQuery offset(Bounds bounds,
                             double offsetX,
                             double offsetY) {
        return point(bounds).atOffset(offsetX, offsetY);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public PointQuery offset(Node node,
                             double offsetX,
                             double offsetY) {
        return point(node).atOffset(offsetX, offsetY);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public PointQuery offset(Scene scene,
                             double offsetX,
                             double offsetY) {
        return point(scene).atOffset(offsetX, offsetY);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public PointQuery offset(Window window,
                             double offsetX,
                             double offsetY) {
        return point(window).atOffset(offsetX, offsetY);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public PointQuery offset(String query,
                             double offsetX,
                             double offsetY) {
        return point(query).atOffset(offsetX, offsetY);
    }

    @Override
    @Unstable(reason = "is missing apidocs; might change to accept all objects")
    public <T extends Node> PointQuery offset(Matcher<T> matcher,
                                              double offsetX,
                                              double offsetY) {
        return point(matcher).atOffset(offsetX, offsetY);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public <T extends Node> PointQuery offset(Predicate<T> predicate,
                                              double offsetX,
                                              double offsetY) {
        return point(predicate).atOffset(offsetX, offsetY);
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR SCREEN CAPTURING.
    //---------------------------------------------------------------------------------------------

    @Override
    @Unstable(reason = "is missing apidocs")
    public Capture capture(Rectangle2D screenRegion) {
        Image image = context.getCaptureSupport().captureRegion(screenRegion);
        return () -> image;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public Capture capture(Bounds bounds) {
        Rectangle2D region = new Rectangle2D(bounds.getMinX(), bounds.getMinX(),
                                             bounds.getWidth(), bounds.getHeight());
        Image image = context.getCaptureSupport().captureRegion(region);
        return () -> image;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public Capture capture(Node node) {
        Image image = context.getCaptureSupport().captureNode(node);
        return () -> image;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public Capture capture(Image image) {
        return () -> image;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public Capture capture(Path path) {
        Image image = context.getCaptureSupport().loadImage(path);
        return () -> image;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public Capture capture(URL url) {
        try {
            Path path = Paths.get(url.toURI());
            Image image = context.getCaptureSupport().loadImage(path);
            return () -> image;
        }
        catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR INTERACTION AND INTERRUPTION.
    //---------------------------------------------------------------------------------------------

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot interact(Runnable runnable) {
        waitFor(asyncFx(runnable));
        waitForFxEvents();
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public <T> FxRobot interact(Callable<T> callable) {
        waitFor(asyncFx(callable));
        waitForFxEvents();
        return this;
    }
    
    @Override
    @Unstable(reason = "is missing tests")
    public FxRobot interactNoWait(Runnable runnable) {
        waitFor(asyncFx(runnable));
        return this;
    }
    @Override
    @Unstable(reason = "is missing tests")
    public <T> FxRobot interactNoWait(Callable<T> callable) {
        waitFor(asyncFx(callable));
        return this;
    }
    

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot interrupt() {
        waitForFxEvents();
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot interrupt(int attemptsCount) {
        waitForFxEvents(attemptsCount);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF TYPE ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot push(KeyCode... combination) {
        context.getTypeRobot().push(combination);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot push(KeyCodeCombination combination) {
        context.getTypeRobot().push(combination);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot type(KeyCode... keyCodes) {
        context.getTypeRobot().type(keyCodes);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot type(KeyCode keyCode,
                        int times) {
        context.getTypeRobot().type(keyCode, times);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot eraseText(int amount) {
        return type(KeyCode.BACK_SPACE, amount);
    }

    @Unstable(reason = "maybe extract this into a new class")
    public FxRobot closeCurrentWindow() {
        return push(KeyCode.ALT, KeyCode.F4).sleep(100);
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF WRITE ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot write(char character) {
        context.getWriteRobot().write(character);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot write(String text) {
        context.getWriteRobot().write(text);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF SLEEP ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot sleep(long milliseconds) {
        context.getSleepRobot().sleep(milliseconds);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot sleep(long duration,
                         TimeUnit timeUnit) {
        context.getSleepRobot().sleep(duration, timeUnit);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF SCROLL ROBOT.
    //---------------------------------------------------------------------------------------------

    @Deprecated
    @Unstable(reason = "is missing apidocs")
    public FxRobot scroll(int amount) {
        context.getScrollRobot().scroll(amount);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot scroll(int amount,
                          VerticalDirection direction) {
        context.getScrollRobot().scroll(amount, direction);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot scroll(VerticalDirection direction) {
        scroll(1, direction);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF KEYBOARD ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    @Unstable(reason = "could be renamed to accept empty arrays")
    public FxRobot press(KeyCode... keys) {
        context.getKeyboardRobot().press(keys);
        return this;
    }

    @Override
    @Unstable(reason = "could be renamed to accept empty arrays")
    public FxRobot release(KeyCode... keys) {
        context.getKeyboardRobot().release(keys);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF MOUSE ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    @Unstable(reason = "could be renamed to accept empty arrays")
    public FxRobot press(MouseButton... buttons) {
        context.getMouseRobot().press(buttons);
        return this;
    }

    @Override
    @Unstable(reason = "could be renamed to accept empty arrays")
    public FxRobot release(MouseButton... buttons) {
        context.getMouseRobot().release(buttons);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF CLICK ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot clickOn(MouseButton... buttons) {
        context.getClickRobot().clickOn(buttons);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot clickOn(PointQuery pointQuery,
                           MouseButton... buttons) {
        context.getClickRobot().clickOn(pointQuery, buttons);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot doubleClickOn(MouseButton... buttons) {
        context.getClickRobot().doubleClickOn(buttons);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot doubleClickOn(PointQuery pointQuery,
                                 MouseButton... buttons) {
        context.getClickRobot().doubleClickOn(pointQuery, buttons);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot clickOn(double x,
                           double y,
                           MouseButton... buttons) {
        return clickOn(point(x, y), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot clickOn(Point2D point,
                           MouseButton... buttons) {
        return clickOn(point(point), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot clickOn(Bounds bounds,
                           MouseButton... buttons) {
        return clickOn(point(bounds), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot clickOn(Node node,
                           MouseButton... buttons) {
        return clickOn(point(node), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot clickOn(Scene scene,
                           MouseButton... buttons) {
        return clickOn(point(scene), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot clickOn(Window window,
                           MouseButton... buttons) {
        return clickOn(point(window), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot clickOn(String query,
                           MouseButton... buttons) {
        return clickOn(pointOfVisibleNode(query), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs; might change to accept all objects")
    public <T extends Node> FxRobot clickOn(Matcher<T> matcher,
                                            MouseButton... buttons) {
        return clickOn(pointOfVisibleNode(matcher), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public <T extends Node> FxRobot clickOn(Predicate<T> predicate,
                                            MouseButton... buttons) {
        return clickOn(pointOfVisibleNode(predicate), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot rightClickOn() {
        return clickOn(MouseButton.SECONDARY);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot rightClickOn(PointQuery pointQuery) {
        return clickOn(pointQuery, MouseButton.SECONDARY);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot rightClickOn(double x,
                                double y) {
        return clickOn(x, y, MouseButton.SECONDARY);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot rightClickOn(Point2D point) {
        return clickOn(point, MouseButton.SECONDARY);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot rightClickOn(Bounds bounds) {
        return clickOn(bounds, MouseButton.SECONDARY);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot rightClickOn(Node node) {
        return clickOn(node, MouseButton.SECONDARY);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot rightClickOn(Scene scene) {
        return clickOn(scene, MouseButton.SECONDARY);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot rightClickOn(Window window) {
        return clickOn(window, MouseButton.SECONDARY);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot rightClickOn(String query) {
        return clickOn(query, MouseButton.SECONDARY);
    }

    @Override
    @Unstable(reason = "is missing apidocs; might change to accept all objects")
    public <T extends Node> FxRobot rightClickOn(Matcher<T> matcher) {
        return clickOn(matcher, MouseButton.SECONDARY);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public <T extends Node> FxRobot rightClickOn(Predicate<T> predicate) {
        return clickOn(predicate, MouseButton.SECONDARY);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot doubleClickOn(double x,
                                 double y,
                                 MouseButton... buttons) {
        return doubleClickOn(point(x, y), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot doubleClickOn(Point2D point,
                                 MouseButton... buttons) {
        return doubleClickOn(point(point), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot doubleClickOn(Bounds bounds,
                                 MouseButton... buttons) {
        return doubleClickOn(point(bounds), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot doubleClickOn(Node node,
                                 MouseButton... buttons) {
        return doubleClickOn(point(node), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot doubleClickOn(Scene scene,
                                 MouseButton... buttons) {
        return doubleClickOn(point(scene), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot doubleClickOn(Window window,
                                 MouseButton... buttons) {
        return doubleClickOn(point(window), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot doubleClickOn(String query,
                                 MouseButton... buttons) {
        return doubleClickOn(pointOfVisibleNode(query), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs; might change to accept all objects")
    public <T extends Node> FxRobot doubleClickOn(Matcher<T> matcher,
                                                  MouseButton... buttons) {
        return doubleClickOn(pointOfVisibleNode(matcher), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public <T extends Node> FxRobot doubleClickOn(Predicate<T> predicate,
                                                  MouseButton... buttons) {
        return doubleClickOn(pointOfVisibleNode(predicate), buttons);
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF DRAG ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot drag(MouseButton... buttons) {
        context.getDragRobot().drag(buttons);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot drag(PointQuery pointQuery,
                        MouseButton... buttons) {
        context.getDragRobot().drag(pointQuery, buttons);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot drop() {
        context.getDragRobot().drop();
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot dropTo(PointQuery pointQuery) {
        context.getDragRobot().dropTo(pointQuery);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot dropBy(double x,
                          double y) {
        context.getDragRobot().dropBy(x, y);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot drag(double x,
                        double y,
                        MouseButton... buttons) {
        return drag(point(x, y), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot drag(Point2D point,
                        MouseButton... buttons) {
        return drag(point(point), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot drag(Bounds bounds,
                        MouseButton... buttons) {
        return drag(point(bounds), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot drag(Node node,
                        MouseButton... buttons) {
        return drag(point(node), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot drag(Scene scene,
                        MouseButton... buttons) {
        return drag(point(scene), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot drag(Window window,
                        MouseButton... buttons) {
        return drag(point(window), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot drag(String query,
                        MouseButton... buttons) {
        return drag(pointOfVisibleNode(query), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs; might change to accept all objects")
    public <T extends Node> FxRobot drag(Matcher<T> matcher,
                                         MouseButton... buttons) {
        return drag(pointOfVisibleNode(matcher), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public <T extends Node> FxRobot drag(Predicate<T> predicate,
                                         MouseButton... buttons) {
        return drag(pointOfVisibleNode(predicate), buttons);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot dropTo(double x,
                          double y) {
        return dropTo(point(x, y));
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot dropTo(Point2D point) {
        return dropTo(point(point));
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot dropTo(Bounds bounds) {
        return dropTo(point(bounds));
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot dropTo(Node node) {
        return dropTo(point(node));
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot dropTo(Scene scene) {
        return dropTo(point(scene));
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot dropTo(Window window) {
        return dropTo(point(window));
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot dropTo(String query) {
        return dropTo(pointOfVisibleNode(query));
    }

    @Override
    @Unstable(reason = "is missing apidocs; might change to accept all objects")
    public <T extends Node> FxRobot dropTo(Matcher<T> matcher) {
        return dropTo(pointOfVisibleNode(matcher));
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public <T extends Node> FxRobot dropTo(Predicate<T> predicate) {
        return dropTo(pointOfVisibleNode(predicate));
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF MOVE ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot moveTo(PointQuery pointQuery) {
        context.getMoveRobot().moveTo(pointQuery);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot moveBy(double x,
                          double y) {
        context.getMoveRobot().moveBy(x, y);
        return this;
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot moveTo(double x,
                          double y) {
        return moveTo(point(new Point2D(x, y)));
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot moveTo(Point2D point) {
        return moveTo(point(point));
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot moveTo(Bounds bounds) {
        return moveTo(point(bounds));
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot moveTo(Node node) {
        return moveTo(point(node));
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot moveTo(Scene scene) {
        return moveTo(point(scene));
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot moveTo(Window window) {
        return moveTo(point(window));
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public FxRobot moveTo(String query) {
        return moveTo(pointOfVisibleNode(query));
    }

    @Override
    @Unstable(reason = "is missing apidocs; might change to accept all objects")
    public <T extends Node> FxRobot moveTo(Matcher<T> matcher) {
        return moveTo(pointOfVisibleNode(matcher));
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public <T extends Node> FxRobot moveTo(Predicate<T> predicate) {
        return moveTo(pointOfVisibleNode(predicate));
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private PointQuery pointOfVisibleNode(String query) {
        NodeQuery nodeQuery = lookup(query);
        Node node = queryVisibleNode(nodeQuery, "the query \"" + query + "\"");
        return point(node);
    }

    private <T extends Node> PointQuery pointOfVisibleNode(Matcher<T> matcher) {
        NodeQuery nodeQuery = lookup(matcher);
        Node node = queryVisibleNode(nodeQuery, "the matcher \"" + matcher.toString() + "\"");
        return point(node);
    }

    private <T extends Node> PointQuery pointOfVisibleNode(Predicate<T> predicate) {
        NodeQuery nodeQuery = lookup(predicate);
        Node node = queryVisibleNode(nodeQuery, "the predicate");
        return point(node);
    }

    private Node queryNode(NodeQuery nodeQuery,
                           String queryDescription) {
        Optional<Node> resultNode = nodeQuery.tryQuery();
        if (!resultNode.isPresent()) {
            String message = queryDescription + " returned no nodes.";
            throw new FxRobotException(message);
        }
        return resultNode.get();
    }

    private Node queryVisibleNode(NodeQuery nodeQuery,
                                  String queryDescription) {
        Set<Node> resultNodes = nodeQuery.queryAll();
        if (resultNodes.isEmpty()) {
            String message = queryDescription + " returned no nodes.";
            throw new FxRobotException(message);
        }
        Optional<Node> resultNode = from(resultNodes).match(isVisible()).tryQuery();
        if (!resultNode.isPresent()) {
            String message = queryDescription + " returned " + resultNodes.size() + " nodes" +
                ", but no nodes were visible.";
            throw new FxRobotException(message);
        }
        return resultNode.get();
    }

}
