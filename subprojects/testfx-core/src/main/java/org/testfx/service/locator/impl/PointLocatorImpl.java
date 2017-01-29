/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.service.locator.impl;

import java.util.concurrent.Callable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import org.testfx.api.annotation.Unstable;
import org.testfx.service.locator.BoundsLocator;
import org.testfx.service.locator.PointLocator;
import org.testfx.service.query.PointQuery;
import org.testfx.service.query.impl.BoundsPointQuery;
import org.testfx.service.query.impl.CallableBoundsPointQuery;

@Unstable
public class PointLocatorImpl implements PointLocator {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    private BoundsLocator boundsLocator;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public PointLocatorImpl(BoundsLocator boundsLocator) {
        this.boundsLocator = boundsLocator;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public PointQuery point(Bounds bounds) {
        return new BoundsPointQuery(bounds);
    }

    @Override
    public PointQuery point(Point2D point) {
        Bounds bounds = new BoundingBox(point.getX(), point.getY(), 0, 0);
        return new BoundsPointQuery(bounds);
    }

    @Override
    public PointQuery point(Node node) {
        Callable<Bounds> callable = callableBoundsFor(node);
        return new CallableBoundsPointQuery(callable, node);
    }

    @Override
    public PointQuery point(Scene scene) {
        Callable<Bounds> callable = callableBoundsFor(scene);
        return new CallableBoundsPointQuery(callable);
    }

    @Override
    public PointQuery point(Window window) {
        Callable<Bounds> callable = callableBoundsFor(window);
        return new CallableBoundsPointQuery(callable);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Callable<Bounds> callableBoundsFor(final Node node) {
        return () -> boundsLocator.boundsOnScreenFor(node);
    }

    private Callable<Bounds> callableBoundsFor(final Scene scene) {
        return () -> boundsLocator.boundsOnScreenFor(scene);
    }

    private Callable<Bounds> callableBoundsFor(final Window window) {
        return () -> boundsLocator.boundsOnScreenFor(window);
    }

}
