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
package org.testfx.service.query.impl;

import java.util.concurrent.Callable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;

import org.testfx.service.query.PointQuery;

public class CallableBoundsPointQuery extends PointQueryBase {

    private final Callable<Bounds> callableBounds;

    public CallableBoundsPointQuery(Callable<Bounds> callableBounds) {
        this(callableBounds, null);
    }

    public CallableBoundsPointQuery(Callable<Bounds> callableBounds, Node node) {
        this.callableBounds = callableBounds;
        this.node = node;
    }
    @Override
    public Point2D query() {
        Bounds bounds = fetchCallableBounds();
        PointQuery boundsQuery = new BoundsPointQuery(bounds)
            .atPosition(getPosition())
            .atOffset(getOffset());
        return boundsQuery.query();
    }

    private Bounds fetchCallableBounds() {
        try {
            return callableBounds.call();
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
