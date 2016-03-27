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
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public interface PixelMatcher extends ColorMatcher {

    /**
     * Returns a {@link PixelMatcherResult} that indicates how similar/dissimilar the two images were.
     */
    PixelMatcherResult match(Image image0, Image image1);

    /**
     * Creates a new {@link WritableImage} using {@code image0}'s width and {@code image1}'s height.
     */
    WritableImage createEmptyMatchImage(Image image0, Image image1);

    /**
     * Creates a color that represents a match between the two images' pixels.
     */
    Color createMatchColor(Color color0, Color color1);

    /**
     * Creates a color that represents a mismatch between the two images' pixels.
     */
    Color createNonMatchColor(Color color0, Color color1);

}
