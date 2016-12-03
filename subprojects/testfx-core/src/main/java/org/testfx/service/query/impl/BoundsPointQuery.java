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
package org.testfx.service.query.impl;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;

import org.testfx.api.annotation.Unstable;
import org.testfx.util.PointQueryUtils;

/**
 * This point query implements a PointQuery within the bounds of an object.
 * The Position ({@link Pos}) can be set to a value within the bounds.
 * 
 * @author Jan Ortner
 *
 */
@Unstable
public class BoundsPointQuery extends PointQueryBase {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private Bounds bounds;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------
    /**
     * Constructor defining the bounds to position the point in.
     * @param bounds the bounds
     */
    public BoundsPointQuery(Bounds bounds) {
        this.bounds = bounds;
    }

    //---------------------------------------------------------------------------------------------
    // GETTER AND SETTER.
    //---------------------------------------------------------------------------------------------

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public Point2D query() {
        Point2D point = pointAtPosition(this.bounds, this.getPosition());
        Point2D offset = getOffset();
        return new Point2D(point.getX() + offset.getX(), point.getY() + offset.getY());
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Point2D pointAtPosition(Bounds bounds,
                                    Point2D position) {
        return PointQueryUtils.atPositionFactors(bounds, position);
    }

}
