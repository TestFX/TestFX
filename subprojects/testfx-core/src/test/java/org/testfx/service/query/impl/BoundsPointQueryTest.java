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
package org.testfx.service.query.impl;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.framework.junit.TestFXRule;

import static org.assertj.core.api.Assertions.assertThat;

public class BoundsPointQueryTest {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    Bounds bounds;

    @Before
    public void setup() {
        bounds = new BoundingBox(100, 200, 300, 400);
    }

    @Test
    public void atPosition_double_double_case_1() {
        // when:
        Point2D point = new BoundsPointQuery(bounds).atPosition(0.0, 0.0).query();

        // then:
        assertThat(point).isEqualTo(new Point2D(100, 200));
    }

    @Test
    public void atPosition_double_double_case_2() {
        // when:
        Point2D point = new BoundsPointQuery(bounds).atPosition(1.0, 1.0).query();

        // then:
        assertThat(point).isEqualTo(new Point2D(100 + 300, 200 + 400));
    }

    @Test
    public void atPosition_double_double_case_3() {
        // when:
        Point2D point = new BoundsPointQuery(bounds).atPosition(0.5, 0.5).query();

        // then:
        assertThat(point).isEqualTo(new Point2D(100 + 150, 200 + 200));
    }

    @Test
    public void atPosition_Pos_case_1() {
        // when:
        Point2D point = new BoundsPointQuery(bounds).atPosition(Pos.TOP_LEFT).query();

        // then:
        assertThat(point).isEqualTo(new Point2D(100, 200));
    }

    @Test
    public void atPosition_Pos_case_2() {
        // when:
        Point2D point = new BoundsPointQuery(bounds).atPosition(Pos.BOTTOM_RIGHT).query();

        // then:
        assertThat(point).isEqualTo(new Point2D(100 + 300, 200 + 400));
    }

    @Test
    public void atPosition_Pos_case_3() {
        // when:
        Point2D point = new BoundsPointQuery(bounds).atPosition(Pos.CENTER).query();

        // then:
        assertThat(point).isEqualTo(new Point2D(100 + 150, 200 + 200));
    }

    @Test
    public void atOffset_double_double_case_1() {
        // when:
        Point2D point = new BoundsPointQuery(bounds).atOffset(0, 0).query();

        // then:
        assertThat(point).isEqualTo(new Point2D(100, 200));
    }

    @Test
    public void atOffset_double_double_case_2() {
        // when:
        Point2D point = new BoundsPointQuery(bounds).atOffset(300, 400).query();

        // then:
        assertThat(point).isEqualTo(new Point2D(100 + 300, 200 + 400));
    }

    @Test
    public void atOffset_double_double_case_3() {
        // when:
        Point2D point = new BoundsPointQuery(bounds).atOffset(150, 200).query();

        // then:
        assertThat(point).isEqualTo(new Point2D(100 + 150, 200 + 200));
    }

}
