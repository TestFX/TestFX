package org.loadui.testfx.robots;

import java.util.concurrent.TimeUnit;

public interface SleepRobot {

    /**
     * Same as Thread.sleep(), but without checked exceptions.
     *
     * @param milliseconds the duration in milliseconds
     */
    public void sleep(long milliseconds);

    /**
     * Same as Thread.sleep(), but without checked exceptions.
     *
     * @param duration the duration
     * @param timeUnit the unit of time
     */
    public void sleep(long duration, TimeUnit timeUnit);

}
