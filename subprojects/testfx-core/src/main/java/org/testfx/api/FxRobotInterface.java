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
import org.testfx.api.annotation.Unstable;
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
@Unstable(reason = "interface was recently added")
public interface FxRobotInterface {

    //---------------------------------------------------------------------------------------------
    // METHODS FOR WINDOW TARGETING.
    //---------------------------------------------------------------------------------------------

    /**
     * Calls {@link WindowFinder#targetWindow()} and returns itself for method chaining.
     */
    public Window targetWindow();

    /**
     * Calls {@link WindowFinder#targetWindow(Window)} and returns itself for method chaining.
     */
    public FxRobotInterface targetWindow(Window window);

    /**
     * Calls {@link WindowFinder#targetWindow(Predicate)} and returns itself for method chaining.
     */
    public FxRobotInterface targetWindow(Predicate<Window> predicate);

    // Convenience methods:
    /**
     * Convenience method: Calls {@link WindowFinder#targetWindow(int)} and returns itself for method chaining.
     */
    public FxRobotInterface targetWindow(int windowIndex);

    /**
     * Convenience method: Calls {@link WindowFinder#targetWindow(String)} and returns itself for method chaining.
     */
    public FxRobotInterface targetWindow(String stageTitleRegex);

    /**
     * Convenience method: Calls {@link WindowFinder#targetWindow(Pattern)} and returns itself for method chaining.
     */
    public FxRobotInterface targetWindow(Pattern stageTitlePattern);

    /**
     * Convenience method: Calls {@link WindowFinder#targetWindow(Scene)} and returns itself for method chaining.
     */
    public FxRobotInterface targetWindow(Scene scene);

    /**
     * Convenience method: Calls {@link WindowFinder#targetWindow(Node)} and returns itself for method chaining.
     */
    public FxRobotInterface targetWindow(Node node);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR WINDOW LOOKUP.
    //---------------------------------------------------------------------------------------------

    /**
     * Calls {@link WindowFinder#listWindows()} ()} and returns itself for method chaining.
     */
    public List<Window> listWindows();

    /**
     * Calls {@link WindowFinder#listTargetWindows()} and returns itself for method chaining.
     */
    public List<Window> listTargetWindows();

    /**
     * Calls {@link WindowFinder#window(Predicate)} and returns itself for method chaining.
     */
    public Window window(Predicate<Window> predicate);

    // Convenience methods:
    /**
     * Convenience method: Calls {@link WindowFinder#window(int)} and returns itself for method chaining.
     */
    public Window window(int windowIndex);

    /**
     * Convenience method: Calls {@link WindowFinder#window(String)} and returns itself for method chaining.
     */
    public Window window(String stageTitleRegex);

    /**
     * Convenience method: Calls {@link WindowFinder#window(Pattern)} and returns itself for method chaining.
     */
    public Window window(Pattern stageTitlePattern);

    /**
     * Convenience method: Calls {@link WindowFinder#window(Scene)} and returns itself for method chaining.
     */
    public Window window(Scene scene);

    /**
     * Convenience method: Calls {@link WindowFinder#window(Node)} and returns itself for method chaining.
     */
    public Window window(Node node);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR NODE LOOKUP.
    //---------------------------------------------------------------------------------------------

    /**
     * Calls {@link NodeFinder#fromAll()} and returns itself for method chaining.
     */
    public NodeQuery fromAll();

    /**
     * Calls {@link NodeFinder#from(Node...)} and returns itself for method chaining.
     */
    public NodeQuery from(Node... parentNodes);

    /**
     * Calls {@link NodeFinder#from(Collection)} and returns itself for method chaining.
     */
    public NodeQuery from(Collection<Node> parentNodes);

    /**
     * Calls {@link NodeFinder#rootNode(Window)} and returns itself for method chaining.
     */
    public Node rootNode(Window window);

    /**
     * Calls {@link NodeFinder#rootNode(Scene)} and returns itself for method chaining.
     */
    public Node rootNode(Scene scene);

    /**
     * Calls {@link NodeFinder#rootNode(Node)} and returns itself for method chaining.
     */
    public Node rootNode(Node node);

    // Convenience methods:
    /**
     * Convenience method: Calls {@link NodeFinder#lookup(String)} and returns itself for method chaining.
     */
    public NodeQuery lookup(String query);

    /**
     * Convenience method: Calls {@link NodeFinder#lookup(Matcher)} and returns itself for method chaining.
     */
    public <T extends Node> NodeQuery lookup(Matcher<T> matcher);

    /**
     * Convenience method: Calls {@link NodeFinder#lookup(Predicate)} and returns itself for method chaining.
     */
    public <T extends Node> NodeQuery lookup(Predicate<T> predicate);

    /**
     * Convenience method: Calls {@link NodeFinder#from(NodeQuery)} and returns itself for method chaining.
     */
    public NodeQuery from(NodeQuery nodeQuery);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR BOUNDS LOCATION.
    //---------------------------------------------------------------------------------------------

    /**
     * Calls {@link org.testfx.util.BoundsQueryUtils#bounds(double, double, double, double)}
     */
    public BoundsQuery bounds(double minX,
                              double minY,
                              double width,
                              double height);

    /**
     * Calls {@link org.testfx.util.BoundsQueryUtils#bounds(Point2D)}
     */
    public BoundsQuery bounds(Point2D point);

    /**
     * Creates a {@code BoundsQuery} that returns the given bounds
     */
    public BoundsQuery bounds(Bounds bounds);

    /**
     * Calls {@link org.testfx.util.BoundsQueryUtils#boundsOnScreen(Node)}
     */
    public BoundsQuery bounds(Node node);

    /**
     * Calls {@link org.testfx.util.BoundsQueryUtils#boundsOnScreen(Bounds, Scene)} with given scene's bounds
     */
    public BoundsQuery bounds(Scene scene);

    /**
     * Calls {@link org.testfx.util.BoundsQueryUtils#boundsOnScreen(Bounds, Window)} with the given window's bounds
     */
    public BoundsQuery bounds(Window window);

    // Convenience methods:
    /**
     * NOT YET IMPLEMENTED
     */
    public BoundsQuery bounds(String query);

    /**
     * NOT YET IMPLEMENTED
     */
    public <T extends Node> BoundsQuery bounds(Matcher<T> matcher);

    /**
     * NOT YET IMPLEMENTED
     */
    public <T extends Node> BoundsQuery bounds(Predicate<T> predicate);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT POSITION.
    //---------------------------------------------------------------------------------------------

    /**
     * Stores the given position as the position to be used in all {@code point()}-related methods
     * such as {@link #point(Node)} and {@link #point(Point2D)}, and returns itself for method chaining.
     * The default value is {@link Pos#CENTER}
     */
    public FxRobotInterface targetPos(Pos pointPosition);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT LOCATION.
    //---------------------------------------------------------------------------------------------

    /**
     * Calls {@link org.testfx.service.locator.PointLocator#point(Point2D)} using {@code new Point2D(x, y)} and sets
     * the {@code PointQuery}'s {@link PointQuery#getPosition()} to {@link FxRobotContext#getPointPosition()}.
     */
    public PointQuery point(double x,
                            double y);

    /**
     * Calls {@link org.testfx.service.locator.PointLocator#point(Point2D)} and sets the {@code PointQuery}'s
     * {@link PointQuery#getPosition()} to {@link FxRobotContext#getPointPosition()}.
     */
    public PointQuery point(Point2D point);

    /**
     * Calls {@link org.testfx.service.locator.PointLocator#point(Bounds)} and sets the {@code PointQuery}'s
     * {@link PointQuery#getPosition()} to {@link FxRobotContext#getPointPosition()}.
     */
    public PointQuery point(Bounds bounds);

    /**
     * Calls {@link org.testfx.service.locator.PointLocator#point(Node)} and sets the {@code PointQuery}'s
     * {@link PointQuery#getPosition()} to {@link FxRobotContext#getPointPosition()}.
     */
    public PointQuery point(Node node);

    /**
     * Calls {@link org.testfx.service.locator.PointLocator#point(Scene)} and sets the {@code PointQuery}'s
     * {@link PointQuery#getPosition()} to {@link FxRobotContext#getPointPosition()}.
     */
    public PointQuery point(Scene scene);

    /**
     * Calls {@link org.testfx.service.locator.PointLocator#point(Window)} and sets the {@code PointQuery}'s
     * {@link PointQuery#getPosition()} to {@link FxRobotContext#getPointPosition()}.
     */
    public PointQuery point(Window window);

    // Convenience methods:
    /**
     * Convenience method: Tries to find a given node via {@link #lookup(String)} before calling {@link #point(Node)},
     * throwing a {@link FxRobotException} if no node is found.
     */
    public PointQuery point(String query);

    /**
     * Convenience method: Tries to find a given node via {@link #lookup(Matcher)} before calling {@link #point(Node)},
     * throwing a {@link FxRobotException} if no node is found.
     */
    public <T extends Node> PointQuery point(Matcher<T> matcher);

    /**
     * Convenience method: Tries to find a given node via {@link #lookup(Predicate)} before calling
     * {@link #point(Node)}, throwing a {@link FxRobotException} if no node is found.
     */
    public <T extends Node> PointQuery point(Predicate<T> predicate);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT OFFSET.
    //---------------------------------------------------------------------------------------------

    //public PointQuery offset(PointQuery pointQuery, double offsetX, double offsetY);

    // Convenience methods:

    /**
     * Convenience method: Calls {@link #point(Point2D)} and sets the query's offset by the given offset values.
     */
    public PointQuery offset(Point2D point,
                             double offsetX,
                             double offsetY);

    /**
     * Convenience method: Calls {@link #point(Bounds)} and sets the query's offset by the given offset values.
     */
    public PointQuery offset(Bounds bounds,
                             double offsetX,
                             double offsetY);

    /**
     * Convenience method: Calls {@link #point(Node)} and sets the query's offset by the given offset values.
     */
    public PointQuery offset(Node node,
                             double offsetX,
                             double offsetY);

    /**
     * Convenience method: Calls {@link #point(Scene)} and sets the query's offset by the given offset values.
     */
    public PointQuery offset(Scene scene,
                             double offsetX,
                             double offsetY);

    /**
     * Convenience method: Calls {@link #point(Window)} and sets the query's offset by the given offset values.
     */
    public PointQuery offset(Window window,
                             double offsetX,
                             double offsetY);

    /**
     * Convenience method: Calls {@link #point(String)} and sets the query's offset by the given offset values.
     */
    public PointQuery offset(String query,
                             double offsetX,
                             double offsetY);

    /**
     * Convenience method: Calls {@link #point(Matcher)} and sets the query's offset by the given offset values.
     */
    public <T extends Node> PointQuery offset(Matcher<T> matcher,
                                              double offsetX,
                                              double offsetY);

    /**
     * Convenience method: Calls {@link #point(Predicate)} and sets the query's offset by the given offset values.
     */
    public <T extends Node> PointQuery offset(Predicate<T> predicate,
                                              double offsetX,
                                              double offsetY);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR SCREEN CAPTURING.
    //---------------------------------------------------------------------------------------------

    /**
     * Returns a {@link Capture} that supplies a screenshot using the given rectangle's bounds.
     */
    public Capture capture(Rectangle2D screenRegion);

    /**
     * Returns a {@link Capture} that supplies a screenshot using the given bounds.
     */
    public Capture capture(Bounds bounds);

    /**
     * Returns a {@link Capture} that supplies a screenshot using the given node's bounds.
     */
    public Capture capture(Node node);

    // Convenience methods:
    /**
     * Convenience method: Returns a {@link Capture} that supplies the given {@link Image}.
     */
    public Capture capture(Image image);

    /**
     * Convenience method: Returns a {@link Capture} that supplies the {@link Image} from the image file of the
     * given {@link Path}.
     */
    public Capture capture(Path path);

    /**
     * Convenience method: Returns a {@link Capture} that supplies the {@link Image} from the image file of the
     * given {@link URL}.
     */
    public Capture capture(URL url);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR INTERACTION AND INTERRUPTION.
    //---------------------------------------------------------------------------------------------

    /**
     * Calls a runnable on the FX application thread and waits for it and
     * consecutive events to execute. So changes to the gui triggered by the
     * runnable will be performed when returned from this method.
     * 
     * @param runnable
     *            the runnable
     * @return this robot
     */
    public FxRobotInterface interact(Runnable runnable);

    /**
     * Calls a callable on the FX application thread and waits for it and
     * consecutive events to execute. So changes to the gui triggered by the
     * callable will be performed when returned from this method.
     * 
     * @param callable
     *            the callable
     * @return this robot
     */
    public <T> FxRobotInterface interact(Callable<T> callable);
    
    /**
     * Calls a runnable on the FX application thread and waits for it to
     * execute. It does not wait for other events on the fx application thread.
     * So changes to the gui triggered by the runnable may not be performed when
     * returned from this method.
     * 
     * @param runnable
     *            the runnable
     * @return this robot
     */
    public FxRobotInterface interactNoWait(Runnable runnable);

    /**
     * Calls a callable on the FX application thread and waits for it to
     * execute. It does not wait for other events on the fx application thread.
     * So changes to the gui triggered by the callable may not be performed when
     * returned from this method.
     * 
     * @param callable
     *            the callable
     * @return this robot
     */
    public <T> FxRobotInterface interactNoWait(Callable<T> callable);

    /**
     * Calls {@link WaitForAsyncUtils#waitForFxEvents()} and returns itself for method chaining.
     */
    public FxRobotInterface interrupt();

    /**
     * Calls {@link WaitForAsyncUtils#waitForFxEvents(int)} and returns itself for method chaining.
     */
    public FxRobotInterface interrupt(int attemptsCount);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR SLEEPING.
    //---------------------------------------------------------------------------------------------

    /**
     * Calls {@link org.testfx.robot.SleepRobot#sleep(long)} and returns itself for more method chaining.
     */
    public FxRobotInterface sleep(long milliseconds);

    /**
     * Calls {@link org.testfx.robot.SleepRobot#sleep(long, TimeUnit)} and returns itself for more method chaining.
     */
    public FxRobotInterface sleep(long duration,
                                  TimeUnit timeUnit);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR CLICKING.
    //---------------------------------------------------------------------------------------------

    /**
     * Calls {@link org.testfx.robot.ClickRobot#clickOn(MouseButton...)} and returns itself for more method chaining.
     */
    public FxRobotInterface clickOn(MouseButton... buttons);

    /**
     * Calls {@link org.testfx.robot.ClickRobot#clickOn(PointQuery, MouseButton...)} and returns itself for more method
     * chaining.
     */
    public FxRobotInterface clickOn(PointQuery pointQuery,
                                    MouseButton... buttons);

    /**
     * Calls {@link org.testfx.robot.ClickRobot#doubleClickOn(MouseButton...)} and returns itself for more method
     * chaining.
     */
    public FxRobotInterface doubleClickOn(MouseButton... buttons);

    /**
     * Calls {@link org.testfx.robot.ClickRobot#doubleClickOn(PointQuery, MouseButton...)} and returns itself
     * for method chaining.
     */
    public FxRobotInterface doubleClickOn(PointQuery pointQuery,
                                          MouseButton... buttons);

    // Convenience methods:
    /**
     * Convenience method: Moves mouse to the given coordinates, clicks the given buttons, and returns itself for
     * method chaining
     */
    public FxRobotInterface clickOn(double x,
                                    double y,
                                    MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the given point, clicks the given buttons, and returns itself for method
     * chaining.
     */
    public FxRobotInterface clickOn(Point2D point,
                                    MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Bounds)}, clicks the given buttons,
     * and returns itself for method chaining.
     */
    public FxRobotInterface clickOn(Bounds bounds,
                                    MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Node)}, clicks the given buttons,
     * and returns itself for method chaining.
     */
    public FxRobotInterface clickOn(Node node,
                                    MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Scene)}, clicks the given buttons,
     * and returns itself for method chaining.
     */
    public FxRobotInterface clickOn(Scene scene,
                                   MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Window)}, clicks the given buttons,
     * and returns itself for method chaining.
     */
    public FxRobotInterface clickOn(Window window,
                                    MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(String)}, clicks the given buttons,
     * and returns itself for method chaining.
     */
    public FxRobotInterface clickOn(String query,
                                    MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Matcher)}, clicks the given buttons,
     * and returns itself for method chaining.
     */
    public <T extends Node> FxRobotInterface clickOn(Matcher<T> matcher,
                                                     MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Predicate)}, clicks the given buttons,
     * and returns itself for method chaining.
     */
    public <T extends Node> FxRobotInterface clickOn(Predicate<T> predicate,
                                                     MouseButton... buttons);

    /**
     * Convenience method: Clicks the {@link MouseButton#SECONDARY} button and returns itself for method chaining.
     */
    public FxRobotInterface rightClickOn();

    /**
     * Convenience method: Moves mouse to the point returned from {@link PointQuery#query()}, clicks
     * the {@link MouseButton#SECONDARY} button and returns itself for method chaining.
     */
    public FxRobotInterface rightClickOn(PointQuery pointQuery);

    /**
     * Convenience method: Moves mouse to the given coordinates, clicks the {@link MouseButton#SECONDARY} button,
     * and returns itself for method chaining.
     */
    public FxRobotInterface rightClickOn(double x,
                                         double y);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Point2D)}, clicks
     * the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    public FxRobotInterface rightClickOn(Point2D point);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Bounds)}, clicks
     * the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    public FxRobotInterface rightClickOn(Bounds bounds);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Node)}, clicks
     * the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    public FxRobotInterface rightClickOn(Node node);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Scene)}, clicks
     * the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    public FxRobotInterface rightClickOn(Scene scene);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Window)}, clicks
     * the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    public FxRobotInterface rightClickOn(Window window);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(String)}, clicks
     * the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    public FxRobotInterface rightClickOn(String query);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Matcher)}, clicks
     * the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    public <T extends Node> FxRobotInterface rightClickOn(Matcher<T> matcher);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Predicate)}, clicks
     * the {@link MouseButton#SECONDARY} button, and returns itself for method chaining.
     */
    public <T extends Node> FxRobotInterface rightClickOn(Predicate<T> predicate);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(double, double)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    public FxRobotInterface doubleClickOn(double x,
                                          double y,
                                          MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Point2D)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    public FxRobotInterface doubleClickOn(Point2D point,
                                          MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Bounds)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    public FxRobotInterface doubleClickOn(Bounds bounds,
                                          MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Node)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    public FxRobotInterface doubleClickOn(Node node,
                                          MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Scene)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    public FxRobotInterface doubleClickOn(Scene scene,
                                          MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Window)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    public FxRobotInterface doubleClickOn(Window window,
                                          MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(String)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    public FxRobotInterface doubleClickOn(String query,
                                          MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Matcher)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    public <T extends Node> FxRobotInterface doubleClickOn(Matcher<T> matcher,
                                                           MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Predicate)}, double
     * clicks the given buttons, and returns itself for method chaining.
     */
    public <T extends Node> FxRobotInterface doubleClickOn(Predicate<T> predicate,
                                                           MouseButton... buttons);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR DRAGGING.
    //---------------------------------------------------------------------------------------------

    /**
     * Calls {@link org.testfx.robot.DragRobot#drag(MouseButton...)} and returns itself for more method chaining.
     */
    public FxRobotInterface drag(MouseButton... buttons);

    /**
     * Calls {@link org.testfx.robot.DragRobot#drag(PointQuery, MouseButton...)} and returns itself for more method
     * chaining.
     */
    public FxRobotInterface drag(PointQuery pointQuery,
                                 MouseButton... buttons);

    /**
     * Calls {@link org.testfx.robot.DragRobot#drop()} and returns itself for more method chaining.
     */
    public FxRobotInterface drop();

    /**
     * Calls {@link org.testfx.robot.DragRobot#dropTo(PointQuery)} and returns itself for more method chaining.
     */
    public FxRobotInterface dropTo(PointQuery pointQuery);

    /**
     * Calls {@link org.testfx.robot.DragRobot#dropBy(double, double)} and returns itself for more method chaining.
     */
    public FxRobotInterface dropBy(double x,
                                   double y);

    // Convenience methods:
    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(double, double)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    public FxRobotInterface drag(double x,
                                 double y,
                                 MouseButton... buttons);
    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Point2D)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    public FxRobotInterface drag(Point2D point,
                                 MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Bounds)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    public FxRobotInterface drag(Bounds bounds,
                                 MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Node)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    public FxRobotInterface drag(Node node,
                                 MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Scene)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    public FxRobotInterface drag(Scene scene,
                                 MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Window)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    public FxRobotInterface drag(Window window,
                                 MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(String)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    public FxRobotInterface drag(String query,
                                 MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Matcher)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    public <T extends Node> FxRobotInterface drag(Matcher<T> matcher,
                                                  MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Predicate)}, presses the given
     * buttons, and returns itself for method chaining.
     */
    public <T extends Node> FxRobotInterface drag(Predicate<T> predicate,
                                                  MouseButton... buttons);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(double, double)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    public FxRobotInterface dropTo(double x,
                                   double y);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Point2D)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    public FxRobotInterface dropTo(Point2D point);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Bounds)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    public FxRobotInterface dropTo(Bounds bounds);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Node)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    public FxRobotInterface dropTo(Node node);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Scene)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    public FxRobotInterface dropTo(Scene scene);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Window)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    public FxRobotInterface dropTo(Window window);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(String)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    public FxRobotInterface dropTo(String query);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Matcher)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    public <T extends Node> FxRobotInterface dropTo(Matcher<T> matcher);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Predicate)}, releases the buttons
     * that were pressed in {@link #drag(MouseButton...)}- or {@link #press(MouseButton...)}-related methods,
     * and returns itself for method chaining.
     */
    public <T extends Node> FxRobotInterface dropTo(Predicate<T> predicate);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR KEYBOARD.
    //---------------------------------------------------------------------------------------------

    /**
     * Calls {@link org.testfx.robot.KeyboardRobot#press(KeyCode...)} and returns itself for method chaining.
     */
    public FxRobotInterface press(KeyCode... keys);

    /**
     * Calls {@link org.testfx.robot.KeyboardRobot#press(KeyCode...)} and returns itself for method chaining.
     */
    public FxRobotInterface release(KeyCode... keys);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR MOUSE.
    //---------------------------------------------------------------------------------------------

    /**
     * Presses and holds mouse buttons.
     *
     * @param buttons mouse buttons to press, defaults to primary mouse button.
     */
    public FxRobotInterface press(MouseButton... buttons);

    /**
     * Releases pressed mouse buttons.
     *
     * @param buttons mouse buttons to release, defaults to all pressed mouse buttons.
     */
    public FxRobotInterface release(MouseButton... buttons);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR MOVING.
    //---------------------------------------------------------------------------------------------

    /**
     * Calls {@link org.testfx.robot.MoveRobot#moveTo(PointQuery)} and returns itself for more method chaining.
     */
    public FxRobotInterface moveTo(PointQuery pointQuery);

    /**
     * Calls {@link org.testfx.robot.MoveRobot#moveBy(double, double)} and returns itself for more method chaining.
     */
    public FxRobotInterface moveBy(double x,
                                   double y);

    // Convenience methods:

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(double, double)} and returns itself
     * for method chaining.
     */
    public FxRobotInterface moveTo(double x,
                                   double y);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Point2D)} and returns itself
     * for method chaining.
     */
    public FxRobotInterface moveTo(Point2D point);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Bounds)} and returns itself
     * for method chaining.
     */
    public FxRobotInterface moveTo(Bounds bounds);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Node)} and returns itself
     * for method chaining.
     */
    public FxRobotInterface moveTo(Node node);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Scene)} and returns itself
     * for method chaining.
     */
    public FxRobotInterface moveTo(Scene scene);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Window)} and returns itself
     * for method chaining.
     */
    public FxRobotInterface moveTo(Window window);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(String)} and returns itself
     * for method chaining.
     */
    public FxRobotInterface moveTo(String query);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Matcher)} and returns itself
     * for method chaining.
     */
    public <T extends Node> FxRobotInterface moveTo(Matcher<T> matcher);

    /**
     * Convenience method: Moves mouse to the point returned from {@link #point(Predicate)} and returns itself
     * for method chaining.
     */
    public <T extends Node> FxRobotInterface moveTo(Predicate<T> predicate);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR SCROLLING.
    //---------------------------------------------------------------------------------------------

    /**
     * Calls {@link org.testfx.robot.ScrollRobot#scroll(int)} and returns itself for more method chaining.
     */
    public FxRobotInterface scroll(int amount);

    /**
     * Calls {@link org.testfx.robot.ScrollRobot#scroll(int, VerticalDirection)} and returns itself for more method
     * chaining.
     */
    public FxRobotInterface scroll(int amount,
                                   VerticalDirection direction);

    // Convenience methods:
    /**
     * Calls {@link org.testfx.robot.ScrollRobot#scroll(int, VerticalDirection)} with arguments {@code 1} and
     * {@code direction} and returns itself for more method chaining.
     */
    public FxRobotInterface scroll(VerticalDirection direction);

    /**
     * Calls {@link org.testfx.robot.ScrollRobot#scroll(int, HorizontalDirection)} and returns itself for more method
     * chaining.
     */
    public FxRobotInterface scroll(int amount,
                                   HorizontalDirection direction);

    // Convenience methods:
    /**
     * Calls {@link org.testfx.robot.ScrollRobot#scroll(int, VerticalDirection)} with arguments {@code 1} and
     * {@code direction} and returns itself for more method chaining.
     */
    public FxRobotInterface scroll(HorizontalDirection direction);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR TYPING.
    //---------------------------------------------------------------------------------------------

    /**
     * Calls {@link org.testfx.robot.TypeRobot#push(KeyCode...)} and returns itself for more method chaining.
     */
    public FxRobotInterface push(KeyCode... combination);

    /**
     * Calls {@link org.testfx.robot.TypeRobot#push(KeyCodeCombination)} and returns itself for more method chaining.
     */
    public FxRobotInterface push(KeyCodeCombination combination);

    /**
     * Calls {@link org.testfx.robot.TypeRobot#type(KeyCode...)} and returns itself for more method chaining.
     */
    public FxRobotInterface type(KeyCode... keys);

    /**
     * Calls {@link org.testfx.robot.TypeRobot#type(KeyCode, int)} and returns itself for more method chaining.
     */
    public FxRobotInterface type(KeyCode key,
                                 int times);

    // Convenience methods:
    /**
     * Convenience method: Calls {@link org.testfx.robot.TypeRobot#type(KeyCode, int)} with {@link KeyCode#BACK_SPACE}
     * and returns itself for more method chaining.
     */
    public FxRobotInterface eraseText(int characters);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR WRITING.
    //---------------------------------------------------------------------------------------------

    /**
     * Calls {@link org.testfx.robot.WriteRobot#write(char)} and returns itself for more method chaining.
     */
    public FxRobotInterface write(char character);

    /**
     * Calls {@link org.testfx.robot.WriteRobot#write(String)} and returns itself for more method chaining.
     */
    public FxRobotInterface write(String text);

}
