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
package org.testfx.framework.junit;

import java.util.concurrent.Semaphore;
import javafx.application.Application;
import javafx.application.Application.Parameters;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.application.Preloader.PreloaderNotification;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.api.annotation.Unstable;
import org.testfx.toolkit.ApplicationFixture;

@Unstable(reason = "might be renamed to ApplicationTestBase")
public abstract class ApplicationTest extends FxRobot implements ApplicationFixture {

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    @Unstable(reason = "is missing apidocs")
    public static void launch(Class<? extends Application> appClass,
                              String... appArgs) throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(appClass, appArgs);
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    @Unstable(reason = "is missing apidocs")
    public final void internalBefore()
                              throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(this);
    }

    @After
    @Unstable(reason = "is missing apidocs")
    public final void internalAfter()
                             throws Exception {
        FxToolkit.cleanupApplication(this);
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public void init()
              throws Exception {}

    @Override
    @Unstable(reason = "is missing apidocs")
    public abstract void start(Stage stage)
                        throws Exception;

    @Override
    @Unstable(reason = "is missing apidocs")
    public void stop()
              throws Exception {}

    public void waitForRunLater() throws InterruptedException {
        final Semaphore semaphore = new Semaphore(0);
        Platform.runLater(() -> semaphore.release());
        semaphore.acquire();
    }

    @Deprecated
    public final HostServices getHostServices() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final Parameters getParameters() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final void notifyPreloader(PreloaderNotification notification) {
        throw new UnsupportedOperationException();
    }

}
