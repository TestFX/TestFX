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
package org.loadui.testfx.robots;

import com.google.common.base.Predicate;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;
import org.hamcrest.Matcher;

public interface MoveRobot {

    /**
     * Moves the mouse cursor to the given coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     */
    public MoveRobot moveTo(double x, double y);

    /**
     * Moves the mouse cursor to the given Point2D.
     *
     * @param point the Point2D
     */
    public MoveRobot moveTo(Point2D point);

    /**
     * Moves the mouse cursor to the given Bounds.
     *
     * @param bounds the Bounds
     */
    public MoveRobot moveTo(Bounds bounds);

    /**
     * Moves the mouse cursor to the given Node.
     *
     * @param node the Node
     */
    public MoveRobot moveTo(Node node);

    /**
     * Moves the mouse cursor to the given Scene.
     *
     * @param scene the Scene
     */
    public MoveRobot moveTo(Scene scene);

    /**
     * Moves the mouse cursor to the given Window.
     *
     * @param window the Window
     */
    public MoveRobot moveTo(Window window);

    /**
     * Moves the mouse cursor to the Node of the given query.
     *
     * @param query the query
     */
    public MoveRobot moveTo(String query);

    /**
     * Moves the mouse cursor to the Node of the given Matcher.
     *
     * @param matcher the Matcher
     */
    public MoveRobot moveTo(Matcher<Object> matcher);

    /**
     * Moves the mouse cursor to the Node of the given Predicate.
     *
     * @param predicate the Predicate
     */
    public <T extends Node> MoveRobot moveTo(Predicate<T> predicate);

    /**
     * Moves the mouse cursor relatively to its current position.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     */
    public MoveRobot moveBy(double x, double y);

}
