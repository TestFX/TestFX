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

import java.io.File;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.shape.Shape;

import org.testfx.service.support.impl.MatchResult;

public interface CaptureSupport {

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    Image captureRegion(Rectangle2D region);
    Image captureNode(Node node);

    Image loadImage(File file);
    void saveImage(File file,
                   Image image);

    Image drawShape(Shape shape,
                    Image image);
    Image blendImages(Image image0,
                      Image image1,
                      BlendMode blendMode,
                      Pos alignment);
    MatchResult<Image> matchImages(Image image0,
                                   Image image1,
                                   MatchAlgorithm algorithm);

}
