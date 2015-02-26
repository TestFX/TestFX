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

import javafx.geometry.Point2D;
import javafx.geometry.Pos;

public interface PointQuery {
    public Point2D getPosition();

    public Point2D getOffset();

    public PointQuery atPosition(Point2D position);

    public PointQuery atPosition(double positionX, double positionY);

    public PointQuery atPosition(Pos position);

    public PointQuery atOffset(Point2D offset);

    public PointQuery atOffset(double offsetX, double offsetY);

    public Point2D query();
}
