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
package org.testfx.service.support;

import javafx.scene.image.Image;

public class PixelMatcherResult {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final Image matchImage;
    private final long totalPixels;

    private final long matchPixels;
    private final double matchFactor;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public PixelMatcherResult(Image matchImage,
                              long matchPixels,
                              long totalPixels) {
        this.matchImage = matchImage;
        this.totalPixels = totalPixels;

        this.matchPixels = matchPixels;
        this.matchFactor = matchPixels / (double) totalPixels;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public Image getMatchImage() {
        return matchImage;
    }

    public long getTotalPixels() {
        return totalPixels;
    }

    public long getMatchPixels() {
        return matchPixels;
    }

    public long getNonMatchPixels() {
        return totalPixels - matchPixels;
    }

    public double getMatchFactor() {
        return matchFactor;
    }

    public double getNonMatchFactor() {
        return 1.0 - matchFactor;
    }

}
