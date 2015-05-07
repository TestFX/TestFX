/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.loadui.testfx.framework.junit;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.stage.Stage;

import org.loadui.testfx.framework.app.AppSetup;
import org.loadui.testfx.framework.app.StageSetupCallback;
import org.loadui.testfx.framework.app.impl.ToolkitAppSetupFactory;
import org.loadui.testfx.framework.app.impl.StageSetupImpl;
import org.loadui.testfx.framework.robot.impl.FxRobotImpl;

public abstract class AppRobotTestBase extends FxRobotImpl {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    static final int APP_SETUP_TIMEOUT_IN_SECONDS = Integer.parseInt(System.getProperty("testfx.app_setup_timeout_in_seconds","25"));
    static final int STAGE_SETUP_TIMEOUT_IN_SECONDS = Integer.parseInt(System.getProperty("testfx.stage_setup_timeout_in_seconds","5"));
    static final int INVOKE_TIMEOUT_IN_SECONDS = Integer.parseInt(System.getProperty("testfx.invoke_timeout_in_seconds","5"));

    //---------------------------------------------------------------------------------------------
    // STATIC PROTECTED FIELDS.
    //---------------------------------------------------------------------------------------------

    protected static final AppSetup appSetup = ToolkitAppSetupFactory.build();
    protected static final StageSetupImpl stageSetup = new StageSetupImpl();

    protected static Stage primaryStage;

    //---------------------------------------------------------------------------------------------
    // STATIC PROTECTED METHODS.
    //---------------------------------------------------------------------------------------------

    protected static void setupApplication() throws TimeoutException {
        if (!appSetup.hasPrimaryStage()) {
            appSetup.launchApplication();
        }
        primaryStage = appSetup.getPrimaryStage(APP_SETUP_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
    }

    protected static void setupStages(StageSetupCallback callback) throws TimeoutException {
        //targetWindow(primaryStage);
        stageSetup.setPrimaryStage(primaryStage);
        stageSetup.setCallback(callback);
        stageSetup.invokeCallbackAndWait(STAGE_SETUP_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        stageSetup.showPrimaryStage(STAGE_SETUP_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
    }

    protected static void invokeAndWait(Runnable runnable) throws TimeoutException {
        stageSetup.invokeAndWait(INVOKE_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS, runnable);
    }

}
