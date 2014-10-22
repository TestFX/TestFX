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
package org.loadui.testfx.framework.app.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.loadui.testfx.framework.app.AppSetup;
import org.loadui.testfx.framework.app.StageSetupCallback;
import org.loadui.testfx.utils.FXTestUtils;
import org.hamcrest.Matchers;

import static org.hamcrest.MatcherAssert.assertThat;

public class StageSetupImplTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    StageSetupImpl stageSetup;
    Stage primaryStage;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    static public void setupClass() throws Throwable {
        AppSetup appSetup = ToolkitAppSetupFactory.build();
        if (!appSetup.hasPrimaryStage()) {
            appSetup.launchApplication();
        }
        appSetup.getPrimaryStage(25, TimeUnit.SECONDS);
    }

    @Before
    public void setup() throws Throwable {
        stageSetup = new StageSetupImpl();
        FXTestUtils.invokeAndWait(() -> {
            primaryStage = new Stage();
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(new Region(), 600, 400));
        }, 5);
    }

    @After
    public void cleanup() throws Throwable {
        Platform.runLater(() -> primaryStage.close());
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void invokeCallbackAndWait() throws Throwable {
        // given:
        stageSetup.setPrimaryStage(primaryStage);

        // and:
        StageSetupCallbackMock stageSetupCallbackMock = new StageSetupCallbackMock();
        stageSetup.setCallback(stageSetupCallbackMock);

        // when:
        stageSetup.invokeCallbackAndWait(1, TimeUnit.SECONDS);

        // then:
        assertThat(stageSetupCallbackMock.calledPrimaryStage, Matchers.is(primaryStage));
    }

    @Test(expected=TimeoutException.class)
    public void invokeCallbackAndWait_throws_TimeoutException() throws Throwable {
        // given:
        SlowStageSetupCallbackMock stageSetupCallbackMock = new SlowStageSetupCallbackMock();
        stageSetup.setCallback(stageSetupCallbackMock);

        // when:
        stageSetup.invokeCallbackAndWait(1, TimeUnit.SECONDS);

        // then:
        assertThat("exception was not thrown", false);
    }

    @Test
    public void showPrimaryStage() throws Throwable {
        // given:
        stageSetup.setPrimaryStage(primaryStage);

        // when:
        stageSetup.showPrimaryStage(1, TimeUnit.SECONDS);

        // then:
        assertThat(primaryStage.isShowing(), Matchers.is(true));
    }

    //---------------------------------------------------------------------------------------------
    // STATIC HELPER METHODS.
    //---------------------------------------------------------------------------------------------

    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    //---------------------------------------------------------------------------------------------
    // MOCK AND STUB CLASSES.
    //---------------------------------------------------------------------------------------------

    static class StageSetupCallbackMock implements StageSetupCallback {
        public Stage calledPrimaryStage;

        @Override
        public void setupStages(Stage primaryStage) {
            sleep(100);
            calledPrimaryStage = primaryStage;
        }
    }

    static class SlowStageSetupCallbackMock implements StageSetupCallback {
        @Override
        public void setupStages(Stage primaryStage) {
            sleep(5000);
        }
    }

}
