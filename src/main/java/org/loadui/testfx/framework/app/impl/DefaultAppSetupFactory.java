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
package org.loadui.testfx.framework.app.impl;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.loadui.testfx.framework.app.AppSetup;
import org.loadui.testfx.utils.StageFuture;

public class DefaultAppSetupFactory {

    //---------------------------------------------------------------------------------------------
    // STATIC FIELDS.
    //---------------------------------------------------------------------------------------------

    public static final StageFuture stageFuture = StageFuture.create();

    //---------------------------------------------------------------------------------------------
    // STATIC CLASSES.
    //---------------------------------------------------------------------------------------------

    public static class DefaultApplication extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setTitle("DefaultApplication: primaryStage");
            stageFuture.set(primaryStage);
        }
    }

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static AppSetup build() {
        AppSetupImpl appSetup = new AppSetupImpl();
        appSetup.setAppClass(DefaultApplication.class);
        appSetup.setAppLauncher(new FxAppLauncher());
        appSetup.setStageFuture(stageFuture);
        return appSetup;
    }

}
