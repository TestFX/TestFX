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
package org.testfx.toolkit;

import java.util.concurrent.CompletableFuture;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The main application used during tests when a developer is not testing his/her own subclass of {@link Application}.
 * The {@code primaryStage} from {@link Application#start(Stage)} can be accessed via {@link #PRIMARY_STAGE_FUTURE}.
 */
public class PrimaryStageApplication extends Application {

    public static final CompletableFuture<Stage> PRIMARY_STAGE_FUTURE = new CompletableFuture<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle(getClass().getSimpleName());
        PRIMARY_STAGE_FUTURE.complete(primaryStage);
    }

}
