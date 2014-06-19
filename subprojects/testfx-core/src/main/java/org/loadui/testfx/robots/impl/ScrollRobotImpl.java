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

import javafx.geometry.VerticalDirection;
import org.loadui.testfx.robots.ScreenRobot;
import org.loadui.testfx.robots.ScrollRobot;

public class ScrollRobotImpl implements ScrollRobot {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final int SCROLL_ONE_UP = -1;
    private static final int SCROLL_ONE_DOWN = 1;

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public ScreenRobot screenRobot;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public ScrollRobotImpl(ScreenRobot screenRobot) {
        this.screenRobot = screenRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void scroll(int amount) {
        if (amount >= 0) {
            scrollDown(amount);
        }
        else {
            scrollUp(Math.abs(amount));
        }
    }

    @Override
    public void scroll(int positiveAmount,
                       VerticalDirection direction) {
        if (direction == VerticalDirection.UP) {
            scrollUp(positiveAmount);
        }
        else if (direction == VerticalDirection.DOWN) {
            scrollDown(positiveAmount);
        }
    }

    @Override
    public void scrollUp(int positiveAmount) {
        for (int scrollTick = 0; scrollTick < positiveAmount; scrollTick++) {
            screenRobot.scrollMouse(SCROLL_ONE_UP);
        }
    }

    @Override
    public void scrollDown(int positiveAmount) {
        for (int scrollTick = 0; scrollTick < positiveAmount; scrollTick++) {
            screenRobot.scrollMouse(SCROLL_ONE_DOWN);
        }
    }

}
