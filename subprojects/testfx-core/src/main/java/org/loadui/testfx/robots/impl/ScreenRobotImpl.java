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

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import org.loadui.testfx.robots.ScreenRobot;
import org.loadui.testfx.service.screen.ScreenRobotAwt;
import org.loadui.testfx.utils.FXTestUtils;

public class ScreenRobotImpl implements ScreenRobot {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final ScreenRobotAwt robotImpl;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public ScreenRobotImpl() {
        robotImpl = new ScreenRobotAwt();
        robotImpl.robotCreate();
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public Image captureRegion(Rectangle2D region) {
        return robotImpl.getCaptureRegion(region);
    }

    @Override
    public Point2D retrieveMouse() {
        return robotImpl.getMouseLocation();
    }

    @Override
    public void moveMouse(Point2D point) {
        robotImpl.mouseMove(point);
    }

    @Override
    public void pressMouse(MouseButton button) {
        robotImpl.mousePress(button);
        awaitEvents();
    }

    @Override
    public void releaseMouse(MouseButton button) {
        robotImpl.mouseRelease(button);
        awaitEvents();
    }

    @Override
    public void scrollMouse(int amount) {
        robotImpl.mouseWheel(amount);
        awaitEvents();
    }

    @Override
    public void pressKey(KeyCode key) {
        robotImpl.keyPress(key);
        awaitEvents();
    }

    @Override
    public void releaseKey(KeyCode key) {
        robotImpl.keyRelease(key);
        awaitEvents();
    }

    @Override
    public void awaitEvents() {
        FXTestUtils.awaitEvents();
    }

}
