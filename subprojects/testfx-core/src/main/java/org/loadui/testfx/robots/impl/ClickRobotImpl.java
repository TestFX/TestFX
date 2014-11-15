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

import javafx.scene.input.MouseButton;

import org.loadui.testfx.robots.ClickRobot;
import org.loadui.testfx.robots.MouseRobot;
import org.loadui.testfx.robots.MoveRobot;
import org.loadui.testfx.robots.SleepRobot;
import org.loadui.testfx.service.query.PointQuery;

public class ClickRobotImpl implements ClickRobot {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final long SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS = 50;

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public MouseRobot mouseRobot;
    public MoveRobot moveRobot;
    public SleepRobot sleepRobot;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public ClickRobotImpl(MouseRobot mouseRobot,
                          MoveRobot moveRobot,
                          SleepRobot sleepRobot) {
        this.mouseRobot = mouseRobot;
        this.moveRobot = moveRobot;
        this.sleepRobot = sleepRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void clickOn(MouseButton... buttons) {
        mouseRobot.pressNoWait(buttons);
        mouseRobot.release(buttons);
    }

    @Override
    public void clickOn(PointQuery pointQuery,
                        MouseButton... buttons) {
        moveRobot.moveTo(pointQuery);
        clickOn(buttons);
    }

    @Override
    public void doubleClickOn(MouseButton... buttons) {
        clickOn(buttons);
        clickOn(buttons);
        sleepRobot.sleep(SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS);
    }

    @Override
    public void doubleClickOn(PointQuery pointQuery,
                              MouseButton... buttons) {
        clickOn(pointQuery, buttons);
        clickOn(buttons);
        sleepRobot.sleep(SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS);
    }

}
