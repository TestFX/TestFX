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

import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeThat;

public class TextMatchersTest extends FxRobot {

    @Rule
    public TestRule rule = new TestFXRule();

    Text foobarText;
    Text quuxText;
    static String fontFamily;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
        findFontFamily();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupFixture(() -> {
            foobarText = new Text("foobar");
            foobarText.setStrikethrough(true);
            foobarText.setFontSmoothingType(FontSmoothingType.GRAY);
            quuxText = new Text("quux");
            quuxText.setUnderline(true);
            quuxText.setFontSmoothingType(FontSmoothingType.LCD);
            if (fontFamily != null) {
                quuxText.setFont(Font.font(fontFamily, 16));
            }
        });
    }

    @Test
    public void hasText() {
        assertThat(foobarText, TextMatchers.hasText("foobar"));
    }

    @Test
    public void hasText_fails() {
        assertThatThrownBy(() -> assertThat(quuxText, TextMatchers.hasText("foobar")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: Text has text \"foobar\"\n     " +
                        "but: was Text with text: \"quux\"");
    }

    @Test
    public void hasText_matcher() {
        assertThat(foobarText, TextMatchers.hasText(endsWith("bar")));
    }

    @Test
    public void hasText_matcher_fails() {
        assertThatThrownBy(() -> assertThat(quuxText, TextMatchers.hasText(endsWith("bar"))))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: Text has a string ending with \"bar\"\n     " +
                        "but: was Text with text: \"quux\"");
    }

    @Test
    public void hasFont() {
        assertThat(foobarText, TextMatchers.hasFont(Font.getDefault()));
        assumeThat("skipping: no testable fonts installed on system", fontFamily, is(notNullValue()));
        assertThat(quuxText, TextMatchers.hasFont(Font.font(fontFamily, 16)));
    }

    @Test
    public void hasFont_fails() {
        assumeThat("skipping: no testable fonts installed on system", fontFamily, is(notNullValue()));
        assertThatThrownBy(() -> assertThat(quuxText, TextMatchers.hasFont(Font.font(fontFamily, 14))))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage(String.format("\nExpected: Text has font " +
                        "\"%1$s\" with family (\"%1$s\") and size (14.0)\n     " +
                        "but: was Text with font: " +
                        "\"%1$s\" with family (\"%1$s\") and size (16.0)", fontFamily));
    }

    @Test
    public void hasFontSmoothingType() {
        assertThat(foobarText, TextMatchers.hasFontSmoothingType(FontSmoothingType.GRAY));
        assertThat(quuxText, TextMatchers.hasFontSmoothingType(FontSmoothingType.LCD));
    }

    @Test
    public void hasFontSmoothingType_fails() {
        assertThatThrownBy(() -> assertThat(foobarText, TextMatchers.hasFontSmoothingType(FontSmoothingType.LCD)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: Text has font smoothing type: \"LCD\"\n     " +
                        "but: was Text with font smoothing type: \"GRAY\"");
    }

    @Test
    public void hasStrikethrough() {
        assertThat(foobarText, TextMatchers.hasStrikethrough(true));
    }

    @Test
    public void hasStrikethrough_fails() {
        assertThatThrownBy(() -> assertThat(quuxText, TextMatchers.hasStrikethrough(true)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: Text has strikethrough\n     " +
                        "but: was Text without strikethrough");
    }

    @Test
    public void isUnderlined() {
        assertThat(quuxText, TextMatchers.isUnderlined(true));
    }

    @Test
    public void isUnderlined_fails() {
        assertThatThrownBy(() -> assertThat(foobarText, TextMatchers.isUnderlined(true)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: Text is underlined\n     " +
                        "but: was Text without underline");
    }

    private static void findFontFamily() {
        fontFamily = Font.getFamilies().stream().filter(
            fontFamily -> Font.getFontNames(fontFamily).size() == 1 && Font.getFontNames(fontFamily).get(0).equals(
                    fontFamily)).findFirst().orElse(null);
    }
}
