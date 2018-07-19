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

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import javafx.geometry.Bounds;
import javafx.geometry.HorizontalDirection;
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
import javafx.stage.Window;

import org.hamcrest.Matcher;
import org.testfx.robot.Motion;
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

public class FxRobot implements FxRobotInterface {

    private final FxRobotContext context;

    /**
     * Constructs all robot-related implementations and sets {@link #targetPos(Pos)} to {@link Pos#CENTER}.
     */
    public FxRobot() {
        context = new FxRobotContext();
    }

    /**
     * Returns the internal context.
     */
    public FxRobotContext robotContext() {
        return context;
    }

    @Override
    public Window targetWindow() {
        return context.getWindowFinder().targetWindow();
    }

    @Override
    public FxRobot targetWindow(Window window) {
        context.getWindowFinder().targetWindow(window);
        return this;
    }

    @Override
    public FxRobot targetWindow(Predicate<Window> predicate) {
        context.getWindowFinder().targetWindow(predicate);
        return this;
    }

    @Override
    public FxRobot targetWindow(int windowNumber) {
        context.getWindowFinder().targetWindow(windowNumber);
        return this;
    }

    @Override
    public FxRobot targetWindow(String stageTitleRegex) {
        context.getWindowFinder().targetWindow(stageTitleRegex);
        return this;
    }

    @Override
    public FxRobot targetWindow(Pattern stageTitlePattern) {
        context.getWindowFinder().targetWindow(stageTitlePattern);
        return this;
    }

    @Override
    public FxRobot targetWindow(Scene scene) {
        context.getWindowFinder().targetWindow(scene);
        return this;
    }

    @Override
    public FxRobot targetWindow(Node node) {
        context.getWindowFinder().targetWindow(node);
        return this;
    }

    @Override
    public List<Window> listWindows() {
        return context.getWindowFinder().listWindows();
    }

    @Override
    public List<Window> listTargetWindows() {
        return context.getWindowFinder().listTargetWindows();
    }

    @Override
    public Window window(Predicate<Window> predicate) {
        return context.getWindowFinder().window(predicate);
    }

    @Override
    public Window window(int windowIndex) {
        return context.getWindowFinder().window(windowIndex);
    }

    @Override
    public Window window(String stageTitleRegex) {
        return context.getWindowFinder().window(stageTitleRegex);
    }

    @Override
    public Window window(Pattern stageTitlePattern) {
        return context.getWindowFinder().window(stageTitlePattern);
    }

    @Override
    public Window window(Scene scene) {
        return context.getWindowFinder().window(scene);
    }

    @Override
    public Window window(Node node) {
        return context.getWindowFinder().window(node);
    }

    @Override
    public NodeQuery fromAll() {
        return context.getNodeFinder().fromAll();
    }

    @Override
    public NodeQuery from(Node... parentNodes) {
        return context.getNodeFinder().from(parentNodes);
    }

    @Override
    public NodeQuery from(Collection<Node> parentNodes) {
        return context.getNodeFinder().from(parentNodes);
    }

    @Override
    public NodeQuery from(NodeQuery nodeQuery) {
        return context.getNodeFinder().from(nodeQuery);
    }

    @Override
    public NodeQuery lookup(String query) {
        return context.getNodeFinder().lookup(query);
    }

    @Override
    public <T extends Node> NodeQuery lookup(Matcher<T> matcher) {
        return context.getNodeFinder().lookup(matcher);
    }

    @Override
    public <T extends Node> NodeQuery lookup(Predicate<T> predicate) {
        return context.getNodeFinder().lookup(predicate);
    }

    @Override
    public Node rootNode(Window window) {
        return context.getNodeFinder().rootNode(window);
    }

    @Override
    public Node rootNode(Scene scene) {
        return context.getNodeFinder().rootNode(scene);
    }

    @Override
    public Node rootNode(Node node) {
        return context.getNodeFinder().rootNode(node);
    }

    @Override
    public BoundsQuery bounds(double minX, double minY, double width, double height) {
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

    @Override
    public FxRobot targetPos(Pos pointPosition) {
        context.setPointPosition(pointPosition);
        return this;
    }

    @Override
    public PointQuery point(double x, double y) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        return pointLocator.point(new Point2D(x, y)).atPosition(pointPosition);
    }

    @Override
    public PointQuery point(Point2D point) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        return pointLocator.point(point).atPosition(pointPosition);
    }

    @Override
    public PointQuery point(Bounds bounds) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        return pointLocator.point(bounds).atPosition(pointPosition);
    }

    @Override
    public PointQuery point(Node node) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        targetWindow(node.getScene().getWindow());
        return pointLocator.point(node).onNode(node).atPosition(pointPosition);
    }

    @Override
    public PointQuery point(Scene scene) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        targetWindow(scene.getWindow());
        return pointLocator.point(scene).atPosition(pointPosition);
    }

    @Override
    public PointQuery point(Window window) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        targetWindow(window);
        return pointLocator.point(window).atPosition(pointPosition);
    }

    @Override
    public PointQuery point(String query) {
        NodeQuery nodeQuery = lookup(query);
        Node node = queryNode(nodeQuery, "the query \"" + query + "\"");
        return point(node).atPosition(context.getPointPosition());
    }

    @Override
    public <T extends Node> PointQuery point(Matcher<T> matcher) {
        NodeQuery nodeQuery = lookup(matcher);
        Node node = queryNode(nodeQuery, "the matcher \"" + matcher.toString() + "\"");
        return point(node).atPosition(context.getPointPosition());
    }

    @Override
    public <T extends Node> PointQuery point(Predicate<T> predicate) {
        NodeQuery nodeQuery = lookup(predicate);
        Node node = queryNode(nodeQuery, "the predicate");
        return point(node).atPosition(context.getPointPosition());
    }

    @Override
    public PointQuery offset(Point2D point, double offsetX, double offsetY) {
        return point(point).atOffset(offsetX, offsetY);
    }

    @Override
    public PointQuery offset(Bounds bounds, double offsetX, double offsetY) {
        return point(bounds).atOffset(offsetX, offsetY);
    }

    @Override
    public PointQuery offset(Node node, double offsetX, double offsetY) {
        return point(node).atOffset(offsetX, offsetY);
    }

    @Override
    public PointQuery offset(Node node, Pos offsetReferencePos, double offsetX, double offsetY) {
        return point(node).atPosition(offsetReferencePos).atOffset(offsetX, offsetY);
    }

    @Override
    public PointQuery offset(Scene scene, double offsetX, double offsetY) {
        return point(scene).atOffset(offsetX, offsetY);
    }

    @Override
    public PointQuery offset(Window window, double offsetX, double offsetY) {
        return point(window).atOffset(offsetX, offsetY);
    }

    @Override
    public PointQuery offset(String query, double offsetX, double offsetY) {
        return point(query).atOffset(offsetX, offsetY);
    }

    @Override
    public <T extends Node> PointQuery offset(Matcher<T> matcher, double offsetX, double offsetY) {
        return point(matcher).atOffset(offsetX, offsetY);
    }

    @Override
    public <T extends Node> PointQuery offset(Predicate<T> predicate, double offsetX, double offsetY) {
        return point(predicate).atOffset(offsetX, offsetY);
    }

    @Override
    public Capture capture(Rectangle2D screenRegion) {
        return () -> context.getCaptureSupport().captureRegion(screenRegion);
    }

    @Override
    public Capture capture(Bounds bounds) {
        Rectangle2D region = new Rectangle2D(bounds.getMinX(), bounds.getMinY(),
                                             bounds.getWidth(), bounds.getHeight());
        return () -> context.getCaptureSupport().captureRegion(region);
    }

    @Override
    public Capture capture(Node node) {
        return () -> context.getCaptureSupport().captureNode(node);
    }

    @Override
    public Capture capture(Image image) {
        return () -> image;
    }

    @Override
    public Capture capture(Path path) {
        return () -> context.getCaptureSupport().loadImage(path);
    }

    @Override
    public Capture capture(URL url) {
        try {
            Path path = Paths.get(url.toURI());
            return () -> context.getCaptureSupport().loadImage(path);
        }
        catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public FxRobot interact(Runnable runnable) {
        waitFor(asyncFx(runnable));
        waitForFxEvents();
        return this;
    }

    @Override
    public <T> FxRobot interact(Callable<T> callable) {
        waitFor(asyncFx(callable));
        waitForFxEvents();
        return this;
    }

    @Override
    public FxRobot interactNoWait(Runnable runnable) {
        waitFor(asyncFx(runnable));
        return this;
    }
    @Override
    public <T> FxRobot interactNoWait(Callable<T> callable) {
        waitFor(asyncFx(callable));
        return this;
    }

    @Override
    public FxRobot interrupt() {
        waitForFxEvents();
        return this;
    }

    @Override
    public FxRobot interrupt(int attemptsCount) {
        waitForFxEvents(attemptsCount);
        return this;
    }

    @Override
    public FxRobot push(KeyCode... combination) {
        context.getTypeRobot().push(combination);
        return this;
    }

    @Override
    public FxRobot push(KeyCodeCombination combination) {
        context.getTypeRobot().push(combination);
        return this;
    }

    @Override
    public FxRobot type(KeyCode... keyCodes) {
        context.getTypeRobot().type(keyCodes);
        return this;
    }

    @Override
    public FxRobot type(KeyCode keyCode, int times) {
        context.getTypeRobot().type(keyCode, times);
        return this;
    }

    @Override
    public FxRobot eraseText(int amount) {
        return type(KeyCode.BACK_SPACE, amount);
    }

    /**
     * @deprecated The implementation of this method simply pushes the keys ALT+F4 which
     * does not close the current window on all platforms.
     */
    @Deprecated
    public FxRobot closeCurrentWindow() {
        return push(KeyCode.ALT, KeyCode.F4).sleep(100);
    }

    @Override
    public FxRobot write(char character) {
        context.getWriteRobot().write(character);
        return this;
    }

    @Override
    public FxRobot write(String text) {
        context.getWriteRobot().write(text);
        return this;
    }

    @Override
    public FxRobot write(String text, int sleepMillis) {
        context.getWriteRobot().write(text, sleepMillis);
        return this;
    }

    @Override
    public FxRobot sleep(long milliseconds) {
        context.getSleepRobot().sleep(milliseconds);
        return this;
    }

    @Override
    public FxRobot sleep(long duration, TimeUnit timeUnit) {
        context.getSleepRobot().sleep(duration, timeUnit);
        return this;
    }
 
    @Override
    @Deprecated
    public FxRobot scroll(int amount) {
        context.getScrollRobot().scroll(amount);
        return this;
    }

    @Override
    public FxRobot scroll(int amount, VerticalDirection direction) {
        context.getScrollRobot().scroll(amount, direction);
        return this;
    }

    @Override
    public FxRobot scroll(VerticalDirection direction) {
        scroll(1, direction);
        return this;
    }

    @Override
    public FxRobot scroll(int amount, HorizontalDirection direction) {
        context.getScrollRobot().scroll(amount, direction);
        return this;
    }

    @Override
    public FxRobot scroll(HorizontalDirection direction) {
        scroll(1, direction);
        return this;
    }

    @Override
    public FxRobot press(KeyCode... keys) {
        context.getKeyboardRobot().press(keys);
        return this;
    }

    @Override
    public FxRobot release(KeyCode... keys) {
        context.getKeyboardRobot().release(keys);
        return this;
    }

    @Override
    public FxRobot press(MouseButton... buttons) {
        context.getMouseRobot().press(buttons);
        return this;
    }

    @Override
    public FxRobot release(MouseButton... buttons) {
        context.getMouseRobot().release(buttons);
        return this;
    }

    @Override
    public FxRobot clickOn(MouseButton... buttons) {
        context.getClickRobot().clickOn(buttons);
        return this;
    }

    @Override
    public FxRobot clickOn(PointQuery pointQuery, Motion motion, MouseButton... buttons) {
        context.getClickRobot().clickOn(pointQuery, motion, buttons);
        return this;
    }

    @Override
    public FxRobot doubleClickOn(MouseButton... buttons) {
        context.getClickRobot().doubleClickOn(buttons);
        return this;
    }

    @Override
    public FxRobot doubleClickOn(PointQuery pointQuery, Motion motion, MouseButton... buttons) {
        context.getClickRobot().doubleClickOn(pointQuery, motion, buttons);
        return this;
    }

    @Override
    public FxRobot clickOn(double x, double y, Motion motion, MouseButton... buttons) {
        return clickOn(point(x, y), motion, buttons);
    }

    @Override
    public FxRobot clickOn(Point2D point, Motion motion, MouseButton... buttons) {
        return clickOn(point(point), motion, buttons);
    }

    @Override
    public FxRobot clickOn(Bounds bounds, Motion motion, MouseButton... buttons) {
        return clickOn(point(bounds), motion, buttons);
    }

    @Override
    public FxRobot clickOn(Node node, Motion motion, MouseButton... buttons) {
        return clickOn(point(node), motion, buttons);
    }

    @Override
    public FxRobot clickOn(Scene scene, Motion motion, MouseButton... buttons) {
        return clickOn(point(scene), motion, buttons);
    }

    @Override
    public FxRobot clickOn(Window window, Motion motion, MouseButton... buttons) {
        return clickOn(point(window), motion, buttons);
    }

    @Override
    public FxRobot clickOn(String query, Motion motion, MouseButton... buttons) {
        return clickOn(pointOfVisibleNode(query), motion, buttons);
    }

    @Override
    public <T extends Node> FxRobot clickOn(Matcher<T> matcher, Motion motion, MouseButton... buttons) {
        return clickOn(pointOfVisibleNode(matcher), motion, buttons);
    }

    @Override
    public <T extends Node> FxRobot clickOn(Predicate<T> predicate, Motion motion, MouseButton... buttons) {
        return clickOn(pointOfVisibleNode(predicate), motion, buttons);
    }

    @Override
    public FxRobot rightClickOn() {
        return clickOn(MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(PointQuery pointQuery, Motion motion) {
        return clickOn(pointQuery, motion, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(double x, double y, Motion motion) {
        return clickOn(x, y, motion, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(Point2D point, Motion motion) {
        return clickOn(point, motion, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(Bounds bounds, Motion motion) {
        return clickOn(bounds, motion, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(Node node, Motion motion) {
        return clickOn(node, motion, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(Scene scene, Motion motion) {
        return clickOn(scene, motion, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(Window window, Motion motion) {
        return clickOn(window, motion, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(String query, Motion motion) {
        return clickOn(query, motion, MouseButton.SECONDARY);
    }

    @Override
    public <T extends Node> FxRobot rightClickOn(Matcher<T> matcher, Motion motion) {
        return clickOn(matcher, motion, MouseButton.SECONDARY);
    }

    @Override
    public <T extends Node> FxRobot rightClickOn(Predicate<T> predicate, Motion motion) {
        return clickOn(predicate, motion, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot doubleClickOn(double x, double y, Motion motion, MouseButton... buttons) {
        return doubleClickOn(point(x, y), motion, buttons);
    }

    @Override
    public FxRobot doubleClickOn(Point2D point, Motion motion, MouseButton... buttons) {
        return doubleClickOn(point(point), motion, buttons);
    }

    @Override
    public FxRobot doubleClickOn(Bounds bounds, Motion motion, MouseButton... buttons) {
        return doubleClickOn(point(bounds), motion, buttons);
    }

    @Override
    public FxRobot doubleClickOn(Node node, Motion motion, MouseButton... buttons) {
        return doubleClickOn(point(node), motion, buttons);
    }

    @Override
    public FxRobot doubleClickOn(Scene scene, Motion motion, MouseButton... buttons) {
        return doubleClickOn(point(scene), motion, buttons);
    }

    @Override
    public FxRobot doubleClickOn(Window window, Motion motion, MouseButton... buttons) {
        return doubleClickOn(point(window), motion, buttons);
    }

    @Override
    public FxRobot doubleClickOn(String query, Motion motion, MouseButton... buttons) {
        return doubleClickOn(pointOfVisibleNode(query), motion, buttons);
    }

    @Override
    public <T extends Node> FxRobot doubleClickOn(Matcher<T> matcher, Motion motion, MouseButton... buttons) {
        return doubleClickOn(pointOfVisibleNode(matcher), motion, buttons);
    }

    @Override
    public <T extends Node> FxRobot doubleClickOn(Predicate<T> predicate, Motion motion, MouseButton... buttons) {
        return doubleClickOn(pointOfVisibleNode(predicate), motion, buttons);
    }

    @Override
    public FxRobot drag(MouseButton... buttons) {
        context.getDragRobot().drag(buttons);
        return this;
    }

    @Override
    public FxRobot drag(PointQuery pointQuery, MouseButton... buttons) {
        context.getDragRobot().drag(pointQuery, buttons);
        return this;
    }

    @Override
    public FxRobot drop() {
        context.getDragRobot().drop();
        return this;
    }

    @Override
    public FxRobot dropTo(PointQuery pointQuery) {
        context.getDragRobot().dropTo(pointQuery);
        return this;
    }

    @Override
    public FxRobot dropBy(double x, double y) {
        context.getDragRobot().dropBy(x, y);
        return this;
    }

    @Override
    public FxRobot drag(double x, double y, MouseButton... buttons) {
        return drag(point(x, y), buttons);
    }

    @Override
    public FxRobot drag(Point2D point, MouseButton... buttons) {
        return drag(point(point), buttons);
    }

    @Override
    public FxRobot drag(Bounds bounds, MouseButton... buttons) {
        return drag(point(bounds), buttons);
    }

    @Override
    public FxRobot drag(Node node, MouseButton... buttons) {
        return drag(point(node), buttons);
    }

    @Override
    public FxRobot drag(Scene scene, MouseButton... buttons) {
        return drag(point(scene), buttons);
    }

    @Override
    public FxRobot drag(Window window, MouseButton... buttons) {
        return drag(point(window), buttons);
    }

    @Override
    public FxRobot drag(String query, MouseButton... buttons) {
        return drag(pointOfVisibleNode(query), buttons);
    }

    @Override
    public <T extends Node> FxRobot drag(Matcher<T> matcher, MouseButton... buttons) {
        return drag(pointOfVisibleNode(matcher), buttons);
    }

    @Override
    public <T extends Node> FxRobot drag(Predicate<T> predicate, MouseButton... buttons) {
        return drag(pointOfVisibleNode(predicate), buttons);
    }

    @Override
    public FxRobot dropTo(double x, double y) {
        return dropTo(point(x, y));
    }

    @Override
    public FxRobot dropTo(Point2D point) {
        return dropTo(point(point));
    }

    @Override
    public FxRobot dropTo(Bounds bounds) {
        return dropTo(point(bounds));
    }

    @Override
    public FxRobot dropTo(Node node) {
        return dropTo(point(node));
    }

    @Override
    public FxRobot dropTo(Scene scene) {
        return dropTo(point(scene));
    }

    @Override
    public FxRobot dropTo(Window window) {
        return dropTo(point(window));
    }

    @Override
    public FxRobot dropTo(String query) {
        return dropTo(pointOfVisibleNode(query));
    }

    @Override
    public <T extends Node> FxRobot dropTo(Matcher<T> matcher) {
        return dropTo(pointOfVisibleNode(matcher));
    }

    @Override
    public <T extends Node> FxRobot dropTo(Predicate<T> predicate) {
        return dropTo(pointOfVisibleNode(predicate));
    }

    @Override
    public FxRobot moveTo(PointQuery pointQuery, Motion motion) {
        context.getMoveRobot().moveTo(pointQuery, motion);
        return this;
    }

    @Override
    public FxRobot moveBy(double x, double y, Motion motion) {
        context.getMoveRobot().moveBy(x, y, motion);
        return this;
    }

    @Override
    public FxRobot moveTo(double x, double y, Motion motion) {
        return moveTo(point(new Point2D(x, y)), motion);
    }

    @Override
    public FxRobot moveTo(Point2D point, Motion motion) {
        return moveTo(point(point), motion);
    }

    @Override
    public FxRobot moveTo(Bounds bounds, Motion motion) {
        return moveTo(point(bounds), motion);
    }

    @Override
    public FxRobot moveTo(Node node, Pos offsetReferencePos, Point2D offset, Motion motion) {
        return moveTo(point(node).atPosition(offsetReferencePos).atOffset(offset), motion);
    }

    @Override
    public FxRobot moveTo(Scene scene, Motion motion) {
        return moveTo(point(scene), motion);
    }

    @Override
    public FxRobot moveTo(Window window, Motion motion) {
        return moveTo(point(window), motion);
    }

    @Override
    public FxRobot moveTo(String query, Motion motion) {
        return moveTo(pointOfVisibleNode(query), motion);
    }

    @Override
    public <T extends Node> FxRobot moveTo(Matcher<T> matcher, Motion motion) {
        return moveTo(pointOfVisibleNode(matcher), motion);
    }

    @Override
    public <T extends Node> FxRobot moveTo(Predicate<T> predicate, Motion motion) {
        return moveTo(pointOfVisibleNode(predicate), motion);
    }

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

    private Node queryNode(NodeQuery nodeQuery, String queryDescription) {
        Optional<Node> resultNode = nodeQuery.tryQuery();
        if (!resultNode.isPresent()) {
            throw new FxRobotException(queryDescription + " returned no nodes.");
        }
        return resultNode.get();
    }

    private Node queryVisibleNode(NodeQuery nodeQuery, String queryDescription) {
        Set<Node> resultNodes = nodeQuery.queryAll();
        if (resultNodes.isEmpty()) {
            throw new FxRobotException(queryDescription + " returned no nodes.");
        }
        Optional<Node> resultNode = from(resultNodes).match(isVisible()).tryQuery();
        if (!resultNode.isPresent()) {
            throw new FxRobotException(queryDescription + " returned " + resultNodes.size() + " nodes" +
                ", but no nodes were visible.");
        }
        return resultNode.get();
    }

}
