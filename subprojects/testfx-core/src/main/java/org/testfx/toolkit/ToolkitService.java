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
package org.testfx.toolkit;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Interface that handles setting up an {@link Application}, {@link Stage}, {@link Scene}, or {@link Parent rootNode}
 * and application cleanup on the {@code JavaFX Application Thread}.
 */
public interface ToolkitService {

    /**
     * If the given {@link CompletableFuture#isDone()}, returns that future; otherwise, launches the given application
     * with its arguments.
     */
    Future<Stage> setupPrimaryStage(CompletableFuture<Stage> primaryStageFuture,
                                    Class<? extends Application> applicationClass,
                                    String... applicationArgs);

    /**
     * Runs the given runnable on the {@code JavaFX Application Thread}.
     */
    Future<Void> setupFixture(Runnable runnable);

    /**
     * Runs the given callable on the {@code JavaFX Application Thread}.
     */
    <T> Future<T> setupFixture(Callable<T> callable);

    /**
     * Calls the stageConsumer with the given stage on the {@code JavaFX Application Thread} and returns a
     * {@link Future} whose {@link Future#get()} returns that stage.
     */
    Future<Stage> setupStage(Stage stage,
                             Consumer<Stage> stageConsumer);

    /**
     * Sets the given scene as the given stage's scene on the {@code JavaFX Application Thread} and returns a
     * {@link Future} whose {@link Future#get()} returns the given scene.
     */
    Future<Scene> setupScene(Stage stage,
                             Supplier<? extends Scene> sceneSupplier);

    /**
     * Wraps the parent in a scene, sets that scene as the given stage's scene on the
     * {@code JavaFX Application Thread}, and returns a {@link Future} whose {@link Future#get()} returns
     * the given parent.
     */
    Future<Parent> setupSceneRoot(Stage stage,
                                  Supplier<? extends Parent> sceneRootSupplier);

    /**
     * Creates, initializes, and starts the given applicationClass and returns a {@link Future} whose
     * {@link Future#get()} returns the created application.
     */
    Future<Application> setupApplication(Supplier<Stage> stageSupplier,
                                         Class<? extends Application> applicationClass,
                                         String... applicationArgs);

    /**
     * Creates, initializes, and starts the supplied application and returns a {@link Future} whose
     * {@link Future#get()} returns the created application.
     */
    Future<Application> setupApplication(Supplier<Stage> stageSupplier,
                                         Supplier<Application> applicationSupplier,
                                         String... applicationArgs);

    /**
     * Calls {@link ApplicationService#stop(Application)} on the given application.
     */
    Future<Void> cleanupApplication(Application application);

}
