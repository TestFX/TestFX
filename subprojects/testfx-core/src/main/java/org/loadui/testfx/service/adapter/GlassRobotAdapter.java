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
package org.loadui.testfx.service.adapter;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Robot;

public class GlassRobotAdapter {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private Robot glassRobot;

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    // ROBOT.

    public void robotCreate() {
        glassRobot = createGlassRobot();
    }

    public void robotDestroy() {
        glassRobot.destroy();
    }

    public Robot getRobotInstance() {
        return glassRobot;
    }

    // KEY.

    public void keyPress(KeyCode key) {
        glassRobot.keyPress(convertToKeyCodeId(key));
    }

    public void keyRelease(KeyCode key) {
        glassRobot.keyRelease(convertToKeyCodeId(key));
    }

    // MOUSE.

    public Point2D getMouseLocation() {
        return convertFromCoordinates(glassRobot.getMouseX(), glassRobot.getMouseY());
    }

    public void mouseMove(Point2D location) {
        glassRobot.mouseMove((int) location.getX(), (int) location.getY());
    }

    public void mousePress(MouseButton button) {
        glassRobot.mousePress(convertToButtonId(button));
    }

    public void mouseRelease(MouseButton button) {
        glassRobot.mouseRelease(convertToButtonId(button));
    }

    public void mouseWheel(int wheelAmount) {
        glassRobot.mouseWheel(wheelAmount);
    }

    // CAPTURE.

    public Color getCapturePixelColor(Point2D location) {
        int glassColor = glassRobot.getPixelColor((int) location.getX(), (int) location.getY());
        return convertFromGlassColor(glassColor);
    }

    public Image getCaptureRegion(Rectangle2D region) {
        Pixels glassPixels = glassRobot.getScreenCapture(
            (int) region.getMinX(), (int) region.getMinY(),
            (int) region.getWidth(), (int) region.getHeight()
        );
        return convertFromGlassPixels(glassPixels);
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

    private Robot createGlassRobot() {
        return Application.GetApplication().createRobot();
    }

    @SuppressWarnings("deprecation")
    private int convertToKeyCodeId(KeyCode keyCode) {
        return keyCode.impl_getCode();
    }

    private Point2D convertFromCoordinates(int x,
                                           int y) {
        return new Point2D(x, y);
    }

    private int convertToButtonId(MouseButton button) {
        //int MOUSE_LEFT_BTN   = 1;
        //int MOUSE_RIGHT_BTN  = 2;
        //int MOUSE_MIDDLE_BTN = 4;
        throw new UnsupportedOperationException();
    }

    private Color convertFromGlassColor(int glassColor) {
        throw new UnsupportedOperationException();
    }

    private Image convertFromGlassPixels(Pixels glassPixels) {
        throw new UnsupportedOperationException();
    }

}
