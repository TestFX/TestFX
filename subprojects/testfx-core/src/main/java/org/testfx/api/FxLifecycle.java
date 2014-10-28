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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.google.common.annotations.Beta;
import org.testfx.lifecycle.LifecycleService;
import org.testfx.lifecycle.impl.LifecycleServiceImpl;

import static org.loadui.testfx.utils.RunWaitUtils.waitFor;

/**
 * Responsible for setup and cleanup of JavaFX fixtures that need the JavaFX thread.
 *
 * <p><b>Overview</b></p>
 *
 * <p>This class methods cover three different kinds of fixtures:</p>
 *
 * <ol>
 * <li>Container fixtures, which are registered as {@code targetStage}.</li>
 * <li>Content fixtures, which are attached to the registered {@code targetStage}.</li>
 * <li>Individual fixtures, which do not require a {@code targetStage}.</li>
 * </ol>
 *
 * <p>Additionally it keeps an internal context.</p>
 *
 * <p><b>1. Container Fixtures</b></p>
 *
 * <p>They can be registered as {@code targetStage} and provide a top-level container, i.e.
 * {@link Stage}s.</p>
 *
 * <p>The primary stage can be registered as {@code targetStage} using
 * {@link #registerPrimaryStage}. This call is mandatory before any other JavaFX fixture can be
 * created.</p>
 *
 * <p>Other stages can be registered as {@code targetStage}  using {@link #registerTargetStage
 * registerTargetStage(Supplier&lt;Stage&gt;)}.</p>
 *
 * <p><b>2. Content Fixtures</b></p>
 *
 * <p>They can be attached to the {@code targetStage}.</p>
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
 * <p>To setup individual Nodes use {@link #setup(Runnable)} and {@link #setup(Callable)}.</p>
 *
 * <p><b>Internal Context</b></p>
 *
 * <p>Is internally responsible for handle the target stage for attachments,
 * handle timeouts, provide the Application for the Toolkit launch and execute the setup
 * in the JavaFX thread. The primary Stage is constructed by the platform.</p>
 */
@Beta
public class FxLifecycle {

    //---------------------------------------------------------------------------------------------
    // STATIC FIELDS.
    //---------------------------------------------------------------------------------------------

    private static FxLifecycleContext context = new FxLifecycleContext();

    private static LifecycleService service = new LifecycleServiceImpl();

    //---------------------------------------------------------------------------------------------
    // PRIVATE CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    private FxLifecycle() {
        throw new UnsupportedOperationException();
    }

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    // CONTAINER FIXTURES.

    public static Stage registerPrimaryStage()
                                      throws TimeoutException {
        Stage primaryStage = waitForLaunch(
            service.setupPrimaryStage(context.getStageFuture(), context.getApplicationClass())
        );
        context.setTargetStage(primaryStage);
        return primaryStage;
    }

    public static Stage registerTargetStage(Supplier<Stage> stageSupplier)
                                     throws TimeoutException {
        Stage targetStage = setup(() -> stageSupplier.get());
        context.setTargetStage(targetStage);
        return targetStage;
    }

    // INDIVIDUAL FIXTURES.

    public static void setup(Runnable runnable)
                      throws TimeoutException {
        waitForSetup(
            service.setup(runnable)
        );
    }

    public static <T> T setup(Callable<T> callable)
                       throws TimeoutException {
        return waitForSetup(
            service.setup(callable)
        );
    }

    // CONTENT FIXTURES.

    public static Stage setupStage(Consumer<Stage> stageConsumer)
                            throws TimeoutException {
        return waitForSetup(
            service.setupStage(context.getTargetStage(), stageConsumer)
        );
    }

    public static Application setupApplication(Class<? extends Application> applicationClass)
                                        throws TimeoutException {
        return waitForSetup(
            service.setupApplication(context.getTargetStage(), applicationClass)
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
            service.setupScene(context.getTargetStage(), sceneSupplier)
        );
    }

    public static Parent setupSceneRoot(Supplier<Parent> sceneRootSupplier)
                                 throws TimeoutException {
        return waitForSetup(
            service.setupSceneRoot(context.getTargetStage(), sceneRootSupplier)
        );
    }

    // INTERNAL CONTEXT.

    public static FxLifecycleContext lifecycleContext() {
        return context;
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static <T> T waitForLaunch(Future<T> future)
                                throws TimeoutException {
        return waitFor(context.getLaunchTimeoutInMillis(), TimeUnit.MILLISECONDS, future);
    }

    private static <T> T waitForSetup(Future<T> future)
                               throws TimeoutException {
        return waitFor(context.getSetupTimeoutInMillis(), TimeUnit.MILLISECONDS, future);
    }

}
