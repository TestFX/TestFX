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

import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.service.support.impl.PixelMatcherRgb;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testfx.assertions.api.Assertions.assertThat;

public class ColorAssertTest extends FxRobot {

    @Test
    public void isColor() {
        assertThat(Color.color(1, 0, 0)).isColor(Color.RED);
    }

    @Test
    public void isColor_fails() {
        assertThatThrownBy(() -> assertThat(Color.color(1, 0, 0)).isColor(Color.BLACK))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Color has color \"BLACK\" (#000000)\n     " +
                        "but: was \"RED\" (#ff0000)");
    }

    @Test
    public void isNotColor() {
        assertThat(Color.color(1, 0, 0)).isNotColor(Color.BROWN);
    }

    @Test
    public void isNotColor_fails() {
        assertThatThrownBy(() -> assertThat(Color.color(1, 0, 0)).isNotColor(Color.RED))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Color has color \"RED\" (#ff0000) to be false\n     " +
                        "but: was \"RED\" (#ff0000)");
    }

    @Test
    public void isColor_colorMatcher() {
        assertThat(Color.color(0.9, 0, 0)).isColor(Color.RED, new PixelMatcherRgb());
    }

    @Test
    public void isColor_colorMatcher_fails() {
        assertThatThrownBy(() -> assertThat(Color.color(0.5, 0, 0)).isColor(Color.RED, new PixelMatcherRgb(0.01, 0)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Expected: Color has color \"RED\" (#ff0000)\n     ");
    }

    @Test
    public void isNotColor_colorMatcher() {
        assertThat(Color.color(0.9, 0, 0)).isNotColor(Color.GREEN, new PixelMatcherRgb());
    }

    @Test
    public void isNotColor_colorMatcher_fails() {
        assertThatThrownBy(() -> assertThat(Color.color(0.9, 0, 0)).isNotColor(Color.RED, new PixelMatcherRgb(0.6, 0)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Expected: Color has color \"RED\" (#ff0000) to be false\n     ");
    }

    @Test
    public void isNamedColor() {
        assertThat(Color.AQUAMARINE).isColor("AQUAMARINE");
    }

    @Test
    public void isNamedColor_fails() {
        assertThatThrownBy(() -> assertThat(Color.ANTIQUEWHITE).isColor("AQUAMARINE"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Color is \"AQUAMARINE\"\n     " +
                        "but: was \"ANTIQUEWHITE\" (#faebd7)");
    }

    @Test
    public void isNotNamedColor() {
        assertThat(Color.AQUAMARINE).isNotColor("BLUE");
    }

    @Test
    public void isNotNamedColor_fails() {
        assertThatThrownBy(() -> assertThat(Color.ANTIQUEWHITE).isNotColor("ANTIQUEWHITE"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Color is \"ANTIQUEWHITE\" to be false\n     " +
                        "but: was \"ANTIQUEWHITE\" (#faebd7)");
    }

    @Test
    public void isNamedColor_non_named_color_fails() {
        assertThatThrownBy(() -> assertThat(Color.web("#f3b2aa")).isColor("BAGEL"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("given color name: \"BAGEL\" is not a named color\n" +
                        "See: https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor");
    }

    @Test
    public void isNotNamedColor_non_named_color_fails() {
        assertThatThrownBy(() -> assertThat(Color.web("#f3b2aa")).isNotColor("BAGEL"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("given color name: \"BAGEL\" is not a named color\n" +
                        "See: https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor");
    }

    @Test
    public void hasClosestNamedColor_string() {
        assertThat(Color.color(0.8, 0.2, 0.1)).hasClosestNamedColor("FIREBRICK");
    }

    @Test
    public void hasClosestNamedColor_string_fails() {
        assertThatThrownBy(() -> assertThat(Color.color(0.6, 0.1, 0.1)).hasClosestNamedColor("GAINSBORO"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Color has closest named color \"GAINSBORO\" (#dcdcdc)\n     " +
                        "but: was \"#991a1a\" which has closest named color: \"BROWN\"");
    }

    @Test
    public void hasClosestNamedColor_non_named_color_string_fails() {
        assertThatThrownBy(() -> assertThat(Color.color(0.6, 0.1, 0.1)).hasClosestNamedColor("BETELGEUSE"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("given color name: \"BETELGEUSE\" is not a named color\n" +
                        "See: https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor");
    }

    @Test
    public void hasClosestNamedColor_non_named_color_fails() {
        assertThatThrownBy(() -> assertThat(Color.web("#dcdcdc")).hasClosestNamedColor(Color.web("#acb2f1")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("given color: \"#acb2f1\" is not a named color\n" +
                        "See: https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor");
    }

    @Test
    public void doesNotHaveClosestNamedColor() {
        assertThat(Color.color(0.8, 0.2, 0.1)).doesNotHaveClosestNamedColor("YELLOW");
    }

    @Test
    public void doesNotHaveClosestNamedColor_string_fails() {
        assertThatThrownBy(() -> assertThat(Color.color(0.6, 0.1, 0.1)).doesNotHaveClosestNamedColor("BROWN"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Color has closest named color \"BROWN\" (#a52a2a) to be false\n     " +
                        "but: was \"#991a1a\" which has closest named color: \"BROWN\"");
    }

    @Test
    public void doesNotHaveClosestNamedColor_color_fails() {
        assertThatThrownBy(() -> assertThat(Color.color(0.6, 0.1, 0.1)).doesNotHaveClosestNamedColor(Color.BROWN))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Color has closest named color \"BROWN\" (#a52a2a) to be false\n     " +
                        "but: was \"#991a1a\" which has closest named color: \"BROWN\"");
    }

    @Test
    public void doesNotHaveClosestNamedColor_non_named_color_fails() {
        assertThatThrownBy(() -> assertThat(Color.web("#dcdcdc")).doesNotHaveClosestNamedColor(Color.web("#acb2f1")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("given color: \"#acb2f1\" is not a named color\n" +
                        "See: https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor");
    }

}
