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
package org.testfx.api;

import javafx.application.Application;
import javafx.stage.Stage;

import com.google.common.annotations.Beta;
import org.testfx.toolkit.StageFuture;
import org.testfx.toolkit.ToolkitApplication;
import org.testfx.toolkit.ToolkitDummyApplication;

import static java.lang.Long.parseLong;
import static java.lang.System.getProperty;

@Beta
public class FxToolkitContext {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private StageFuture stageFuture = ToolkitApplication.primaryStageFuture;

    private Class<? extends ToolkitApplication> applicationClass = ToolkitDummyApplication.class;

    private String[] applicationArgs = new String[] {};

    private Stage targetStage = null;

    private long launchTimeoutInMillis = parseLong(getProperty("testfx.launch.timeout", "60000"));

    private long setupTimeoutInMillis = parseLong(getProperty("testfx.setup.timeout", "30000"));

    //---------------------------------------------------------------------------------------------
    // GETTER AND SETTER.
    //---------------------------------------------------------------------------------------------

    public StageFuture getStageFuture() {
        return stageFuture;
    }

    public void setStageFuture(StageFuture stageFuture) {
        this.stageFuture = stageFuture;
    }

    public Class<? extends Application> getApplicationClass() {
        return applicationClass;
    }

    public void setApplicationClass(Class<? extends ToolkitApplication> applicationClass) {
        this.applicationClass = applicationClass;
    }

    public String[] getApplicationArgs() {
        return applicationArgs;
    }

    public void setApplicationArgs(String[] applicationArgs) {
        this.applicationArgs = applicationArgs;
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
