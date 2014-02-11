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

/**
 * Class that launches an application class and retrieves its primary stage.
 *
 * <p><b>Sample usage:</b></p>
 *
 * <pre><code>
 * AppSetupImpl appSetup = new AppSetupImpl();
 *
 * // Set the application class, launcher implementation and future for primary stage.
 * appSetup.setAppClass(DefaultApplication.class);
 * appSetup.setAppLauncher(new DefaultAppLauncher());
 * appSetup.setStageFuture(stageFuture);
 *
 * // Launch the application if the primary stage was not already retrieved.
 * if (!appSetup.hasPrimaryStage()) {
 *     appSetup.launchApplication();
 * }
 *
 * // Wait until the primary stage from application is retrieved or a timeout expires.
 * Stage primaryStage = appSetup.getPrimaryStage(10, TimeUnit.SECONDS);
 * </code></pre>
 *
 * <p><b>StageFuture and DefaultApplication:</b></p>
 *
 * <pre><code>
 * // The future that holds the primary stage of the application.
 * public static final StageFuture stageFuture = StageFuture.create();
 *
 * // An implementation of application that passes its primary stage to the future.
 * public static class DefaultApplication extends Application {
 *     public void start(Stage primaryStage) throws Exception {
 *         primaryStage.initStyle(StageStyle.UNDECORATED);
 *         primaryStage.setTitle("DefaultApplication: primaryStage");
 *         stageFuture.set(primaryStage);
 *     }
 * }
 * </code></pre>
 */
public class AppSetupImpl implements AppSetup {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    // Application class that is constructed and executed by the appLauncher.
    private Class<? extends Application> appClass;

    // Launcher that constructs and executes the appClass.
    private AppLauncher appLauncher;

    // Future that waits for and holds the primary stage from the application.
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

    /**
     * Launches the application class.
     *
     * <p>This method starts a new thread and passes the application class {@link #appClass} to
     * {@link #appLauncher}.
     *
     * <p>The <b>default launcher</b> starts the mandatory toolkit. Then it calls {@link
     * Application#init()} in its thread and {@link Application#start(Stage)} in the JavaFX thread
     * and does not return until the application hat exited. For this reason the launcher will be
     * called in a new thread. It will throw an IllegalStateException if it is called more than
     * once.
     *
     * @param appArgs the command line arguments passed to the application.
     */
    public void launchApplication(String... appArgs) {
        launchApplicationInThread(appArgs);
    }

    /**
     * Retrieves the primary stage.
     *
     * <p>This method blocks until the primary stage from the application is retrieved or the
     * timeout expires. If the primary stage was already retrieved it will return it directly.
     *
     * @param timeout the duration of timeout
     * @param timeUnit the unit of duration
     * @return the primary stage
     * @throws TimeoutException if the timer expires
     */
    public Stage getPrimaryStage(long timeout, TimeUnit timeUnit) throws TimeoutException {
        return waitForPrimaryStage(timeout, timeUnit);
    }

    /**
     * Checks if the primary stage was retrieved.
     *
     * @return true if the primary stage was retrieved
     */
    public boolean hasPrimaryStage() {
        return isStageFutureDone();
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private void launchApplicationInThread(final String... appArgs) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                launchApplicationImpl(appArgs);
            }
        };
        invokeInThread(runnable);
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
