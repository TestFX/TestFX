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
    public MoveRobot moveTo(Predicate<Node> predicate);

    /**
     * Moves the mouse cursor relatively to its current position.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     */
    public MoveRobot moveBy(double x, double y);

}
