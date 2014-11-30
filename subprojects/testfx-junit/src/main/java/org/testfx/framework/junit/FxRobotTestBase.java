package org.testfx.framework.junit;

import org.junit.BeforeClass;
import org.loadui.testfx.framework.robot.impl.FxRobotImpl;
import org.testfx.api.FxLifecycle;

public abstract class FxRobotTestBase extends FxRobotImpl {

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void internalSetupSpec()
                                  throws Exception {
        FxLifecycle.registerPrimaryStage();
    }

}
