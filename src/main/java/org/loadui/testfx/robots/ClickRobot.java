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
