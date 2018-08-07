package org.testfx.internal;

import java.util.Locale;

/**
 * Provides an API for platform specific features
 * 
 */
public class PlatformAdapter {

    /**
     * Stores the operating system we are running on. Shouldn't change during
     * execution, so singleton.
     */
    private static OS os;

    /**
     * Gets the operting system
     * 
     * @return the operating system
     */
    public static OS getOs() {
        if (os == null) {
            if (System.getProperty("os.name").toLowerCase(Locale.US).contains("nux")) {
                os = OS.unix;
            } else if (System.getProperty("os.name").toLowerCase(Locale.US).startsWith("win")) {
                os = OS.windows;
            } else {
                os = OS.mac;
            }
        }
        return os;
    }

    public static enum OS {
        windows, unix, mac
    }

}
