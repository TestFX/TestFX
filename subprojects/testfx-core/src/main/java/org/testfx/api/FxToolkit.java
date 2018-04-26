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

import org.testfx.toolkit.ApplicationLauncher;
import org.testfx.toolkit.ApplicationService;
import org.testfx.toolkit.ToolkitService;
import org.testfx.toolkit.impl.ApplicationLauncherImpl;
import org.testfx.toolkit.impl.ApplicationServiceImpl;
import org.testfx.toolkit.impl.ToolkitServiceImpl;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.testfx.internal.JavaVersionAdapter.getWindows;
import static org.testfx.util.WaitForAsyncUtils.waitFor;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

/**
 * Responsible for setup and cleanup of JavaFX fixtures that need the JavaFX thread.
 * <p>
 * <h3>Overview</h3>
 * <p>
 * This class methods cover three different kinds of fixtures:
 * <ol>
 * <li>Container fixtures, which are registered as {@code registeredStage}.</li>
 * <li>Content fixtures, which are attached to the registered {@code registeredStage}.</li>
 * <li>Individual fixtures, which do not require a {@code registeredStage}.</li>
 * </ol>
 * Additionally it keeps an internal context.
 * <p>
 * <h4>1. Container Fixtures</h4>
 * <p>
 * They can be registered as {@code registeredStage} and provide a top-level container, i.e.
 * {@link Stage}s.
 * <p>
 * The primary stage can be registered as {@code registeredStage} using {@link #registerPrimaryStage}.
 * This call is mandatory before any other JavaFX fixture can be created.
 * <p>
 * Other stages can be registered as {@code registeredStage}  using {@link #registerStage
 * registerStage(Supplier&lt;Stage&gt;)}.
 * <p>
 * <h4>2. Content Fixtures</h4>
 * <p>
 * They can be attached to the {@code registeredStage}.
 * <p>
 * Either constructed by calling an {@link Application#start Application.start()}, by
 * supplying {@link Scene}s, {@link Parent}s, or by consuming a {@link Stage}.
 * <p>
 * Use: {@link #setupStage setupStage(Consumer&lt;Stage&gt;)},
 * {@link #setupApplication setupApplication(Class&lt;? extends Application&gt;)},
 * {@link #setupScene setupScene(Supplier&lt;Scene&gt;)} or
 * {@link #setupSceneRoot setupSceneRoot(Supplier&lt;Parent&gt;)}
 * <p>
 * <h4>3. Individual Fixtures</h4>
 * <p>
 * To setup individual Stages, Scenes or Nodes use {@link #setupFixture(Runnable)} and
 * {@link #setupFixture(Callable)}.
 * <p>
 * <h4>Internal Context</h4>
 * <p>
 * Is internally responsible for handle the registered Stage for attachments,
 * handle timeouts, provide the Application for the Toolkit launch and execute the setup
 * in the JavaFX thread. The primary Stage is constructed by the platform.
 */
public final class FxToolkit {

    private static final ApplicationLauncher APP_LAUNCHER = new ApplicationLauncherImpl();
    private static final ApplicationService APP_SERVICE = new ApplicationServiceImpl();
    private static final FxToolkitContext CONTEXT = new FxToolkitContext();
    private static final ToolkitService SERVICE = new ToolkitServiceImpl(APP_LAUNCHER, APP_SERVICE);

    private FxToolkit() {}

    /**
     * Sets up the {@link org.testfx.toolkit.PrimaryStageApplication} to use in tests, prevents it from shutting
     * down when the last window is closed, and returns the {@link Stage} from {@link Application#start(Stage)}.
     *
     * @throws TimeoutException if execution is not finished before {@link FxToolkitContext#getLaunchTimeoutInMillis()}
     */
    public static Stage registerPrimaryStage() throws TimeoutException {
        Stage primaryStage = waitFor(CONTEXT.getLaunchTimeoutInMillis(), MILLISECONDS,
                SERVICE.setupPrimaryStage(CONTEXT.getPrimaryStageFuture(),
                        CONTEXT.getApplicationClass(), CONTEXT.getApplicationArgs()));
        CONTEXT.setRegisteredStage(primaryStage);
        Platform.setImplicitExit(false);
        return primaryStage;
    }

    /**
     * Runs the stageSupplier on the {@code JavaFX Application Thread}, registers the supplied stage,
     * and returns that stage.
     *
     * @throws TimeoutException if execution is not finished before {@link FxToolkitContext#getSetupTimeoutInMillis()}
     */
    public static Stage registerStage(Supplier<Stage> stageSupplier) throws TimeoutException {
        Stage stage = setupFixture(stageSupplier::get);
        CONTEXT.setRegisteredStage(stage);
        return stage;
    }

    /**
     * Sets up the registered stage by passing it into the given {@code stageConsumer} on the
     * {@code JavaFX Application Thread} and returns the stage once finished.
     *
     * @throws TimeoutException if execution is not finished before {@link FxToolkitContext#getSetupTimeoutInMillis()}
     */
    public static Stage setupStage(Consumer<Stage> stageConsumer) throws TimeoutException {
        return waitForSetup(SERVICE.setupStage(CONTEXT.getRegisteredStage(), stageConsumer));
    }

    /**
     * Sets up the given application with its given arguments and returns that application once finished.
     *
     * @throws TimeoutException if execution is not finished before {@link FxToolkitContext#getSetupTimeoutInMillis()}
     */
    public static Application setupApplication(Class<? extends Application> applicationClass, String... applicationArgs)
            throws TimeoutException {
        return waitForSetup(SERVICE.setupApplication(CONTEXT::getRegisteredStage, applicationClass, applicationArgs));
    }

    /**
     * Sets up the supplied application and returns that application once finished.
     *
     * @throws TimeoutException if execution is not finished before {@link FxToolkitContext#getSetupTimeoutInMillis()}
     */
    public static Application setupApplication(Supplier<Application> applicationSupplier) throws TimeoutException {
        return waitForSetup(SERVICE.setupApplication(CONTEXT::getRegisteredStage, applicationSupplier));
    }

    /**
     * Performs the clean up of the application. This is done by calling
     * {@link ToolkitService#cleanupApplication(Application)} (which usually
     * calls the {@code stop} method of the application).
     *
     * @param application the application to clean up
     * @throws TimeoutException if cleanup is not finished before {@link FxToolkitContext#getSetupTimeoutInMillis()}
     *      or the FX Application Thread is not running
     */
    public static void cleanupApplication(Application application) throws TimeoutException {
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
    public static Scene setupScene(Supplier<Scene> sceneSupplier) throws TimeoutException {
        return waitForSetup(SERVICE.setupScene(CONTEXT.getRegisteredStage(), sceneSupplier));
    }

    /**
     * Runs the {@code sceneRootSupplier} on the {@code JavaFX Application Thread}, sets the registered stage's scene's
     * root node to the supplied root node, and returns the supplied root node once finished.
     */
    public static Parent setupSceneRoot(Supplier<Parent> sceneRootSupplier) throws TimeoutException {
        return waitForSetup(SERVICE.setupSceneRoot(CONTEXT.getRegisteredStage(), sceneRootSupplier));
    }
    /**
     * Runs the given {@code runnable} on the {@code JavaFX Application Thread} and returns once finished.
     */
    public static void setupFixture(Runnable runnable) throws TimeoutException {
        waitForSetup(SERVICE.setupFixture(runnable));
    }

    /**
     * Runs the given {@code callable} on the {@code JavaFX Application Thread} and returns once finished.
     * before returning.
     */
    public static <T> T setupFixture(Callable<T> callable) throws TimeoutException {
        return waitForSetup(SERVICE.setupFixture(callable));
    }

    /**
     * Runs on the {@code JavaFX Application Thread}: Shows the registered stage via {@link Stage#show()},
     * moves it to the front via {@link Stage#toFront()}, and returns once finished.
     */
    public static void showStage() throws TimeoutException {
        setupStage(stage -> {
            stage.show();
            stage.toBack();
            stage.toFront();
        });
    }

    /**
     * Runs on the {@code JavaFX Application Thread}: Hides the registered stage via {@link Stage#hide()}
     * and returns once finished.
     */
    public static void hideStage() throws TimeoutException {
        setupStage(Window::hide);
    }

    /**
     * Runs on the {@code JavaFX Application Thread}: Hides all windows returned from
     * {@link org.testfx.internal.JavaVersionAdapter#getWindows()} and returns once finished.
     */
    public static void cleanupStages() throws TimeoutException {
        setupFixture(() -> getWindows().forEach(Window::hide));
    }

    /**
     * Returns the internal context.
     */
    public static FxToolkitContext toolkitContext() {
        return CONTEXT;
    }

    /**
     * Waits for the given future to be set before returning or times out after
     * {@link FxToolkitContext#getSetupTimeoutInMillis()} is reached.
     */
    private static <T> T waitForSetup(Future<T> future) throws TimeoutException {
        T ret = waitFor(CONTEXT.getSetupTimeoutInMillis(), MILLISECONDS, future);
        waitForFxEvents();
        return ret;
    }

    /**
     * Detects if the JavaFx Application Thread is currently running.
     * @return {@literal true} if the FX Application Thread is running, false otherwise
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
