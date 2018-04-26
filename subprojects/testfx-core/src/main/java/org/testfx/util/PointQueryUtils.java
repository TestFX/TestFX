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
package org.testfx.util;

import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;

public final class PointQueryUtils {

    private PointQueryUtils() {}

    /**
     *
     * @param bounds the given bounds
     * @param position the position within the bounds
     * @return a point somewhere in the given bounds whose x and y values are determined by
     * passing the given {@code position} to {@link #computePositionFactors(Pos)}.
     */
    public static Point2D atPosition(Bounds bounds, Pos position) {
        return atPositionFactors(bounds, computePositionFactors(position));
    }

    /**
     * Returns the point within the given bounds computed using the given {@code positionFactors}.
     * <p>
     * The x-component of the returned point is computed using:
     * <p>
     * {@code x = bounds.x + positionFactors.x * bounds.width}
     * <p>
     * and analogously the y-component of the returned point is computed using:
     * {@code y = bounds.y + positionFactors.y * bounds.height}
     *
     * @param bounds the given bounds
     * @param positionFactors a {@code Point2D} object whose x and y values represent percentages
     * (0.0 = 0% and 1.0 = 100%). As an example, an x value of 0 will return {@link Bounds#getMinX()},
     * 1.0 will return {@link Bounds#getMaxX()}, and 0.5 will return
     * {@code bounds.getMinX() + (bounds.getWidth() * positionFactors.getX()}.
     * @return the point somewhere within the given bounds whose x and y values are determined by the given
     * {@code positionFactors}.
     */
    public static Point2D atPositionFactors(Bounds bounds, Point2D positionFactors) {
        double pointX = lerp(bounds.getMinX(), bounds.getWidth(), positionFactors.getX());
        double pointY = lerp(bounds.getMinY(), bounds.getHeight(), positionFactors.getY());
        return new Point2D(pointX, pointY);
    }

    /**
     * Computes the width/height factors for the point defined by the given {@code position}.
     * <p>
     * The width factor is returned in the x component and the height factor in the y component of the point.
     * That is, {@link Pos#TOP_LEFT} has a width and height factor of {@literal 0.0}, whereas {@link Pos#BOTTOM_RIGHT}
     * has a width and height factor of {@literal 1.0}.
     *
     * @param position the position to compute width/height factors for
     * @return a {@code Point2D} that can be used as a {@code positionFactor} object in
     * {@link #atPositionFactors(Bounds, Point2D)}.
     */
    public static Point2D computePositionFactors(Pos position) {
        double positionX = computePositionX(position.getHpos());
        double positionY = computePositionY(position.getVpos());
        return new Point2D(positionX, positionY);
    }

    private static double lerp(double start, double distance, double factor) {
        return start + (distance * factor);
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
                throw new RuntimeException("unhandled hPos: " + hPos);
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
                throw new RuntimeException("unhandled vPos: " + vPos);
        }
    }

}
