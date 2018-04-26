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
package org.testfx.service.query.impl;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;

import org.testfx.util.PointQueryUtils;

/**
 * This point query implements a PointQuery within the bounds of an object.
 * <p>
 * The {@code position} ({@link javafx.geometry.Pos}) can be set to a value within the bounds.
 */
public class BoundsPointQuery extends PointQueryBase {

    private Bounds bounds;

    public BoundsPointQuery(Bounds bounds) {
        this.bounds = bounds;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    @Override
    public Point2D query() {
        Point2D point = PointQueryUtils.atPositionFactors(bounds, getPosition());
        Point2D offset = getOffset();
        return new Point2D(point.getX() + offset.getX(), point.getY() + offset.getY());
    }

}
