package org.loadui.testfx.service.locator;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

public class BoundsLocator {

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public Bounds screenBoundsFor(Node node) {
        Bounds nodeBounds = sceneBoundsVisibleFor(node);
        return screenBoundsFor(nodeBounds, node.getScene());
    }

    public Bounds screenBoundsFor(Bounds boundsInScene, Scene scene) {
        Bounds sceneBoundsOnScreen = screenBoundsFor(scene);
        double screenX = sceneBoundsOnScreen.getMinX() + boundsInScene.getMinX();
        double screenY = sceneBoundsOnScreen.getMinY() + boundsInScene.getMinY();
        return new BoundingBox(
            screenX, screenY,
            boundsInScene.getWidth(), boundsInScene.getHeight()
        );
    }

    public Bounds screenBoundsFor(Scene scene) {
        Window window = scene.getWindow();
        double screenX = window.getX() + scene.getX();
        double screenY = window.getY() + scene.getY();
        return new BoundingBox(screenX, screenY, scene.getWidth(), scene.getHeight());
    }

    public Bounds screenBoundsFor(Window window) {
        return new BoundingBox(
            window.getX(), window.getY(),
            window.getWidth(), window.getHeight()
        );
    }

    public Bounds sceneBoundsFor(Node node) {
        return node.localToScene(node.getBoundsInLocal());
    }

    public Bounds sceneBoundsVisibleFor(Node node) {
        Bounds nodeBounds = sceneBoundsFor(node);
        Bounds sceneBounds = getSceneBounds(node.getScene());
        return intersectBounds(nodeBounds, sceneBounds);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Bounds getSceneBounds(Scene scene) {
        return new BoundingBox(0, 0, scene.getWidth(), scene.getHeight());
    }

    private Bounds intersectBounds(Bounds a, Bounds b) {
        double minX = Math.max(a.getMinX(), b.getMinX());
        double minY = Math.max(a.getMinY(), b.getMinY());
        double maxX = Math.min(a.getMaxX(), b.getMaxX());
        double maxY = Math.min(a.getMaxY(), b.getMaxY());
        double width = maxX - minX;
        double height = maxY - minY;
        return new BoundingBox(minX, minY, width, height);
    }

}
