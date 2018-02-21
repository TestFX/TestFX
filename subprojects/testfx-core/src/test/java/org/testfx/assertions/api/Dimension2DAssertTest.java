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

import javafx.geometry.Dimension2D;

import org.junit.Test;
import org.testfx.api.FxRobot;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testfx.assertions.api.Assertions.assertThat;

public class Dimension2DAssertTest extends FxRobot {

    @Test
    public void hasDimension() {
        assertThat(new Dimension2D(10, 20)).hasDimension(10, 20);
    }

    @Test
    public void hasDimension_fails() {
        assertThatThrownBy(() -> assertThat(new Dimension2D(10, 20)).hasDimension(0, 0))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Expected: Dimension2D has dimension (0.0, 0.0)\n");
    }

    @Test
    public void doesNotHaveDimension() {
        assertThat(new Dimension2D(10, 20)).doesNotHaveDimension(5, 10);
    }

    @Test
    public void doesNotHaveDimension_same_width() {
        assertThat(new Dimension2D(10, 20)).doesNotHaveDimension(10, 21);
    }

    @Test
    public void doesNotHaveDimension_same_height() {
        assertThat(new Dimension2D(10, 20)).doesNotHaveDimension(9, 20);
    }

    @Test
    public void doesNotHaveDimension_fails() {
        assertThatThrownBy(() -> assertThat(new Dimension2D(10, 20)).doesNotHaveDimension(10, 20))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("Expected: Dimension2D has dimension (10.0, 20.0) to be false\n");
    }
}
