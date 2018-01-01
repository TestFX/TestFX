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
package org.testfx.cases.issue;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Region;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.cases.TestCaseBase;

@Ignore
public class StageHideDeadlockBug extends TestCaseBase {

    @BeforeClass
    public static void setupSpec() {
        //FxToolkit.setupFixture(() -> new Stage().show());
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupStage(stage -> stage.setScene(new Scene(new Region(), 500, 500)));
        FxToolkit.showStage();
    }

    @After
    public void cleanup() throws Exception {
        FxToolkit.hideStage();
        //FxToolkit.setupStage((stage) -> stage.hide());
        Platform.runLater(() -> {}); // <-- BUG
    }

    @Test
    public void should_call_cleanup_once() {}

    @Test
    public void should_call_cleanup_twice() {}

    @Test
    public void should_call_cleanup_thrice() {}

    @Test
    public void should_call_cleanup_fourfold() {}

    @Test
    public void should_call_cleanup_five_times() {}

}
