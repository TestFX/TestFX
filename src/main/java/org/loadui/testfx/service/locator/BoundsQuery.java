package org.loadui.testfx.service.locator;

import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;

public class BoundsQuery implements PointQuery {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    final Bounds bounds;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public BoundsQuery(Bounds bounds) {
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
        double pointX = this.computePointX(bounds, position.getHpos());
        double pointY = this.computePointY(bounds, position.getVpos());
        return new Point2D(pointX, pointY);
    }

    private double computePointX(Bounds bounds, HPos hPos) {
        switch (hPos) {
            case LEFT:
                return bounds.getMinX();
            case CENTER:
                return (bounds.getMinX() + bounds.getMaxX()) / 2;
            case RIGHT:
                return bounds.getMaxX();
            default:
                throw new AssertionError("Unhandled hPos");
        }
    }

    private double computePointY(Bounds bounds, VPos vPos) {
        switch (vPos) {
            case TOP:
                return bounds.getMinY();
            case BASELINE:
            case CENTER:
                return (bounds.getMinY() + bounds.getMaxY()) / 2;
            case BOTTOM:
                return bounds.getMaxY();
            default:
                throw new AssertionError("Unhandled vPos");
        }
    }

}
