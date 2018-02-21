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
package org.testfx.assertions.api;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testfx.assertions.api.Assertions.assertThat;

public class TextFlowAssertTest extends FxRobot {

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
        assertThat(textFlow).hasText("foobar quux");
    }

    @Test
    public void hasText_fails() {
        assertThatThrownBy(() -> assertThat(textFlow).hasText("foobar baaz"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TextFlow has text \"foobar baaz\"\n     " +
                        "but: was TextFlow containing text: \"foobar quux\"");
    }

    @Test
    public void doesNotHaveText() {
        assertThat(textFlow).doesNotHaveText("chess master");
    }

    @Test
    public void doesNotHaveText_fails() {
        assertThatThrownBy(() -> assertThat(textFlow).doesNotHaveText("foobar quux"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TextFlow has text \"foobar quux\" to be false\n     " +
                        "but: was TextFlow containing text: \"foobar quux\"");
    }

    @Test
    public void hasColoredText() {
        assertThat(textFlow).hasColoredText("foobar <RED>quux</RED>");
    }

    @Test
    public void hasColoredText_fails() {
        assertThatThrownBy(() -> assertThat(textFlow).hasColoredText("foobar <BLUE>quux</BLUE>"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TextFlow has colored text \"foobar <BLUE>quux</BLUE>\"\n     " +
                        "but: was TextFlow with colored text: \"foobar <RED>quux</RED>\"");
    }

    @Test
    public void hasColoredText_withBogusColor_fails() {
        assertThatThrownBy(() -> assertThat(textFlow).hasColoredText("foobar <LALALA>quux</LALALA>"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TextFlow has colored text \"foobar <LALALA>quux</LALALA>\"\n     " +
                        "but: was TextFlow with colored text: \"foobar <RED>quux</RED>\"");
    }

    @Test
    public void doesNotHaveColoredText() {
        assertThat(textFlow).doesNotHaveColoredText("foobar <GREEN>quux</GREEN>");
    }

    @Test
    public void doesNotHaveColoredText_fails() {
        assertThatThrownBy(() -> assertThat(textFlow).doesNotHaveColoredText("foobar <RED>quux</RED>"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TextFlow has colored text \"foobar <RED>quux</RED>\" to be false\n     " +
                        "but: was TextFlow with colored text: \"foobar <RED>quux</RED>\"");
    }

    @Test
    public void hasExactlyColoredText_fails() {
        assertThatThrownBy(() -> assertThat(exactTextFlow).hasExactlyColoredText("<LIMEGREEN>exact</LIMEGREEN>"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TextFlow has exactly colored text \"<LIMEGREEN>exact</LIMEGREEN>\"\n     " +
                        "but: was impossible to exactly match TextFlow containing " +
                        "colored text: \"exact\" which has color: \"#33cd32\".\n" +
                        "This is not a named color. The closest named color is: \"LIMEGREEN\".\n" +
                        "See: https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor");
    }
}
