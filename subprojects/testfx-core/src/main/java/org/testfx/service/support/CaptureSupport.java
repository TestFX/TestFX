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

import java.nio.file.Path;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.shape.Shape;

public interface CaptureSupport {

    /**
     * Returns a snapshot of the node.
     */
    Image captureNode(Node node);

    /**
     * Returns a screenshot of the given region.
     */
    Image captureRegion(Rectangle2D region);

    /**
     * Loads the image file from the given path.
     */
    Image loadImage(Path path);

    /**
     * Saves the given image to the given path.
     */
    void saveImage(Image image, Path path);

    /**
     * NOT YET IMPLEMENTED
     */
    Image annotateImage(Shape shape, Image image);

    /**
     * Compares two images and returns a {@link PixelMatcherResult} that defines
     * the how similar/dissimilar one was from the other.
     */
    PixelMatcherResult matchImages(Image image0, Image image1, PixelMatcher pixelMatcher);

}
