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

public final class FxApplicationUtils {

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    private FxApplicationUtils() {
        throw new UnsupportedOperationException();
    }

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static void launchApp(Class<? extends Application> appClass, String... appArgs) {
        Application.launch(appClass, appArgs);
    }

    public static void launchAppInThread(final Class<? extends Application> appClass,
                                         final String... appArgs) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                launchApp(appClass, appArgs);
            }
        };
        invokeThread(runnable);
    }

    public static boolean hasPrimaryStage(StageFuture stageFuture) {
        return stageFuture.isDone();
    }

    public static Stage waitForPrimaryStage(long timeout, TimeUnit timeUnit,
                                            StageFuture stageFuture) throws Throwable {
        return stageFuture.get(timeout, timeUnit);
    }

    public static void prepareStage(long timeout, TimeUnit timeUnit,
                                    final Stage stage) throws Throwable {
        int timeoutInSeconds = (int) timeUnit.toSeconds(timeout);
        FxTestUtils.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                showAndBringToFront(stage);
            }
        }, timeoutInSeconds);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static void invokeThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    private static void showAndBringToFront(Stage stage) {
        stage.show();
        stage.toBack();
        stage.toFront();
    }

}
