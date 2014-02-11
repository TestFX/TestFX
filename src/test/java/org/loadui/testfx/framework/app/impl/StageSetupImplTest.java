package org.loadui.testfx.framework.app.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
        AppSetup appSetup = DefaultAppSetupFactory.build();
        if (!appSetup.hasPrimaryStage()) {
            appSetup.launchApplication();
        }
        appSetup.getPrimaryStage(25, TimeUnit.SECONDS);
    }

    @Before
    public void setup() throws Throwable {
        stageSetup = new StageSetupImpl();
        FXTestUtils.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                primaryStage = new Stage();
                primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.setScene(new Scene(new Region(), 600, 400));
            }
        }, 5);
    }

    @After
    public void cleanup() throws Throwable {
        FXTestUtils.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                primaryStage.close();
            }
        }, 5);
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

    @Ignore @Test
    public void showPrimaryStage() throws Throwable {
        // TODO: Fix lock here when FXTestUtils#invokeAndWait() awaits events via Semaphore.

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
