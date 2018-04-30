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
package org.testfx.api;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javafx.application.Application;
import javafx.event.Event;
import javafx.stage.Stage;

import org.testfx.service.support.FiredEvents;
import org.testfx.toolkit.PrimaryStageApplication;
/**
 * Stores the contextual information for {@link FxToolkit}:
 * <ul>
 *     <li>the primary stage future</li>
 *     <li>the {@link Application} as a {@link Class} object</li>
 *     <li>the application's arguments</li>
 *     <li>the registered {@link Stage}</li>
 *     <li>the timeout limit for launching an application</li>
 *     <li>the timeout limit for setting up a component</li>
 * </ul>
 */
public class FxToolkitContext {

    /**
     * The {@link java.util.concurrent.Future Future&lt;Stage&gt;} that can run listeners when completed.
     * Default value: {@link PrimaryStageApplication#PRIMARY_STAGE_FUTURE}.
     */
    private final CompletableFuture<Stage> primaryStageFuture = PrimaryStageApplication.PRIMARY_STAGE_FUTURE;

    /**
     * The {@link Application} as a {@link Class} object to use in {@link Application#launch(Class, String...)}.
     * Default value: {@link PrimaryStageApplication}.
     */
    private final Class<? extends Application> applicationClass = PrimaryStageApplication.class;

    /**
     * The application arguments. Default value: an empty {@code String[]}
     */
    private String[] applicationArgs = new String[] {};

    private Stage registeredStage;

    private FiredEvents firedEvents;

    /**
     * The number of milliseconds before timing out launch-related components. Default value: 60,000 (1 minute)
     */
    private long launchTimeoutInMillis = Long.getLong("testfx.launch.timeout", 60000);

    /**
     * The number of milliseconds before timing out setup-related components. Default value: 30,000 (30 seconds)
     */
    private long setupTimeoutInMillis = Long.getLong("testfx.setup.timeout", 30000);

    public CompletableFuture<Stage> getPrimaryStageFuture() {
        return primaryStageFuture;
    }

    public Class<? extends Application> getApplicationClass() {
        return applicationClass;
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
        if (firedEvents != null) {
            firedEvents.stopStoringFiredEvents();
        }
        firedEvents = FiredEvents.beginStoringFiredEventsOf(registeredStage);
    }

    public List<Event> getFiredEvents() {
        return firedEvents.getEvents();
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
