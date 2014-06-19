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

import java.util.concurrent.TimeUnit;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Window;

import com.google.common.base.Predicate;
import org.hamcrest.Matcher;
import org.loadui.testfx.service.query.PointQuery;

public interface FxRobot {

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT POSITION.
    //---------------------------------------------------------------------------------------------

    public FxRobot pos(Pos pointPosition);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT LOCATION.
    //---------------------------------------------------------------------------------------------

    public PointQuery pointFor(double x,
                               double y);
    public PointQuery pointFor(Point2D point);
    public PointQuery pointFor(Bounds bounds);
    public PointQuery pointFor(Node node);
    public PointQuery pointFor(Scene scene);
    public PointQuery pointFor(Window window);

    // Convenience methods:
    public PointQuery pointFor(String query);
    public <T extends Node> PointQuery pointFor(Predicate<T> predicate);

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
    public <T extends Node> PointQuery offset(Predicate<T> predicate,
                                              double offsetX,
                                              double offsetY);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR WINDOW TARGETING.
    //---------------------------------------------------------------------------------------------

    public FxRobot target(Window window);
    public FxRobot target(int windowNumber);
    public FxRobot target(String stageTitleRegex);

    // Convenience methods:
    public FxRobot target(Scene scene);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR WINDOW LOOKUP.
    //---------------------------------------------------------------------------------------------

    //public List<Window> listWindows();
    //public List<Window> listTargetWindows();

    //public Window window(int windowIndex);
    //public Window window(String stageTitleRegex);

    // Convenience methods:
    //public Window window(Scene scene);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR NODE LOOKUP.
    //---------------------------------------------------------------------------------------------

    //public <T extends Node> T node(String query);
    //public <T extends Node> Set<T> nodes(String query)
    //public <T extends Node> T node(Predicate<T> predicate);
    //public <T extends Node> Set<T> nodes(Predicate<T> predicate);

    //public <T extends Node> T node(String query, Node... parentNodes);
    //public <T extends Node> Set<T> nodes(String query, Node... parentNodes);
    //public <T extends Node> T node(Predicate<T> predicate, Node... parentNodes);
    //public <T extends Node> Set<T> nodes(Predicate<T> predicate, Node... parentNodes);

    //public Node parentNode(Window window);
    //public Node parentNode(int windowIndex);
    //public Node parentNode(String stageTitleRegex);
    //public Node parentNode(Scene scene);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR CLICKING.
    //---------------------------------------------------------------------------------------------

    public FxRobot clickOn(MouseButton... buttons);
    public FxRobot clickOn(PointQuery pointQuery,
                           MouseButton... buttons);
    public FxRobot doubleClickOn(MouseButton... buttons);
    public FxRobot doubleClickOn(PointQuery pointQuery,
                                 MouseButton... buttons);

    // Convenience methods:
    public FxRobot clickOn(double x,
                           double y,
                           MouseButton... buttons);
    public FxRobot clickOn(Point2D point,
                           MouseButton... buttons);
    public FxRobot clickOn(Bounds bounds,
                           MouseButton... buttons);
    public FxRobot clickOn(Node node,
                           MouseButton... buttons);
    public FxRobot clickOn(Scene scene,
                           MouseButton... buttons);
    public FxRobot clickOn(Window window,
                           MouseButton... buttons);
    public FxRobot clickOn(String query,
                           MouseButton... buttons);
    public FxRobot clickOn(Matcher<Object> matcher,
                           MouseButton... buttons);
    public <T extends Node> FxRobot clickOn(Predicate<T> predicate,
                                            MouseButton... buttons);
    public FxRobot rightClickOn();
    public FxRobot rightClickOn(PointQuery pointQuery);
    public FxRobot rightClickOn(double x,
                                double y);
    public FxRobot rightClickOn(Point2D point);
    public FxRobot rightClickOn(Bounds bounds);
    public FxRobot rightClickOn(Node node);
    public FxRobot rightClickOn(Scene scene);
    public FxRobot rightClickOn(Window window);
    public FxRobot rightClickOn(String query);
    public FxRobot rightClickOn(Matcher<Object> matcher);
    public <T extends Node> FxRobot rightClickOn(Predicate<T> predicate);
    public FxRobot doubleClickOn(double x,
                                 double y,
                                 MouseButton... buttons);
    public FxRobot doubleClickOn(Point2D point,
                                 MouseButton... buttons);
    public FxRobot doubleClickOn(Bounds bounds,
                                 MouseButton... buttons);
    public FxRobot doubleClickOn(Node node,
                                 MouseButton... buttons);
    public FxRobot doubleClickOn(Scene scene,
                                 MouseButton... buttons);
    public FxRobot doubleClickOn(Window window,
                                 MouseButton... buttons);
    public FxRobot doubleClickOn(String query,
                                 MouseButton... buttons);
    public FxRobot doubleClickOn(Matcher<Object> matcher,
                                 MouseButton... buttons);
    public <T extends Node> FxRobot doubleClickOn(Predicate<T> predicate,
                                                  MouseButton... buttons);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR DRAGGING.
    //---------------------------------------------------------------------------------------------

    public FxRobot drag(MouseButton... buttons);
    public FxRobot drag(PointQuery pointQuery, MouseButton... buttons);
    public FxRobot drop();
    public FxRobot dropTo(PointQuery pointQuery);
    public FxRobot dropBy(double x,
                          double y);

    // Convenience methods:
    public FxRobot drag(double x,
                        double y,
                        MouseButton... buttons);
    public FxRobot drag(Point2D point,
                        MouseButton... buttons);
    public FxRobot drag(Bounds bounds,
                        MouseButton... buttons);
    public FxRobot drag(Node node,
                        MouseButton... buttons);
    public FxRobot drag(Scene scene,
                        MouseButton... buttons);
    public FxRobot drag(Window window,
                        MouseButton... buttons);
    public FxRobot drag(String query,
                        MouseButton... buttons);
    public FxRobot drag(Matcher<Object> matcher,
                        MouseButton... buttons);
    public <T extends Node> FxRobot drag(Predicate<T> predicate,
                                         MouseButton... buttons);
    public FxRobot dropTo(double x,
                          double y);
    public FxRobot dropTo(Point2D point);
    public FxRobot dropTo(Bounds bounds);
    public FxRobot dropTo(Node node);
    public FxRobot dropTo(Scene scene);
    public FxRobot dropTo(Window window);
    public FxRobot dropTo(String query);
    public FxRobot dropTo(Matcher<Object> matcher);
    public <T extends Node> FxRobot dropTo(Predicate<T> predicate);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR KEYBOARD.
    //---------------------------------------------------------------------------------------------

    public FxRobot press(KeyCode... keys);
    public FxRobot release(KeyCode... keys);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR MOUSE.
    //---------------------------------------------------------------------------------------------

    /**
     * Presses and holds mouse buttons.
     *
     * @param buttons mouse buttons to press, defaults to primary mouse button.
     */
    public FxRobot press(MouseButton... buttons);

    /**
     * Releases pressed mouse buttons.
     *
     * @param buttons mouse buttons to release, defaults to all pressed mouse buttons.
     */
    public FxRobot release(MouseButton... buttons);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR MOVING.
    //---------------------------------------------------------------------------------------------

    public FxRobot moveTo(PointQuery pointQuery);
    public FxRobot moveBy(double x,
                          double y);

    // Convenience methods:
    public FxRobot moveTo(double x,
                          double y);
    public FxRobot moveTo(Point2D point);
    public FxRobot moveTo(Bounds bounds);
    public FxRobot moveTo(Node node);
    public FxRobot moveTo(Scene scene);
    public FxRobot moveTo(Window window);
    public FxRobot moveTo(String query);
    public FxRobot moveTo(Matcher<Object> matcher);
    public <T extends Node> FxRobot moveTo(Predicate<T> predicate);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR SCROLLING.
    //---------------------------------------------------------------------------------------------

    public FxRobot scroll(int amount,
                          VerticalDirection direction);

    // Convenience methods:
    public FxRobot scroll(VerticalDirection direction);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR SLEEPING.
    //---------------------------------------------------------------------------------------------

    public FxRobot sleep(long milliseconds);
    public FxRobot sleep(long duration,
                         TimeUnit timeUnit);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR TYPING.
    //---------------------------------------------------------------------------------------------

    public FxRobot type(KeyCode... keys);
    public FxRobot type(char character);
    public FxRobot type(String text);
    public FxRobot erase(int characters);

    // Convenience methods:
    public FxRobot push(KeyCode... keys);
    public FxRobot push(char character);

}
