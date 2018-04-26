/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2018 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.util;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import org.testfx.internal.JavaVersionAdapter;

public final class BoundsQueryUtils {

    private BoundsQueryUtils() {}

    /**
     * Creates a new {@link Bounds} object with the given parameters.
     */
    public static Bounds bounds(double minX, double minY, double width, double height) {
        return new BoundingBox(minX, minY, width, height);
    }

    /**
     * Creates a new {@link Bounds} object whose top-left corner is the given point and whose width and height
     * are 0.
     */
    public static Bounds bounds(Point2D point) {
        return bounds(point.getX(), point.getY(), 0, 0);
    }

    /**
     * Creates a new {@link Bounds} object whose top-left corner is 0 and whose width and height are
     * {@code dimension.getWidth()} and {@code dimension.getHeight()}, respectively.
     */
    public static Bounds bounds(Dimension2D dimension) {
        return bounds(0, 0, dimension.getWidth(), dimension.getHeight());
    }

    /**
     * Converts the given region to a {@link Bounds} object.
     */
    public static Bounds bounds(Rectangle2D region) {
        return bounds(region.getMinX(), region.getMinY(), region.getWidth(), region.getHeight());
    }

    /**
     * Bounds of Scene in Window.
     */
    public static Bounds bounds(Scene scene) {
        return bounds(scene.getX(), scene.getY(), scene.getWidth(), scene.getHeight());
    }

    /**
     * Bounds of Window on Screen.
     */
    public static Bounds bounds(Window window) {
        return bounds(window.getX(), window.getY(), window.getWidth(), window.getHeight());
    }

    /**
     * Retrieve the logical bounds (geom) of a Node.
     */
    public static Bounds nodeBounds(Node node) {
        return node.getLayoutBounds();
    }

    /**
     * Retrieve the physical untransformed bounds (geom + effect + clip) of a Node.
     */
    public static Bounds nodeBoundsInLocal(Node node) {
        return node.getBoundsInLocal();
    }

    /**
     * Retrieve the physical transformed bounds (geom + effect + clip + transform) of a Node.
     */
    public static Bounds nodeBoundsInParent(Node node) {
        return node.getBoundsInParent();
    }

    /**
     * Retrieves the physical untransformed bounds (geom + effect + clip) of a Node before transforming
     * that to the node's Scene's coordinate system.
     */
    public static Bounds nodeBoundsInScene(Node node) {
        return node.localToScene(node.getBoundsInLocal());
    }

    /**
     * Retrieves the physical untransformed bounds (geom + effect + clip) of a Node before transforming that
     * to the screen's coordinate system.
     */
    public static Bounds boundsOnScreen(Node node) {
        Bounds boundsInScene = nodeBoundsInScene(node);
        // Bounds visibleBoundsInScene = limitToVisibleBounds(boundsInScene, node.getScene());
        return boundsOnScreen(boundsInScene, node.getScene());
    }

    /**
     * Transforms the given bounds in the given scene to the screen's coordinate system.
     */
    public static Bounds boundsOnScreen(Bounds boundsInScene, Scene scene) {
        Bounds sceneBoundsInWindow = bounds(scene);
        Bounds windowBoundsOnScreen = bounds(scene.getWindow());
        return translateBounds(boundsInScene, byOffset(
                sceneBoundsInWindow.getMinX() + windowBoundsOnScreen.getMinX(),
                sceneBoundsInWindow.getMinY() + windowBoundsOnScreen.getMinY()));
    }

    /**
     * Translates the given bounds in the given window to the screen's coordinate system
     */
    public static Bounds boundsOnScreen(Bounds boundsInWindow, Window window) {
        Bounds windowBoundsOnScreen = bounds(window);
        return translateBounds(boundsInWindow, byOffset(
                windowBoundsOnScreen.getMinX(), windowBoundsOnScreen.getMinY()));
    }

    /**
     * Translates the given bounds in the screen to a relative coordinate system where the given screenRegion's
     * top-left corner represents coordinate (0, 0).
     */
    public static Bounds boundsOnScreen(Bounds boundsOnScreen, Rectangle2D screenRegion) {
        return translateBounds(boundsOnScreen, byOffset(screenRegion.getMinX(), screenRegion.getMinY()));
    }

    public static Bounds scale(Bounds bounds) {
        double scaleX = JavaVersionAdapter.getScreenScaleX();
        double scaleY = JavaVersionAdapter.getScreenScaleY();
        if (scaleX != 1d || scaleY != 1d) {
            return new BoundingBox(bounds.getMinX() * scaleX,
                    bounds.getMinY() * scaleY,
                    bounds.getMinZ(),
                    bounds.getWidth() * scaleX,
                    bounds.getHeight() * scaleY,
                    bounds.getDepth());
        }
        return bounds;
    }

    private static Bounds limitToVisibleBounds(Bounds boundsInScene, Scene scene) {
        Bounds sceneBounds = makeSceneBounds(scene);
        Bounds visibleBounds = intersectBounds(boundsInScene, sceneBounds);
        if (!areBoundsVisible(visibleBounds)) {
            throw new RuntimeException("bounds are not visible in Scene.");
        }
        return visibleBounds;
    }

    private static Bounds makeSceneBounds(Scene scene) {
        return new BoundingBox(0, 0, scene.getWidth(), scene.getHeight());
    }

    private static Bounds intersectBounds(Bounds a, Bounds b) {
        double minX = Math.max(a.getMinX(), b.getMinX());
        double minY = Math.max(a.getMinY(), b.getMinY());
        double maxX = Math.min(a.getMaxX(), b.getMaxX());
        double maxY = Math.min(a.getMaxY(), b.getMaxY());
        double width = maxX - minX;
        double height = maxY - minY;
        return new BoundingBox(minX, minY, width, height);
    }

    private static boolean areBoundsVisible(Bounds bounds) {
        return !bounds.isEmpty();
    }

    private static Bounds translateBounds(Bounds bounds, Point2D offset) {
        return new BoundingBox(
                bounds.getMinX() + offset.getX(),
                bounds.getMinY() + offset.getY(),
                bounds.getWidth(),
                bounds.getHeight());
    }

    private static Point2D byOffset(double x, double y) {
        return new Point2D(x, y);
    }

}
