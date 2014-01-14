package org.loadui.testfx.robots;

import com.google.common.base.Predicate;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Window;
import org.hamcrest.Matcher;

public interface DragRobot {
    public DragRobot drag(MouseButton... buttons);
    public DragRobot drag(double x, double y, MouseButton... buttons);
    public DragRobot drag(Point2D point, MouseButton... buttons);
    public DragRobot drag(Bounds bounds, MouseButton... buttons);
    public DragRobot drag(Node node, MouseButton... buttons);
    public DragRobot drag(Scene scene, MouseButton... buttons);
    public DragRobot drag(Window window, MouseButton... buttons);
    public DragRobot drag(String query, MouseButton... buttons);
    public DragRobot drag(Matcher<Object> matcher, MouseButton... buttons);
    public <T extends Node> DragRobot drag(Predicate<T> predicate, MouseButton... buttons);

    public DragRobot drop();
    public DragRobot dropTo(double x, double y);
    public DragRobot dropTo(Point2D point);
    public DragRobot dropTo(Bounds bounds);
    public DragRobot dropTo(Node node);
    public DragRobot dropTo(Scene scene);
    public DragRobot dropTo(Window window);
    public DragRobot dropTo(String query);
    public DragRobot dropTo(Matcher<Object> matcher);
    public <T extends Node> DragRobot dropTo(Predicate<T> predicate);

    public DragRobot dropBy(double x, double y);
}
