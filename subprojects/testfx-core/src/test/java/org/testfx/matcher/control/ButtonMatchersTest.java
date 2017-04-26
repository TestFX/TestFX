/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2017 The TestFX Contributors
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
package org.testfx.matcher.control;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testfx.TestFXRule;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static org.hamcrest.MatcherAssert.assertThat;

public class ButtonMatchersTest extends FxRobot {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public Button button;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            button = new Button();
            return new StackPane(button);
        });
        FxToolkit.showStage();
    }

    @Test
    public void isCancelButton() {
        // setup:
        button.setCancelButton(true);

        // expect:
        assertThat(button, ButtonMatchers.isCancelButton());
    }

    @Test
    public void isCancelButton_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: Button is cancel button\n");

        assertThat(button, ButtonMatchers.isCancelButton());
    }

    @Test
    public void isDefaultButton() {
        // setup:
        button.setDefaultButton(true);

        // expect:
        assertThat(button, ButtonMatchers.isDefaultButton());
    }

    @Test
    public void isDefaultButton_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: Button is default button\n");

        assertThat(button, ButtonMatchers.isDefaultButton());
    }
}
