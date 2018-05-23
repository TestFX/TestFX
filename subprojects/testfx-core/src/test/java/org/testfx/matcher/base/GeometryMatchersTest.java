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
package org.testfx.matcher.base;

import javafx.geometry.Dimension2D;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit.TestFXRule;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;

public class GeometryMatchersTest extends FxRobot {

    @Rule
    public TestRule rule = new TestFXRule();

    @Test
    public void hasDimension() {
        assertThat(new Dimension2D(10, 20), GeometryMatchers.hasDimension(10, 20));
    }

    @Test
    public void hasDimension_fails() {
        assertThatThrownBy(() -> assertThat(new Dimension2D(10, 20), GeometryMatchers.hasDimension(0, 0)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("\nExpected: Dimension2D has dimension (0.0, 0.0)\n");
    }

}
