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
package org.testfx.service.query;

import org.testfx.service.query.impl.BoundsPointQuery;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;

/**
 * Defines the interface queries to points on the screen must implement.
 * It has a position and a offset to the position. Offsets to the Position can be
 * added by the {@code atOffset} methods.<br>
 * Point queries usually refer to screen coordinates.
 * 
 * @see BoundsPointQuery
 * 
 * @author Jan Ortner
 *
 */
public interface PointQuery {
	/**
	 * Get the position of the point query
	 * @return the position
	 */
    public Point2D getPosition();
    /**
     * Gets the offset of this point query.
     * The resulting point is calculated by adding the offset to the position.
     * @return the offset
     */
    public Point2D getOffset();
    /**
     * Sets the position to the given value.
     * @param position the position
     * @return the query with the position set
     */
    public PointQuery atPosition(Point2D position);
    /**
     * Sets the position to the given value.
     * @param positionX the x value of the position
     * @param positionY the y value of the position
     * @return the query with the position set
     */
    public PointQuery atPosition(double positionX, double positionY);
    /**
     * Sets the position to the given Position within a area. 
     * The value refers to a certain position within the bounds of a object (Top left, top right...).
     * @see BoundsPointQuery
     * @param position the position
     * @return the query with the position set
     */
    public PointQuery atPosition(Pos position);

    /**
     * Adds an offset to this query. The offset is added to the existing offset
     * of this query.
     * @param offset the offset to add
     * @return this query with the offset set
     */
    public PointQuery atOffset(Point2D offset);

    /**
     * Adds an offset to this query. The offset is added to the existing offset
     * of this query.
     * @param offsetX the x offset
     * @param offsetY the y offset
     * @return this query with the offset set
     */
    public PointQuery atOffset(double offsetX, double offsetY);
    /**
     * Gets the resulting Point. The offset is added to the position.
     * @return the point
     */
    public Point2D query();
}
