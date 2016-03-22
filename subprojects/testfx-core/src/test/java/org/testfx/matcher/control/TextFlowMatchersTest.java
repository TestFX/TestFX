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
package org.testfx.matcher.control;

import static org.hamcrest.MatcherAssert.assertThat;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

public class TextFlowMatchersTest extends FxRobot
{
    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public Text foobarText;
    public Text quuxText;
    public TextFlow textFlow;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupFixture(() -> {
            foobarText = new Text("foobar ");
            quuxText = new Text("quux");
            quuxText.setFill(Color.RED);
            textFlow = new TextFlow(foobarText, quuxText);
        });
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void hasText() {
        // expect:
        assertThat(textFlow, TextFlowMatchers.hasText("foobar quux"));
    }

    @Test
    public void hasText_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TextFlow has text \"foobar baaz\"\n");

        assertThat(textFlow, TextFlowMatchers.hasText("foobar baaz"));
    }

    @Test
    public void hasColoredText() {
        assertThat(textFlow, TextFlowMatchers.hasColoredText("foobar <RED>quux</RED>"));
    }

    @Test
    public void hasColorText_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TextFlow has colored text \"foobar <BLUE>quux</BLUE>\"\n");

        assertThat(textFlow, TextFlowMatchers.hasColoredText("foobar <BLUE>quux</BLUE>"));
    }
}
