package org.testfx.api;

import org.testfx.robot.impl.ClickRobotImpl;
import org.testfx.robot.impl.KeyboardRobotImpl;
import org.testfx.robot.impl.MouseRobotImpl;
import org.testfx.robot.impl.MoveRobotImpl;
import org.testfx.robot.impl.TypeRobotImpl;
import org.testfx.robot.impl.WriteRobotImpl;
import org.testfx.util.WaitForAsyncUtils;

/**
 * This class provides access to the various timing settings in the TestFx internals. 
 * It provides switching between predefined sets of parameters, that can be used in 
 * different scenarios.<br>
 * All methods should be called before a test is started (e.g. the static initializer
 * or at the beginning of test case).<br>
 * The timing may also be set using the system property <code>testfx.timing</code>.
 * Valid values are: <code>default</code>, <code>aggressive</code> and <code>debug</code>
 */
public class FxTiming {
    
    private static TimingMode mode = TimingMode.DEFAULT;
    
    static {
        String timing = System.getProperty("testfx.timing");
        if (timing == null || timing.toLowerCase().equals("default")) {
            setDefaultTiming();
        } else if (timing.toLowerCase().equals("aggressive")){
            setAggressiveTiming();
        } else if (timing.toLowerCase().equals("debug")){
            setDebugTiming();
        } else {
            System.err.println("Unknown value of testfx.timing: " + timing + ". Using default timing");
            setDefaultTiming();
        }
    }
    
    
    /**
     * Initializes the timing. This method is called internally in the TestFx framework.
     */
    public static void init() {
        // just ensures class is loaded...
    }

    /**
     * Sets all timing relevant values to the defined default values. The Default values are tested
     * by the TestFx Team on several build servers with different operating systems, Java versions
     * and rendering piplines (software, hardware, headless...). They should be valid for most scenarios.
     */
    public static void setDefaultTiming() {
        mode = TimingMode.DEFAULT;
        WaitForAsyncUtils.setDefaultTiming();
        ClickRobotImpl.setDefaultTiming();
        MoveRobotImpl.setDefaultTiming();
        TypeRobotImpl.setDefaultTiming();
        WriteRobotImpl.setDefaultTiming();
        KeyboardRobotImpl.setDefaultTiming();
        MouseRobotImpl.setDefaultTiming();
    }
    /**
     * Sets all timing relevant values to be very fast. Timing may not be guaranteed in all cases,
     * violations may occur. This setup shouldn't generally be used. It is mainly used for testing. 
     * Do not expect support when using this mode nor post issues when using this mode.
     */
    public static void setAggressiveTiming() {
        mode = TimingMode.AGGRESSIVE;
        WaitForAsyncUtils.setAggressiveTiming();
        ClickRobotImpl.setAggressiveTiming();
        MoveRobotImpl.setAggressiveTiming();
        TypeRobotImpl.setAggressiveTiming();
        WriteRobotImpl.setAggressiveTiming();
        KeyboardRobotImpl.setAggressiveTiming();
        MouseRobotImpl.setAggressiveTiming();
    }
    /**
     * Sets all timing relevant values to a value, that allows the user to follow the test
     * on screen for debugging. This option may also be used to identify timing issues in 
     * a test
     */
    public static void setDebugTiming() {
        mode = TimingMode.DEBUG;
        WaitForAsyncUtils.setDebugTiming();
        ClickRobotImpl.setDebugTiming();
        MoveRobotImpl.setDebugTiming();
        TypeRobotImpl.setDebugTiming();
        WriteRobotImpl.setDebugTiming();
        KeyboardRobotImpl.setDebugTiming();
        MouseRobotImpl.setDebugTiming();
    }
    
    /**
     * Resets all timing constraints to the selected mode. 
     */
    public static void reset() {
        if (mode == TimingMode.AGGRESSIVE) {
            setAggressiveTiming();
        } else if (mode == TimingMode.DEBUG) {
            setDebugTiming();
        } else {
            setDefaultTiming();
        }
    }
    
    private enum TimingMode {
        DEFAULT,
        AGGRESSIVE,
        DEBUG
    }
    
    
}
