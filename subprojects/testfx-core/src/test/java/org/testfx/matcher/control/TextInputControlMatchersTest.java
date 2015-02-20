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
package org.testfx.matcher.control;

import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static org.hamcrest.MatcherAssert.assertThat;

public class TextInputControlMatchersTest extends FxRobot {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    @Rule
    public ExpectedException exception = ExpectedException.none().handleAssertionErrors();

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void hasText_with_text_field() throws Exception {
        // given:
        TextField textField = FxToolkit.setupFixture(() -> new TextField("foo"));

        // expect:
        assertThat(textField, TextInputControlMatchers.hasText("foo"));
    }

    @Test
    public void hasText_with_text_field_fails() throws Exception {
        // given:
        TextField textField = FxToolkit.setupFixture(() -> new TextField("bar"));

        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TextInputControl has text 'foo'\n");

        assertThat(textField, TextInputControlMatchers.hasText("foo"));
    }

    @Test
    public void hasText_with_region_fails() throws Exception {
        // given:
        Region region = FxToolkit.setupFixture(() -> new Region());

        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TextInputControl has text 'foo'\n");

        assertThat(region, TextInputControlMatchers.hasText("foo"));
    }

}
