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

import org.assertj.core.api.AbstractAssert;
import org.testfx.matcher.base.GeometryMatchers;

import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.assertions.impl.Adapter.fromInverseMatcher;
import static org.testfx.assertions.impl.Adapter.fromMatcher;

/**
 * Base class for all {@link javafx.geometry.Dimension2D} assertions.
 */
public class AbstractDimension2DAssert<SELF extends AbstractDimension2DAssert<SELF>>
        extends AbstractAssert<SELF, Dimension2D> {

    protected AbstractDimension2DAssert(Dimension2D dimension2D, Class<?> selfType) {
        super(dimension2D, selfType);
    }

    /**
     * Verifies that the actual {@link javafx.geometry.Dimension2D} has the given {@code width}
     * and {@code height}.
     *
     * @param width the given width to compare the actual width to
     * @param height the given height to compare the actual height to
     * @return this assertion object
     */
    public SELF hasDimension(double width, double height) {
        assertThat(actual).is(fromMatcher(GeometryMatchers.hasDimension(width, height)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.geometry.Dimension2D} does not have either the
     * given {@code width} and/or {@code height}.
     *
     * @param width the given width to compare the actual width to
     * @param height the given height to compare the actual height to
     * @return this assertion object
     */
    public SELF doesNotHaveDimension(double width, double height) {
        assertThat(actual).is(fromInverseMatcher(GeometryMatchers.hasDimension(width, height)));
        return myself;
    }
}
