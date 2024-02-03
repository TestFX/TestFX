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

import java.lang.reflect.InvocationTargetException;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import org.testfx.internal.JavaVersionAdapter;
import org.testfx.internal.PlatformAdapter;

import static org.testfx.util.WaitForAsyncUtils.asyncFx;
import static org.testfx.util.WaitForAsyncUtils.waitForAsyncFx;

/**
 * {@link org.testfx.service.adapter.RobotAdapter} implementation that uses the public {@link javafx.scene.robot.Robot}
 * API added in JavaFX 11.
 * <p>
 * All methods are called reflectively so that this class can compile on any JavaFX version.
 */
class PublicGlassRobotAdapter extends GlassRobotAdapter {

    @Override
    public void robotCreate() {
        try {
            glassRobot = Class.forName("javafx.scene.robot.Robot").getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void robotDestroy() {
        // NO-OP, destroy() was removed from public robot API pending https://bugs.openjdk.java.net/browse/JDK-8207373.
    }

    @Override
    public void keyPress(KeyCode key) {
        asyncFx(() -> getRobot().getClass().getMethod("keyPress", KeyCode.class).invoke(getRobot(), key));
    }

    @Override
    public void keyRelease(KeyCode key) {
        asyncFx(() -> getRobot().getClass().getMethod("keyRelease", KeyCode.class).invoke(getRobot(), key));
    }

    @Override
    public Point2D getMouseLocation() {
        // Note the current JavaFX version (10.0.2) behavior below is quite inconsistent (no scaling on
        // set, but scaling on read) - this might change in the future.
        // Please keep backwards compatibility to the latest version with this behavior in this case.
        if (PlatformAdapter.getOs() == PlatformAdapter.OS.UNIX &&
                !JavaVersionAdapter.currentVersion().isJava11Compatible()) {
            return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS,
                () -> new Point2D(((Double) getRobot().getClass().getMethod("getMouseX").invoke(
                        getRobot())).intValue() / JavaVersionAdapter.getScreenScaleX(),
                        ((Double) getRobot().getClass().getMethod("getMouseY").invoke(
                                getRobot())).intValue() / JavaVersionAdapter.getScreenScaleY()));
        }
        else {
            return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS, () -> new Point2D(
                    ((Double) getRobot().getClass().getMethod("getMouseX").invoke(glassRobot)).intValue(),
                    ((Double) getRobot().getClass().getMethod("getMouseY").invoke(glassRobot)).intValue()));
        }
    }

    @Override
    public void mouseMove(Point2D location) {
        final Rectangle2D scaled = new Rectangle2D(location.getX(), location.getY(), 0, 0);
        asyncFx(() -> getRobot().getClass().getMethod("mouseMove", double.class, double.class)
                .invoke(getRobot(), (int) scaled.getMinX(), (int) scaled.getMinY()));
    }

    @Override
    public void mousePress(MouseButton button) {
        asyncFx(() -> getRobot().getClass().getMethod("mousePress", MouseButton[].class).invoke(getRobot(),
                new Object[] {new MouseButton[] {button}}));
    }

    @Override
    public void mouseRelease(MouseButton button) {
        asyncFx(() -> getRobot().getClass().getMethod("mouseRelease", MouseButton[].class)
                .invoke(getRobot(), new Object[] {new MouseButton[] {button}}));
    }

    @Override
    public Color getCapturePixelColor(Point2D location) {
        final Rectangle2D scaled = new Rectangle2D(location.getX(), location.getY(), 0, 0);
        return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS,
            () -> (Color) getRobot().getClass().getMethod("getPixelColor", double.class, double.class)
                    .invoke(getRobot(), scaled.getMinX(), scaled.getMinY()));
    }

    @Override
    protected Image getScreenCapture(Rectangle2D region, boolean raw) {
        return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS, () -> (WritableImage)
                getRobot().getClass().getMethod("getScreenCapture", WritableImage.class, double.class, double.class,
                        double.class, double.class, boolean.class).invoke(glassRobot, null,
                        region.getMinX(), region.getMinY(), region.getWidth(), region.getHeight(), !raw));
    }
}
