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

import java.util.concurrent.TimeoutException;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.util.WaitForAsyncUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;

public class TextFlowMatchersTest extends FxRobot {

    @Rule
    public TestRule rule = new TestFXRule();

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
        assertThat(textFlow, TextFlowMatchers.hasText("foobar quux"));
    }

    @Test
    public void hasText_fails() {
        assertThatThrownBy(() -> assertThat(textFlow, TextFlowMatchers.hasText("foobar baaz")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: TextFlow has text \"foobar baaz\"\n     " +
                        "but: was TextFlow containing text: \"foobar quux\"");
    }

    @Test
    public void hasColoredText() {
        assertThat(textFlow, TextFlowMatchers.hasColoredText("foobar <RED>quux</RED>"));
    }

    @Test
    public void hasColoredText_fails() {
        assertThatThrownBy(() -> assertThat(textFlow, TextFlowMatchers.hasColoredText("foobar <BLUE>quux</BLUE>")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: TextFlow has colored text \"foobar <BLUE>quux</BLUE>\"\n     " +
                        "but: was TextFlow with colored text: \"foobar <RED>quux</RED>\"");
    }

    @Test
    public void hasColoredText_withBogusColor_fails() {
        assertThatThrownBy(() -> assertThat(textFlow, TextFlowMatchers.hasColoredText("foobar <LALALA>quux</LALALA>")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: TextFlow has colored text \"foobar <LALALA>quux</LALALA>\"\n     " +
                        "but: was TextFlow with colored text: \"foobar <RED>quux</RED>\"");
    }

    @Test
    public void hasExactlyColoredText_fails() {
        assertThatThrownBy(() -> assertThat(exactTextFlow,
                TextFlowMatchers.hasExactlyColoredText("<LIMEGREEN>exact</LIMEGREEN>")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: TextFlow has exactly colored text \"<LIMEGREEN>exact</LIMEGREEN>\"\n     " +
                        "but: was impossible to exactly match TextFlow containing " +
                        "colored text: \"exact\" which has color: \"#33cd32\".\n" +
                        "This is not a named color. The closest named color is: \"LIMEGREEN\".\n" +
                        "See: https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor");
    }

    /**
     * TODO: Implement a a way to test against gradients, and other types of fills - hopefully without reimplementing
     * some adhoc version of CSS.
     */
    @Test
    public void hasExactlyColoredText_gradient() throws TimeoutException {
        // given (a TextFlow with a LinearGradient fill)
        FxToolkit.setupFixture(() -> {
            Text foobarText = new Text("foobar ");
            Text quuxText = new Text("quux");
            Stop[] stops = new Stop[] {new Stop(0, Color.BLACK), new Stop(1, Color.RED)};
            LinearGradient linearGradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
            quuxText.setFill(linearGradient);
            textFlow = new TextFlow(foobarText, quuxText);
        });

        WaitForAsyncUtils.waitForFxEvents();

        // when (an exact color match is attempted when the text has a gradient fill), then an exception is thrown
        assertThatThrownBy(() -> assertThat(textFlow,
                TextFlowMatchers.hasExactlyColoredText("foobar <BLACK>quux</BLACK>")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: TextFlow has exactly colored text \"foobar <BLACK>quux</BLACK>\"\n     " +
                        "but: was exact color matching for subclasses of javafx.scene.paint.Paint besides " +
                        "javafx.scene.paint.Color is not (yet) supported.");
    }
}
