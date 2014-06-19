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
package org.loadui.testfx.framework.robot.impl;

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
import org.loadui.testfx.framework.robot.FxRobot;
import org.loadui.testfx.robots.ClickRobot;
import org.loadui.testfx.robots.KeyboardRobot;
import org.loadui.testfx.robots.MouseRobot;
import org.loadui.testfx.robots.MoveRobot;
import org.loadui.testfx.robots.ScreenRobot;
import org.loadui.testfx.robots.ScrollRobot;
import org.loadui.testfx.robots.SleepRobot;
import org.loadui.testfx.robots.TypeRobot;
import org.loadui.testfx.robots.impl.ClickRobotImpl;
import org.loadui.testfx.robots.impl.KeyboardRobotImpl;
import org.loadui.testfx.robots.impl.MouseRobotImpl;
import org.loadui.testfx.robots.impl.MoveRobotImpl;
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

public class FxRobotImpl implements FxRobot {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final WindowFinder windowFinder = new WindowFinderImpl();
    private final NodeFinder nodeFinder = new NodeFinderImpl(windowFinder);

    private Pos pointPosition = Pos.CENTER;
    private final BoundsLocator boundsLocator;
    private final PointLocator pointLocator;

    private final ScreenRobot screenRobot;
    private final MouseRobot mouseRobot;
    private final KeyboardRobot keyboardRobot;
    private final MoveRobot moveRobot;
    private final SleepRobot sleepRobot;

    private final ClickRobot clickRobot;
    private final ScrollRobot scrollRobot;
    private final TypeRobot typeRobot;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public FxRobotImpl() {
        boundsLocator = new BoundsLocatorImpl();
        pointLocator = new PointLocatorImpl(boundsLocator);

        screenRobot = new ScreenRobotImpl();
        mouseRobot = new MouseRobotImpl(screenRobot);
        keyboardRobot = new KeyboardRobotImpl(screenRobot);
        moveRobot = new MoveRobotImpl(screenRobot);
        sleepRobot = new SleepRobotImpl();

        clickRobot = new ClickRobotImpl(mouseRobot, moveRobot, sleepRobot);
        scrollRobot = new ScrollRobotImpl(screenRobot);
        typeRobot = new TypeRobotImpl(keyboardRobot);
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT POSITION.
    //---------------------------------------------------------------------------------------------

    @Override
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

    public FxRobotImpl target(Window window) {
        windowFinder.target(window);
        return this;
    }

    public FxRobotImpl target(int windowNumber) {
        windowFinder.target(windowNumber);
        return this;
    }

    public FxRobotImpl target(String stageTitleRegex) {
        windowFinder.target(stageTitleRegex);
        return this;
    }

    public FxRobotImpl target(Scene scene) {
        windowFinder.target(scene);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF TYPE ROBOT.
    //---------------------------------------------------------------------------------------------

    public FxRobotImpl type(KeyCode... keys) {
        typeRobot.type(keys);
        return this;
    }

    public FxRobotImpl type(char character) {
        typeRobot.type(character);
        return this;
    }

    public FxRobotImpl type(String text) {
        typeRobot.type(text);
        return this;
    }

    public FxRobotImpl erase(int characters) {
        typeRobot.erase(characters);
        return this;
    }

    public FxRobotImpl push(KeyCode... keys) {
        type(keys);
        return this;
    }

    public FxRobotImpl push(char character) {
        type(character);
        return this;
    }

    /**
     * Closes the front-most window using the Alt+F4 keyboard shortcut.
     */
    public FxRobotImpl closeCurrentWindow() {
        push(KeyCode.ALT, KeyCode.F4).sleep(100);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF SLEEP ROBOT.
    //---------------------------------------------------------------------------------------------

    public FxRobotImpl sleep(long milliseconds) {
        sleepRobot.sleep(milliseconds);
        return this;
    }

    public FxRobotImpl sleep(long duration, TimeUnit timeUnit) {
        sleepRobot.sleep(duration, timeUnit);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF SCROLL ROBOT.
    //---------------------------------------------------------------------------------------------

    @Deprecated
    public FxRobotImpl scroll(int amount) {
        scrollRobot.scroll(amount);
        return this;
    }

    public FxRobotImpl scroll(int amount, VerticalDirection direction) {
        scrollRobot.scroll(amount, direction);
        return this;
    }

    public FxRobotImpl scroll(VerticalDirection direction) {
        scroll(1, direction);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF MOUSE ROBOT.
    //---------------------------------------------------------------------------------------------

    public FxRobotImpl press(MouseButton... buttons) {
        mouseRobot.press(buttons);
        return this;
    }

    public FxRobotImpl release(MouseButton... buttons) {
        mouseRobot.release(buttons);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF KEYBOARD ROBOT.
    //---------------------------------------------------------------------------------------------

    public FxRobotImpl press(KeyCode... keys) {
        keyboardRobot.press(keys);
        return this;
    }

    public FxRobotImpl release(KeyCode... keys) {
        keyboardRobot.release(keys);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF CLICK ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    public FxRobot clickOn(MouseButton... buttons) {
        clickRobot.clickOn(buttons);
        return this;
    }

    @Override
    public FxRobot clickOn(PointQuery pointQuery, MouseButton... buttons) {
        clickRobot.clickOn(pointQuery, buttons);
        return this;
    }

    @Override
    public FxRobot doubleClickOn(MouseButton... buttons) {
        clickRobot.clickOn(buttons);
        return this;
    }

    @Override
    public FxRobot doubleClickOn(PointQuery pointQuery, MouseButton... buttons) {
        clickRobot.doubleClickOn(pointQuery, buttons);
        return this;
    }

    @Override
    public FxRobot clickOn(double x, double y, MouseButton... buttons) {
        return clickOn(pointFor(x, y), buttons);
    }

    @Override
    public FxRobot clickOn(Point2D point, MouseButton... buttons) {
        return clickOn(pointFor(point), buttons);
    }

    @Override
    public FxRobot clickOn(Bounds bounds, MouseButton... buttons) {
        return clickOn(pointFor(bounds), buttons);
    }

    @Override
    public FxRobot clickOn(Node node, MouseButton... buttons) {
        return clickOn(pointFor(node), buttons);
    }

    @Override
    public FxRobot clickOn(Scene scene, MouseButton... buttons) {
        return clickOn(pointFor(scene), buttons);
    }

    @Override
    public FxRobot clickOn(Window window, MouseButton... buttons) {
        return clickOn(pointFor(window), buttons);
    }

    @Override
    public FxRobot clickOn(String query, MouseButton... buttons) {
        return clickOn(pointFor(query), buttons);
    }

    @Override
    public FxRobot clickOn(Matcher<Object> matcher, MouseButton... buttons) {
        return clickOn(pointFor(matcher), buttons);
    }

    @Override
    public <T extends Node> FxRobot clickOn(Predicate<T> predicate, MouseButton... buttons) {
        return clickOn(pointFor(predicate), buttons);
    }

    @Override
    public FxRobot rightClickOn() {
        return clickOn(MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(PointQuery pointQuery) {
        return clickOn(pointQuery, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(double x, double y) {
        return clickOn(x, y, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(Point2D point) {
        return clickOn(point, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(Bounds bounds) {
        return clickOn(bounds, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(Node node) {
        return clickOn(node, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(Scene scene) {
        return clickOn(scene, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(Window window) {
        return clickOn(window, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(String query) {
        return clickOn(query, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot rightClickOn(Matcher<Object> matcher) {
        return clickOn(matcher, MouseButton.SECONDARY);
    }

    @Override
    public <T extends Node> FxRobot rightClickOn(Predicate<T> predicate) {
        return clickOn(predicate, MouseButton.SECONDARY);
    }

    @Override
    public FxRobot doubleClickOn(double x, double y, MouseButton... buttons) {
        return doubleClickOn(pointFor(x, y), buttons);
    }

    @Override
    public FxRobot doubleClickOn(Point2D point, MouseButton... buttons) {
        return doubleClickOn(pointFor(point), buttons);
    }

    @Override
    public FxRobot doubleClickOn(Bounds bounds, MouseButton... buttons) {
        return doubleClickOn(pointFor(bounds), buttons);
    }

    @Override
    public FxRobot doubleClickOn(Node node, MouseButton... buttons) {
        return doubleClickOn(pointFor(node), buttons);
    }

    @Override
    public FxRobot doubleClickOn(Scene scene, MouseButton... buttons) {
        return doubleClickOn(pointFor(scene), buttons);
    }

    @Override
    public FxRobot doubleClickOn(Window window, MouseButton... buttons) {
        return doubleClickOn(pointFor(window), buttons);
    }

    @Override
    public FxRobot doubleClickOn(String query, MouseButton... buttons) {
        return doubleClickOn(pointFor(query), buttons);
    }

    @Override
    public FxRobot doubleClickOn(Matcher<Object> matcher, MouseButton... buttons) {
        return doubleClickOn(pointFor(matcher), buttons);
    }

    @Override
    public <T extends Node> FxRobot doubleClickOn(Predicate<T> predicate, MouseButton... buttons) {
        return doubleClickOn(pointFor(predicate), buttons);
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF DRAG ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    public FxRobotImpl drag(MouseButton... buttons) {
        mouseRobot.press(buttons);
        return this;
    }

    @Override
    public FxRobotImpl drag(double x, double y, MouseButton... buttons) {
        moveTo(x, y);
        drag(buttons);
        return this;
    }

    @Override
    public FxRobotImpl drag(Point2D point, MouseButton... buttons) {
        moveTo(point);
        drag(buttons);
        return this;
    }

    @Override
    public FxRobotImpl drag(Bounds bounds, MouseButton... buttons) {
        moveTo(bounds);
        drag(buttons);
        return this;
    }

    @Override
    public FxRobotImpl drag(Node node, MouseButton... buttons) {
        moveTo(node);
        drag(buttons);
        return this;
    }

    @Override
    public FxRobotImpl drag(Scene scene, MouseButton... buttons) {
        moveTo(scene);
        drag(buttons);
        return this;
    }

    @Override
    public FxRobotImpl drag(Window window, MouseButton... buttons) {
        moveTo(window);
        drag(buttons);
        return this;
    }

    @Override
    public FxRobotImpl drag(String query, MouseButton... buttons) {
        moveTo(query);
        drag(buttons);
        return this;
    }

    @Override
    public FxRobotImpl drag(Matcher<Object> matcher, MouseButton... buttons) {
        moveTo(matcher);
        drag(buttons);
        return this;
    }

    @Override
    public <T extends Node> FxRobotImpl drag(Predicate<T> predicate, MouseButton... buttons) {
        moveTo(predicate);
        drag(buttons);
        return this;
    }

    @Override
    public FxRobotImpl drop() {
        mouseRobot.release();
        return this;
    }

    @Override
    public FxRobotImpl dropTo(double x, double y) {
        moveTo(x, y);
        drop();
        return this;
    }

    @Override
    public FxRobotImpl dropTo(Point2D point) {
        moveTo(point);
        drop();
        return this;
    }

    @Override
    public FxRobotImpl dropTo(Bounds bounds) {
        moveTo(bounds);
        drop();
        return this;
    }

    @Override
    public FxRobotImpl dropTo(Node node) {
        moveTo(node);
        drop();
        return this;
    }

    @Override
    public FxRobotImpl dropTo(Scene scene) {
        moveTo(scene);
        drop();
        return this;
    }

    @Override
    public FxRobotImpl dropTo(Window window) {
        moveTo(window);
        drop();
        return this;
    }

    @Override
    public FxRobotImpl dropTo(String query) {
        moveTo(query);
        drop();
        return this;
    }

    @Override
    public FxRobotImpl dropTo(Matcher<Object> matcher) {
        moveTo(matcher);
        drop();
        return this;
    }

    @Override
    public <T extends Node> FxRobotImpl dropTo(Predicate<T> predicate) {
        moveTo(predicate);
        drop();
        return this;
    }

    @Override
    public FxRobotImpl dropBy(double x, double y) {
        moveBy(x, y);
        drop();
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF MOVE ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    public FxRobotImpl moveTo(double x, double y) {
        moveRobot.moveTo(pointFor(new Point2D(x, y)));
        return this;
    }

    @Override
    public FxRobotImpl moveTo(Point2D point) {
        moveRobot.moveTo(pointFor(point));
        return this;
    }

    @Override
    public FxRobotImpl moveTo(Bounds bounds) {
        moveRobot.moveTo(pointFor(bounds));
        return this;
    }

    @Override
    public FxRobotImpl moveTo(Node node) {
        moveRobot.moveTo(pointFor(node));
        return this;
    }

    @Override
    public FxRobotImpl moveTo(Scene scene) {
        moveRobot.moveTo(pointFor(scene));
        return this;
    }

    @Override
    public FxRobotImpl moveTo(Window window) {
        moveRobot.moveTo(pointFor(window));
        return this;
    }

    @Override
    public FxRobotImpl moveTo(String query) {
        moveRobot.moveTo(pointFor(query));
        return this;
    }

    @Override
    public FxRobotImpl moveTo(Matcher<Object> matcher) {
        moveRobot.moveTo(pointFor(matcher));
        return this;
    }

    @Override
    public <T extends Node> FxRobotImpl moveTo(Predicate<T> predicate) {
        moveRobot.moveTo(pointFor(predicate));
        return this;
    }

    @Override
    public FxRobotImpl moveBy(double x, double y) {
        moveRobot.moveBy(x, y);
        return this;
    }

}
