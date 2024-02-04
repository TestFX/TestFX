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
package org.testfx.service.adapter.impl;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

import org.testfx.service.adapter.RobotAdapter;

import static org.testfx.util.WaitForAsyncUtils.asyncFx;

public abstract class GlassRobotAdapter implements RobotAdapter {

    protected static final int RETRIEVAL_TIMEOUT_IN_MILLIS = 10000;
    protected Object glassRobot;
    private static boolean publicRobot;

    static {
        try {
            // Whether or not we should use the public robot is not as simple as checking
            // the JDK version because, for example, OpenJDK 10 does not ship with JavaFX
            // and since we include OpenJFX as a dependency on 10+ only the public robot
            // API will be available. So we simply check to see if the old class exists
            // assuming that if it doesn't the new one must (if it doesn't, we can't create
            // a Glass robot.
            Class.forName("com.sun.glass.ui.Robot");
            publicRobot = false;
        }
        catch (ClassNotFoundException e) {
            try {
                Class.forName("javafx.scene.robot.Robot");
                publicRobot = true;
            }
            catch (ClassNotFoundException e1) {
                throw new RuntimeException("neither \"com.sun.glass.ui.Robot\" nor \"javafx.scene.robot.Robot\" " +
                        "could be found - please report this issue on https://github.com/TestFX/TestFX/issues with " +
                        "JDK version information (\"java -version\").");
            }
        }
    }

    public static GlassRobotAdapter createGlassRobot() {
        if (publicRobot) {
            return new PublicGlassRobotAdapter();
        }
        else {
            return new PrivateGlassRobotAdapter();
        }
    }

    @Override
    public final void mouseWheel(int wheelAmount) {
        asyncFx(() -> getRobot().getClass().getMethod("mouseWheel", int.class).invoke(getRobot(), wheelAmount));
    }

    @Override
    public final Image getCaptureRegion(Rectangle2D region) {
        return getScreenCapture(region, false);
    }

    public final Image getCaptureRegionRaw(Rectangle2D region) {
        return getScreenCapture(region, true);
    }

    protected final Object getRobot() {
        if (glassRobot == null) {
            robotCreate();
        }
        return glassRobot;
    }

    protected abstract Image getScreenCapture(Rectangle2D region, boolean raw);

}
