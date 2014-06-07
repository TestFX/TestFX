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
package org.loadui.testfx.framework.robot;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.Window;

import com.google.common.base.Predicate;
import org.hamcrest.Matcher;
import org.loadui.testfx.robots.ClickRobot;
import org.loadui.testfx.robots.DragRobot;
import org.loadui.testfx.robots.KeyboardRobot;
import org.loadui.testfx.robots.MouseRobot;
import org.loadui.testfx.robots.MoveRobot;
import org.loadui.testfx.robots.ScreenRobot;
import org.loadui.testfx.robots.ScrollRobot;
import org.loadui.testfx.robots.SleepRobot;
import org.loadui.testfx.robots.TypeRobot;
import org.loadui.testfx.robots.impl.KeyboardRobotImpl;
import org.loadui.testfx.robots.impl.MouseRobotImpl;
import org.loadui.testfx.robots.impl.ScreenRobotImpl;
import org.loadui.testfx.robots.impl.ScrollRobotImpl;
import org.loadui.testfx.robots.impl.SleepRobotImpl;
import org.loadui.testfx.robots.impl.TypeRobotImpl;
import org.loadui.testfx.service.finder.NodeFinder;
import org.loadui.testfx.service.finder.WindowFinder;
import org.loadui.testfx.service.finder.impl.NodeFinderImpl;
import org.loadui.testfx.service.finder.impl.WindowFinderImpl;
import org.loadui.testfx.service.locator.BoundsLocator;
import org.loadui.testfx.service.locator.PointLocator;
import org.loadui.testfx.service.locator.impl.BoundsLocatorImpl;
import org.loadui.testfx.service.locator.impl.PointLocatorImpl;
import org.loadui.testfx.service.query.PointQuery;
import org.loadui.testfx.service.support.CaptureSupport;
import org.loadui.testfx.service.support.WaitUntilSupport;

public class FxRobot implements ClickRobot, DragRobot, MoveRobot {

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
        return new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return findAll(nodeQuery).size();
            }
        };
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

    private static final WindowFinder windowFinder = new WindowFinderImpl();
    private static final NodeFinder nodeFinder = new NodeFinderImpl(windowFinder);
    private static final WaitUntilSupport waitUntilSupport = new WaitUntilSupport();
    private static final CaptureSupport captureSupport = new CaptureSupport(new ScreenRobotImpl());

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private Pos pointPosition = Pos.CENTER;
    private final BoundsLocator boundsLocator;
    private final PointLocator pointLocator;

    private final ScreenRobot screenRobot;
    private final MouseRobot mouseRobot;
    private final KeyboardRobot keyboardRobot;
    private final ScrollRobot scrollRobot;
    private final SleepRobot sleepRobot;
    private final TypeRobot typeRobot;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public FxRobot() {
        boundsLocator = new BoundsLocatorImpl();
        pointLocator = new PointLocatorImpl(boundsLocator);

        screenRobot = new ScreenRobotImpl();
        mouseRobot = new MouseRobotImpl(screenRobot);
        keyboardRobot = new KeyboardRobotImpl(screenRobot);
        scrollRobot = new ScrollRobotImpl(screenRobot);
        sleepRobot = new SleepRobotImpl();
        typeRobot = new TypeRobotImpl(keyboardRobot);
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT POSITION.
    //---------------------------------------------------------------------------------------------

    public FxRobot pos(Pos pointPosition) {
        this.pointPosition = pointPosition;
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT LOCATION.
    //---------------------------------------------------------------------------------------------

    public PointQuery pointFor(double x, double y) {
        return pointLocator.pointFor(new Point2D(x, y)).atPosition(pointPosition);
    }

    public PointQuery pointFor(Point2D point) {
        return pointLocator.pointFor(point).atPosition(pointPosition);
    }

    public PointQuery pointFor(Bounds bounds) {
        return pointLocator.pointFor(bounds).atPosition(pointPosition);
    }

    public PointQuery pointFor(Node node) {
        target(node.getScene().getWindow());
        return pointLocator.pointFor(node).atPosition(pointPosition);
    }

    public PointQuery pointFor(Scene scene) {
        target(scene.getWindow());
        return pointLocator.pointFor(scene).atPosition(pointPosition);
    }

    public PointQuery pointFor(Window window) {
        target(window);
        return pointLocator.pointFor(window).atPosition(pointPosition);
    }

    public PointQuery pointFor(String query) {
        Node node = nodeFinder.node(query);
        return pointFor(node).atPosition(pointPosition);
    }

    public PointQuery pointFor(Matcher<Object> matcher) {
        Node node = nodeFinder.node(matcher);
        return pointFor(node).atPosition(pointPosition);
    }

    @SuppressWarnings("unchecked")
    public <T extends Node> PointQuery pointFor(Predicate<T> predicate) {
        Node node = nodeFinder.node((Predicate<Node>) predicate);
        return pointFor(node).atPosition(pointPosition);
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT OFFSET.
    //---------------------------------------------------------------------------------------------

    public PointQuery offset(Point2D point, double offsetX, double offsetY) {
        return pointFor(point).atOffset(offsetX, offsetY);
    }

    public PointQuery offset(Bounds bounds, double offsetX, double offsetY) {
        return pointFor(bounds).atOffset(offsetX, offsetY);
    }

    public PointQuery offset(Node node, double offsetX, double offsetY) {
        return pointFor(node).atOffset(offsetX, offsetY);
    }

    public PointQuery offset(Scene scene, double offsetX, double offsetY) {
        return pointFor(scene).atOffset(offsetX, offsetY);
    }

    public PointQuery offset(Window window, double offsetX, double offsetY) {
        return pointFor(window).atOffset(offsetX, offsetY);
    }

    public PointQuery offset(String query, double offsetX, double offsetY) {
        return pointFor(query).atOffset(offsetX, offsetY);
    }

    public PointQuery offset(Matcher<Object> matcher, double offsetX, double offsetY) {
        return pointFor(matcher).atOffset(offsetX, offsetY);
    }

    public <T extends Node> PointQuery offset(Predicate<T> predicate, double offsetX, double offsetY) {
        return pointFor(predicate).atOffset(offsetX, offsetY);
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR WINDOW TARGETING.
    //---------------------------------------------------------------------------------------------

    public FxRobot target(Window window) {
        windowFinder.target(window);
        return this;
    }

    public FxRobot target(int windowNumber) {
        windowFinder.target(windowNumber);
        return this;
    }

    public FxRobot target(String stageTitleRegex) {
        windowFinder.target(stageTitleRegex);
        return this;
    }

    public FxRobot target(Scene scene) {
        windowFinder.target(scene);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF TYPE ROBOT.
    //---------------------------------------------------------------------------------------------

    public FxRobot type(KeyCode... keys) {
        typeRobot.type(keys);
        return this;
    }

    public FxRobot type(char character) {
        typeRobot.type(character);
        return this;
    }

    public FxRobot type(String text) {
        typeRobot.type(text);
        return this;
    }

    public FxRobot eraseCharacters(int characters) {
        typeRobot.erase(characters);
        return this;
    }

    public FxRobot push(KeyCode... keys) {
        type(keys);
        return this;
    }

    public FxRobot push(char character) {
        type(character);
        return this;
    }

    /**
     * Closes the front-most window using the Alt+F4 keyboard shortcut.
     */
    public FxRobot closeCurrentWindow() {
        push(KeyCode.ALT, KeyCode.F4).sleep(100);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF SLEEP ROBOT.
    //---------------------------------------------------------------------------------------------

    public FxRobot sleep(long milliseconds) {
        sleepRobot.sleep(milliseconds);
        return this;
    }

    public FxRobot sleep(long duration, TimeUnit timeUnit) {
        sleepRobot.sleep(duration, timeUnit);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF SCROLL ROBOT.
    //---------------------------------------------------------------------------------------------

    @Deprecated
    public FxRobot scroll(int amount) {
        scrollRobot.scroll(amount);
        return this;
    }

    public FxRobot scroll(int amount, VerticalDirection direction) {
        scrollRobot.scroll(amount, direction);
        return this;
    }

    public FxRobot scroll(VerticalDirection direction) {
        scroll(1, direction);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF MOUSE ROBOT.
    //---------------------------------------------------------------------------------------------

    public FxRobot press(MouseButton... buttons) {
        mouseRobot.press(buttons);
        return this;
    }

    public FxRobot release(MouseButton... buttons) {
        mouseRobot.release(buttons);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF KEYBOARD ROBOT.
    //---------------------------------------------------------------------------------------------

    public FxRobot press(KeyCode... keys) {
        keyboardRobot.press(keys);
        return this;
    }

    public FxRobot release(KeyCode... keys) {
        keyboardRobot.release(keys);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF CLICK ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    public FxRobot click(MouseButton... buttons) {
        if (buttons.length == 0) {
            return click(MouseButton.PRIMARY);
        }
        mouseRobot.press(buttons);
        mouseRobot.release(buttons);
        return this;
    }

    @Override
    public FxRobot click(double x, double y, MouseButton... buttons) {
        moveTo(x, y);
        click(buttons);
        return this;
    }

    @Override
    public FxRobot click(Point2D point, MouseButton... buttons) {
        moveTo(point);
        click(buttons);
        return this;
    }

    @Override
    public FxRobot click(Bounds bounds, MouseButton... buttons) {
        moveTo(bounds);
        click(buttons);
        return this;
    }

    @Override
    public FxRobot click(Node node, MouseButton... buttons) {
        moveTo(node);
        click(buttons);
        return this;
    }

    @Override
    public FxRobot click(Scene scene, MouseButton... buttons) {
        moveTo(scene);
        click(buttons);
        return this;
    }

    @Override
    public FxRobot click(Window window, MouseButton... buttons) {
        moveTo(window);
        click(buttons);
        return this;
    }

    @Override
    public FxRobot click(String query, MouseButton... buttons) {
        moveTo(query);
        click(buttons);
        return this;
    }

    @Override
    public FxRobot click(Matcher<Object> matcher, MouseButton... buttons) {
        moveTo(matcher);
        click(buttons);
        return this;
    }

    @Override
    public <T extends Node> FxRobot click(Predicate<T> predicate, MouseButton... buttons) {
        moveTo(predicate);
        click(buttons);
        return this;
    }

    @Override
    public FxRobot rightClick() {
        click(MouseButton.SECONDARY);
        return this;
    }

    @Override
    public FxRobot rightClick(double x, double y) {
        click(x, y, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public FxRobot rightClick(Point2D point) {
        click(point, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public FxRobot rightClick(Bounds bounds) {
        click(bounds, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public FxRobot rightClick(Node node) {
        click(node, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public FxRobot rightClick(Scene scene) {
        click(scene, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public FxRobot rightClick(Window window) {
        click(window, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public FxRobot rightClick(String query) {
        click(query, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public FxRobot rightClick(Matcher<Object> matcher) {
        click(matcher, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public <T extends Node> FxRobot rightClick(Predicate<T> predicate) {
        click(predicate, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public FxRobot doubleClick(MouseButton... buttons) {
        click(buttons).click().sleep(50);
        return this;
    }

    @Override
    public FxRobot doubleClick(double x, double y, MouseButton... buttons) {
        click(x, y, buttons).click().sleep(50);
        return this;
    }

    @Override
    public FxRobot doubleClick(Point2D point, MouseButton... buttons) {
        click(point, buttons).click().sleep(50);
        return this;
    }

    @Override
    public FxRobot doubleClick(Bounds bounds, MouseButton... buttons) {
        click(bounds, buttons).click().sleep(50);
        return this;
    }

    @Override
    public FxRobot doubleClick(Node node, MouseButton... buttons) {
        click(node, buttons).click().sleep(50);
        return this;
    }

    @Override
    public FxRobot doubleClick(Scene scene, MouseButton... buttons) {
        click(scene, buttons).click().sleep(50);
        return this;
    }

    @Override
    public FxRobot doubleClick(Window window, MouseButton... buttons) {
        click(window, buttons).click().sleep(50);
        return this;
    }

    @Override
    public FxRobot doubleClick(String query, MouseButton... buttons) {
        click(query, buttons).click().sleep(50);
        return this;
    }

    @Override
    public FxRobot doubleClick(Matcher<Object> matcher, MouseButton... buttons) {
        click(matcher, buttons).click().sleep(50);
        return this;
    }

    @Override
    public <T extends Node> FxRobot doubleClick(Predicate<T> predicate, MouseButton... buttons) {
        click(predicate, buttons).click().sleep(50);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF DRAG ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    public FxRobot drag(MouseButton... buttons) {
        mouseRobot.press(buttons);
        return this;
    }

    @Override
    public FxRobot drag(double x, double y, MouseButton... buttons) {
        moveTo(x, y);
        drag(buttons);
        return this;
    }

    @Override
    public FxRobot drag(Point2D point, MouseButton... buttons) {
        moveTo(point);
        drag(buttons);
        return this;
    }

    @Override
    public FxRobot drag(Bounds bounds, MouseButton... buttons) {
        moveTo(bounds);
        drag(buttons);
        return this;
    }

    @Override
    public FxRobot drag(Node node, MouseButton... buttons) {
        moveTo(node);
        drag(buttons);
        return this;
    }

    @Override
    public FxRobot drag(Scene scene, MouseButton... buttons) {
        moveTo(scene);
        drag(buttons);
        return this;
    }

    @Override
    public FxRobot drag(Window window, MouseButton... buttons) {
        moveTo(window);
        drag(buttons);
        return this;
    }

    @Override
    public FxRobot drag(String query, MouseButton... buttons) {
        moveTo(query);
        drag(buttons);
        return this;
    }

    @Override
    public FxRobot drag(Matcher<Object> matcher, MouseButton... buttons) {
        moveTo(matcher);
        drag(buttons);
        return this;
    }

    @Override
    public <T extends Node> FxRobot drag(Predicate<T> predicate, MouseButton... buttons) {
        moveTo(predicate);
        drag(buttons);
        return this;
    }

    @Override
    public FxRobot drop() {
        mouseRobot.release();
        return this;
    }

    @Override
    public FxRobot dropTo(double x, double y) {
        moveTo(x, y);
        drop();
        return this;
    }

    @Override
    public FxRobot dropTo(Point2D point) {
        moveTo(point);
        drop();
        return this;
    }

    @Override
    public FxRobot dropTo(Bounds bounds) {
        moveTo(bounds);
        drop();
        return this;
    }

    @Override
    public FxRobot dropTo(Node node) {
        moveTo(node);
        drop();
        return this;
    }

    @Override
    public FxRobot dropTo(Scene scene) {
        moveTo(scene);
        drop();
        return this;
    }

    @Override
    public FxRobot dropTo(Window window) {
        moveTo(window);
        drop();
        return this;
    }

    @Override
    public FxRobot dropTo(String query) {
        moveTo(query);
        drop();
        return this;
    }

    @Override
    public FxRobot dropTo(Matcher<Object> matcher) {
        moveTo(matcher);
        drop();
        return this;
    }

    @Override
    public <T extends Node> FxRobot dropTo(Predicate<T> predicate) {
        moveTo(predicate);
        drop();
        return this;
    }

    @Override
    public FxRobot dropBy(double x, double y) {
        moveBy(x, y);
        drop();
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF MOVE ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    public FxRobot moveTo(double x, double y) {
        moveToImpl(pointLocator.pointFor(new Point2D(x, y)));
        return this;
    }

    @Override
    public FxRobot moveTo(Point2D point) {
        moveToImpl(pointLocator.pointFor(point));
        return this;
    }

    @Override
    public FxRobot moveTo(Bounds bounds) {
        moveToImpl(pointLocator.pointFor(bounds));
        return this;
    }

    @Override
    public FxRobot moveTo(Node node) {
        moveToImpl(pointLocator.pointFor(node));
        return this;
    }

    @Override
    public FxRobot moveTo(Scene scene) {
        moveToImpl(pointLocator.pointFor(scene));
        return this;
    }

    @Override
    public FxRobot moveTo(Window window) {
        moveToImpl(pointLocator.pointFor(window));
        return this;
    }

    @Override
    public FxRobot moveTo(String query) {
        moveToImpl(pointFor(query));
        return this;
    }

    @Override
    public FxRobot moveTo(Matcher<Object> matcher) {
        moveToImpl(pointFor(matcher));
        return this;
    }

    @Override
    public <T extends Node> FxRobot moveTo(Predicate<T> predicate) {
        moveToImpl(pointFor(predicate));
        return this;
    }

    @Override
    public FxRobot moveBy(double x, double y) {
        Point2D mouseLocation = screenRobot.getMouseLocation();
        Point2D targetPoint = new Point2D(mouseLocation.getX() + x, mouseLocation.getY() + y);
        screenRobot.moveMouseLinearTo(targetPoint.getX(), targetPoint.getY());
        return this;
    }

    private void moveToImpl(PointQuery pointQuery) {
        // Since moving takes time, only do it if we're not already at the desired point.
        Point2D point = pointQuery.atPosition(pointPosition).query();
        if (!isPointAtMouseLocation(point)) {
            screenRobot.moveMouseLinearTo(point.getX(), point.getY());
        }

        // If the target has moved while we were moving the mouse, update to the new position.
        Point2D endPoint = pointQuery.atPosition(pointPosition).query();
        screenRobot.moveMouseTo(endPoint.getX(), endPoint.getY());
    }

    private boolean isPointAtMouseLocation(Point2D point) {
        Point2D mouseLocation = screenRobot.getMouseLocation();
        return mouseLocation.equals(point);
    }

}
