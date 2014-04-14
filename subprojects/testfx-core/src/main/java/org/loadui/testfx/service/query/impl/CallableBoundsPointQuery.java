package org.loadui.testfx.service.query.impl;

import java.util.concurrent.Callable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;

import org.loadui.testfx.service.query.PointQuery;

public class CallableBoundsPointQuery extends PointQueryBase {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private Callable<Bounds> callableBounds;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public CallableBoundsPointQuery(Callable<Bounds> callableBounds) {
        this.callableBounds = callableBounds;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public Point2D query() {
        Bounds bounds = fetchCallableBounds();
        PointQuery boundsQuery = new BoundsPointQuery(bounds)
            .atPosition(getPosition())
            .atOffset(getOffset());
        return boundsQuery.query();
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Bounds fetchCallableBounds() {
        try {
            return callableBounds.call();
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
