package org.loadui.testfx.service.locator;

import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;

public class PointQuery {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    final Bounds bounds;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public PointQuery(Bounds bounds) {
        this.bounds = bounds;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public Point2D atPosition(Pos position) {
        return this.pointForPosition(this.bounds, position);
    }

    public Point2D atOffset(double x, double y) {
        Point2D point = this.atPosition(Pos.TOP_LEFT);
        return new Point2D(point.getX() + x, point.getY() + y);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Point2D pointForPosition(Bounds bounds, Pos position) {
        double pointX = 0;
        double pointY = 0;

        if (position.getHpos() == HPos.LEFT) {
            pointX = bounds.getMinX();
        }
        else if (position.getHpos() == HPos.CENTER) {
            pointX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        }
        else if (position.getHpos() == HPos.RIGHT) {
            pointX = bounds.getMaxX();
        }

        if (position.getVpos() == VPos.TOP) {
            pointY = bounds.getMinY();
        }
        else if (position.getVpos() == VPos.CENTER || position.getVpos() == VPos.BASELINE) {
            pointY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        }
        else if (position.getVpos() == VPos.BOTTOM) {
            pointY = bounds.getMaxY();
        }

        return new Point2D(pointX, pointY);
    }

}
