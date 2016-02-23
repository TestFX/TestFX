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
import java.io.InputStream;
import java.io.OutputStream;
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

import com.google.common.io.Files;
import org.testfx.api.annotation.Unstable;
import org.testfx.robot.BaseRobot;
import org.testfx.service.support.CaptureSupport;
import org.testfx.service.support.MatchAlgorithm;

import static org.testfx.util.WaitForAsyncUtils.asyncFx;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

@Unstable(reason = "needs more tests")
public class CaptureSupportImpl implements CaptureSupport {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final String PNG_IMAGE_FORMAT = "png";

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
        return waitFor(asyncFx(() -> snapshotNodeToImage(node)));
    }

    @Override
    public Image loadImage(File file) {
        try {
            return readImageFromStream(Files.asByteSource(file).openBufferedStream());
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void saveImage(File file,
                          Image image) {
        try {
            writeImageToStream(Files.asByteSink(file).openBufferedStream(), image);
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Image drawShape(Shape shape,
                           Image image) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Image blendImages(Image image0,
                             Image image1,
                             BlendMode blendMode,
                             Pos alignment) {
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(alignment);
        stackPane.setBlendMode(blendMode);
        stackPane.getChildren().add(new ImageView(image0));
        stackPane.getChildren().add(new ImageView(image1));
        return captureNode(stackPane);
    }

    @Override
    public MatchResult<Image> matchImages(Image image0,
                                          Image image1,
                                          MatchAlgorithm algorithm) {
        throw new UnsupportedOperationException();
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Image snapshotNodeToImage(Node node) {
        return node.snapshot(null, null);
    }

    private Image readImageFromStream(InputStream inputStream) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    private void writeImageToStream(OutputStream outputStream,
                                    Image image) throws IOException {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ImageIO.write(bufferedImage, PNG_IMAGE_FORMAT, outputStream);
    }

}

