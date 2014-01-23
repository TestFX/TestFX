package org.loadui.testfx.service.support;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.stage.Screen;

import org.loadui.testfx.framework.ScreenRobot;

public class CaptureSupport {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private ScreenRobot screenRobot;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public CaptureSupport(ScreenRobot screenRobot) {
        this.screenRobot = screenRobot;
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
        return screenRobot.captureRegion(region);
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
