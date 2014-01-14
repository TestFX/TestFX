package org.loadui.testfx.service.locator;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;

public interface PointQuery {
    public Point2D atPosition(Pos position);
    public Point2D atOffset(double x, double y);
}
