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
package org.testfx.framework.junit5;

import javafx.application.Application;
import javafx.application.Application.Parameters;
import javafx.application.HostServices;
import javafx.application.Preloader.PreloaderNotification;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

public abstract class ApplicationTest extends FxRobot implements ApplicationFixture {

    public static void launch(Class<? extends Application> appClass, String... appArgs) throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(appClass, appArgs);
    }

    @BeforeEach
    public final void internalBefore() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(() -> new ApplicationAdapter(this));
    }

    @AfterEach
    public final void internalAfter() throws Exception {
        // release all keys
        release(new KeyCode[0]);
        // release all mouse buttons
        release(new MouseButton[0]);
        FxToolkit.cleanupApplication(new ApplicationAdapter(this));
    }

    @Override
    public void init() throws Exception {}

    @Override
    public void start(Stage stage) throws Exception {}

    @Override
    public void stop() throws Exception {}

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
