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
package org.testfx.service.locator.impl;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import org.testfx.service.locator.BoundsLocator;
import org.testfx.service.locator.BoundsLocatorException;

import static org.testfx.util.BoundsQueryUtils.scale;

public class BoundsLocatorImpl implements BoundsLocator {

    @Override
    public Bounds boundsInSceneFor(Node node) {
        Bounds sceneBounds = node.localToScene(node.getBoundsInLocal());
        return limitToVisibleBounds(sceneBounds, node.getScene());
    }

    @Override
    public Bounds boundsInWindowFor(Scene scene) {
        return new BoundingBox(scene.getX(), scene.getY(), scene.getWidth(), scene.getHeight());
    }

    @Override
    public Bounds boundsInWindowFor(Bounds boundsInScene, Scene scene) {
        Bounds visibleBoundsInScene = limitToVisibleBounds(boundsInScene, scene);
        Bounds windowBounds = boundsInWindowFor(scene);
        return translateBounds(visibleBoundsInScene, windowBounds.getMinX(), windowBounds.getMinY());
    }

    @Override
    public Bounds boundsOnScreenFor(Node node) {
        Bounds sceneBounds = boundsInSceneFor(node);
        return boundsOnScreenFor(sceneBounds, node.getScene());
    }

    @Override
    public Bounds boundsOnScreenFor(Scene scene) {
        Bounds windowBounds = boundsInWindowFor(scene);
        Window window = scene.getWindow();
        return translateBounds(windowBounds, window.getX(), window.getY());
    }

    @Override
    public Bounds boundsOnScreenFor(Window window) {
        return new BoundingBox(
            window.getX(), window.getY(),
            window.getWidth(), window.getHeight()
        );
    }

    @Override
    public Bounds boundsOnScreenFor(Bounds boundsInScene, Scene scene) {
        Bounds windowBounds = boundsInWindowFor(boundsInScene, scene);
        Window window = scene.getWindow();
        Bounds original = translateBounds(windowBounds, window.getX(), window.getY());
        return scale(original);
    }

    private Bounds limitToVisibleBounds(Bounds boundsInScene, Scene scene) {
        Bounds sceneBounds = new BoundingBox(0, 0, scene.getWidth(), scene.getHeight());
        Bounds visibleBounds = intersectBounds(boundsInScene, sceneBounds);
        if (!areBoundsVisible(visibleBounds)) {
            throw new BoundsLocatorException("bounds are not visible in Scene");
        }
        return visibleBounds;
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

    private boolean areBoundsVisible(Bounds bounds) {
        return bounds.getWidth() >= 0 && bounds.getHeight() >= 0; //TODO always true...
    }

    private Bounds translateBounds(Bounds bounds, double x, double y) {
        return new BoundingBox(bounds.getMinX() + x, bounds.getMinY() + y,
                bounds.getWidth(), bounds.getHeight());
    }

}
