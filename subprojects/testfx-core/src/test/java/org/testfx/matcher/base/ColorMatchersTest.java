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
package org.testfx.matcher.base;

import javafx.scene.paint.Color;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testfx.api.FxRobot;
import org.testfx.service.support.impl.PixelMatcherRgb;

import static org.hamcrest.MatcherAssert.assertThat;

public class ColorMatchersTest extends FxRobot {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    @Rule
    public ExpectedException exception = ExpectedException.none();

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void hasColor() {
        // expect:
        assertThat(Color.color(1, 0, 0), ColorMatchers.hasColor(Color.RED));
    }

    @Test
    public void hasColor_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: Color has color (0x000000ff)\n");

        assertThat(Color.color(1, 0, 0), ColorMatchers.hasColor(Color.BLACK));
    }

    @Test
    public void hasColor_pixelMatcher() {
        // expect:
        assertThat(Color.color(0.9, 0, 0),
                ColorMatchers.hasColor(Color.RED, new PixelMatcherRgb()));
    }

    @Test
    public void hasColor_pixelMatcher_fails() {
        // expect:
        assertThat(Color.color(0.5, 0, 0),
                ColorMatchers.hasColor(Color.RED, new PixelMatcherRgb(0.5, 0)));
    }

}
