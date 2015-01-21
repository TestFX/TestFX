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
package org.loadui.testfx;

import javafx.application.Application;
import org.junit.Before;
import org.testfx.api.FxToolkit;
import org.testfx.api.FxToolkitContext;
import org.testfx.toolkit.ToolkitApplication;
import org.testfx.util.WaitForAsyncUtils;

public abstract class ApplicationTest extends FxTest {

    private static Class<? extends Application> applicationClass = null;

    public static class ToolkitApplicationProxy extends ToolkitApplication {

        public ToolkitApplicationProxy() throws InstantiationException, IllegalAccessException {
            super();
        }

        @Override
        public Application getDelegate() throws IllegalAccessException, InstantiationException {
            return ApplicationTest.applicationClass.newInstance();
        }
    }

    public ApplicationTest() {
        applicationClass = getApplicationClass();
    }

    @Before
    public void internalSetup() throws Exception {
        if (ToolkitApplication.primaryStageFuture.isDone()) {
            throw new RuntimeException("There is already one existing stage.");
        }

        FxToolkitContext context = new FxToolkitContext();
        FxToolkit toolkit = new FxToolkit(context);

        context.setApplicationClass(ToolkitApplicationProxy.class);
        context.setStageFuture(ToolkitApplicationProxy.primaryStageFuture);

        target(toolkit.registerPrimaryStage());
        WaitForAsyncUtils.waitForFxEvents();

        toolkit.setupStage((stage) -> {
            stage.show();
            stage.toBack();
            stage.toFront();
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    protected abstract Class<? extends Application> getApplicationClass();
}
