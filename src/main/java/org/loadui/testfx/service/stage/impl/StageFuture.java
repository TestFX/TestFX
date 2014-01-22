package org.loadui.testfx.service.stage.impl;

import javafx.stage.Stage;
import com.google.common.util.concurrent.AbstractFuture;

public class StageFuture extends AbstractFuture<Stage> {

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static StageFuture create() {
        return new StageFuture();
    }

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    private StageFuture() {}

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public boolean set(Stage stage) {
        return super.set(stage);
    }

    @Override
    public boolean setException(Throwable throwable) {
        return super.setException(throwable);
    }

}
