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
package org.testfx.api;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import com.google.common.collect.ImmutableSet;
import org.testfx.api.annotation.Unstable;
import org.testfx.toolkit.ApplicationLauncher;
import org.testfx.toolkit.ApplicationService;
import org.testfx.toolkit.ToolkitService;
import org.testfx.toolkit.impl.ApplicationLauncherImpl;
import org.testfx.toolkit.impl.ApplicationServiceImpl;
import org.testfx.toolkit.impl.ToolkitServiceImpl;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.testfx.service.adapter.JavaVersionAdapter.getWindows;
import static org.testfx.util.WaitForAsyncUtils.waitFor;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

/**
 * Responsible for setup and cleanup of JavaFX fixtures that need the JavaFX thread.
 *
 * <p><b>Overview</b></p>
 *
 * <p>This class methods cover three different kinds of fixtures:</p>
 *
 * <ol>
 * <li>Container fixtures, which are registered as {@code registeredStage}.</li>
 * <li>Content fixtures, which are attached to the registered {@code registeredStage}.</li>
 * <li>Individual fixtures, which do not require a {@code registeredStage}.</li>
 * </ol>
 *
 * <p>Additionally it keeps an internal context.</p>
 *
 * <p><b>1. Container Fixtures</b></p>
 *
 * <p>They can be registered as {@code registeredStage} and provide a top-level container, i.e.
 * {@link Stage}s.</p>
 *
 * <p>The primary stage can be registered as {@code registeredStage} using
 * {@link #registerPrimaryStage}. This call is mandatory before any other JavaFX fixture can be
 * created.</p>
 *
 * <p>Other stages can be registered as {@code registeredStage}  using {@link #registerStage
 * registerStage(Supplier&lt;Stage&gt;)}.</p>
 *
 * <p><b>2. Content Fixtures</b></p>
 *
 * <p>They can be attached to the {@code registeredStage}.</p>
 *
 * <p>Either constructed by calling an {@link Application#start Application.start()}, by
 * supplying {@link Scene}s, {@link Parent}s, or by consuming a {@link Stage}.</p>
 *
 * <p>Use: {@link #setupStage setupStage(Consumer&lt;Stage&gt;)},
 * {@link #setupApplication setupApplication(Class&lt;? extends Application&gt;)},
 * {@link #setupScene setupScene(Supplier&lt;Scene&gt;)} or
 * {@link #setupSceneRoot setupSceneRoot(Supplier&lt;Parent&gt;)}</p>
 *
 * <p><b>3. Individual Fixtures</b></p>
 *
 * <p>To setup individual Stages, Scenes or Nodes use {@link #setupFixture(Runnable)} and
 * {@link #setupFixture(Callable)}.</p>
 *
 * <p><b>Internal Context</b></p>
 *
 * <p>Is internally responsible for handle the registered Stage for attachments,
 * handle timeouts, provide the Application for the Toolkit launch and execute the setup
 * in the JavaFX thread. The primary Stage is constructed by the platform.</p>
 */
@Unstable(reason = "class was recently added")
public final class FxToolkit {

    //---------------------------------------------------------------------------------------------
    // STATIC FIELDS.
    //---------------------------------------------------------------------------------------------

    private static final ApplicationLauncher APP_LAUNCHER = new ApplicationLauncherImpl();

    private static final ApplicationService APP_SERVICE = new ApplicationServiceImpl();

    private static final FxToolkitContext CONTEXT = new FxToolkitContext();

    private static final ToolkitService SERVICE = new ToolkitServiceImpl(APP_LAUNCHER, APP_SERVICE);

    //---------------------------------------------------------------------------------------------
    // PRIVATE CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    private FxToolkit() {
        throw new UnsupportedOperationException();
    }

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    // REGISTER STAGES (CONTAINERS).

    /**
     * Sets up the {@link org.testfx.toolkit.PrimaryStageApplication} to use in tests, prevents it from shutting
     * down when the last window is closed, and returns the {@link Stage} from {@link Application#start(Stage)}.
     *
     * @throws TimeoutException if execution is not finished before {@link FxToolkitContext#getLaunchTimeoutInMillis()}
     */
    public static Stage registerPrimaryStage()
            throws TimeoutException {
        Stage primaryStage = waitForLaunch(
                SERVICE.setupPrimaryStage(
                        CONTEXT.getPrimaryStageFuture(),
                        CONTEXT.getApplicationClass(),
                        CONTEXT.getApplicationArgs()
                )
        );
        CONTEXT.setRegisteredStage(primaryStage);
        preventShutdownWhenLastWindowIsClosed();
        return primaryStage;
    }

    /**
     * Runs the stageSupplier on the {@code JavaFX Application Thread}, registers the supplied stage,
     * and returns that stage.
     *
     * @throws TimeoutException if execution is not finished before {@link FxToolkitContext#getSetupTimeoutInMillis()}
     */
    public static Stage registerStage(Supplier<Stage> stageSupplier)
            throws TimeoutException {
        Stage stage = setupFixture(stageSupplier::get);
        CONTEXT.setRegisteredStage(stage);
        return stage;
    }

    // SETUP REGISTERED STAGES (CONTENTS).

    /**
     * Sets up the registered stage by passing it into the given {@code stageConsumer} on the
     * {@code JavaFX Application Thread} and returns the stage once finished.
     *
     * @throws TimeoutException if execution is not finished before {@link FxToolkitContext#getSetupTimeoutInMillis()}
     */
    public static Stage setupStage(Consumer<Stage> stageConsumer)
            throws TimeoutException {
        return waitForSetup(
                SERVICE.setupStage(
                        CONTEXT.getRegisteredStage(),
                        stageConsumer
                )
        );
    }

    /**
     * Sets up the given application with its given arguments and returns that application once finished.
     *
     * @throws TimeoutException if execution is not finished before {@link FxToolkitContext#getSetupTimeoutInMillis()}
     */
    public static Application setupApplication(Class<? extends Application> applicationClass,
                                               String... applicationArgs)
            throws TimeoutException {
        return waitForSetup(
                SERVICE.setupApplication(
                        CONTEXT::getRegisteredStage,
                        applicationClass,
                        applicationArgs
                )
        );
    }

    /**
     * Sets up the supplied application and returns that application once finished.
     *
     * @throws TimeoutException if execution is not finished before {@link FxToolkitContext#getSetupTimeoutInMillis()}
     */
    public static Application setupApplication(Supplier<Application> applicationSupplier)
            throws TimeoutException {
        return waitForSetup(
                SERVICE.setupApplication(
                        CONTEXT::getRegisteredStage,
                        applicationSupplier
                )
        );
    }

    /**
     * Performs the clean up of the application. This is done by calling
     * {@link ToolkitService#cleanupApplication(Application)} (which usually
     * calls the {@code stop} method of the application).
     * @param application the application to clean up
     * @throws TimeoutException if cleanup is not finished before {@link FxToolkitContext#getSetupTimeoutInMillis()}
     *      or the FX Application Thread is not running
     */
    @Unstable(reason = "is missing apidocs")
    public static void cleanupApplication(Application application)
            throws TimeoutException {
        if (isFXApplicationThreadRunning()) {
            waitForSetup(SERVICE.cleanupApplication(application));
        } else {
            throw new TimeoutException("FX Application Thread not running");
        }
    }

    /**
     * Runs the {@code sceneSupplier} on the {@code JavaFX Application Thread}, sets the registered stage's scene to the
     * supplied scene, and returns the supplied scene once finished.
     *
     * @throws TimeoutException if execution is not finished before {@link FxToolkitContext#getSetupTimeoutInMillis()}
     */
    public static Scene setupScene(Supplier<Scene> sceneSupplier)
            throws TimeoutException {
        return waitForSetup(
                SERVICE.setupScene(
                        CONTEXT.getRegisteredStage(),
                        sceneSupplier
                )
        );
    }

    /**
     * Runs the {@code sceneRootSupplier} on the {@code JavaFX Application Thread}, sets the registered stage's scene's
     * root node to the supplied root node, and returns the supplied root node once finished.
     */
    public static Parent setupSceneRoot(Supplier<Parent> sceneRootSupplier)
            throws TimeoutException {
        return waitForSetup(
                SERVICE.setupSceneRoot(
                        CONTEXT.getRegisteredStage(),
                        sceneRootSupplier
                )
        );
    }

    // UTILITY METHODS.
    /**
     * Runs the given {@code runnable} on the {@code JavaFX Application Thread} and returns once finished.
     */
    public static void setupFixture(Runnable runnable)
            throws TimeoutException {
        waitForSetup(
                SERVICE.setupFixture(runnable)
        );
    }

    /**
     * Runs the given {@code callable} on the {@code JavaFX Application Thread} and returns once finished.
     * before returning.
     */
    public static <T> T setupFixture(Callable<T> callable)
            throws TimeoutException {
        return waitForSetup(
                SERVICE.setupFixture(callable)
        );
    }

    /**
     * Runs on the {@code JavaFX Application Thread}: Shows the registered stage via {@link Stage#show()},
     * moves it to the front via {@link Stage#toFront()}, and returns once finished.
     */
    public static void showStage()
            throws TimeoutException {
        setupStage(FxToolkit::showStage);
    }

    /**
     * Runs on the {@code JavaFX Application Thread}: Hides the registered stage via {@link Stage#hide()}
     * and returns once finished.
     */
    public static void hideStage()
            throws TimeoutException {
        setupStage(FxToolkit::hideStage);
    }

    /**
     * Runs on the {@code JavaFX Application Thread}: Hides all windows returned from
     * {@link org.testfx.service.adapter.JavaVersionAdapter#getWindows()} and returns once finished.
     */
    public static void cleanupStages()
            throws TimeoutException {
        setupFixture(() -> fetchAllWindows().forEach(FxToolkit::hideWindow));
    }

    // INTERNAL CONTEXT.

    /**
     * Returns the internal context
     */
    public static FxToolkitContext toolkitContext() {
        return CONTEXT;
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    /**
     * Waits for the given future to be set before returning or times out after
     * {@link FxToolkitContext#getLaunchTimeoutInMillis() launch timeout limit} is reached.
     */
    private static <T> T waitForLaunch(Future<T> future)
            throws TimeoutException {
        return waitFor(CONTEXT.getLaunchTimeoutInMillis(), MILLISECONDS, future);
    }

    /**
     * Waits for the given future to be set before returning or times out after
     * {@link FxToolkitContext#getSetupTimeoutInMillis()} setup timeout limit} is reached.
     */
    private static <T> T waitForSetup(Future<T> future)
            throws TimeoutException {
        T ret = waitFor(CONTEXT.getSetupTimeoutInMillis(), MILLISECONDS, future);
        waitForFxEvents();
        return ret;
    }

    private static void showStage(Stage stage) {
        stage.show();
        stage.toBack();
        stage.toFront();
    }

    private static void hideStage(Stage stage) {
        stage.hide();
    }

    private static void hideWindow(Window window) {
        window.hide();
    }

    @SuppressWarnings("deprecation")
    private static Set<Window> fetchAllWindows() {
        return ImmutableSet.copyOf(getWindows());
    }

    private static void preventShutdownWhenLastWindowIsClosed() {
        Platform.setImplicitExit(false);
    }

    /**
     * Detects if the JavaFx Application Thread is currently running.
     * @return true, if the FX Application Thread is running
     */
    public static boolean isFXApplicationThreadRunning() {
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        for (Thread thread : threads) {
            if (thread.getName().equals("JavaFX Application Thread")) {
                return true;
            }
        }
        return false;
    }

}
