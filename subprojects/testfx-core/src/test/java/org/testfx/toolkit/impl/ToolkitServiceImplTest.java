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
package org.testfx.toolkit.impl;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.toolkit.PrimaryStageApplication;
import org.testfx.toolkit.ToolkitService;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testfx.util.WaitForAsyncUtils.sleep;
import static org.testfx.util.WaitForAsyncUtils.waitFor;
import static org.testfx.util.WaitForAsyncUtils.waitForAsyncFx;

public class ToolkitServiceImplTest {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    static Stage primaryStage;
    static ToolkitService toolkitService;

    @BeforeClass
    public static void setupSpec() throws Exception {
        CompletableFuture<Stage> primaryStageFuture = PrimaryStageApplication.PRIMARY_STAGE_FUTURE;
        Class<? extends Application> toolkitApplication = PrimaryStageApplication.class;
        toolkitService = new ToolkitServiceImpl(
                new ApplicationLauncherImpl(), new ApplicationServiceImpl());

        primaryStage = waitFor(10, TimeUnit.SECONDS,
                toolkitService.setupPrimaryStage(primaryStageFuture, toolkitApplication));
    }

    @Before
    public void setup() {
        waitForAsyncFx(2000, () -> {
            primaryStage.show();
            primaryStage.toBack();
            primaryStage.toFront();
        });
    }

    @After
    public void cleanup() {
        waitForAsyncFx(2000, () -> {
            Platform.setImplicitExit(false);
            primaryStage.hide();
        });
    }

    @Test
    public void should_construct_application() throws Exception {
        printCurrentThreadName("should_construct_application()");
        Application application = waitFor(5, TimeUnit.SECONDS,
                toolkitService.setupApplication(() -> primaryStage, FixtureApplication.class)
        );

        sleep(2, TimeUnit.SECONDS);
        assertThat(application, instanceOf(FixtureApplication.class));
        assertThat(application.getParameters().getNamed(), notNullValue());
    }

    @Test
    public void should_construct_scene() throws Exception {
        printCurrentThreadName("should_construct_scene");
        Scene scene = waitFor(5, TimeUnit.SECONDS,
                toolkitService.setupScene(primaryStage, FixtureScene::new)
        );

        sleep(2, TimeUnit.SECONDS);
        assertThat(scene, instanceOf(FixtureScene.class));
    }

    public static class FixtureApplication extends Application {
        @Override
        public void init() {
            printCurrentThreadName("Application#init()");
        }

        @Override
        public void start(Stage primaryStage) {
            printCurrentThreadName("Application#start()");
            Parent parent = new StackPane(new Label(getClass().getSimpleName()));
            Scene scene = new Scene(parent, 400, 200);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        @Override
        public void stop() {
            printCurrentThreadName("Application#stop()");
        }

        private String getApplicationParameter(String parameterName) {
            Map<String, String> applicationParameters = getParameters().getNamed();
            return applicationParameters.get(parameterName);
        }
    }

    public static class FixtureScene extends Scene {
        public FixtureScene() {
            super(new Region(), 400, 200);
            printCurrentThreadName("Scene#init()");
            Parent parent = new StackPane(new Label(getClass().getSimpleName()));
            setRoot(parent);
        }
    }

    private static void printCurrentThreadName(String methodSignature) {
        String threadName = Thread.currentThread().getName();
        System.out.println(methodSignature + " in '" + threadName + "'");
    }

}
