package org.loadui.testfx.service.adapter;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import com.sun.javafx.robot.FXRobot;
import com.sun.javafx.robot.FXRobotFactory;
import com.sun.javafx.robot.FXRobotImage;

public class JavafxRobotAdapter {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private FXRobot fxRobot;

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    // ROBOT.

    public void robotCreate(Scene scene) {
        fxRobot = createFxRobot(scene);
    }

    public void robotDestroy() {
        throw new UnsupportedOperationException();
    }

    public FXRobot getRobotInstance() {
        return fxRobot;
    }

    // KEY.

    public void keyPress(KeyCode key) {
        fxRobot.keyPress(key);
    }

    public void keyRelease(KeyCode key) {
        fxRobot.keyRelease(key);
    }

    public void keyType(KeyCode key, String character) {
        fxRobot.keyType(key, character);
    }

    // MOUSE.

    public Point2D getMouseLocation() {
        throw new UnsupportedOperationException();
    }

    public void mouseMove(Point2D location) {
        fxRobot.mouseMove((int) location.getX(), (int) location.getY());
    }

    public void mousePress(MouseButton button, int clickCount) {
        fxRobot.mousePress(button, clickCount);
    }

    public void mouseRelease(MouseButton button, int clickCount) {
        fxRobot.mouseRelease(button, clickCount);
    }

    public void mouseClick(MouseButton button, int clickCount) {
        fxRobot.mouseClick(button, clickCount);
    }

    public void mousePress(MouseButton button) {
        fxRobot.mousePress(button);
    }

    public void mouseRelease(MouseButton button) {
        fxRobot.mouseRelease(button);
    }

    public void mouseClick(MouseButton button) {
        fxRobot.mouseClick(button);
    }

    public void mouseDrag(MouseButton button) {
        fxRobot.mouseDrag(button);
    }

    public void mouseWheel(int wheelAmount) {
        fxRobot.mouseWheel(wheelAmount);
    }

    // CAPTURE.

    public Color getCapturePixelColor(Point2D location) {
        int fxRobotColor = fxRobot.getPixelColor((int) location.getX(), (int) location.getY());
        return convertFromFxRobotColor(fxRobotColor);
    }

    public Image getCaptureRegion(Rectangle2D region) {
        FXRobotImage fxRobotImage = fxRobot.getSceneCapture(
            (int) region.getMinX(), (int) region.getMinY(),
            (int) region.getWidth(), (int) region.getHeight()
        );
        return convertFromFxRobotImage(fxRobotImage);
    }

    // TIMER.

    /**
     * Block until events in the queue are processed.
     */
    public void timerWaitForIdle() {
        throw new UnsupportedOperationException();
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private FXRobot createFxRobot(Scene scene) {
        return FXRobotFactory.createRobot(scene);
    }

    private Color convertFromFxRobotColor(int fxRobotColor) {
        throw new UnsupportedOperationException();
    }

    private Image convertFromFxRobotImage(FXRobotImage fxRobotImage) {
        throw new UnsupportedOperationException();
    }

}
