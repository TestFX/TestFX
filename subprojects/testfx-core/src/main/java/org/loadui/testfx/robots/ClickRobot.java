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
import javafx.scene.input.MouseButton;
import javafx.stage.Window;
import org.hamcrest.Matcher;

public interface ClickRobot {
    public ClickRobot click(MouseButton... buttons);
    public ClickRobot click(double x, double y, MouseButton... buttons);
    public ClickRobot click(Point2D point, MouseButton... buttons);
    public ClickRobot click(Bounds bounds, MouseButton... buttons);
    public ClickRobot click(Node node, MouseButton... buttons);
    public ClickRobot click(Scene scene, MouseButton... buttons);
    public ClickRobot click(Window window, MouseButton... buttons);
    public ClickRobot click(String query, MouseButton... buttons);
    public ClickRobot click(Matcher<Object> matcher, MouseButton... buttons);
    public <T extends Node> ClickRobot click(Predicate<T> predicate, MouseButton... buttons);

    public ClickRobot rightClick();
    public ClickRobot rightClick(double x, double y);
    public ClickRobot rightClick(Point2D point);
    public ClickRobot rightClick(Bounds bounds);
    public ClickRobot rightClick(Node node);
    public ClickRobot rightClick(Scene scene);
    public ClickRobot rightClick(Window window);
    public ClickRobot rightClick(String query);
    public ClickRobot rightClick(Matcher<Object> matcher);
    public <T extends Node> ClickRobot rightClick(Predicate<T> predicate);

    public ClickRobot doubleClick(MouseButton... buttons);
    public ClickRobot doubleClick(double x, double y, MouseButton... buttons);
    public ClickRobot doubleClick(Point2D point, MouseButton... buttons);
    public ClickRobot doubleClick(Bounds bounds, MouseButton... buttons);
    public ClickRobot doubleClick(Node node, MouseButton... buttons);
    public ClickRobot doubleClick(Scene scene, MouseButton... buttons);
    public ClickRobot doubleClick(Window window, MouseButton... buttons);
    public ClickRobot doubleClick(String query, MouseButton... buttons);
    public ClickRobot doubleClick(Matcher<Object> matcher, MouseButton... buttons);
    public <T extends Node> ClickRobot doubleClick(Predicate<T> predicate, MouseButton... buttons);
}
