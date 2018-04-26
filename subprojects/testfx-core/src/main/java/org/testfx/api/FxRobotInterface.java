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

import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
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
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.finder.WindowFinder;
import org.testfx.service.query.BoundsQuery;
import org.testfx.service.query.NodeQuery;
import org.testfx.service.query.PointQuery;
import org.testfx.service.support.Capture;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Wrapper-like interface that makes it easier to chain together multiple robot methods while adding a number of
 * convenience methods, such as finding a given node, scene or window via a {@link PointQuery}, a {@link Predicate},
 * or a {@link Matcher}.
 */
public interface FxRobotInterface {

    /**
     * Calls {@link WindowFinder#targetWindow()} and returns itself for method chaining.
     */
    Window targetWindow();

    /**
     * Calls {@link WindowFinder#targetWindow(Window)} and returns itself for method chaining.
     */
    FxRobotInterface targetWindow(Window window);

    /**
     * Calls {@link WindowFinder#targetWindow(Predicate)} and returns itself for method chaining.
     */
    FxRobotInterface targetWindow(Predicate<Window> predicate);

    // Convenience methods:
    /**
     * Convenience method: Calls {@link WindowFinder#targetWindow(int)} and returns itself for method chaining.
     */
    FxRobotInterface targetWindow(int windowIndex);

    /**
     * Convenience method: Calls {@link WindowFinder#targetWindow(String)} and returns itself for method chaining.
     */
    FxRobotInterface targetWindow(String stageTitleRegex);

    /**
     * Convenience method: Calls {@link WindowFinder#targetWindow(Pattern)} and returns itself for method chaining.
     */
    FxRobotInterface targetWindow(Pattern stageTitlePattern);

    /**
     * Convenience method: Calls {@link WindowFinder#targetWindow(Scene)} and returns itself for method chaining.
     */
    FxRobotInterface targetWindow(Scene scene);

    /**
     * Convenience method: Calls {@link WindowFinder#targetWindow(Node)} and returns itself for method chaining.
     */
    FxRobotInterface targetWindow(Node node);

    /**
     * Calls {@link WindowFinder#listWindows()} ()} and returns itself for method chaining.
     */
    List<Window> listWindows();

    /**
     * Calls {@link WindowFinder#listTargetWindows()} and returns itself for method chaining.
     */
    List<Window> listTargetWindows();

    /**
     * Calls {@link WindowFinder#window(Predicate)} and returns itself for method chaining.
     */
    Window window(Predicate<Window> predicate);

    // Convenience methods:
    /**
     * Convenience method: Calls {@link WindowFinder#window(int)} and returns itself for method chaining.
     */
    Window window(int windowIndex);

    /**
     * Convenience method: Calls {@link WindowFinder#window(String)} and returns itself for method chaining.
     */
    Window window(String stageTitleRegex);

    /**
     * Convenience method: Calls {@link WindowFinder#window(Pattern)} and returns itself for method chaining.
     */
    Window window(Pattern stageTitlePattern);

    /**
     * Convenience method: Calls {@link WindowFinder#window(Scene)} and returns itself for method chaining.
     */
    Window window(Scene scene);

    /**
     * Convenience method: Calls {@link WindowFinder#window(Node)} and returns itself for method chaining.
     */
    Window window(Node node);

    /**
     * Calls {@link NodeFinder#fromAll()} and returns itself for method chaining.
     */
    NodeQuery fromAll();

    /**
     * Calls {@link NodeFinder#from(Node...)} and returns itself for method chaining.
     */
    NodeQuery from(Node... parentNodes);

    /**
     * Calls {@link NodeFinder#from(Collection)} and returns itself for method chaining.
     */
    NodeQuery from(Collection<Node> parentNodes);

    /**
     * Calls {@link NodeFinder#rootNode(Window)} and returns itself for method chaining.
     */
    Node rootNode(Window window);

    /**
     * Calls {@link NodeFinder#rootNode(Scene)} and returns itself for method chaining.
     */
    Node rootNode(Scene scene);

    /**
     * Calls {@link NodeFinder#rootNode(Node)} and returns itself for method chaining.
     */
    Node rootNode(Node node);

    // Convenience methods:
    /**
     * Convenience method: Calls {@link NodeFinder#lookup(String)} and returns itself for method chaining.
     */
    NodeQuery lookup(String query);

    /**
     * Convenience method: Calls {@link NodeFinder#lookup(Matcher)} and returns itself for method chaining.
     */
    <T extends Node> NodeQuery lookup(Matcher<T> matcher);

    /**
     * Convenience method: Calls {@link NodeFinder#lookup(Predicate)} and returns itself for method chaining.
     */
    <T extends Node> NodeQuery lookup(Predicate<T> predicate);

    /**
     * Convenience method: Calls {@link NodeFinder#from(NodeQuery)} and returns itself for method chaining.
     */
    NodeQuery from(NodeQuery nodeQuery);

    /**
     * Calls {@link org.testfx.util.BoundsQueryUtils#bounds(double, double, double, double)}
     */
    BoundsQuery bounds(double minX, double minY, double width, double height);

    /**
     * Calls {@link org.testfx.util.BoundsQueryUtils#bounds(Point2D)}
     */
    BoundsQuery bounds(Point2D point);

    /**
     * Creates a {@code BoundsQuery} that returns the given bounds
     */
    BoundsQuery bounds(Bounds bounds);

    /**
     * Calls {@link org.testfx.util.BoundsQueryUtils#boundsOnScreen(Node)}
     */
    BoundsQuery bounds(Node node);

    /**
     * Calls {@link org.testfx.util.BoundsQueryUtils#boundsOnScreen(Bounds, Scene)} with given scene's bounds
     */
    BoundsQuery bounds(Scene scene);

    /**
     * Calls {@link org.testfx.util.BoundsQueryUtils#boundsOnScreen(Bounds, Window)} with the given window's bounds
     */
    BoundsQuery bounds(Window window);

    /**
     * NOT YET IMPLEMENTED
     */
    BoundsQuery bounds(String query);

    /**
     * NOT YET IMPLEMENTED
     */
    <T extends Node> BoundsQuery bounds(Matcher<T> matcher);

    /**
     * NOT YET IMPLEMENTED
     */
    <T extends Node> BoundsQuery bounds(Predicate<T> predicate);

    /**
     * Stores the given position as the position to be used in all {@code point()}-related methods
     * such as {@link #point(Node)} and {@link #point(Point2D)}, and returns itself for method chaining.
     * The default value is {@link Pos#CENTER}
     */
    FxRobotInterface targetPos(Pos pointPosition);

    /**
     * Calls {@link org.testfx.service.locator.PointLocator#point(Point2D)} using {@code new Point2D(x, y)} and sets
     * the {@code PointQuery}'s {@link PointQuery#getPosition()} to {@link FxRobotContext#getPointPosition()}.
     */
    PointQuery point(double x, double y);

    /**
     * Calls {@link org.testfx.service.locator.PointLocator#point(Point2D)} and sets the {@code PointQuery}'s
     * {@link PointQuery#getPosition()} to {@link FxRobotContext#getPointPosition()}.
     */
    PointQuery point(Point2D point);

    /**
     * Calls {@link org.testfx.service.locator.PointLocator#point(Bounds)} and sets the {@code PointQuery}'s
     * {@link PointQuery#getPosition()} to {@link FxRobotContext#getPointPosition()}.
     */
    PointQuery point(Bounds bounds);

    /**
     * Calls {@link org.testfx.service.locator.PointLocator#point(Node)} and sets the {@code PointQuery}'s
     * {@link PointQuery#getPosition()} to {@link FxRobotContext#getPointPosition()}.
     */
    PointQuery point(Node node);

    /**
     * Calls {@link org.testfx.service.locator.PointLocator#point(Scene)} and sets the {@code PointQuery}'s
     * {@link PointQuery#getPosition()} to {@link FxRobotContext#getPointPosition()}.
     */
    PointQuery point(Scene scene);

    /**
     * Calls {@link org.testfx.service.locator.PointLocator#point(Window)} and sets the {@code PointQuery}'s
     * {@link PointQuery#getPosition()} to {@link FxRobotContext#getPointPosition()}.
     */
    PointQuery point(Window window);

    /**
     * Convenience method: Tries to find a given node via {@link #lookup(String)} before calling {@link #point(Node)},
     * throwing a {@link FxRobotException} if no node is found.
     */
    PointQuery point(String query);

    /**
     * Convenience method: Tries to find a given node via {@link #lookup(Matcher)} before calling {@link #point(Node)},
     * throwing a {@link FxRobotException} if no node is found.
     */
    <T extends Node> PointQuery point(Matcher<T> matcher);

    /**
     * Convenience method: Tries to find a given node via {@link #lookup(Predicate)} before calling
     * {@link #point(Node)}, throwing a {@link FxRobotException} if no node is found.
     */
    <T extends Node> PointQuery point(Predicate<T> predicate);

    /**
     * Convenience method: Calls {@link #point(Point2D)} and sets the query's offset by the given offset values.
     */
    PointQuery offset(Point2D point, double offsetX, double offsetY);

    /**
     * Convenience method: Calls {@link #point(Point2D)} and sets the query's offset by the given offset point
     * (where the point's x-component is the offsetX, and the point's y-component is the offsetY).
     */
    default PointQuery offset(Point2D point, Point2D offset) {
        return offset(point, offset.getX(), offset.getY());
    }

    /**
     * Convenience method: Calls {@link #point(Bounds)} and sets the query's offset by the given offset values.
     */
    PointQuery offset(Bounds bounds, double offsetX, double offsetY);

    /**
     * Convenience method: Calls {@link #point(Bounds)} and sets the query's offset by the given offset point
     * (where the point's x-component is the offsetX, and the point's y-component is the offsetY).
     */
    default PointQuery offset(Bounds bounds, Point2D offset) {
        return offset(bounds, offset.getX(), offset.getY());
    }

    /**
     * Convenience method: Calls {@link #point(Node)} and sets the query's offset by the given offset values.
     */
    PointQuery offset(Node node, double offsetX, double offsetY);

    /**
     * Convenience method: Calls {@link #point(Node)} and sets the query's offset by the given offset point
     * (where the point's x-component is the offsetX, and the point's y-component is the offsetY).
     */
    default PointQuery offset(Node node, Point2D offset) {
        return offset(node, offset.getX(), offset.getY());
    }

    /**
     * Convenience method: Calls {@link #point(Node)} and sets the query's offset by the given offset values
     * where the offset is computed with respect to the given offset reference position.
     */
    PointQuery offset(Node node, Pos offsetReferencePos, double offsetX, double offsetY);

    /**
     * Convenience method: Calls {@link #point(Node)} and sets the query's offset by the given offset point
     * (where the point's x-component is the offsetX, and the point's y-component is the offsetY) where the
     * offset is computed with respect to the given offset reference position.
     */
    default PointQuery offset(Node node, Pos offsetReferencePos, Point2D offset) {
        return offset(node, offsetReferencePos, offset.getX(), offset.getY());
    }

    /**
     * Convenience method: Calls {@link #point(Scene)} and sets the query's offset by the given offset values.
     */
    PointQuery offset(Scene scene, double offsetX, double offsetY);

    /**
     * Convenience method: Calls {@link #point(Scene)} and sets the query's offset by the given offset offset point
     * (where the point's x-component is the offsetX, and the point's y-component is the offsetY).
     */
    default PointQuery offset(Scene scene, Point2D offset) {
        return offset(scene, offset.getX(), offset.getY());
    }

    /**
     * Convenience method: Calls {@link #point(Window)} and sets the query's offset by the given offset values.
     */
    PointQuery offset(Window window, double offsetX, double offsetY);

    /**
     * Convenience method: Calls {@link #point(Window)} and sets the query's offset by the given offset point
     * (where the point's x-component is the offsetX, and the point's y-component is the offsetY).
     */
    default PointQuery offset(Window window, Point2D offset) {
        return offset(window, offset.getX(), offset.getY());
    }

    /**
     * Convenience method: Calls {@link #point(String)} and sets the query's offset by the given offset values.
     */
    PointQuery offset(String query, double offsetX, double offsetY);

    /**
     * Convenience method: Calls {@link #point(String)} and sets the query's offset by the given offset point
     * (where the point's x-component is the offsetX, and the point's y-component is the offsetY).
     */
    default PointQuery offset(String query, Point2D offset) {
        return offset(query, offset.getX(), offset.getY());
    }

    /**
     * Convenience method: Calls {@link #point(Matcher)} and sets the query's offset by the given offset values.
     */
    <T extends Node> PointQuery offset(Matcher<T> matcher, double offsetX, double offsetY);

    /**
     * Convenience method: Calls {@link #point(Matcher)} and sets the query's offset by the given offset point
     * (where the point's x-component is the offsetX, and the point's y-component is the offsetY).
     */
    default <T extends Node> PointQuery offset(Matcher<T> matcher, Point2D offset) {
        return offset(matcher, offset.getX(), offset.getY());
    }

    /**
     * Convenience method: Calls {@link #point(Predicate)} and sets the query's offset by the given offset values.
     */
    <T extends Node> PointQuery offset(Predicate<T> predicate, double offsetX, double offsetY);

    /**
     * Convenience method: Calls {@link #point(Predicate)} and sets the query's offset by the given offset point
     * (where the point's x-component is the offsetX, and the point's y-component is the offsetY).
     */
    default <T extends Node> PointQuery offset(Predicate<T> predicate, Point2D offset) {
        return offset(predicate, offset.getX(), offset.getY());
    }

    /**
     * Returns a {@link Capture} that supplies a screenshot using the given rectangle's bounds.
     */
    Capture capture(Rectangle2D screenRegion);

    /**
     * Returns a {@link Capture} that supplies a screenshot using the given bounds.
     */
    Capture capture(Bounds bounds);

    /**
     * Returns a {@link Capture} that supplies a screenshot using the given node's bounds.
     */
    Capture capture(Node node);

    // Convenience methods:
    /**
     * Convenience method: Returns a {@link Capture} that supplies the given {@link Image}.
     */
    Capture capture(Image image);

    /**
     * Convenience method: Returns a {@link Capture} that supplies the {@link Image} from the image file of the
     * given {@link Path}.
     */
    Capture capture(Path path);

    /**
     * Convenience method: Returns a {@link Capture} that supplies the {@link Image} from the image file of the
     * given {@link URL}.
     */
    Capture capture(URL url);

    /**
     * Calls a runnable on the FX application thread and waits for it and
     * consecutive events to execute. So changes to the gui triggered by the
     * runnable will be performed when returned from this method.
     *
     * @param runnable the runnable
     * @return this robot
     */
    FxRobotInterface interact(Runnable runnable);

    /**
     * Calls a callable on the FX application thread and waits for it and
     * consecutive events to execute. So changes to the gui triggered by the
     * callable will be performed when returned from this method.
     *
     * @param callable the callable
     * @return this robot
     */
    <T> FxRobotInterface interact(Callable<T> callable);

    /**
     * Calls a runnable on the FX application thread and waits for it to
     * execute. It does not wait for other events on the fx application thread.
     * So changes to the gui triggered by the runnable may not be performed when
     * returned from this method.
     *
     * @param runnable the runnable
     * @return this robot
     */
    FxRobotInterface interactNoWait(Runnable runnable);

    /**
     * Calls a callable on the FX application thread and waits for it to
     * execute. It does not wait for other events on the fx application thread.
     * So changes to the gui triggered by the callable may not be performed when
     * returned from this method.
     *
     * @param callable the callable
     * @return this robot
     */
    <T> FxRobotInterface interactNoWait(Callable<T> callable);

    /**
     * Calls {@link WaitForAsyncUtils#waitForFxEvents()} and returns itself for method chaining.
     */
    FxRobotInterface interrupt();

    /**
     * Calls {@link WaitForAsyncUtils#waitForFxEvents(int)} and returns itself for method chaining.
     */
    FxRobotInterface interrupt(int attemptsCount);

    /**
     * Calls {@link org.testfx.robot.SleepRobot#sleep(long)} and returns itself for more method chaining.
     */
    FxRobotInterface sleep(long milliseconds);

    /**
     * Calls {@link org.testfx.robot.SleepRobot#sleep(long, TimeUnit)} and returns itself for more method chaining.
     */
    FxRobotInterface sleep(long duration, TimeUnit timeUnit);

    /**
     * Calls {@link org.testfx.robot.ClickRobot#clickOn(MouseButton...)} and returns itself for more method chaining.
     */
    FxRobotInterface clickOn(MouseButton... buttons);

    /**
     * Calls {@link org.testfx.robot.ClickRobot#clickOn(PointQuery, Motion, MouseButton...)} and returns itself for
     * more method chaining.
     */
    default FxRobotInterface clickOn(PointQuery pointQuery, MouseButton... buttons) {
        return clickOn(pointQuery, Motion.DEFAULT, buttons);
    }

    /**
     * Calls {@link org.testfx.robot.ClickRobot#clickOn(PointQuery, MouseButton...)} and returns itself for more method
     * chaining.
     */
    FxRobotInterface clickOn(PointQuery pointQuery, Motion motion, MouseButton... buttons);

    /**
     * Calls {@link org.testfx.robot.ClickRobot#doubleClickOn(MouseButton...)} and returns itself for more method
     * chaining.
     */
    FxRobotInterface doubleClickOn(MouseButton... buttons);

    /**
     * Calls {@link org.testfx.robot.ClickRobot#doubleClickOn(PointQuery, Motion, MouseButton...)} and returns itself
     * for method chaining.
     */
    default FxRobotInterface doubleClickOn(PointQuery pointQuery, MouseButton... buttons) {
        return doubleClickOn(pointQuery, Motion.DEFAULT, buttons);
    }

    /**
     * Calls {@link org.testfx.robot.ClickRobot#doubleClickOn(PointQuery, Motion, MouseButton...)} and returns itself
     * for method chaining.
     */
    FxRobotInterface doubleClickOn(PointQuery pointQuery, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the given coordinates, clicks the given buttons, and returns itself
     * for method chaining.
     */
    default FxRobotInterface clickOn(double x, double y, MouseButton... buttons) {
        return clickOn(x, y, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the given coordinates,
     * clicks the given buttons, and returns itself for method chaining.
     */
    FxRobotInterface clickOn(double x, double y, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the given point, clicks the given buttons, and returns itself for
     * method chaining.
     */
    default FxRobotInterface clickOn(Point2D point, MouseButton... buttons) {
        return clickOn(point, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the given point, clicks
     * the given buttons, and returns itself for method chaining.
     */
    FxRobotInterface clickOn(Point2D point, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Bounds)}, clicks the given
     * buttons, and returns itself for method chaining.
     */
    default FxRobotInterface clickOn(Bounds bounds, MouseButton... buttons) {
        return clickOn(bounds, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned
     * from {@link #point(Bounds)}, clicks the given buttons, and returns itself for method chaining.
     */
    FxRobotInterface clickOn(Bounds bounds, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Node)}, clicks the given
     * buttons, and returns itself for method chaining.
     */
    default FxRobotInterface clickOn(Node node, MouseButton... buttons) {
        return clickOn(node, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Node)}, clicks the given buttons, and returns itself for method chaining.
     */
    FxRobotInterface clickOn(Node node, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Scene)}, clicks the given
     * buttons, and returns itself for method chaining.
     */
    default FxRobotInterface clickOn(Scene scene, MouseButton... buttons) {
        return clickOn(scene, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Scene)}, clicks the given buttons, and returns itself for method chaining.
     */
    FxRobotInterface clickOn(Scene scene, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Window)}, clicks the given
     * buttons, and returns itself for method chaining.
     */
    default FxRobotInterface clickOn(Window window, MouseButton... buttons) {
        return clickOn(window, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Window)}, clicks the given buttons, and returns itself for method chaining.
     */
    FxRobotInterface clickOn(Window window, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(String)}, clicks the given
     * buttons, and returns itself for method chaining.
     */
    default FxRobotInterface clickOn(String query, MouseButton... buttons) {
        return clickOn(query, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(String)}, clicks the given buttons, and returns itself for method chaining.
     */
    FxRobotInterface clickOn(String query, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Matcher)}, clicks the given
     * buttons, and returns itself for method chaining.
     */
    default <T extends Node> FxRobotInterface clickOn(Matcher<T> matcher, MouseButton... buttons) {
        return clickOn(matcher, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Matcher)}, clicks the given buttons, and returns itself for method chaining.
     */
    <T extends Node> FxRobotInterface clickOn(Matcher<T> matcher, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Predicate)}, clicks the given
     * buttons, and returns itself for method chaining.
     */
    default <T extends Node> FxRobotInterface clickOn(Predicate<T> predicate, MouseButton... buttons) {
        return clickOn(predicate, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Predicate)}, clicks the given buttons, and returns itself for method chaining.
     */
    <T extends Node> FxRobotInterface clickOn(Predicate<T> predicate, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Clicks the {@link MouseButton#SECONDARY} button and returns itself for method chaining.
     */
    FxRobotInterface rightClickOn();

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link PointQuery#query()}, clicks
     * the {@link MouseButton#SECONDARY} button and returns itself for method chaining.
     */
    default FxRobotInterface rightClickOn(PointQuery pointQuery) {
        return rightClickOn(pointQuery, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link PointQuery#query()}, clicks the {@link MouseButton#SECONDARY} button and returns itself for method
     * chaining.
     */
    FxRobotInterface rightClickOn(PointQuery pointQuery, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the given coordinates, clicks the {@link MouseButton#SECONDARY}
     * button, and returns itself for method chaining.
     */
    default FxRobotInterface rightClickOn(double x, double y) {
        return rightClickOn(x, y, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the given coordinates,
     * clicks the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    FxRobotInterface rightClickOn(double x, double y, Motion motion);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Point2D)}, clicks
     * the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    default FxRobotInterface rightClickOn(Point2D point) {
        return rightClickOn(point, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Point2D)}, clicks the {@link MouseButton#SECONDARY} button, and returns itself for method
     * chaining.
     */
    FxRobotInterface rightClickOn(Point2D point, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Bounds)}, clicks
     * the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    default FxRobotInterface rightClickOn(Bounds bounds) {
        return rightClickOn(bounds, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Bounds)}, clicks the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    FxRobotInterface rightClickOn(Bounds bounds, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Node)}, clicks
     * the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    default FxRobotInterface rightClickOn(Node node) {
        return rightClickOn(node, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Node)}, clicks the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    FxRobotInterface rightClickOn(Node node, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Scene)}, clicks
     * the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    default FxRobotInterface rightClickOn(Scene scene) {
        return rightClickOn(scene, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Scene)}, clicks the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    FxRobotInterface rightClickOn(Scene scene, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Window)}, clicks
     * the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    default FxRobotInterface rightClickOn(Window window) {
        return rightClickOn(window, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Window)}, clicks the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    FxRobotInterface rightClickOn(Window window, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(String)}, clicks
     * the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    default FxRobotInterface rightClickOn(String query) {
        return rightClickOn(query, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(String)}, clicks the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    FxRobotInterface rightClickOn(String query, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Matcher)}, clicks
     * the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    default <T extends Node> FxRobotInterface rightClickOn(Matcher<T> matcher) {
        return rightClickOn(matcher, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Matcher)}, clicks the {@link MouseButton#SECONDARY} button, and returns itself for method
     * chaining.
     */
    <T extends Node> FxRobotInterface rightClickOn(Matcher<T> matcher, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Predicate)}, clicks
     * the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    default <T extends Node> FxRobotInterface rightClickOn(Predicate<T> predicate) {
        return rightClickOn(predicate, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Predicate)}, clicks the {@link MouseButton#SECONDARY} button, and returns itself for method
     * chaining.
     */
    <T extends Node> FxRobotInterface rightClickOn(Predicate<T> predicate, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(double, double)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    default FxRobotInterface doubleClickOn(double x, double y, MouseButton... buttons) {
        return doubleClickOn(x, y, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(double, double)}, double clicks the given buttons, and returns itself for method chaining.
     */
    FxRobotInterface doubleClickOn(double x, double y, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Point2D)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    default FxRobotInterface doubleClickOn(Point2D point, MouseButton... buttons) {
        return doubleClickOn(point, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Point2D)}, double clicks the given buttons, and returns itself for method chaining.
     */
    FxRobotInterface doubleClickOn(Point2D point, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Bounds)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    default FxRobotInterface doubleClickOn(Bounds bounds, MouseButton... buttons) {
        return doubleClickOn(bounds, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Bounds)}, double clicks the given buttons, and returns itself for method chaining.
     */
    FxRobotInterface doubleClickOn(Bounds bounds, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Node)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    default FxRobotInterface doubleClickOn(Node node, MouseButton... buttons) {
        return doubleClickOn(node, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Node)}, double clicks the given buttons, and returns itself for method chaining.
     */
    FxRobotInterface doubleClickOn(Node node, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Scene)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    default FxRobotInterface doubleClickOn(Scene scene, MouseButton... buttons) {
        return doubleClickOn(scene, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Scene)}, double clicks the given buttons, and returns itself for method chaining.
     */
    FxRobotInterface doubleClickOn(Scene scene, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Window)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    default FxRobotInterface doubleClickOn(Window window, MouseButton... buttons) {
        return doubleClickOn(window, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Window)}, double clicks the given buttons, and returns itself for method chaining.
     */
    FxRobotInterface doubleClickOn(Window window, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(String)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    default FxRobotInterface doubleClickOn(String query, MouseButton... buttons) {
        return doubleClickOn(query, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(String)}, double clicks the given buttons, and returns itself for method chaining.
     */
    FxRobotInterface doubleClickOn(String query, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Matcher)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    default <T extends Node> FxRobotInterface doubleClickOn(Matcher<T> matcher, MouseButton... buttons) {
        return doubleClickOn(matcher, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Matcher)}, double clicks the given buttons, and returns itself for method chaining.
     */
    <T extends Node> FxRobotInterface doubleClickOn(Matcher<T> matcher, Motion motion, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Predicate)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    default <T extends Node> FxRobotInterface doubleClickOn(Predicate<T> predicate, MouseButton... buttons) {
        return doubleClickOn(predicate, Motion.DEFAULT, buttons);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Predicate)}, double clicks the given buttons, and returns itself for method chaining.
     */
    <T extends Node> FxRobotInterface doubleClickOn(Predicate<T> predicate, Motion motion, MouseButton... buttons);

    /**
     * Calls {@link org.testfx.robot.DragRobot#drag(MouseButton...)} and returns itself for more method chaining.
     */
    FxRobotInterface drag(MouseButton... buttons);

    /**
     * Calls {@link org.testfx.robot.DragRobot#drag(PointQuery, MouseButton...)} and returns itself for more method
     * chaining.
     */
    FxRobotInterface drag(PointQuery pointQuery, MouseButton... buttons);

    /**
     * Calls {@link org.testfx.robot.DragRobot#drop()} and returns itself for more method chaining.
     */
    FxRobotInterface drop();

    /**
     * Calls {@link org.testfx.robot.DragRobot#dropTo(PointQuery)} and returns itself for more method chaining.
     */
    FxRobotInterface dropTo(PointQuery pointQuery);

    /**
     * Calls {@link org.testfx.robot.DragRobot#dropBy(double, double)} and returns itself for more method chaining.
     */
    FxRobotInterface dropBy(double x, double y);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(double, double)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    FxRobotInterface drag(double x, double y, MouseButton... buttons);
    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Point2D)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    FxRobotInterface drag(Point2D point, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Bounds)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    FxRobotInterface drag(Bounds bounds, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Node)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    FxRobotInterface drag(Node node, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Scene)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    FxRobotInterface drag(Scene scene, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Window)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    FxRobotInterface drag(Window window, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(String)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    FxRobotInterface drag(String query, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Matcher)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    <T extends Node> FxRobotInterface drag(Matcher<T> matcher, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Predicate)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    <T extends Node> FxRobotInterface drag(Predicate<T> predicate, MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(double, double)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    FxRobotInterface dropTo(double x, double y);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Point2D)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    FxRobotInterface dropTo(Point2D point);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Bounds)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    FxRobotInterface dropTo(Bounds bounds);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Node)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    FxRobotInterface dropTo(Node node);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Scene)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    FxRobotInterface dropTo(Scene scene);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Window)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    FxRobotInterface dropTo(Window window);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(String)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    FxRobotInterface dropTo(String query);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Matcher)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    <T extends Node> FxRobotInterface dropTo(Matcher<T> matcher);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Predicate)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    <T extends Node> FxRobotInterface dropTo(Predicate<T> predicate);

    /**
     * Calls {@link org.testfx.robot.KeyboardRobot#press(KeyCode...)} and returns itself for method chaining.
     */
    FxRobotInterface press(KeyCode... keys);

    /**
     * Calls {@link org.testfx.robot.KeyboardRobot#press(KeyCode...)} and returns itself for method chaining.
     */
    FxRobotInterface release(KeyCode... keys);

    /**
     * Presses and holds mouse buttons.
     *
     * @param buttons mouse buttons to press, defaults to primary mouse button.
     */
    FxRobotInterface press(MouseButton... buttons);

    /**
     * Releases pressed mouse buttons.
     *
     * @param buttons mouse buttons to release, defaults to all pressed mouse buttons.
     */
    FxRobotInterface release(MouseButton... buttons);

    /**
     * Calls {@link org.testfx.robot.MoveRobot#moveTo(PointQuery)} and returns itself for more method chaining.
     */
    default FxRobotInterface moveTo(PointQuery pointQuery) {
        return moveTo(pointQuery, Motion.DEFAULT);
    }

    /**
     * Calls {@link org.testfx.robot.MoveRobot#moveTo(PointQuery, Motion)} and returns itself for more method chaining.
     */
    FxRobotInterface moveTo(PointQuery pointQuery, Motion motion);

    /**
     * Calls {@link org.testfx.robot.MoveRobot#moveBy(double, double)} and returns itself for more method chaining.
     */
    default FxRobotInterface moveBy(double x, double y) {
        return moveBy(x, y, Motion.DEFAULT);
    }

    /**
     * Calls {@link org.testfx.robot.MoveRobot#moveBy(double, double, Motion)} and returns itself for more method
     * chaining.
     */
    FxRobotInterface moveBy(double x, double y, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(double, double)} and returns
     * itself for method chaining.
     */
    default FxRobotInterface moveTo(double x, double y) {
        return moveTo(x, y, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(double, double)} and returns itself for method chaining.
     */
    FxRobotInterface moveTo(double x, double y, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Point2D)} and returns itself
     * for method chaining.
     */
    default FxRobotInterface moveTo(Point2D point) {
        return moveTo(point, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Point2D)} and returns itself for method chaining.
     */
    FxRobotInterface moveTo(Point2D point, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the center of the given {@code Bounds} and returns itself for
     * method chaining.
     */
    default FxRobotInterface moveTo(Bounds bounds) {
        return moveTo(bounds, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Bounds)} and returns itself for method chaining.
     */
    FxRobotInterface moveTo(Bounds bounds, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the center of the given {@code Node} and returns itself
     * for method chaining.
     */
    default FxRobotInterface moveTo(Node node) {
        return moveTo(node, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the center of the
     * given {@code Node} and returns itself for method chaining.
     */
    default FxRobotInterface moveTo(Node node, Motion motion) {
        return moveTo(node, Pos.CENTER, Point2D.ZERO, motion);
    }

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Node)},
     * with the given offset from the center of the given {@code Node}, and returns itself for method chaining.
     */
    default FxRobotInterface moveTo(Node node, Point2D offset) {
        return moveTo(node, Pos.CENTER, offset, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Node)}, with the given offset (from the {@code offsetReferencePos}, and returns itself for
     * method chaining.
     */
    FxRobotInterface moveTo(Node node, Pos offsetReferencePos, Point2D offset, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the center of the given {@code Scene} and returns itself
     * for method chaining.
     */
    default FxRobotInterface moveTo(Scene scene) {
        return moveTo(scene, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Scene)} and returns itself for method chaining.
     */
    FxRobotInterface moveTo(Scene scene, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the center of the given {@code Window} and returns itself
     * for method chaining.
     */
    default FxRobotInterface moveTo(Window window) {
        return moveTo(window, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Window)} and returns itself for method chaining.
     */
    FxRobotInterface moveTo(Window window, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(String)} and returns itself
     * for method chaining.
     */
    default FxRobotInterface moveTo(String query) {
        return moveTo(query, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(String)} and returns itself for method chaining.
     */
    FxRobotInterface moveTo(String query, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Matcher)} and returns itself
     * for method chaining.
     */
    default <T extends Node> FxRobotInterface moveTo(Matcher<T> matcher) {
        return moveTo(matcher, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Matcher)} and returns itself for method chaining.
     */
    <T extends Node> FxRobotInterface moveTo(Matcher<T> matcher, Motion motion);

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(Predicate)} and returns itself
     * for method chaining.
     */
    default <T extends Node> FxRobotInterface moveTo(Predicate<T> predicate) {
        return moveTo(predicate, Motion.DEFAULT);
    }

    /**
     * Convenience method: Moves mouse using the given {@code motion} (see: {@link Motion} to the point returned from
     * {@link #point(Predicate)} and returns itself for method chaining.
     */
    <T extends Node> FxRobotInterface moveTo(Predicate<T> predicate, Motion motion);

    /**
     * Calls {@link org.testfx.robot.ScrollRobot#scroll(int)} and returns itself for more method chaining.
     */
    FxRobotInterface scroll(int amount);

    /**
     * Calls {@link org.testfx.robot.ScrollRobot#scroll(int, VerticalDirection)} and returns itself for more method
     * chaining.
     */
    FxRobotInterface scroll(int amount, VerticalDirection direction);

    // Convenience methods:
    /**
     * Calls {@link org.testfx.robot.ScrollRobot#scroll(int, VerticalDirection)} with arguments {@code 1} and
     * {@code direction} and returns itself for more method chaining.
     */
    FxRobotInterface scroll(VerticalDirection direction);

    /**
     * Calls {@link org.testfx.robot.ScrollRobot#scroll(int, HorizontalDirection)} and returns itself for more method
     * chaining.
     */
    FxRobotInterface scroll(int amount, HorizontalDirection direction);

    /**
     * Calls {@link org.testfx.robot.ScrollRobot#scroll(int, VerticalDirection)} with arguments {@code 1} and
     * {@code direction} and returns itself for more method chaining.
     */
    FxRobotInterface scroll(HorizontalDirection direction);

    /**
     * Calls {@link org.testfx.robot.TypeRobot#push(KeyCode...)} and returns itself for more method chaining.
     */
    FxRobotInterface push(KeyCode... combination);

    /**
     * Calls {@link org.testfx.robot.TypeRobot#push(KeyCodeCombination)} and returns itself for more method chaining.
     */
    FxRobotInterface push(KeyCodeCombination combination);

    /**
     * Calls {@link org.testfx.robot.TypeRobot#type(KeyCode...)} and returns itself for more method chaining.
     */
    FxRobotInterface type(KeyCode... keys);

    /**
     * Calls {@link org.testfx.robot.TypeRobot#type(KeyCode, int)} and returns itself for more method chaining.
     */
    FxRobotInterface type(KeyCode key, int times);

    /**
     * Convenience method: Calls {@link org.testfx.robot.TypeRobot#type(KeyCode, int)} with {@link KeyCode#BACK_SPACE}
     * and returns itself for more method chaining.
     */
    FxRobotInterface eraseText(int characters);

    /**
     * Calls {@link org.testfx.robot.WriteRobot#write(char)} and returns itself for more method chaining.
     */
    FxRobotInterface write(char character);

    /**
     * Calls {@link org.testfx.robot.WriteRobot#write(String)} and returns itself for more method chaining.
     */
    FxRobotInterface write(String text);

    /**
     * Calls {@link org.testfx.robot.WriteRobot#write(String, int)} and returns itself for more method chaining.
     */
    FxRobotInterface write(String text, int sleepMillis);

}
