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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.stage.Stage;

import org.loadui.testfx.framework.app.StageSetup;
import org.loadui.testfx.framework.app.StageSetupCallback;
import org.loadui.testfx.utils.FXTestUtils;

public class StageSetupImpl implements StageSetup {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private Stage primaryStage;
    private StageSetupCallback callback;

    //---------------------------------------------------------------------------------------------
    // GETTER AND SETTER.
    //---------------------------------------------------------------------------------------------

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public StageSetupCallback getCallback() {
        return callback;
    }

    public void setCallback(StageSetupCallback callback) {
        this.callback = callback;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public void invokeCallbackAndWait(long timeout, TimeUnit timeUnit) throws TimeoutException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                callStageSetup();
            }
        };
        invokeAndWait(timeout, timeUnit, runnable);
    }

    public void showPrimaryStage(long timeout, TimeUnit timeUnit) throws TimeoutException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                showStage(primaryStage);
                bringStageToFront(primaryStage);
            }
        };
        invokeAndWait(timeout, timeUnit, runnable);
    }

    public void invokeAndWait(long timeout, TimeUnit timeUnit,
                              Runnable runnable) throws TimeoutException {
        int timeoutInSeconds = (int) timeUnit.toSeconds(timeout);
        try {
            FXTestUtils.invokeAndWait(runnable, timeoutInSeconds);
        }
        catch (TimeoutException exception) {
            throw exception;
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private void callStageSetup() {
        callback.setupStages(primaryStage);
    }

    private void showStage(Stage stage) {
        stage.show();
    }

    private void bringStageToFront(Stage stage) {
        stage.toBack();
        stage.toFront();
    }

}
