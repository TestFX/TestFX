package org.loadui.testfx.robots;

import com.google.common.base.Predicate;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;
import org.hamcrest.Matcher;

public interface MoveRobot {
    public MoveRobot moveTo(double x, double y);

    public MoveRobot moveTo(Point2D point);
    public MoveRobot moveTo(Bounds bounds);
    public MoveRobot moveTo(Node node);
    public MoveRobot moveTo(Scene scene);
    public MoveRobot moveTo(Window window);

    public MoveRobot moveTo(String query);
    public MoveRobot moveTo(Matcher<Object> matcher);
    public MoveRobot moveTo(Predicate<Node> predicate);

    public MoveRobot moveBy(double x, double y);
}
