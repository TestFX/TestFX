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
package org.testfx.api;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseButton;
import javafx.stage.Window;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.hamcrest.Matcher;
import org.testfx.api.annotation.Unstable;
import org.testfx.service.locator.PointLocator;
import org.testfx.service.query.NodeQuery;
import org.testfx.service.query.PointQuery;

import static org.testfx.service.query.impl.NodeQueryUtils.isVisible;

@Unstable(reason = "class was recently added")
public class FxRobot implements FxRobotInterface {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final FxRobotContext context;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public FxRobot() {
        context = new FxRobotContext();
        context.setPointPosition(Pos.CENTER);
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR ROBOT CONTEXT.
    //---------------------------------------------------------------------------------------------

    public FxRobotContext robotContext() {
        return context;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT POSITION.
    //---------------------------------------------------------------------------------------------

    @Override
    public FxRobot pos(Pos pointPosition) {
        context.setPointPosition(pointPosition);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT LOCATION.
    //---------------------------------------------------------------------------------------------

    @Override
    public PointQuery pointFor(double x,
                               double y) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        return pointLocator.pointFor(new Point2D(x, y)).atPosition(pointPosition);
    }

    @Override
    public PointQuery pointFor(Point2D point) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        return pointLocator.pointFor(point).atPosition(pointPosition);
    }

    @Override
    public PointQuery pointFor(Bounds bounds) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        return pointLocator.pointFor(bounds).atPosition(pointPosition);
    }

    @Override
    public PointQuery pointFor(Node node) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        target(node.getScene().getWindow());
        return pointLocator.pointFor(node).atPosition(pointPosition);
    }

    @Override
    public PointQuery pointFor(Scene scene) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        target(scene.getWindow());
        return pointLocator.pointFor(scene).atPosition(pointPosition);
    }

    @Override
    public PointQuery pointFor(Window window) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        target(window);
        return pointLocator.pointFor(window).atPosition(pointPosition);
    }

    @Override
    public PointQuery pointFor(String query) {
        Node node = context.getNodeFinder().nodes(query).queryFirst();
        return pointFor(node).atPosition(context.getPointPosition());
    }

    public PointQuery pointFor(Matcher<Object> matcher) {
        Node node = context.getNodeFinder().nodes(matcher).queryFirst();
        return pointFor(node).atPosition(context.getPointPosition());
    }

    @Override
    public <T extends Node> PointQuery pointFor(Predicate<T> predicate) {
        Node node = context.getNodeFinder().nodes(predicate).queryFirst();
        return pointFor(node).atPosition(context.getPointPosition());
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR POINT OFFSET.
    //---------------------------------------------------------------------------------------------

    @Override
    public PointQuery offset(Point2D point,
                             double offsetX,
                             double offsetY) {
        return pointFor(point).atOffset(offsetX, offsetY);
    }

    @Override
    public PointQuery offset(Bounds bounds,
                             double offsetX,
                             double offsetY) {
        return pointFor(bounds).atOffset(offsetX, offsetY);
    }

    @Override
    public PointQuery offset(Node node,
                             double offsetX,
                             double offsetY) {
        return pointFor(node).atOffset(offsetX, offsetY);
    }

    @Override
    public PointQuery offset(Scene scene,
                             double offsetX,
                             double offsetY) {
        return pointFor(scene).atOffset(offsetX, offsetY);
    }

    @Override
    public PointQuery offset(Window window,
                             double offsetX,
                             double offsetY) {
        return pointFor(window).atOffset(offsetX, offsetY);
    }

    @Override
    public PointQuery offset(String query,
                             double offsetX,
                             double offsetY) {
        return pointFor(query).atOffset(offsetX, offsetY);
    }

    public PointQuery offset(Matcher<Object> matcher,
                             double offsetX,
                             double offsetY) {
        return pointFor(matcher).atOffset(offsetX, offsetY);
    }

    @Override
    public <T extends Node> PointQuery offset(Predicate<T> predicate,
                                              double offsetX,
                                              double offsetY) {
        return pointFor(predicate).atOffset(offsetX, offsetY);
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR WINDOW TARGETING.
    //---------------------------------------------------------------------------------------------

    @Override
    public FxRobot target(Window window) {
        context.getWindowFinder().target(window);
        return this;
    }

    @Override
    public FxRobot target(int windowNumber) {
        context.getWindowFinder().target(windowNumber);
        return this;
    }

    @Override
    public FxRobot target(String stageTitleRegex) {
        context.getWindowFinder().target(stageTitleRegex);
        return this;
    }

    @Override
    public FxRobot target(Scene scene) {
        context.getWindowFinder().target(scene);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS FOR NODE LOOKUP.
    //---------------------------------------------------------------------------------------------

    @Override
    public NodeQuery nodes() {
        return context.getNodeFinder().nodes();
    }

    @Override
    public NodeQuery nodes(String query) {
        return context.getNodeFinder().nodes(query);
    }

    @Override
    public <T extends Node> NodeQuery nodes(Predicate<T> predicate) {
        return context.getNodeFinder().nodes(predicate);
    }

    @Override
    public NodeQuery nodes(Matcher<Object> matcher) {
        return context.getNodeFinder().nodes(matcher);
    }

    @Override
    public NodeQuery nodesFrom(Node... parentNodes) {
        return context.getNodeFinder().nodesFrom(parentNodes);
    }

    @Override
    public NodeQuery nodesFrom(Set<Node> parentNodes) {
        return context.getNodeFinder().nodesFrom(parentNodes);
    }

    @Override
    public NodeQuery nodesFrom(NodeQuery nodeQuery) {
        return context.getNodeFinder().nodesFrom(nodeQuery);
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

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF TYPE ROBOT.
    //---------------------------------------------------------------------------------------------

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
    public FxRobot type(KeyCode keyCode,
                        int times) {
        context.getTypeRobot().type(keyCode, times);
        return this;
    }

    @Override
    public FxRobot eraseText(int amount) {
        return type(KeyCode.BACK_SPACE, amount);
    }

    public FxRobot closeCurrentWindow() {
        return push(KeyCode.ALT, KeyCode.F4).sleep(100);
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF WRITE ROBOT.
    //---------------------------------------------------------------------------------------------

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

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF SLEEP ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    public FxRobot sleep(long milliseconds) {
        context.getSleepRobot().sleep(milliseconds);
        return this;
    }

    @Override
    public FxRobot sleep(long duration,
                         TimeUnit timeUnit) {
        context.getSleepRobot().sleep(duration, timeUnit);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF SCROLL ROBOT.
    //---------------------------------------------------------------------------------------------

    @Deprecated
    public FxRobot scroll(int amount) {
        context.getScrollRobot().scroll(amount);
        return this;
    }

    @Override
    public FxRobot scroll(int amount,
                          VerticalDirection direction) {
        context.getScrollRobot().scroll(amount, direction);
        return this;
    }

    @Override
    public FxRobot scroll(VerticalDirection direction) {
        scroll(1, direction);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF KEYBOARD ROBOT.
    //---------------------------------------------------------------------------------------------

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

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF MOUSE ROBOT.
    //---------------------------------------------------------------------------------------------

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

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF CLICK ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    public FxRobot clickOn(MouseButton... buttons) {
        context.getClickRobot().clickOn(buttons);
        return this;
    }

    @Override
    public FxRobot clickOn(PointQuery pointQuery,
                           MouseButton... buttons) {
        context.getClickRobot().clickOn(pointQuery, buttons);
        return this;
    }

    @Override
    public FxRobot doubleClickOn(MouseButton... buttons) {
        context.getClickRobot().doubleClickOn(buttons);
        return this;
    }

    @Override
    public FxRobot doubleClickOn(PointQuery pointQuery,
                                 MouseButton... buttons) {
        context.getClickRobot().doubleClickOn(pointQuery, buttons);
        return this;
    }

    @Override
    public FxRobot clickOn(double x,
                           double y,
                           MouseButton... buttons) {
        return clickOn(pointFor(x, y), buttons);
    }

    @Override
    public FxRobot clickOn(Point2D point,
                           MouseButton... buttons) {
        return clickOn(pointFor(point), buttons);
    }

    @Override
    public FxRobot clickOn(Bounds bounds,
                           MouseButton... buttons) {
        return clickOn(pointFor(bounds), buttons);
    }

    @Override
    public FxRobot clickOn(Node node,
                           MouseButton... buttons) {
        return clickOn(pointFor(node), buttons);
    }

    @Override
    public FxRobot clickOn(Scene scene,
                           MouseButton... buttons) {
        return clickOn(pointFor(scene), buttons);
    }

    @Override
    public FxRobot clickOn(Window window,
                           MouseButton... buttons) {
        return clickOn(pointFor(window), buttons);
    }

    @Override
    public FxRobot clickOn(String query,
                           MouseButton... buttons) {
        return clickOn(pointFor(query), buttons);
    }

    @Override
    public FxRobot clickOn(Matcher<Object> matcher,
                           MouseButton... buttons) {
        return clickOn(pointFor(matcher), buttons);
    }

    @Override
    public <T extends Node> FxRobot clickOn(Predicate<T> predicate,
                                            MouseButton... buttons) {
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
    public FxRobot rightClickOn(double x,
                                double y) {
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
    public FxRobot doubleClickOn(double x,
                                 double y,
                                 MouseButton... buttons) {
        return doubleClickOn(pointFor(x, y), buttons);
    }

    @Override
    public FxRobot doubleClickOn(Point2D point,
                                 MouseButton... buttons) {
        return doubleClickOn(pointFor(point), buttons);
    }

    @Override
    public FxRobot doubleClickOn(Bounds bounds,
                                 MouseButton... buttons) {
        return doubleClickOn(pointFor(bounds), buttons);
    }

    @Override
    public FxRobot doubleClickOn(Node node,
                                 MouseButton... buttons) {
        return doubleClickOn(pointFor(node), buttons);
    }

    @Override
    public FxRobot doubleClickOn(Scene scene,
                                 MouseButton... buttons) {
        return doubleClickOn(pointFor(scene), buttons);
    }

    @Override
    public FxRobot doubleClickOn(Window window,
                                 MouseButton... buttons) {
        return doubleClickOn(pointFor(window), buttons);
    }

    @Override
    public FxRobot doubleClickOn(String query,
                                 MouseButton... buttons) {
        return doubleClickOn(pointFor(query), buttons);
    }

    @Override
    public FxRobot doubleClickOn(Matcher<Object> matcher,
                                 MouseButton... buttons) {
        return doubleClickOn(pointFor(matcher), buttons);
    }

    @Override
    public <T extends Node> FxRobot doubleClickOn(Predicate<T> predicate,
                                                  MouseButton... buttons) {
        return doubleClickOn(pointFor(predicate), buttons);
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF DRAG ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    public FxRobot drag(MouseButton... buttons) {
        context.getDragRobot().drag(buttons);
        return this;
    }

    @Override
    public FxRobot drag(PointQuery pointQuery,
                        MouseButton... buttons) {
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
    public FxRobot dropBy(double x,
                          double y) {
        context.getDragRobot().dropBy(x, y);
        return this;
    }

    @Override
    public FxRobot drag(double x,
                        double y,
                        MouseButton... buttons) {
        return drag(pointFor(x, y), buttons);
    }

    @Override
    public FxRobot drag(Point2D point,
                        MouseButton... buttons) {
        return drag(pointFor(point), buttons);
    }

    @Override
    public FxRobot drag(Bounds bounds,
                        MouseButton... buttons) {
        return drag(pointFor(bounds), buttons);
    }

    @Override
    public FxRobot drag(Node node,
                        MouseButton... buttons) {
        return drag(pointFor(node), buttons);
    }

    @Override
    public FxRobot drag(Scene scene,
                        MouseButton... buttons) {
        return drag(pointFor(scene), buttons);
    }

    @Override
    public FxRobot drag(Window window,
                        MouseButton... buttons) {
        return drag(pointFor(window), buttons);
    }

    @Override
    public FxRobot drag(String query,
                        MouseButton... buttons) {
        return drag(pointFor(query), buttons);
    }

    @Override
    public FxRobot drag(Matcher<Object> matcher,
                        MouseButton... buttons) {
        return drag(pointFor(matcher), buttons);
    }

    @Override
    public <T extends Node> FxRobot drag(Predicate<T> predicate,
                                         MouseButton... buttons) {
        return drag(pointFor(predicate), buttons);
    }

    @Override
    public FxRobot dropTo(double x,
                          double y) {
        return dropTo(pointFor(x, y));
    }

    @Override
    public FxRobot dropTo(Point2D point) {
        return dropTo(pointFor(point));
    }

    @Override
    public FxRobot dropTo(Bounds bounds) {
        return dropTo(pointFor(bounds));
    }

    @Override
    public FxRobot dropTo(Node node) {
        return dropTo(pointFor(node));
    }

    @Override
    public FxRobot dropTo(Scene scene) {
        return dropTo(pointFor(scene));
    }

    @Override
    public FxRobot dropTo(Window window) {
        return dropTo(pointFor(window));
    }

    @Override
    public FxRobot dropTo(String query) {
        return dropTo(pointFor(query));
    }

    @Override
    public FxRobot dropTo(Matcher<Object> matcher) {
        return dropTo(pointFor(matcher));
    }

    @Override
    public <T extends Node> FxRobot dropTo(Predicate<T> predicate) {
        return dropTo(pointFor(predicate));
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF MOVE ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    public FxRobot moveTo(PointQuery pointQuery) {
        context.getMoveRobot().moveTo(pointQuery);
        return this;
    }

    @Override
    public FxRobot moveBy(double x,
                          double y) {
        context.getMoveRobot().moveBy(x, y);
        return this;
    }

    @Override
    public FxRobot moveTo(double x,
                          double y) {
        return moveTo(pointFor(new Point2D(x, y)));
    }

    @Override
    public FxRobot moveTo(Point2D point) {
        return moveTo(pointFor(point));
    }

    @Override
    public FxRobot moveTo(Bounds bounds) {
        return moveTo(pointFor(bounds));
    }

    @Override
    public FxRobot moveTo(Node node) {
        return moveTo(pointFor(node));
    }

    @Override
    public FxRobot moveTo(Scene scene) {
        return moveTo(pointFor(scene));
    }

    @Override
    public FxRobot moveTo(Window window) {
        return moveTo(pointFor(window));
    }

    @Override
    public FxRobot moveTo(String query) {
        return moveTo(pointFor(query));
    }

    @Override
    public FxRobot moveTo(Matcher<Object> matcher) {
        return moveTo(pointFor(matcher));
    }

    @Override
    public <T extends Node> FxRobot moveTo(Predicate<T> predicate) {
        return moveTo(pointFor(predicate));
    }

    // -----

    public PointQuery _point(String query) {
        NodeQuery nodeQuery = nodes(query);
        Node resultNode = queryFirstNode(nodeQuery, "the query \"" + query + "\"");
        return pointFor(resultNode).atPosition(context.getPointPosition());
    }

    private PointQuery _visiblePoint(String query) {
        NodeQuery nodeQuery = nodes(query);
        Node resultNode = queryFirstVisibleNode(nodeQuery, "the query \"" + query + "\"");
        return pointFor(resultNode).atPosition(context.getPointPosition());
    }

    public FxRobot _moveTo(String query) {
        return moveTo(_visiblePoint(query));
    }

    private Node queryFirstNode(NodeQuery nodeQuery,
                                String queryDescription) {
        Optional<Node> resultNode = nodeQuery.tryQueryFirst();
        if (!resultNode.isPresent()) {
            String message = queryDescription + " returned no nodes.";
            throw new FxRobotException(message);
        }
        return resultNode.get();
    }

    private Node queryFirstVisibleNode(NodeQuery nodeQuery,
                                       String queryDescription) {
        Set<Node> resultNodes = nodeQuery.queryAll();
        if (resultNodes.isEmpty()) {
            String message = queryDescription + " returned no nodes.";
            throw new FxRobotException(message);
        }
        Optional<Node> resultNode = nodesFrom(resultNodes).select(isVisible()).tryQueryFirst();
        if (!resultNode.isPresent()) {
            String message = queryDescription + " returned " + resultNodes.size() + " nodes" +
                ", but no nodes were visible.";
            throw new FxRobotException(message);
        }
        return resultNode.get();
    }

}
