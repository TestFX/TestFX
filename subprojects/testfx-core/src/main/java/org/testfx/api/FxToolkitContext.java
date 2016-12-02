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
package org.testfx.api;

import javafx.application.Application;
import javafx.stage.Stage;

import org.testfx.api.annotation.Unstable;
import org.testfx.toolkit.PrimaryStageApplication;
import org.testfx.toolkit.PrimaryStageFuture;

import static java.lang.Long.parseLong;
import static java.lang.System.getProperty;

@Unstable(reason = "class was recently added")
public class FxToolkitContext {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private PrimaryStageFuture primaryStageFuture = PrimaryStageApplication.PRIMARY_STAGE_FUTURE;

    private Class<? extends Application> applicationClass = PrimaryStageApplication.class;

    private String[] applicationArgs = new String[] {};

    private Stage registeredStage;

    private long launchTimeoutInMillis = parseLong(getProperty("testfx.launch.timeout", "60000"));

    private long setupTimeoutInMillis = parseLong(getProperty("testfx.setup.timeout", "30000"));

    //---------------------------------------------------------------------------------------------
    // GETTER AND SETTER.
    //---------------------------------------------------------------------------------------------

    public PrimaryStageFuture getPrimaryStageFuture() {
        return primaryStageFuture;
    }

    public void setPrimaryStageFuture(PrimaryStageFuture primaryStageFuture) {
        this.primaryStageFuture = primaryStageFuture;
    }

    public Class<? extends Application> getApplicationClass() {
        return applicationClass;
    }

    public void setApplicationClass(Class<? extends Application> applicationClass) {
        this.applicationClass = applicationClass;
    }

    public String[] getApplicationArgs() {
        return applicationArgs;
    }

    public void setApplicationArgs(String[] applicationArgs) {
        this.applicationArgs = applicationArgs;
    }

    public Stage getRegisteredStage() {
        return registeredStage;
    }

    public void setRegisteredStage(Stage registeredStage) {
        this.registeredStage = registeredStage;
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
