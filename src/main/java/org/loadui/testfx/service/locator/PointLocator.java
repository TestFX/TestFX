package org.loadui.testfx.service.locator;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

public class PointLocator {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    private BoundsLocator boundsLocator;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public PointLocator(BoundsLocator boundsLocator) {
        this.boundsLocator = boundsLocator;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public PointQuery pointFor(Bounds bounds) {
        return new BoundsQuery(bounds);
    }

    public PointQuery pointFor(Point2D point) {
        Bounds bounds = new BoundingBox(point.getX(), point.getY(), 0, 0);
        return pointFor(bounds);
    }

    public PointQuery pointFor(Node node) {
        Bounds bounds = boundsLocator.screenBoundsFor(node);
        return pointFor(bounds);
    }

    public PointQuery pointFor(Scene scene) {
        Bounds bounds = boundsLocator.screenBoundsFor(scene);
        return pointFor(bounds);
    }

    public PointQuery pointFor(Window window) {
        Bounds bounds = boundsLocator.screenBoundsFor(window);
        return pointFor(bounds);
    }

}
