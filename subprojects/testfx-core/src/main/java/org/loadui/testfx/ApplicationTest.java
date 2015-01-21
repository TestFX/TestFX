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
package org.loadui.testfx;

import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.testfx.api.FxToolkit;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.TimeoutException;

public abstract class ApplicationTest extends FxTest {

    protected Application demoApplication = null;

    protected Stage primaryStage = null;
    protected Stage targetStage = null;

    @Before
    public void internalSetup() throws Exception {
        primaryStage = FxToolkit.registerPrimaryStage();
        targetStage = FxToolkit.registerTargetStage(() -> new Stage());

        // Setup, show and cleanup Application.
        demoApplication = FxToolkit.setupApplication(getApplicationClass());
    }

    @After
    public void cleanup() throws TimeoutException {
        WaitForAsyncUtils.asyncFx(() -> targetStage.close());
        WaitForAsyncUtils.asyncFx(() -> primaryStage.close());

        FxToolkit.cleanupApplication(demoApplication);
    }


    protected abstract Class<? extends Application> getApplicationClass();
}
