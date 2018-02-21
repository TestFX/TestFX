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

import javafx.stage.Stage;

import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.cases.TestCaseBase;
import org.testfx.framework.junit.TestFXRule;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.testfx.api.FxAssert.verifyThat;

public class FxToolkitBasicTest extends TestCaseBase {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    @AfterClass
    public static void cleanupSpec() throws Exception {
        FxToolkit.cleanupStages();
    }

    @Test
    public void registerPrimaryStage_should_be_callable_multiple_times() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.registerPrimaryStage();
        FxToolkit.registerPrimaryStage();
    }

    @Test
    public void registerPrimaryStage_should_return_the_primary_stage() throws Exception {
        // when:
        Stage stage = FxToolkit.registerPrimaryStage();

        // then:
        verifyThat(stage, instanceOf(Stage.class));
    }

    @Test
    public void registerPrimaryStage_should_update_the_registered_stage() throws Exception {
        // given:
        FxToolkit.toolkitContext().setRegisteredStage(null);

        // when:
        Stage stage = FxToolkit.registerPrimaryStage();

        // then:
        verifyThat(FxToolkit.toolkitContext().getRegisteredStage(), is(stage));
    }

    @Test
    public void registerStage_should_return_the_stage() throws Exception {
        // when:
        Stage stage = FxToolkit.registerStage(Stage::new);

        // then:
        verifyThat(stage, instanceOf(Stage.class));
    }

    @Test
    public void registerStage_should_update_the_registered_stage() throws Exception {
        // given:
        FxToolkit.toolkitContext().setRegisteredStage(null);

        // when:
        Stage stage = FxToolkit.registerStage(Stage::new);

        // then:
        verifyThat(FxToolkit.toolkitContext().getRegisteredStage(), is(stage));
    }

    @Test
    public void showStage_should_show_the_registered_stage() throws Exception {
        // given:
        Stage stage = FxToolkit.registerStage(Stage::new);

        // when:
        FxToolkit.showStage();

        // then:
        verifyThat(stage.isShowing(), is(true));
    }

    @Test
    @Ignore
    public void hideStage_should_hide_the_registered_stage() throws Exception {
        // given:
        Stage stage = FxToolkit.registerStage(Stage::new);
        FxToolkit.showStage();

        // when:
        FxToolkit.hideStage();

        // then:
        verifyThat(stage.isShowing(), is(false));
    }

}
