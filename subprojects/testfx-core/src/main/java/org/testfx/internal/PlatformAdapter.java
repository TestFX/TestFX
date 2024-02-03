/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2024 The TestFX Contributors
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
package org.testfx.internal;

import java.util.Locale;

/**
 * Provides an API for platform specific features.
 */
public class PlatformAdapter {

    /**
     * Stores the operating system we are running on. Shouldn't change during
     * execution, so singleton.
     */
    private static OS os;

    /**
     * Gets the operating system.
     *
     * @return the operating system
     */
    public static OS getOs() {
        if (os == null) {
            if (System.getProperty("os.name").toLowerCase(Locale.US).contains("nux")) {
                os = OS.UNIX;
            } else if (System.getProperty("os.name").toLowerCase(Locale.US).startsWith("win")) {
                os = OS.WINDOWS;
            } else {
                os = OS.MAC;
            }
        }
        return os;
    }

    public enum OS {
        WINDOWS, UNIX, MAC
    }

}
