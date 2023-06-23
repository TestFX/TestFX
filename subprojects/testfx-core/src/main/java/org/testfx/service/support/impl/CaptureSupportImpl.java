/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2023 The TestFX Contributors
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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;
import javax.imageio.ImageIO;

import org.testfx.robot.BaseRobot;
import org.testfx.service.support.CaptureFileFormat;
import org.testfx.service.support.CaptureSupport;
import org.testfx.service.support.PixelMatcher;
import org.testfx.service.support.PixelMatcherResult;

import static org.testfx.util.WaitForAsyncUtils.asyncFx;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

public class CaptureSupportImpl implements CaptureSupport {

    public static final CaptureFileFormat DEFAULT_FORMAT = CaptureFileFormat.PNG;

    private final BaseRobot baseRobot;

    public CaptureSupportImpl(BaseRobot baseRobot) {
        this.baseRobot = baseRobot;
    }

    @Override
    public Image captureNode(Node node) {
        return waitFor(asyncFx(() -> snapshotNodeToImage(node)));
    }

    @Override
    public Image captureRegion(Rectangle2D region) {
        return baseRobot.captureRegion(region);
    }

    @Override
    public Image loadImage(Path path) {
        checkFileExists(path);
        try (InputStream inputStream = Files.newInputStream(path)) {
            return readImageFromStream(inputStream);
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void saveImage(Image image,
                          Path path) {
        saveImage(image, DEFAULT_FORMAT, path);
    }

    @Override
    public void saveImage(Image image, CaptureFileFormat format, Path path) {
        checkParentDirectoryExists(path);
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            writeImageToStream(image, format.toString(), outputStream);
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Image annotateImage(Shape shape,
                               Image image) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PixelMatcherResult matchImages(Image image0,
                                          Image image1,
                                          PixelMatcher pixelMatcher) {
        return pixelMatcher.match(image0, image1);
    }

    private void checkFileExists(Path path) {
        if (!path.toFile().isFile()) {
            throw new RuntimeException("File " + path.getFileName() + " not found.");
        }
    }

    private void checkParentDirectoryExists(Path path) {
        if (!path.toAbsolutePath().getParent().toFile().isDirectory()) {
            throw new RuntimeException("Directory " + path.getFileName() + " not found.");
        }
    }

    private Image snapshotNodeToImage(Node node) {
        return node.snapshot(null, null);
    }

    private Image readImageFromStream(InputStream inputStream) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    private void writeImageToStream(Image image,
                                    String imageFormat,
                                    OutputStream outputStream) throws IOException {
        BufferedImage imageWithType = new BufferedImage((int) image.getWidth(),
                (int) image.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, imageWithType);
        ImageIO.write(bufferedImage, imageFormat, outputStream);
        if (!ImageIO.write(bufferedImage, imageFormat, outputStream)) {
            throw new IOException("Image was not created");
        }
    }

    private Image blendImages(Image image0,
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

}
