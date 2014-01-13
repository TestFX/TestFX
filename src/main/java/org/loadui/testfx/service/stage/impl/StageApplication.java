/*
 * Copyright 2013 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the Licence for the specific language governing permissions and limitations
 * under the Licence.
 */
package org.loadui.testfx.service.stage.impl;

import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.google.common.util.concurrent.SettableFuture;

public class StageApplication extends Application {

    //---------------------------------------------------------------------------------------------
    // STATIC FIELDS.
    //---------------------------------------------------------------------------------------------

    public static SettableFuture<Stage> primaryStageFuture = SettableFuture.create();

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static void launchInThread() {
        // TODO: Document what happens if the next line is missing.
        // java.lang.IllegalStateException: Application launch must not be called more than once
        if (!primaryStageFuture.isDone()) {
            // TODO: Document what happens if the next line is missing.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Application.launch(StageApplication.class);
                }
            }).start();
        }
    }

    public static Stage waitForPrimaryStage(long timeout, TimeUnit timeUnit) throws Throwable {
        // Blocks until the task is complete or the timeout expires.
        // Throws a `TimeoutException` if the timer expires.
        return primaryStageFuture.get(timeout, timeUnit);
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStageFuture.set(primaryStage);
    }

}
