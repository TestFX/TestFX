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
package org.loadui.testfx.service.locator.impl;

import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import org.loadui.testfx.service.locator.PointQuery;

public class BoundsPointQuery implements PointQuery {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    final Bounds bounds;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public BoundsPointQuery(Bounds bounds) {
        this.bounds = bounds;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public Point2D atPosition(Pos position) {
        return pointForPosition(bounds, position);
    }

    public Point2D atOffset(double x, double y) {
        Point2D point = atPosition(Pos.TOP_LEFT);
        return new Point2D(point.getX() + x, point.getY() + y);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Point2D pointForPosition(Bounds bounds, Pos position) {
        double pointX = computePointX(bounds, position.getHpos());
        double pointY = computePointY(bounds, position.getVpos());
        return new Point2D(pointX, pointY);
    }

    private double computePointX(Bounds bounds, HPos hPos) {
        switch (hPos) {
            case LEFT:
                return bounds.getMinX();
            case CENTER:
                return (bounds.getMinX() + bounds.getMaxX()) / 2;
            case RIGHT:
                return bounds.getMaxX();
            default:
                throw new AssertionError("Unhandled hPos");
        }
    }

    private double computePointY(Bounds bounds, VPos vPos) {
        switch (vPos) {
            case TOP:
                return bounds.getMinY();
            case BASELINE:
            case CENTER:
                return (bounds.getMinY() + bounds.getMaxY()) / 2;
            case BOTTOM:
                return bounds.getMaxY();
            default:
                throw new AssertionError("Unhandled vPos");
        }
    }

}
