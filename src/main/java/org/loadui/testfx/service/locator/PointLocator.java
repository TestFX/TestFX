/*
 * Copyright 2013 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
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
        Bounds bounds = boundsLocator.boundsOnScreenFor(node);
        return pointFor(bounds);
    }

    public PointQuery pointFor(Scene scene) {
        Bounds bounds = boundsLocator.boundsOnScreenFor(scene);
        return pointFor(bounds);
    }

    public PointQuery pointFor(Window window) {
        Bounds bounds = boundsLocator.boundsOnScreenFor(window);
        return pointFor(bounds);
    }

}
