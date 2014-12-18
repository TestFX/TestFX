/*
 * Copyright 2013-2014 SmartBear Software
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
package org.testfx.api;

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

import com.google.common.annotations.Beta;
import org.testfx.toolkit.ApplicationLauncher;
import org.testfx.toolkit.ApplicationService;
import org.testfx.toolkit.ToolkitService;
import org.testfx.toolkit.impl.ApplicationLauncherImpl;
import org.testfx.toolkit.impl.ApplicationServiceImpl;
import org.testfx.toolkit.impl.ToolkitServiceImpl;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

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
 * {@link #setupApplication setupApplication(Class&lt;? extends Application&gt;)</?>},
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
@Beta
public class FxToolkit {

    //---------------------------------------------------------------------------------------------
    // STATIC FIELDS.
    //---------------------------------------------------------------------------------------------

    private static final ApplicationLauncher appLauncher = new ApplicationLauncherImpl();

    private static final ApplicationService appService = new ApplicationServiceImpl();

    private static final FxToolkitContext context = new FxToolkitContext();

    private static final ToolkitService service = new ToolkitServiceImpl(appLauncher, appService);

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

    public static Stage registerPrimaryStage()
                                      throws TimeoutException {
        Stage primaryStage = waitForLaunch(
            service.setupPrimaryStage(
                context.getPrimaryStageFuture(),
                context.getApplicationClass(),
                context.getApplicationArgs()
            )
        );
        context.setRegisteredStage(primaryStage);
        return primaryStage;
    }

    public static Stage registerStage(Supplier<Stage> stageSupplier)
                               throws TimeoutException {
        Stage stage = setupFixture(() -> stageSupplier.get());
        context.setRegisteredStage(stage);
        return stage;
    }

    // SETUP REGISTERED STAGES (CONTENTS).

    public static Stage setupStage(Consumer<Stage> stageConsumer)
                            throws TimeoutException {
        return waitForSetup(
            service.setupStage(
                context.getRegisteredStage(),
                stageConsumer
            )
        );
    }

    public static Application setupApplication(Class<? extends Application> applicationClass,
                                               String... applicationArgs)
                                        throws TimeoutException {
        return waitForSetup(
            service.setupApplication(
                context.getRegisteredStage(),
                applicationClass,
                applicationArgs
            )
        );
    }

    public static void cleanupApplication(Application application)
                                   throws TimeoutException {
        waitForSetup(
            service.cleanupApplication(application)
        );
    }

    public static Scene setupScene(Supplier<Scene> sceneSupplier)
                            throws TimeoutException {
        return waitForSetup(
            service.setupScene(
                context.getRegisteredStage(),
                sceneSupplier
            )
        );
    }

    public static Parent setupSceneRoot(Supplier<Parent> sceneRootSupplier)
                                 throws TimeoutException {
        return waitForSetup(
            service.setupSceneRoot(
                context.getRegisteredStage(),
                sceneRootSupplier
            )
        );
    }

    // UTILITY METHODS.

    public static void setupFixture(Runnable runnable)
                             throws TimeoutException {
        waitForSetup(
            service.setupFixture(runnable)
        );
    }

    public static <T> T setupFixture(Callable<T> callable)
                              throws TimeoutException {
        return waitForSetup(
            service.setupFixture(callable)
        );
    }

    public static void showStage()
                          throws TimeoutException {
        setupStage((stage) -> showStage(stage));
    }

    public static void hideStage()
                          throws TimeoutException {
        Platform.setImplicitExit(false);
        setupStage((stage) -> hideStage(stage));
    }

    // INTERNAL CONTEXT.

    public static FxToolkitContext toolkitContext() {
        return context;
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static <T> T waitForLaunch(Future<T> future)
                                throws TimeoutException {
        return waitFor(context.getLaunchTimeoutInMillis(), MILLISECONDS, future);
    }

    private static <T> T waitForSetup(Future<T> future)
                               throws TimeoutException {
        return waitFor(context.getSetupTimeoutInMillis(), MILLISECONDS, future);
    }

    private static void showStage(Stage stage) {
        stage.show();
        stage.toBack();
        stage.toFront();
    }

    private static void hideStage(Stage stage) {
        stage.hide();
    }

}
