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

import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assume.assumeThat;
import static org.testfx.assertions.api.Assertions.assertThat;

public class TextAssertTest extends FxRobot {

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
        assertThat(foobarText).hasText("foobar");
    }

    @Test
    public void hasText_fails() {
        assertThatThrownBy(() -> assertThat(quuxText).hasText("foobar"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Text has text \"foobar\"\n     " +
                        "but: was Text with text: \"quux\"");
    }

    @Test
    public void doesNotHaveText() {
        assertThat(foobarText).doesNotHaveText("veritas");
    }

    @Test
    public void doesNotHaveText_fails() {
        assertThatThrownBy(() -> assertThat(quuxText).doesNotHaveText("quux"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Text has text \"quux\" to be false\n     " +
                        "but: was Text with text: \"quux\"");
    }

    @Test
    public void hasText_matcher() {
        assertThat(foobarText).hasText(endsWith("bar"));
    }

    @Test
    public void hasText_matcher_fails() {
        assertThatThrownBy(() -> assertThat(quuxText).hasText(endsWith("bar")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Text has a string ending with \"bar\"\n     " +
                        "but: was Text with text: \"quux\"");
    }

    @Test
    public void doesNotHaveText_matcher() {
        assertThat(foobarText).doesNotHaveText(startsWith("bar"));
    }

    @Test
    public void doesNotHaveText_matcher_fails() {
        assertThatThrownBy(() -> assertThat(foobarText).doesNotHaveText(startsWith("foo")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Text has a string starting with \"foo\" to be false\n     " +
                        "but: was Text with text: \"foobar\"");
    }

    @Test
    public void hasFont() {
        assertThat(foobarText).hasFont(Font.getDefault());
        assumeThat("skipping: no testable fonts installed on system", fontFamily, is(notNullValue()));
        assertThat(quuxText).hasFont(Font.font(fontFamily, 16));
    }

    @Test
    public void hasFont_fails() {
        assumeThat("skipping: no testable fonts installed on system", fontFamily, is(notNullValue()));
        assertThatThrownBy(() -> assertThat(quuxText).hasFont(Font.font(fontFamily, 14)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage(String.format("Expected: Text has font " +
                        "\"%1$s\" with family (\"%1$s\") and size (14.0)\n     " +
                        "but: was Text with font: " +
                        "\"%1$s\" with family (\"%1$s\") and size (16.0)", fontFamily));
    }

    @Test
    public void doesNotHaveFont() {
        assertThat(foobarText).doesNotHaveFont(Font.font(14));
    }

    @Test
    public void doesNotHaveFont_fails() {
        assertThatThrownBy(() -> assertThat(foobarText).doesNotHaveFont(Font.getDefault()))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage(String.format("Expected: Text has font " +
                        "\"%1$s\" with family (\"%2$s\") and size (%3$.1f) to be false\n     " +
                        "but: was Text with font: " +
                        "\"%1$s\" with family (\"%2$s\") and size (%3$.1f)",
                        Font.getDefault().getName(), Font.getDefault().getFamily(), Font.getDefault().getSize()));
    }

    @Test
    public void hasFontSmoothingType() {
        assertThat(foobarText).hasFontSmoothingType(FontSmoothingType.GRAY);
        assertThat(quuxText).hasFontSmoothingType(FontSmoothingType.LCD);
    }

    @Test
    public void hasFontSmoothingType_fails() {
        assertThatThrownBy(() -> assertThat(foobarText).hasFontSmoothingType(FontSmoothingType.LCD))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Text has font smoothing type: \"LCD\"\n     " +
                        "but: was Text with font smoothing type: \"GRAY\"");
    }

    @Test
    public void doesNotHaveFontSmoothingType() {
        assertThat(foobarText).doesNotHaveFontSmoothingType(FontSmoothingType.LCD);
        assertThat(quuxText).doesNotHaveFontSmoothingType(FontSmoothingType.GRAY);
    }

    @Test
    public void doesNotHaveFontSmoothingType_fails() {
        assertThatThrownBy(() -> assertThat(foobarText).doesNotHaveFontSmoothingType(FontSmoothingType.GRAY))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Text has font smoothing type: \"GRAY\" to be false\n     " +
                        "but: was Text with font smoothing type: \"GRAY\"");
    }

    @Test
    public void hasStrikethrough() {
        assertThat(foobarText).hasStrikethrough();
    }

    @Test
    public void hasStrikethrough_fails() {
        assertThatThrownBy(() -> assertThat(quuxText).hasStrikethrough())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Text has strikethrough\n     " +
                        "but: was Text without strikethrough");
    }

    @Test
    public void doesNotHaveStrikethrough() {
        assertThat(quuxText).doesNotHaveStrikethrough();
    }

    @Test
    public void doesNotHaveStrikethrough_fails() {
        assertThatThrownBy(() -> assertThat(foobarText).doesNotHaveStrikethrough())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Text does not have strikethrough\n     " +
                        "but: was Text with strikethrough");
    }

    @Test
    public void isUnderlined() {
        assertThat(quuxText).isUnderlined();
    }

    @Test
    public void isUnderlined_fails() {
        assertThatThrownBy(() -> assertThat(foobarText).isUnderlined())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Text is underlined\n     " +
                        "but: was Text without underline");
    }

    @Test
    public void isNotUnderlined() {
        assertThat(foobarText).isNotUnderlined();
    }

    @Test
    public void isNotUnderlined_fails() {
        assertThatThrownBy(() -> assertThat(quuxText).isNotUnderlined())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Text is not underlined\n     " +
                        "but: was Text with underline");
    }

    private static void findFontFamily() {
        for (String fontFamily : Font.getFamilies()) {
            if (Font.getFontNames(fontFamily).size() == 1 && Font.getFontNames(fontFamily).get(0).equals(fontFamily)) {
                TextAssertTest.fontFamily = fontFamily;
                break;
            }
        }
    }
}
