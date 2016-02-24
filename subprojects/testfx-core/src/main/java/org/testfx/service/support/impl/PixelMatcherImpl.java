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
package org.testfx.service.support.impl;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import org.testfx.service.support.PixelMatcher;
import org.testfx.service.support.PixelMatcherResult;

public class PixelMatcherImpl implements PixelMatcher {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private double minColorDistFactor = 0.20;

    private double colorBlendFactor = 0.75;

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public PixelMatcherResult match(Image image0,
                                    Image image1) {
        int imageWidth = (int) image0.getWidth();
        int imageHeight = (int) image0.getHeight();
        WritableImage matchImage = new WritableImage(imageWidth, imageHeight);

        int matchPixels = 0;
        int totalPixels = imageWidth * imageHeight;

        for (int imageY = 0; imageY < imageHeight; imageY += 1) {
            for (int imageX = 0; imageX < imageWidth; imageX += 1) {
                Color color0 = image0.getPixelReader().getColor(imageX, imageY);
                Color color1 = image1.getPixelReader().getColor(imageX, imageY);
                boolean areColorsMatching = matchColors(color0, color1);

                if (areColorsMatching) {
                    matchPixels += 1;
                    Color blendColor = createMatchColor(color0, color1);
                    matchImage.getPixelWriter().setColor(imageX, imageY, blendColor);
                }
                else {
                    Color markColor = createNonMatchColor(color0, color1);
                    matchImage.getPixelWriter().setColor(imageX, imageY, markColor);
                }
            }
        }

        return new PixelMatcherResult(matchImage, matchPixels, totalPixels);
    }

    @Override
    public boolean matchColors(Color color0, Color color1) {
        double maxColorDistSq = calculateColorDistSq(Color.BLACK, Color.WHITE);
        double minColorDistSq = maxColorDistSq * (minColorDistFactor * minColorDistFactor);

        double colorDistSq = calculateColorDistSq(color0, color1);
        return colorDistSq < minColorDistSq;
    }

    @Override
    public Color createMatchColor(Color color0, Color color1) {
        double luma = calculateLuma(color0);
        double opacity = color0.getOpacity();
        double colorValue = blendToWhite(luma, colorBlendFactor);
        return Color.gray(colorValue, opacity);
    }

    @Override
    public Color createNonMatchColor(Color color0, Color color1) {
        return Color.RED;
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private double calculateLuma(Color color) {
        double luma = (color.getRed() * 0.29889531) +
                      (color.getGreen() * 0.58662247) +
                      (color.getBlue() * 0.11448223);
        return Math.max(0.0, Math.min(1.0, luma));
    }

    private double blendToWhite(double luma,
                                double factor) {
        return ((1.0 - factor) * luma) + factor;
    }

    private double calculateColorDistSq(Color color0,
                                        Color color1) {
        double diffRed = color0.getRed() - color1.getRed();
        double diffGreen = color0.getGreen() - color1.getGreen();
        double diffBlue = color0.getBlue() - color1.getBlue();
        return (diffRed * diffRed) + (diffGreen * diffGreen) + (diffBlue * diffBlue);
    }

}
