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
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import org.testfx.service.locator.BoundsLocator;
import org.testfx.service.locator.PointLocator;
import org.testfx.service.query.PointQuery;
import org.testfx.service.query.impl.BoundsPointQuery;
import org.testfx.service.query.impl.CallableBoundsPointQuery;

public class PointLocatorImpl implements PointLocator {

    private final BoundsLocator boundsLocator;

    public PointLocatorImpl(BoundsLocator boundsLocator) {
        this.boundsLocator = boundsLocator;
    }

    @Override
    public PointQuery point(Bounds bounds) {
        return new BoundsPointQuery(bounds);
    }

    @Override
    public PointQuery point(Point2D point) {
        return new BoundsPointQuery(new BoundingBox(point.getX(), point.getY(), 0, 0));
    }

    @Override
    public PointQuery point(Node node) {
        return new CallableBoundsPointQuery(() -> boundsLocator.boundsOnScreenFor(node), node);
    }

    @Override
    public PointQuery point(Scene scene) {
        return new CallableBoundsPointQuery(() -> boundsLocator.boundsOnScreenFor(scene));
    }

    @Override
    public PointQuery point(Window window) {
        return new CallableBoundsPointQuery(() -> boundsLocator.boundsOnScreenFor(window));
    }

}
