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
package org.testfx.service.support;

import javafx.scene.image.Image;

/**
 * Indicates how similar/dissimilar two images were on a pixel-to-pixel comparison level via
 * {@link PixelMatcher#match(Image, Image)}.
 */
public class PixelMatcherResult {

    private final Image matchImage;
    private final long totalPixels;
    private final long matchPixels;
    private final double matchFactor;

    public PixelMatcherResult(Image matchImage, long matchPixels, long totalPixels) {
        this.matchImage = matchImage;
        this.totalPixels = totalPixels;
        this.matchPixels = matchPixels;
        this.matchFactor = matchPixels / (double) totalPixels;
    }

    /**
     * Gets the image whose pixels indicate matches and mismatches between the two original images.
     */
    public Image getMatchImage() {
        return matchImage;
    }

    /**
     * Gets the total number of pixels in the match image.
     */
    public long getTotalPixels() {
        return totalPixels;
    }

    /**
     * Gets the total number of pixels that matched between the two original images.
     */
    public long getMatchPixels() {
        return matchPixels;
    }

    /**
     * Gets the total number of pixels that did not match between the two original images.
     */
    public long getNonMatchPixels() {
        return totalPixels - matchPixels;
    }

    /**
     * Gets the percentage of pixels that matched between the two original images.
     */
    public double getMatchFactor() {
        return matchFactor;
    }

    /**
     * Gets the percentage of pixels that did not match between the two original images.
     */
    public double getNonMatchFactor() {
        return 1.0 - matchFactor;
    }

}
