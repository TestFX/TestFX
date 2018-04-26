/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2018 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.robot.impl;

import java.util.Objects;

import javafx.geometry.HorizontalDirection;
import javafx.geometry.VerticalDirection;

import org.testfx.robot.MouseRobot;
import org.testfx.robot.ScrollRobot;

public class ScrollRobotImpl implements ScrollRobot {

    private static final int SCROLL_ONE_UP_OR_LEFT = -1;
    private static final int SCROLL_ONE_DOWN_OR_RIGHT = 1;

    private final MouseRobot mouseRobot;

    public ScrollRobotImpl(MouseRobot mouseRobot) {
        Objects.requireNonNull(mouseRobot, "mouseRobot must not be null");
        this.mouseRobot = mouseRobot;
    }

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
    public void scroll(int positiveAmount, VerticalDirection direction) {
        switch (direction) {
            case UP:
                scrollUp(positiveAmount);
                break;
            case DOWN:
                scrollDown(positiveAmount);
                break;
            default:
                throw new IllegalArgumentException("unknown vertical direction: " + direction);
        }
    }

    @Override
    public void scrollUp(int positiveAmount) {
        for (int scrollTick = 0; scrollTick < positiveAmount; scrollTick++) {
            mouseRobot.scroll(SCROLL_ONE_UP_OR_LEFT);
        }
    }

    @Override
    public void scrollDown(int positiveAmount) {
        for (int scrollTick = 0; scrollTick < positiveAmount; scrollTick++) {
            mouseRobot.scroll(SCROLL_ONE_DOWN_OR_RIGHT);
        }
    }

    @Override
    public void scroll(int positiveAmount, HorizontalDirection direction) {
        switch (direction) {
            case RIGHT:
                scrollRight(positiveAmount);
                break;
            case LEFT:
                scrollLeft(positiveAmount);
                break;
            default:
                throw new IllegalArgumentException("unknown horizontal direction: " + direction);
        }
    }

    @Override
    public void scrollRight(int positiveAmount) {
        for (int scrollTick = 0; scrollTick < positiveAmount; scrollTick++) {
            mouseRobot.scroll(SCROLL_ONE_DOWN_OR_RIGHT);
        }
    }

    @Override
    public void scrollLeft(int positiveAmount) {
        for (int scrollTick = 0; scrollTick < positiveAmount; scrollTick++) {
            mouseRobot.scroll(SCROLL_ONE_UP_OR_LEFT);
        }
    }
}
