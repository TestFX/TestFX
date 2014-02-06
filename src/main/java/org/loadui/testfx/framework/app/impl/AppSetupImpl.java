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

import javafx.application.Application;
import javafx.stage.Stage;

import org.loadui.testfx.framework.app.AppSetup;
import org.loadui.testfx.framework.app.AppLauncher;
import org.loadui.testfx.utils.StageFuture;

public class AppSetupImpl implements AppSetup {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private Class<? extends Application> appClass;
    private AppLauncher appLauncher;
    private StageFuture stageFuture;

    //---------------------------------------------------------------------------------------------
    // GETTER AND SETTER.
    //---------------------------------------------------------------------------------------------

    public Class<? extends Application> getAppClass() {
        return appClass;
    }

    public void setAppClass(Class<? extends Application> appClass) {
        this.appClass = appClass;
    }

    public AppLauncher getAppLauncher() {
        return appLauncher;
    }

    public void setAppLauncher(AppLauncher appLauncher) {
        this.appLauncher = appLauncher;
    }

    public StageFuture getStageFuture() {
        return stageFuture;
    }

    public void setStageFuture(StageFuture stageFuture) {
        this.stageFuture = stageFuture;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public void launchApplication(String... appArgs) {
        launchApplicationInThreadOnce(appArgs);
    }

    public Stage getPrimaryStage(long timeout, TimeUnit timeUnit) throws TimeoutException {
        return waitForPrimaryStage(timeout, timeUnit);
    }

    public boolean hasPrimaryStage() {
        return isStageFutureDone();
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private void launchApplicationInThreadOnce(final String... appArgs) {
        if (!hasPrimaryStage()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    launchApplicationImpl(appArgs);
                }
            };
            invokeInThread(runnable);
        }
    }

    private void launchApplicationImpl(String... appArgs) {
        appLauncher.launch(appClass, appArgs);
    }

    private void invokeInThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    private Stage waitForPrimaryStage(long timeout, TimeUnit timeUnit) throws TimeoutException {
        try {
            return stageFuture.get(timeout, timeUnit);
        }
        catch (TimeoutException exception) {
            throw exception;
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private boolean isStageFutureDone() {
        return stageFuture.isDone();
    }

}
