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

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import static org.testfx.matcher.base.GeneralMatchers.typeSafeMatcher;

/**
 * TestFX matchers for {@link Dimension2D} instances.
 */
public class GeometryMatchers {

    private GeometryMatchers() {}

    /**
     * Creates a {@link org.hamcrest.TypeSafeMatcher} that matches when a given {@link Dimension2D}'s width and height
     * equal the given width and height.
     */
    @Factory
    public static Matcher<Object> hasDimension(double width, double height) {
        String descriptionText = "has dimension (" + width + ", " + height + ")";
        return typeSafeMatcher(Dimension2D.class, descriptionText,
            dimension -> hasDimension(dimension, width, height));
    }

    private static boolean hasDimension(Dimension2D dimension, double width, double height) {
        return dimension.getWidth() == width && dimension.getHeight() == height;
    }

}
