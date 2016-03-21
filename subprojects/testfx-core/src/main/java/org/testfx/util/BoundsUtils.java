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
package org.testfx.util;

import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;

import org.testfx.api.annotation.Unstable;

@Unstable
public final class BoundsUtils {

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    private BoundsUtils() {
        throw new UnsupportedOperationException();
    }

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static Point2D atPosition(Bounds bounds,
                                     Pos position) {
        return atPositionFactors(bounds, computePositionFactors(position));
    }

    public static Point2D atPositionFactors(Bounds bounds,
                                            Point2D positionFactors) {
        double pointX = lerp(bounds.getMinX(), bounds.getMaxX(), positionFactors.getX());
        double pointY = lerp(bounds.getMinY(), bounds.getMaxY(), positionFactors.getY());
        return new Point2D(pointX, pointY);
    }

    public static Point2D computePositionFactors(Pos position) {
        double positionX = computePositionX(position.getHpos());
        double positionY = computePositionY(position.getVpos());
        return new Point2D(positionX, positionY);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static double lerp(double start,
                               double end,
                               double factor) {
        return start + ((end - start) * factor);
    }

    private static double computePositionX(HPos hPos) {
        switch (hPos) {
            case LEFT:
                return 0.0;
            case CENTER:
                return 0.5;
            case RIGHT:
                return 1.0;
            default:
                throw new AssertionError("Unhandled hPos");
        }
    }

    private static double computePositionY(VPos vPos) {
        switch (vPos) {
            case TOP:
                return 0.0;
            case BASELINE:
            case CENTER:
                return 0.5;
            case BOTTOM:
                return 1.0;
            default:
                throw new AssertionError("Unhandled vPos");
        }
    }

}
