/*
 * Copyright 2013 SmartBear Software
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
package org.loadui.testfx.framework;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
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

    private static final long MOVE_TIME = 175;

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final Robot awtRobot;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public ScreenRobotImpl() {
        try {
            awtRobot = new Robot();
        }
        catch (AWTException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public Point2D getMouseLocation() {
        Point awtPoint = MouseInfo.getPointerInfo().getLocation();
        return new Point2D(awtPoint.getX(), awtPoint.getY());
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

    @Override
    public void moveMouseTo(double x, double y) {
        awtRobot.mouseMove((int) x, (int) y);
    }

    @Override
    public void moveMouseLinearTo(double x, double y) {
        Point2D location = getMouseLocation();
        Point2D difference = getMouseDifferenceTo(x, y);

        // The maximum time for the movement is MOVE_TIME. Far movements will make the cursor go
        // faster. In order to be not too slow on small distances, the minimum speed is one pixel
        // per millisecond.
        double distance = getMouseDistance(difference);
        double totalTime = MOVE_TIME;
        if (distance < totalTime) {
            totalTime = Math.max(1, distance);
        }

        Point2D speed = new Point2D(difference.getX() / totalTime, difference.getY() / totalTime);
        for (int time = 0; time < totalTime; time++) {
            Point2D stepLocation = new Point2D(
                location.getX() + (speed.getX() * time),
                location.getY() + (speed.getY() * time)
            );
            awtRobot.mouseMove((int) stepLocation.getX(), (int) stepLocation.getY());
            try {
                Thread.sleep(1);
            }
            catch (InterruptedException ignore) {
                return;
            }
        }

        // We should be less than one step away from the target. Make one last step to hit it.
        awtRobot.mouseMove((int) x, (int) y);
        awaitCompletionOfEvents();
    }

    @Override
    public void pressMouse(MouseButton button) {
        if (button == null) {
            return;
        }
        awtRobot.mousePress(AWT_BUTTONS.get(button));
        awaitCompletionOfEvents();
    }

    @Override
    public void releaseMouse(MouseButton button) {
        if (button == null) {
            return;
        }
        awtRobot.mouseRelease(AWT_BUTTONS.get(button));
        awaitCompletionOfEvents();
    }

    @Override
    public void scrollMouse(int amount) {
        awtRobot.mouseWheel(amount);
        awaitCompletionOfEvents();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void pressKey(KeyCode key) {
        awtRobot.keyPress(key.impl_getCode());
        awaitCompletionOfEvents();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void releaseKey(KeyCode key) {
        awtRobot.keyRelease(key.impl_getCode());
        awaitCompletionOfEvents();
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private void awaitCompletionOfEvents() {
        FXTestUtils.awaitEvents();
    }

    private Point2D getMouseDifferenceTo(double x, double y) {
        Point2D location = getMouseLocation();
        return new Point2D(x - location.getX(), y - location.getY());
    }

    private double getMouseDistance(Point2D difference) {
        return Math.sqrt(Math.pow(difference.getX(), 2) + Math.pow(difference.getY(), 2));
    }

}
