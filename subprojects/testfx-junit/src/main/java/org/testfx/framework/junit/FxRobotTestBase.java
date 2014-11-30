package org.testfx.framework.junit;

import java.awt.GraphicsEnvironment;

import org.junit.BeforeClass;
import org.loadui.testfx.framework.robot.impl.FxRobotImpl;
import org.testfx.api.FxLifecycle;

import static org.junit.Assume.assumeFalse;

public abstract class FxRobotTestBase extends FxRobotImpl {

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void internalSetupSpec()
                                  throws Exception {
        assumeFalse(
            "Cannot run JavaFX in headless environment",
            GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance()
        );
        FxLifecycle.registerPrimaryStage();
    }

}
