/*
 * Copyright 2013 SmartBear Software
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

import javafx.application.Application;
import javafx.stage.Stage;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.framework.app.AppLauncher;
import org.loadui.testfx.utils.FXTestUtils;
import org.loadui.testfx.utils.StageFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AppSetupImplTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    AppSetupImpl appSetup;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setup() throws Throwable {
        appSetup = new AppSetupImpl();
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void launchApplication() throws Throwable {
        // given:
        AppLauncherMock launcherMock = new AppLauncherMock();

        // and:
        appSetup.setAppClass(Application.class);
        appSetup.setAppLauncher(launcherMock);
        appSetup.setStageFuture(StageFuture.create());

        // when:
        appSetup.launchApplication("arg1", "arg2");

        // then:
        FXTestUtils.awaitCondition(wasAppLauncherMockCalledCondition(launcherMock));
        MatcherAssert.assertThat(launcherMock.calledAppClass, Matchers.equalTo((Class) Application.class));
        MatcherAssert.assertThat(launcherMock.calledAppArgs, Matchers.equalTo(new String[]{"arg1", "arg2"}));
    }

    @Test
    public void getPrimaryStage() throws Throwable {
        // given:
        appSetup.setStageFuture(StageFuture.create());

        // when:
        appSetup.getStageFuture().set(null);
        Stage primaryStage = appSetup.getPrimaryStage(1, TimeUnit.SECONDS);

        // then:
        MatcherAssert.assertThat(primaryStage, Matchers.is(Matchers.nullValue()));
    }

    @Test(expected=TimeoutException.class)
    public void getPrimaryStage_throws_TimeoutException() throws Throwable {
        // given:
        appSetup.setStageFuture(StageFuture.create());

        // when:
        appSetup.getPrimaryStage(1, TimeUnit.SECONDS);

        // then:
        MatcherAssert.assertThat("exception was not thrown", false);
    }

    @Test
    public void hasPrimaryStage_returns_true() throws Throwable {
        // given:
        appSetup.setStageFuture(StageFuture.create());
        appSetup.getStageFuture().set(null);

        // expect:
        MatcherAssert.assertThat(appSetup.hasPrimaryStage(), Matchers.is(true));
    }

    @Test
    public void hasPrimaryStage_returns_false() throws Throwable {
        // given:
        appSetup.setStageFuture(StageFuture.create());

        // expect:
        MatcherAssert.assertThat(appSetup.hasPrimaryStage(), Matchers.is(false));
    }

    //---------------------------------------------------------------------------------------------
    // HELPER METHODS.
    //---------------------------------------------------------------------------------------------

    private Callable<Boolean> wasAppLauncherMockCalledCondition(
            final AppLauncherMock launcherMock) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return Matchers.notNullValue().matches(launcherMock.calledAppClass) &&
                    Matchers.notNullValue().matches(launcherMock.calledAppArgs);
            }
        };
    }

    //---------------------------------------------------------------------------------------------
    // MOCK AND STUB CLASSES.
    //---------------------------------------------------------------------------------------------

    static class AppLauncherMock implements AppLauncher {
        public Class calledAppClass;
        public String[] calledAppArgs;

        @Override
        public void launch(Class<? extends Application> appClass, String... appArgs) {
            calledAppClass = appClass;
            calledAppArgs = appArgs;
        }
    }

}
