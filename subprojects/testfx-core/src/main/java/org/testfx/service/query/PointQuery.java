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
package org.testfx.service.query;

import java.util.Optional;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;

import org.testfx.robot.Motion;

/**
 * Used to calculate a position within a given {@link javafx.geometry.Bounds}.
 */
public interface PointQuery {

    /**
     *
     * @return the position that stores the {@code x} and {@code y} percentages (0.0 = 0% to 1.0 = 100%)
     * to use when calculating a relative position within a {@code Bounds} object.
     */
    Point2D getPosition();

    /**
     *
     * @return the amount by which to offset the point calculated via {@link #getPosition()}.
     */
    Point2D getOffset();

    /**
     * Updates {@link #getPosition()} to the new {@code position}.
     *
     * @param position the new position
     * @return itself
     */
    PointQuery atPosition(Point2D position);

    /**
     * Updates {@link #getPosition()} to the new {@code position}.
     *
     * @param positionX the percentage to use: 0.0 (0%) to 1.0 (100%).
     * @param positionY the percentage to use: 0.0 (0%) to 1.0 (100%).
     * @return itself
     */
    PointQuery atPosition(double positionX, double positionY);

    /**
     * Updates {@link #getPosition()} to a new one based on the given {@code position}.
     *
     * @param position left/up = 0.0 (0%); center = 0.5 (50%); right/down = 1.0 (100%)
     * @return itself
     */
    PointQuery atPosition(Pos position);

    /**
     * Updates {@link #getOffset()} to be
     * {@code new Point2D(this.offset.getX() + offset.getX(), this.offset.getY() + offset.getY())}.
     *
     * @param offset the amount by which to increase/decrease the offset's x and y values
     * @return itself
     */
    PointQuery atOffset(Point2D offset);

    /**
     * Updates {@link #getOffset()} by the combination of the current {@code offset}'s {@code x} value and
     * {@code offsetX} and its {@code y} value and {@code offsetY}.
     *
     * @param offsetX the amount by which to increase/decrease the offset's x value
     * @param offsetY the amount by which to increase/decrease the offset's y value
     * @return itself
     */
    PointQuery atOffset(double offsetX, double offsetY);

    /**
     *
     * @return a position that offsets the relative position within the initial {@code Bounds}object that is calculated
     * via {@link #getPosition()} by {@link #getOffset()} amount.
     */
    Point2D query();

    PointQuery onNode(Node node);

    Optional<Motion> queryMotion();
}
