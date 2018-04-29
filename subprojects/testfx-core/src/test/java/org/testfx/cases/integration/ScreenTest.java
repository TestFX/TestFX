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
package org.testfx.cases.integration;

import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.cases.TestCaseBase;
import org.testfx.framework.junit.TestFXRule;

public class ScreenTest extends TestCaseBase {

    @Rule
    public TestFXRule testFXRule = new TestFXRule(2);

    @Before
    public void setup() throws Exception {
        FxToolkit.setupStage(stage -> {
            Scene scene = new Scene(new StackPane(new Label("Label")), 300, 100);
            stage.setScene(scene);
            stage.show();
        });
    }

    @Test
    public void screen_test() throws InterruptedException {
        CountDownLatch screenLatch = new CountDownLatch(1);
        Platform.runLater(() -> System.out.println(Screen.getScreens()));
        screenLatch.await();
    }
}
