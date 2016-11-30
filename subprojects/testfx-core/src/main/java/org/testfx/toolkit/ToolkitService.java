/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.toolkit;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ToolkitService {

    Future<Stage> setupPrimaryStage(PrimaryStageFuture primaryStageFuture,
                                    Class<? extends Application> applicationClass,
                                    String... applicationArgs);

    Future<Void> setupFixture(Runnable runnable);

    <T> Future<T> setupFixture(Callable<T> callable);

    Future<Stage> setupStage(Stage stage,
                             Consumer<Stage> stageConsumer);

    Future<Scene> setupScene(Stage stage,
                             Supplier<? extends Scene> sceneSupplier);

    Future<Parent> setupSceneRoot(Stage stage,
                                  Supplier<? extends Parent> sceneRootSupplier);

    Future<Application> setupApplication(Supplier<Stage> stageSupplier,
                                         Class<? extends Application> applicationClass,
                                         String... applicationArgs);

    Future<Application> setupApplication(Supplier<Stage> stageSupplier,
                                         Supplier<Application> applicationSupplier,
                                         String... applicationArgs);

    Future<Void> cleanupApplication(Application application);

}
