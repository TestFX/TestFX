package org.testfx.api;

import java.util.concurrent.Future;
import javafx.application.Application;
import javafx.stage.Stage;

import org.loadui.testfx.framework.launch.ToolkitApplication;

import static java.lang.Long.parseLong;
import static java.lang.System.getProperty;

public class FxLifecycleContext {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private Future<Stage> stageFuture = ToolkitApplication.primaryStageFuture;

    private Class<? extends Application> applicationClass = ToolkitApplication.class;

    private Stage targetStage = null;

    private long launchTimeoutInMillis = parseLong(getProperty("testfx.launch.timeout", "60000"));

    private long setupTimeoutInMillis = parseLong(getProperty("testfx.setup.timeout", "30000"));

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public Future<Stage> getStageFuture() {
        return stageFuture;
    }

    public void setStageFuture(Future<Stage> stageFuture) {
        this.stageFuture = stageFuture;
    }

    public Class<? extends Application> getApplicationClass() {
        return applicationClass;
    }

    public void setApplicationClass(Class<? extends Application> applicationClass) {
        this.applicationClass = applicationClass;
    }

    public Stage getTargetStage() {
        return targetStage;
    }

    public void setTargetStage(Stage targetStage) {
        this.targetStage = targetStage;
    }

    public long getLaunchTimeoutInMillis() {
        return launchTimeoutInMillis;
    }

    public void setLaunchTimeoutInMillis(long launchTimeoutInMillis) {
        this.launchTimeoutInMillis = launchTimeoutInMillis;
    }

    public long getSetupTimeoutInMillis() {
        return setupTimeoutInMillis;
    }

    public void setSetupTimeoutInMillis(long setupTimeoutInMillis) {
        this.setupTimeoutInMillis = setupTimeoutInMillis;
    }

}
