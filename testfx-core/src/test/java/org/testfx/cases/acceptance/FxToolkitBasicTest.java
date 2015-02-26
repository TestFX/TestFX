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
package org.testfx.cases.acceptance;

import javafx.stage.Stage;

import org.junit.AfterClass;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.cases.TestCaseBase;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FxToolkitBasicTest extends TestCaseBase {

    // `FxToolkit` is responsible for setup and cleanup of JavaFX fixtures.

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @AfterClass
    public static void cleanupSpec() throws Exception {
        FxToolkit.cleanupStages();
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void registerPrimaryStage_should_be_callable_multiple_times() throws Exception {
        // expect:
        FxToolkit.registerPrimaryStage();
        FxToolkit.registerPrimaryStage();
        FxToolkit.registerPrimaryStage();
    }

    @Test
    public void registerPrimaryStage_should_return_the_primary_stage() throws Exception {
        // when:
        Stage stage = FxToolkit.registerPrimaryStage();

        // then:
        assertThat(stage, instanceOf(Stage.class));
    }

    @Test
    public void registerPrimaryStage_should_update_the_registered_stage() throws Exception {
        // given:
        FxToolkit.toolkitContext().setRegisteredStage(null);

        // when:
        Stage stage = FxToolkit.registerPrimaryStage();

        // then:
        assertThat(FxToolkit.toolkitContext().getRegisteredStage(), is(stage));
    }

    @Test
    public void registerStage_should_return_the_stage() throws Exception {
        // when:
        Stage stage = FxToolkit.registerStage(() -> new Stage());

        // then:
        assertThat(stage, instanceOf(Stage.class));
    }

    @Test
    public void registerStage_should_update_the_registered_stage() throws Exception {
        // given:
        FxToolkit.toolkitContext().setRegisteredStage(null);

        // when:
        Stage stage = FxToolkit.registerStage(() -> new Stage());

        // then:
        assertThat(FxToolkit.toolkitContext().getRegisteredStage(), is(stage));
    }

    @Test
    public void showStage_should_show_the_registered_stage() throws Exception {
        // given:
        Stage stage = FxToolkit.registerStage(() -> new Stage());

        // when:
        FxToolkit.showStage();

        // then:
        assertThat(stage.isShowing(), is(true));
    }

    @Test
    public void hideStage_should_hide_the_registered_stage() throws Exception {
        // given:
        Stage stage = FxToolkit.registerStage(() -> new Stage());
        FxToolkit.showStage();

        // when:
        FxToolkit.hideStage();

        // then:
        assertThat(stage.isShowing(), is(false));
    }

}
