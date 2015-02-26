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
package org.testfx.service.adapter.impl;

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
        fxRobot.waitForIdle();
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private FXRobot createFxRobot(Scene scene) {
        FXRobot fxRobot = FXRobotFactory.createRobot(scene);
        fxRobot.setAutoWaitForIdle(false);
        return fxRobot;
    }

    private Color convertFromFxRobotColor(int fxRobotColor) {
        throw new UnsupportedOperationException();
    }

    private Image convertFromFxRobotImage(FXRobotImage fxRobotImage) {
        throw new UnsupportedOperationException();
    }

}
