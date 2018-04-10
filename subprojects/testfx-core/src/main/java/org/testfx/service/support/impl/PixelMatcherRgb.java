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
package org.testfx.service.support.impl;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import org.testfx.service.support.PixelMatcher;
import org.testfx.util.ColorUtils;

public class PixelMatcherRgb extends PixelMatcherBase implements PixelMatcher {

    private final double colorBlendFactor;
    private final double minColorDistSq;

    public PixelMatcherRgb() {
        this(0.20, 0.75);
    }

    public PixelMatcherRgb(double minColorDistFactor, double colorBlendFactor) {
        this.colorBlendFactor = colorBlendFactor;
        double maxColorDistSq = ColorUtils.calculateColorDistSq(Color.BLACK, Color.WHITE);
        minColorDistSq = maxColorDistSq * (minColorDistFactor * minColorDistFactor);
    }

    @Override
    public boolean matchColors(Color color0, Color color1) {
        double colorDistSq = ColorUtils.calculateColorDistSq(color0, color1);
        return colorDistSq < minColorDistSq;
    }

    @Override
    public WritableImage createEmptyMatchImage(Image image0,
                                               Image image1) {
        return new WritableImage((int) image0.getWidth(), (int) image1.getHeight());
    }

    @Override
    public Color createMatchColor(Color color0, Color color1) {
        double gray = color0.grayscale().getRed();
        double opacity = color0.getOpacity();
        return Color.gray(blendToWhite(gray, colorBlendFactor), opacity);
    }

    @Override
    public Color createNonMatchColor(Color color0, Color color1) {
        return Color.RED;
    }

    private double blendToWhite(double gray, double factor) {
        return ((1.0 - factor) * gray) + factor;
    }

}
