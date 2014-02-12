package org.loadui.testfx.service.locator.impl;

import java.util.concurrent.Callable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;

public class CallableBoundsPointQuery extends BoundsPointQuery {

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
    public Point2D atPosition(Pos position) {
        setBounds(retrieveUpdatedBounds());
        return super.atPosition(position);
    }

    @Override
    public Point2D atOffset(double x, double y) {
        setBounds(retrieveUpdatedBounds());
        return super.atOffset(x, y);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Bounds retrieveUpdatedBounds() {
        try {
            return callableBounds.call();
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
