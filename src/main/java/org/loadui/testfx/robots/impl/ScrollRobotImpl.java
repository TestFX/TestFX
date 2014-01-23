package org.loadui.testfx.robots.impl;

import javafx.geometry.VerticalDirection;
import org.loadui.testfx.framework.ScreenRobot;
import org.loadui.testfx.robots.ScrollRobot;

public class ScrollRobotImpl implements ScrollRobot {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private ScreenRobot screenRobot;

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
            scrollUp(amount);
        }
        else {
            scrollDown(Math.abs(amount));
        }
    }

    @Override
    public void scroll(int positiveAmount, VerticalDirection direction) {
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
            screenRobot.scrollMouse(1);
        }
    }

    @Override
    public void scrollDown(int positiveAmount) {
        for (int scrollTick = 0; scrollTick < positiveAmount; scrollTick++) {
            screenRobot.scrollMouse(-1);
        }
    }

}
