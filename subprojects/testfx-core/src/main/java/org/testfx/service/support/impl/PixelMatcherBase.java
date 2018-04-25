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
import org.testfx.service.support.PixelMatcherResult;

public abstract class PixelMatcherBase implements PixelMatcher {

    @Override
    public PixelMatcherResult match(Image image0,
                                    Image image1) {
        WritableImage matchImage = createEmptyMatchImage(image0, image1);
        int imageWidth = (int) matchImage.getWidth();
        int imageHeight = (int) matchImage.getHeight();

        long matchPixels = 0L;
        long totalPixels = imageWidth * imageHeight;

        for (int imageY = 0; imageY < imageHeight; imageY += 1) {
            for (int imageX = 0; imageX < imageWidth; imageX += 1) {
                Color color0 = image0.getPixelReader().getColor(imageX, imageY);
                Color color1 = image1.getPixelReader().getColor(imageX, imageY);
                boolean areColorsMatching = matchColors(color0, color1);

                if (areColorsMatching) {
                    matchPixels += 1;
                    Color matchColor = createMatchColor(color0, color1);
                    matchImage.getPixelWriter().setColor(imageX, imageY, matchColor);
                }
                else {
                    Color nonMatchColor = createNonMatchColor(color0, color1);
                    matchImage.getPixelWriter().setColor(imageX, imageY, nonMatchColor);
                }
            }
        }

        return new PixelMatcherResult(matchImage, matchPixels, totalPixels);
    }

}
