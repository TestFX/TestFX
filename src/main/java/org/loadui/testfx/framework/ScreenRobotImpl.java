/*
 * Copyright 2013 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the Licence for the specific language governing permissions and limitations
 * under the Licence.
 */
package org.loadui.testfx.framework;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.loadui.testfx.utils.FXTestUtils;

public class ScreenRobotImpl implements ScreenRobot {
    private static final Map<MouseButton, Integer> BUTTONS = ImmutableMap.of(MouseButton.PRIMARY,
        InputEvent.BUTTON1_MASK, MouseButton.MIDDLE, InputEvent.BUTTON2_MASK, MouseButton.SECONDARY,
        InputEvent.BUTTON3_MASK);

    private final Robot robot;
    private long moveTime = 175;

    public ScreenRobotImpl() {
        try {
            robot = new Robot();
        }
        catch (AWTException e) {
            throw new IllegalArgumentException(e);
        }

    }

    @Override
    public Point2D getMouse() {
        Point awtPoint = MouseInfo.getPointerInfo().getLocation();
        return new Point2D(awtPoint.getX(), awtPoint.getY());
    }

    @Override
    public void position(double x, double y) {
        robot.mouseMove((int) x, (int) y);
    }

    @Override
    public void move(double x, double y) {
        // Calculate how far we need to go
        Point position = MouseInfo.getPointerInfo().getLocation();
        double distanceX = x - position.getX();
        double distanceY = y - position.getY();
        double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));

        // The maximum time for the movement is "moveTime". Far movements will make the cursor go faster.
        // In order to be not too slow on small distances, the minimum speed is 1 pixel per millisecond.
        double totalTime = moveTime;
        if (distance < totalTime) {
            totalTime = Math.max(1, distance);
        }

        double speedX = distanceX / totalTime;
        double speedY = distanceY / totalTime;
        for (int time = 0; time < totalTime; time++) {

            robot.mouseMove(position.x + (int) (speedX * time), position.y + (int) (speedY * time));

            try {
                Thread.sleep(1);
            }
            catch (InterruptedException e) {
                return;
            }

        }

        // We should be less than one step away from the target
        // => Make one last step to hit it.
        robot.mouseMove((int) x, (int) y);
        FXTestUtils.awaitEvents();
    }

    @Override
    public void press(MouseButton button) {
        if (button == null) {
            return;
        }
        robot.mousePress(BUTTONS.get(button));
        FXTestUtils.awaitEvents();
    }

    @Override
    public void release(MouseButton button) {
        if (button == null) {
            return;
        }
        robot.mouseRelease(BUTTONS.get(button));
        FXTestUtils.awaitEvents();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void press(KeyCode key) {
        robot.keyPress(key.impl_getCode());
        FXTestUtils.awaitEvents();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void release(KeyCode key) {
        robot.keyRelease(key.impl_getCode());
        FXTestUtils.awaitEvents();
    }

    @Override
    public void scroll(int amount) {
        robot.mouseWheel(amount);
        FXTestUtils.awaitEvents();
    }
}
