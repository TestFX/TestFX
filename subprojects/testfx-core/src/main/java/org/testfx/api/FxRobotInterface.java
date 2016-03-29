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

import com.google.common.base.Predicate;
import org.hamcrest.Matcher;
import org.testfx.api.annotation.Unstable;
import org.testfx.service.query.BoundsQuery;
import org.testfx.service.query.NodeQuery;
import org.testfx.service.query.PointQuery;
import org.testfx.service.support.Capture;

@Unstable(reason = "interface was recently added")
public interface FxRobotInterface {

    //---------------------------------------------------------------------------------------------
    // METHODS FOR WINDOW TARGETING.
    //---------------------------------------------------------------------------------------------

    public Window targetWindow();
    public FxRobotInterface targetWindow(Window window);
    public FxRobotInterface targetWindow(Predicate<Window> predicate);

    // Convenience methods:
    public FxRobotInterface targetWindow(int windowIndex);
    public FxRobotInterface targetWindow(String stageTitleRegex);
    public FxRobotInterface targetWindow(Pattern stageTitlePattern);
    public FxRobotInterface targetWindow(Scene scene);
    public FxRobotInterface targetWindow(Node node);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR WINDOW LOOKUP.
    //---------------------------------------------------------------------------------------------

    public List<Window> listWindows();
    public List<Window> listTargetWindows();
    public Window window(Predicate<Window> predicate);

    // Convenience methods:
    public Window window(int windowIndex);
    public Window window(String stageTitleRegex);
    public Window window(Pattern stageTitlePattern);
    public Window window(Scene scene);
    public Window window(Node node);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR NODE LOOKUP.
    //---------------------------------------------------------------------------------------------

    public NodeQuery fromAll();
    public NodeQuery from(Node... parentNodes);
    public NodeQuery from(Collection<Node> parentNodes);

    public Node rootNode(Window window);
    public Node rootNode(Scene scene);
    public Node rootNode(Node node);

    // Convenience methods:
    public NodeQuery lookup(String query);
    public <T extends Node> NodeQuery lookup(Matcher<T> matcher);
    public <T extends Node> NodeQuery lookup(Predicate<T> predicate);
    public NodeQuery from(NodeQuery nodeQuery);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR BOUNDS LOCATION.
    //---------------------------------------------------------------------------------------------

    public BoundsQuery bounds(double minX,
                              double minY,
                              double width,
                              double height);
    public BoundsQuery bounds(Point2D point);
    public BoundsQuery bounds(Bounds bounds);
    public BoundsQuery bounds(Node node);
    public BoundsQuery bounds(Scene scene);
    public BoundsQuery bounds(Window window);

    // Convenience methods:
    public BoundsQuery bounds(String query);
    public <T extends Node> BoundsQuery bounds(Matcher<T> matcher);
    public <T extends Node> BoundsQuery bounds(Predicate<T> predicate);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT POSITION.
    //---------------------------------------------------------------------------------------------

    public FxRobotInterface targetPos(Pos pointPosition);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT LOCATION.
    //---------------------------------------------------------------------------------------------

    public PointQuery point(double x,
                            double y);
    public PointQuery point(Point2D point);
    public PointQuery point(Bounds bounds);
    public PointQuery point(Node node);
    public PointQuery point(Scene scene);
    public PointQuery point(Window window);

    // Convenience methods:
    public PointQuery point(String query);
    public <T extends Node> PointQuery point(Matcher<T> matcher);
    public <T extends Node> PointQuery point(Predicate<T> predicate);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT OFFSET.
    //---------------------------------------------------------------------------------------------

    //public PointQuery offset(PointQuery pointQuery, double offsetX, double offsetY);

    // Convenience methods:
    public PointQuery offset(Point2D point,
                             double offsetX,
                             double offsetY);
    public PointQuery offset(Bounds bounds,
                             double offsetX,
                             double offsetY);
    public PointQuery offset(Node node,
                             double offsetX,
                             double offsetY);
    public PointQuery offset(Scene scene,
                             double offsetX,
                             double offsetY);
    public PointQuery offset(Window window,
                             double offsetX,
                             double offsetY);
    public PointQuery offset(String query,
                             double offsetX,
                             double offsetY);
    public <T extends Node> PointQuery offset(Matcher<T> matcher,
                                              double offsetX,
                                              double offsetY);
    public <T extends Node> PointQuery offset(Predicate<T> predicate,
                                              double offsetX,
                                              double offsetY);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR SCREEN CAPTURING.
    //---------------------------------------------------------------------------------------------

    public Capture capture(Rectangle2D screenRegion);
    public Capture capture(Bounds bounds);
    public Capture capture(Node node);

    // Convenience methods:
    public Capture capture(Image image);
    public Capture capture(Path path);
    public Capture capture(URL url);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR INTERACTION AND INTERRUPTION.
    //---------------------------------------------------------------------------------------------

    public FxRobotInterface interact(Runnable runnable);
    public <T> FxRobotInterface interact(Callable<T> callable);

    public FxRobotInterface interrupt();
    public FxRobotInterface interrupt(int attemptsCount);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR SLEEPING.
    //---------------------------------------------------------------------------------------------

    public FxRobotInterface sleep(long milliseconds);
    public FxRobotInterface sleep(long duration,
                                  TimeUnit timeUnit);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR CLICKING.
    //---------------------------------------------------------------------------------------------

    public FxRobotInterface clickOn(MouseButton... buttons);
    public FxRobotInterface clickOn(PointQuery pointQuery,
                                    MouseButton... buttons);
    public FxRobotInterface doubleClickOn(MouseButton... buttons);
    public FxRobotInterface doubleClickOn(PointQuery pointQuery,
                                          MouseButton... buttons);

    // Convenience methods:
    public FxRobotInterface clickOn(double x,
                                    double y,
                                    MouseButton... buttons);
    public FxRobotInterface clickOn(Point2D point,
                                    MouseButton... buttons);
    public FxRobotInterface clickOn(Bounds bounds,
                                    MouseButton... buttons);
    public FxRobotInterface clickOn(Node node,
                                    MouseButton... buttons);
    public FxRobotInterface clickOn(Scene scene,
                                   MouseButton... buttons);
    public FxRobotInterface clickOn(Window window,
                                    MouseButton... buttons);
    public FxRobotInterface clickOn(String query,
                                    MouseButton... buttons);
    public <T extends Node> FxRobotInterface clickOn(Matcher<T> matcher,
                                                     MouseButton... buttons);
    public <T extends Node> FxRobotInterface clickOn(Predicate<T> predicate,
                                                     MouseButton... buttons);
    public FxRobotInterface rightClickOn();
    public FxRobotInterface rightClickOn(PointQuery pointQuery);
    public FxRobotInterface rightClickOn(double x,
                                         double y);
    public FxRobotInterface rightClickOn(Point2D point);
    public FxRobotInterface rightClickOn(Bounds bounds);
    public FxRobotInterface rightClickOn(Node node);
    public FxRobotInterface rightClickOn(Scene scene);
    public FxRobotInterface rightClickOn(Window window);
    public FxRobotInterface rightClickOn(String query);
    public <T extends Node> FxRobotInterface rightClickOn(Matcher<T> matcher);
    public <T extends Node> FxRobotInterface rightClickOn(Predicate<T> predicate);
    public FxRobotInterface doubleClickOn(double x,
                                          double y,
                                          MouseButton... buttons);
    public FxRobotInterface doubleClickOn(Point2D point,
                                          MouseButton... buttons);
    public FxRobotInterface doubleClickOn(Bounds bounds,
                                          MouseButton... buttons);
    public FxRobotInterface doubleClickOn(Node node,
                                          MouseButton... buttons);
    public FxRobotInterface doubleClickOn(Scene scene,
                                          MouseButton... buttons);
    public FxRobotInterface doubleClickOn(Window window,
                                          MouseButton... buttons);
    public FxRobotInterface doubleClickOn(String query,
                                          MouseButton... buttons);
    public <T extends Node> FxRobotInterface doubleClickOn(Matcher<T> matcher,
                                                           MouseButton... buttons);
    public <T extends Node> FxRobotInterface doubleClickOn(Predicate<T> predicate,
                                                           MouseButton... buttons);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR DRAGGING.
    //---------------------------------------------------------------------------------------------

    public FxRobotInterface drag(MouseButton... buttons);
    public FxRobotInterface drag(PointQuery pointQuery,
                                 MouseButton... buttons);
    public FxRobotInterface drop();
    public FxRobotInterface dropTo(PointQuery pointQuery);
    public FxRobotInterface dropBy(double x,
                                   double y);

    // Convenience methods:
    public FxRobotInterface drag(double x,
                                 double y,
                                 MouseButton... buttons);
    public FxRobotInterface drag(Point2D point,
                                 MouseButton... buttons);
    public FxRobotInterface drag(Bounds bounds,
                                 MouseButton... buttons);
    public FxRobotInterface drag(Node node,
                                 MouseButton... buttons);
    public FxRobotInterface drag(Scene scene,
                                 MouseButton... buttons);
    public FxRobotInterface drag(Window window,
                                 MouseButton... buttons);
    public FxRobotInterface drag(String query,
                                 MouseButton... buttons);
    public <T extends Node> FxRobotInterface drag(Matcher<T> matcher,
                                                  MouseButton... buttons);
    public <T extends Node> FxRobotInterface drag(Predicate<T> predicate,
                                                  MouseButton... buttons);
    public FxRobotInterface dropTo(double x,
                                   double y);
    public FxRobotInterface dropTo(Point2D point);
    public FxRobotInterface dropTo(Bounds bounds);
    public FxRobotInterface dropTo(Node node);
    public FxRobotInterface dropTo(Scene scene);
    public FxRobotInterface dropTo(Window window);
    public FxRobotInterface dropTo(String query);
    public <T extends Node> FxRobotInterface dropTo(Matcher<T> matcher);
    public <T extends Node> FxRobotInterface dropTo(Predicate<T> predicate);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR KEYBOARD.
    //---------------------------------------------------------------------------------------------

    public FxRobotInterface press(KeyCode... keys);
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

    public FxRobotInterface moveTo(PointQuery pointQuery);
    public FxRobotInterface moveBy(double x,
                                   double y);

    // Convenience methods:
    public FxRobotInterface moveTo(double x,
                                   double y);
    public FxRobotInterface moveTo(Point2D point);
    public FxRobotInterface moveTo(Bounds bounds);
    public FxRobotInterface moveTo(Node node);
    public FxRobotInterface moveTo(Scene scene);
    public FxRobotInterface moveTo(Window window);
    public FxRobotInterface moveTo(String query);
    public <T extends Node> FxRobotInterface moveTo(Matcher<T> matcher);
    public <T extends Node> FxRobotInterface moveTo(Predicate<T> predicate);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR SCROLLING.
    //---------------------------------------------------------------------------------------------

    public FxRobotInterface scroll(int amount,
                                   VerticalDirection direction);

    // Convenience methods:
    public FxRobotInterface scroll(VerticalDirection direction);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR TYPING.
    //---------------------------------------------------------------------------------------------

    public FxRobotInterface push(KeyCode... combination);
    public FxRobotInterface push(KeyCodeCombination combination);
    public FxRobotInterface type(KeyCode... keys);
    public FxRobotInterface type(KeyCode key,
                                 int times);

    // Convenience methods:
    public FxRobotInterface eraseText(int characters);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR WRITING.
    //---------------------------------------------------------------------------------------------

    public FxRobotInterface write(char character);
    public FxRobotInterface write(String text);

}
