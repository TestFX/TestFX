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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;

import org.testfx.api.annotation.Unstable;
import org.testfx.robot.BaseRobot;
import org.testfx.service.support.CaptureSupport;
import org.testfx.service.support.MatchAlgorithm;

@Unstable(reason = "needs more tests")
public class CaptureSupportImpl implements CaptureSupport {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private BaseRobot baseRobot;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public CaptureSupportImpl(BaseRobot baseRobot) {
        this.baseRobot = baseRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public Image captureRegion(Rectangle2D region) {
        return baseRobot.captureRegion(region);
    }

    @Override
    public Image captureNode(Node node) {
        return node.snapshot(null, null);
    }

    @Override
    public Image drawShape(Shape shape,
                           Image image) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Image blendImages(Image image0,
                             Image image1,
                             BlendMode blendMode) {
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.TOP_LEFT);
        stackPane.getChildren().add(new ImageView(image0));
        stackPane.getChildren().add(new ImageView(image1));
        stackPane.setBlendMode(blendMode);
        return captureNode(stackPane);
    }

    @Override
    public MatchResult<Image> matchImages(Image image0,
                                          Image image1,
                                          MatchAlgorithm algorithm) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Image loadImage(File file) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveImage(Image image,
                          File file) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bufferedImage, "png", file);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}

