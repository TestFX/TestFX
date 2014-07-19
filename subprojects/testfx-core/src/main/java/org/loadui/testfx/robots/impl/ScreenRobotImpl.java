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
package org.loadui.testfx.robots.impl;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.Map;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import com.google.common.collect.ImmutableMap;
import org.loadui.testfx.robots.ScreenRobot;
import org.loadui.testfx.utils.FXTestUtils;

public class ScreenRobotImpl implements ScreenRobot {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final Map<MouseButton, Integer> AWT_BUTTONS = ImmutableMap.of(
        MouseButton.PRIMARY, InputEvent.BUTTON1_MASK,
        MouseButton.MIDDLE, InputEvent.BUTTON2_MASK,
        MouseButton.SECONDARY, InputEvent.BUTTON3_MASK
    );

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final Robot awtRobot;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public ScreenRobotImpl() {
        System.setProperty("java.awt.headless", "false");
        Toolkit.getDefaultToolkit();
        awtRobot = createAwtRobot();
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public Point2D retrieveMouse() {
        Point awtPoint = MouseInfo.getPointerInfo().getLocation();
        return new Point2D(awtPoint.getX(), awtPoint.getY());
    }

    @Override
    public void moveMouse(Point2D point) {
        awtRobot.mouseMove((int) point.getX(), (int) point.getY());
    }

    @Override
    public void pressMouse(MouseButton button) {
        awtRobot.mousePress(AWT_BUTTONS.get(button));
        awaitEvents();
    }

    @Override
    public void releaseMouse(MouseButton button) {
        awtRobot.mouseRelease(AWT_BUTTONS.get(button));
        awaitEvents();
    }

    @Override
    public void scrollMouse(int amount) {
        awtRobot.mouseWheel(amount);
        awaitEvents();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void pressKey(KeyCode key) {
        awtRobot.keyPress(key.impl_getCode());
        awaitEvents();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void releaseKey(KeyCode key) {
        awtRobot.keyRelease(key.impl_getCode());
        awaitEvents();
    }

    @Override
    public void awaitEvents() {
        FXTestUtils.awaitEvents();
    }

    @Override
    public Image captureRegion(Rectangle2D region) {
        Rectangle awtRectangle = new Rectangle(
            (int) region.getMinX(), (int) region.getMinY(),
            (int) region.getWidth(), (int) region.getHeight()
        );
        BufferedImage bufferedImage = awtRobot.createScreenCapture(awtRectangle);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Robot createAwtRobot() {
        try {
            return new Robot();
        }
        catch (AWTException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

}
