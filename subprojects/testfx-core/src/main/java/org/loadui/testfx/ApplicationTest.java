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
