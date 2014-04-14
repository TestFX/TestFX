/*
 * Copyright 2013-2014 SmartBear Software
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
package org.loadui.testfx.service.query.impl;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;

import org.loadui.testfx.utils.BoundsUtils;

public class BoundsPointQuery extends PointQueryBase {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private Bounds bounds;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

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

    public Point2D query() {
        return this.pointAtPosition(this.bounds, this.getPosition()).add(this.getOffset());
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Point2D pointAtPosition(Bounds bounds, Point2D position) {
        return BoundsUtils.atPositionFactors(bounds, position);
    }

}
