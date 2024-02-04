/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2024 The TestFX Contributors
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
package org.testfx.toolkit.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.testfx.api.FxToolkit;
import org.testfx.toolkit.ApplicationLauncher;
import org.testfx.toolkit.ApplicationService;
import org.testfx.toolkit.ToolkitService;

import static org.testfx.util.WaitForAsyncUtils.async;
import static org.testfx.util.WaitForAsyncUtils.asyncFx;

public class ToolkitServiceImpl implements ToolkitService {

    private final ApplicationLauncher applicationLauncher;
    private final ApplicationService applicationService;

    public ToolkitServiceImpl(ApplicationLauncher applicationLauncher,
                              ApplicationService applicationService) {
        this.applicationLauncher = applicationLauncher;
        this.applicationService = applicationService;
    }

    @Override
    public Future<Stage> setupPrimaryStage(CompletableFuture<Stage> primaryStageFuture,
                                           Class<? extends Application> applicationClass,
                                           String... applicationArgs) {
        if (!primaryStageFuture.isDone()) {
            async(() -> {
                try {
                    applicationLauncher.launch(applicationClass, applicationArgs);
                }
                catch (Throwable exception) {
                    primaryStageFuture.completeExceptionally(exception);
                }
            });
        }
        return primaryStageFuture;
    }

    @Override
    public Future<Void> setupFixture(Runnable runnable) {
        return asyncFx(runnable);
    }

    @Override
    public <T> Future<T> setupFixture(Callable<T> callable) {
        return asyncFx(callable);
    }

    @Override
    public Future<Stage> setupStage(Stage stage,
                                    Consumer<Stage> stageConsumer) {
        return asyncFx(() -> {
            stageConsumer.accept(stage);
            return stage;
        });
    }

    @Override
    public Future<Scene> setupScene(Stage stage,
                                    Supplier<? extends Scene> sceneSupplier) {
        return asyncFx(() -> {
            Scene scene = sceneSupplier.get();
            stage.setScene(scene);
            return scene;
        });
    }

    @Override
    public Future<Parent> setupSceneRoot(Stage stage,
                                         Supplier<? extends Parent> sceneRootSupplier) {
        return asyncFx(() -> {
            Parent rootNode = sceneRootSupplier.get();
            stage.setScene(new Scene(rootNode));
            return rootNode;
        });
    }

    @Override
    public Future<Application> setupApplication(Supplier<Stage> stageSupplier,
                                                Class<? extends Application> applicationClass,
                                                String... applicationArgs) {
        return async(() -> {
            Application application = asyncFx(() -> createApplication(applicationClass)).get();
            registerApplicationParameters(application, applicationArgs);
            applicationService.init(application).get();
            applicationService.start(application, stageSupplier.get()).get();
            return application;
        });
    }

    @Override
    public Future<Application> setupApplication(Supplier<Stage> stageSupplier,
                                                Supplier<Application> applicationSupplier,
                                                String... applicationArgs) {
        return async(() -> {
            Application application = asyncFx(applicationSupplier::get).get();
            registerApplicationParameters(application, applicationArgs);
            applicationService.init(application).get();
            applicationService.start(application, stageSupplier.get()).get();
            return application;
        });
    }

    @Override
    public Future<Void> cleanupApplication(Application application) {
        cleanupParameters(application);
        return applicationService.stop(application);
    }

    private Application createApplication(Class<? extends Application> applicationClass) throws Exception {
        return applicationClass.getDeclaredConstructor().newInstance();
    }

    private void registerApplicationParameters(Application application, String... applicationArgs) {
        String type = "com.sun.javafx.application.ParametersImpl";
        String methodName = "registerParameters";

        try {
            // Use reflection to get the class, constructor and method
            Class<?> parametersImpl = getClass().getClassLoader().loadClass(type);
            Constructor<?> constructor = parametersImpl.getDeclaredConstructor(List.class);
            Method method =
                parametersImpl.getDeclaredMethod(methodName, Application.class, Application.Parameters.class);

            // Create an instance of the ParametersImpl class
            Application.Parameters parameters =
                (Application.Parameters)constructor.newInstance(Arrays.asList(applicationArgs));
            // Call the registerParameters on the ParametersImpl instance
            method.invoke(parametersImpl, application, parameters);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static void cleanupParameters(Application application) {
        // This block removes the application parameters from ParametersImpl
        try {
            // Use reflection to get the ParametersImpl.params field
            String type = "com.sun.javafx.application.ParametersImpl";
            String fieldName = "params";
            Class<?> parametersImpl = FxToolkit.class.getClassLoader().loadClass(type);
            Field field = parametersImpl.getDeclaredField(fieldName);
            field.setAccessible(true);
            Map<Application, Application.Parameters> params;
            params = (Map<Application, Application.Parameters>) field.get(null);
            params.remove(application);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
