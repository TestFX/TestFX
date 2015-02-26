/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.testfx.service.support;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.stage.Screen;

import org.testfx.robot.BaseRobot;

public class CaptureSupport {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private BaseRobot baseRobot;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public CaptureSupport(BaseRobot baseRobot) {
        this.baseRobot = baseRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public void captureScreenToFile(Screen screen, File captureFile) {
        Image captureImage = captureScreenToImage(screen);
        writeCaptureImageToFile(captureImage, captureFile);
    }

    public void capturePrimaryScreenToFile(File captureFile) {
        captureScreenToFile(Screen.getPrimary(), captureFile);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Image captureScreenToImage(Screen screen) {
        Rectangle2D region = screen.getBounds();
        return baseRobot.captureRegion(region);
    }

    private Image captureNodeToImage(Node node) {
        return node.snapshot(null, null);
    }

    private void writeCaptureImageToFile(Image captureImage, File captureFile) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(captureImage, null);
        try {
            ImageIO.write(bufferedImage, "png", captureFile);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
