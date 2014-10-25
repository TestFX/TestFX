package org.testfx.lifecycle;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public interface LifecycleService {

    Future<Stage> setupPrimaryStage(Future<Stage> primaryStageFuture,
                                    Class<? extends Application> toolkitApplication);

    Future<Void> setup(Runnable runnable);

    <T> Future<T> setup(Callable<T> callable);

    Future<Stage> setupStage(Stage stage,
                             Consumer<Stage> stageConsumer);

    Future<Scene> setupScene(Stage stage,
                             Supplier<? extends Scene> sceneSupplier);

    Future<Parent> setupSceneRoot(Stage stage,
                                  Supplier<? extends Parent> sceneRootSupplier);

    Future<Application> setupApplication(Stage stage,
                                         Class<? extends Application> appClass);

    Future<Void> cleanupApplication(Application application);

}
