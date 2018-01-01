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
package org.testfx.matcher.control;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;

import static org.hamcrest.MatcherAssert.assertThat;

public class TextFlowMatchersTest extends FxRobot {

    @Rule
    public TestRule rule = RuleChain.outerRule(new TestFXRule()).around(exception = ExpectedException.none());
    public ExpectedException exception;

    TextFlow textFlow;
    TextFlow exactTextFlow;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupFixture(() -> {
            Text foobarText = new Text("foobar ");
            Text quuxText = new Text("quux");
            quuxText.setFill(Color.RED);
            textFlow = new TextFlow(foobarText, quuxText);

            Text exactText = new Text("exact");
            // set the fill to the closest color to, but not exactly, LimeGreen (50, 205, 50)
            exactText.setFill(Color.rgb(51, 205, 50));
            exactTextFlow = new TextFlow(exactText);
        });
    }

    @Test
    public void hasText() {
        // expect:
        assertThat(textFlow, TextFlowMatchers.hasText("foobar quux"));
    }

    @Test
    public void hasText_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TextFlow has text \"foobar baaz\"\n     " +
                "but: was TextFlow containing text: \"foobar quux\"");

        assertThat(textFlow, TextFlowMatchers.hasText("foobar baaz"));
    }

    @Test
    public void hasColoredText() {
        assertThat(textFlow, TextFlowMatchers.hasColoredText("foobar <RED>quux</RED>"));
    }

    @Test
    public void hasColoredText_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TextFlow has colored text \"foobar <BLUE>quux</BLUE>\"\n     " +
                "but: was TextFlow with colored text: \"foobar <RED>quux</RED>\"");

        assertThat(textFlow, TextFlowMatchers.hasColoredText("foobar <BLUE>quux</BLUE>"));
    }

    @Test
    public void hasColoredText_withBogusColor_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TextFlow has colored text \"foobar <LALALA>quux</LALALA>\"\n     " +
                "but: was TextFlow with colored text: \"foobar <RED>quux</RED>\"");

        assertThat(textFlow, TextFlowMatchers.hasColoredText("foobar <LALALA>quux</LALALA>"));
    }

    @Test
    public void hasExactlyColoredText_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TextFlow has exactly colored text \"<LIMEGREEN>exact</LIMEGREEN>\"\n     " +
                "but: was impossible to exactly match TextFlow containing " +
                "colored text: \"exact\" which has color: \"33cd32\".\n" +
                "This is not a named color. The closest named color is: \"LIMEGREEN\".\n" +
                "See: https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor");

        assertThat(exactTextFlow, TextFlowMatchers.hasExactlyColoredText("<LIMEGREEN>exact</LIMEGREEN>"));
    }
}
