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

import javafx.geometry.Point2D;
import javafx.geometry.Pos;

import org.testfx.api.annotation.Unstable;
import org.testfx.service.query.PointQuery;
import org.testfx.util.PointQueryUtils;

@Unstable
public abstract class PointQueryBase implements PointQuery {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private Point2D position = new Point2D(0, 0);

    private Point2D offset = new Point2D(0, 0);

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public Point2D getOffset() {
        return offset;
    }

    @Override
    public PointQuery atPosition(Point2D position) {
        this.position = position;
        return this;
    }

    @Override
    public PointQuery atPosition(double positionX,
                                 double positionY) {
        return atPosition(new Point2D(positionX, positionY));
    }

    @Override
    public PointQuery atPosition(Pos position) {
        return atPosition(PointQueryUtils.computePositionFactors(position));
    }

    @Override
    public PointQuery atOffset(Point2D offset) {
        this.offset = new Point2D(
            this.offset.getX() + offset.getX(),
            this.offset.getY() + offset.getY()
        );
        return this;
    }

    @Override
    public PointQuery atOffset(double offsetX,
                               double offsetY) {
        return atOffset(new Point2D(offsetX, offsetY));
    }

}
