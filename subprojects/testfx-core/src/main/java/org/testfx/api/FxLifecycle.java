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

import org.testfx.lifecycle.LifecycleService;
import org.testfx.lifecycle.impl.LifecycleServiceImpl;

import static org.loadui.testfx.utils.RunWaitUtils.waitFor;

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

    // LAUNCH: SETUP PRIMARY STAGE (LAUNCH TOOLKIT AND APPLICATION).

    public static Stage setupPrimaryStage() throws TimeoutException {
        Stage primaryStage = waitForLaunch(
            service.setupPrimaryStage(context.getStageFuture(), context.getApplicationClass())
        );
        context.setTargetStage(primaryStage);
        return primaryStage;
    }

    public static Stage setupTargetStage(Stage stage) {
        context.setTargetStage(stage);
        return stage;
    }

    // SETUP: SETUP CONTAINERS (STAGE, SCENE) AND CONTENTS (NODE, PARENT, CONTROL, ...).

    public static void setup(Runnable runnable) throws TimeoutException {
        waitForSetup(
            service.setup(runnable)
        );
    }

    public static <T> T setup(Callable<T> callable) throws TimeoutException {
        return waitForSetup(
            service.setup(callable)
        );
    }

    // SETUP/CONSUME: USE STAGE TO SETUP CONTAINERS AND CONTENTS.

    public static Stage setupStage(Consumer<Stage> stageConsumer) throws TimeoutException {
        return waitForSetup(
            service.setupStage(context.getTargetStage(), stageConsumer)
        );
    }

    // SETUP/SUPPLY: USE STAGE TO SETUP APPLICATION, SCENE OR ROOT.

    public static Application setupApplication(Class<? extends Application> applicationClass)
            throws TimeoutException {
        return waitForSetup(
            service.setupApplication(context.getTargetStage(), applicationClass)
        );
    }

    public static void cleanupApplication(Application application) throws TimeoutException {
        waitForSetup(
            service.cleanupApplication(application)
        );
    }

    public static Scene setupScene(Supplier<Scene> sceneSupplier) throws TimeoutException {
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

    // CONTEXT.

    public static FxLifecycleContext lifecycleContext() {
        return context;
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    private static <T> T waitForLaunch(Future<T> future) throws TimeoutException {
        return waitFor(context.getLaunchTimeoutInMillis(), TimeUnit.MILLISECONDS, future);
    }

    private static <T> T waitForSetup(Future<T> future) throws TimeoutException {
        return waitFor(context.getSetupTimeoutInMillis(), TimeUnit.MILLISECONDS, future);
    }

}
