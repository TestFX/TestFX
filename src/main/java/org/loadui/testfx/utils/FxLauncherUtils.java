/*
 * Copyright 2013 SmartBear Software
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
package org.loadui.testfx.utils;

import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.stage.Stage;

public final class FxLauncherUtils {

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    private FxLauncherUtils() {
        throw new UnsupportedOperationException();
    }

    //---------------------------------------------------------------------------------------------
    // STATIC FIELDS.
    //---------------------------------------------------------------------------------------------

    private static final StageFuture stageFuture = StageFuture.create();

    //---------------------------------------------------------------------------------------------
    // STATIC CLASSES.
    //---------------------------------------------------------------------------------------------

    public static class FxLauncherApplication extends Application {
        @Override
        public void start(Stage stage) throws Exception {
            stageFuture.set(stage);
        }
    }

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static Stage launchOnce(long stageTimeout, TimeUnit timeUnit,
                                   String... appArgs) throws Throwable {
        if (!FxApplicationUtils.hasPrimaryStage(stageFuture)) {
            FxApplicationUtils.launchAppInThread(FxLauncherApplication.class, appArgs);
        }
        Stage primaryStage = FxApplicationUtils.waitForPrimaryStage(stageTimeout,
            timeUnit, stageFuture);
        FxApplicationUtils.prepareStage(stageTimeout, timeUnit, primaryStage);
        return primaryStage;
    }

}
