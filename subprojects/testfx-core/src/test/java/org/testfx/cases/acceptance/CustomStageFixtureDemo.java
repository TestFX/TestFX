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
package org.testfx.cases.acceptance;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import org.testfx.api.FxToolkit;

public class CustomStageFixtureDemo {

    public static void main(String[] args) throws Exception {
        beforeClass();

        before();
        test();
        after();

        before();
        test();
        after();

        before();
        test();
        after();

        afterClass();
        cleanup();
    }

    private static void beforeClass() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.hideStage(); // hide the primary Stage, if was previously shown.
    }

    private static void afterClass() {}

    private static void before() throws Exception {
        FxToolkit.registerStage(Stage::new);
        FxToolkit.setupStage(stage -> stage.setScene(new Scene(new Label("within custom stage"))));
        FxToolkit.showStage();
    }

    private static void after() throws Exception {
        FxToolkit.hideStage();
    }

    private static void test() throws Exception {
        Thread.sleep(500);
    }

    private static void cleanup() {
        Platform.setImplicitExit(true);
    }

}
