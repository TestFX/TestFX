package org.loadui.testfx.service.locator;

import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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

    public Point2D pointFor(Bounds bounds, Pos position) {
        return pointForBoundsPosition(bounds, position);
    }

    public Point2D pointFor(Node node, Pos position) {
        Bounds nodeBounds = boundsLocator.sceneBoundsVisibleFor(node);
        Bounds screenBounds = boundsLocator.screenBoundsFor(nodeBounds, node.getScene());
        return pointFor(screenBounds, position);
    }

    public Point2D pointFor(Scene scene, Pos position) {
        Bounds screenBounds = boundsLocator.screenBoundsFor(scene);
        return pointFor(screenBounds, position);
    }

    public Point2D pointFor(Window window, Pos position) {
        Bounds screenBounds = boundsLocator.screenBoundsFor(window);
        return this.pointFor(screenBounds, position);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Point2D pointForBoundsPosition(Bounds bounds, Pos position) {
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
