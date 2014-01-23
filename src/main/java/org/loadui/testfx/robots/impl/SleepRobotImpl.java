package org.loadui.testfx.robots.impl;

import java.util.concurrent.TimeUnit;
import org.loadui.testfx.robots.SleepRobot;

public class SleepRobotImpl implements SleepRobot {

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void sleep(long duration, TimeUnit timeUnit) {
        sleep(timeUnit.toMillis(duration));
    }

}
