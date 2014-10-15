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
import org.loadui.testfx.service.adapter.AwtRobotAdapter;
import org.loadui.testfx.utils.FXTestUtils;

public class ScreenRobotImpl implements ScreenRobot {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private final AwtRobotAdapter robotAdapter;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public ScreenRobotImpl() {
        robotAdapter = new AwtRobotAdapter();
        robotAdapter.robotCreate();
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public Image captureRegion(Rectangle2D region) {
        return robotAdapter.getCaptureRegion(region);
    }

    @Override
    public Point2D retrieveMouse() {
        return robotAdapter.getMouseLocation();
    }

    @Override
    public void moveMouse(Point2D point) {
        robotAdapter.mouseMove(point);
    }

    @Override
    public void pressMouse(MouseButton button) {
        robotAdapter.mousePress(button);
        awaitEvents();
    }

    @Override
    public void releaseMouse(MouseButton button) {
        robotAdapter.mouseRelease(button);
        awaitEvents();
    }

    @Override
    public void scrollMouse(int amount) {
        robotAdapter.mouseWheel(amount);
        awaitEvents();
    }

    @Override
    public void pressKey(KeyCode key) {
        robotAdapter.keyPress(key);
        awaitEvents();
    }

    @Override
    public void releaseKey(KeyCode key) {
        robotAdapter.keyRelease(key);
        awaitEvents();
    }

    @Override
    public void awaitEvents() {
        FXTestUtils.awaitEvents();
    }

}
