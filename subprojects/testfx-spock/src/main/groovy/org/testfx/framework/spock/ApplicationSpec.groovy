/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2017 The TestFX Contributors
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
package org.testfx.framework.spock

import javafx.application.Application
import javafx.application.HostServices
import javafx.application.Preloader
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.stage.Stage
import org.testfx.api.FxRobot
import org.testfx.api.FxToolkit
import org.testfx.api.annotation.Unstable
import spock.lang.Specification

abstract class ApplicationSpec extends Specification implements ApplicationFixture {

    @Delegate
    private final FxRobot robot = new FxRobot()

    @Unstable(reason = "is missing apidocs")
    public static void launch(Class<? extends Application> appClass,
                              String... appArgs) throws Exception {
        FxToolkit.registerPrimaryStage()
        FxToolkit.setupApplication(appClass, appArgs)
    }

    def setup() {
        internalBefore()
    }

    def cleanup() {
        internalAfter()
    }

    @Unstable(reason = "is missing apidocs")
    public final void internalBefore() throws Exception {
        FxToolkit.registerPrimaryStage()
        FxToolkit.setupApplication( { new ApplicationAdapter(this) })
    }

    @Unstable(reason = "is missing apidocs")
    public final void internalAfter() throws Exception {
        // release all keys
        release(new KeyCode[0])
        // release all mouse buttons
        release(new MouseButton[0])
        FxToolkit.cleanupApplication(new ApplicationAdapter(this))
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public void init() throws Exception {}

    @Override
    @Unstable(reason = "is missing apidocs")
    public abstract void start(Stage stage) throws Exception

    @Override
    @Unstable(reason = "is missing apidocs")
    public void stop() throws Exception {}

    @Deprecated
    public final HostServices getHostServices() {
        throw new UnsupportedOperationException()
    }

    @Deprecated
    public final Application.Parameters getParameters() {
        throw new UnsupportedOperationException()
    }

    @Deprecated
    public final void notifyPreloader(Preloader.PreloaderNotification notification) {
        throw new UnsupportedOperationException()
    }
}
