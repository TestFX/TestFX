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
package org.testfx.robot;

import org.testfx.service.query.PointQuery;

public interface MoveRobot {

    /**
     * Moves the mouse directly to the (x,y) position specified by the given
     * {@link PointQuery#query()}.
     *
     * @param pointQuery the pointQuery to move to
     */
    default void moveTo(PointQuery pointQuery) {
        moveTo(pointQuery, Motion.DEFAULT);
    }

    /**
     * Moves the mouse to the (x,y) position specified by the given {@link PointQuery#query()}
     * using the specified {@code motion} (see: {@link Motion}) and clicks whatever is under it.
     *
     * @param pointQuery the pointQuery to move to
     * @param motion the type of motion to use for movement
     */
    void moveTo(PointQuery pointQuery, Motion motion);

    /**
     * Moves the mouse directly (see: {@link Motion#DIRECT} from the current location to the
     * given ({@code x}, {@code y}) location.
     *
     * @param x the amount by which to directly move the mouse horizontally
     * @param y the amount by which to directly move the mouse vertically
     */
    default void moveBy(double x, double y) {
        moveBy(x, y, Motion.DEFAULT);
    }

    /**
     * Moves the mouse from the current location to the given ({@code x}, {@code y}) location.
     * The movement is done using the given {@link Motion}.
     *
     * @param x the amount by which to move the mouse horizontally
     * @param y the amount by which to move the mouse vertically
     * @param motion the type of motion to use for movement
     */
    void moveBy(double x, double y, Motion motion);

}
