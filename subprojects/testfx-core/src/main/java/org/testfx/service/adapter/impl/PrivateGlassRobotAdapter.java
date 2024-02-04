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
import java.lang.reflect.Method;
import java.nio.IntBuffer;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import org.testfx.internal.JavaVersionAdapter;
import org.testfx.internal.PlatformAdapter;

import static org.testfx.internal.JavaVersionAdapter.convertToKeyCodeId;
import static org.testfx.util.WaitForAsyncUtils.asyncFx;
import static org.testfx.util.WaitForAsyncUtils.waitForAsyncFx;

/**
 * {@link org.testfx.service.adapter.RobotAdapter} implementation that uses the private
 * {@code com.sun.glass.ui.GlassRobot} implementation to support JavaFX versions before 11.
 * <p>
 * All methods are called reflectively so that this class can compile on any JavaFX version.
 */
class PrivateGlassRobotAdapter extends GlassRobotAdapter {

    @Override
    public void robotCreate() {
        try {
            Object application = Class.forName("com.sun.glass.ui.Application")
                    .getMethod("GetApplication").invoke(null);
            Method createRobotMethod = application.getClass().getDeclaredMethod("createRobot");
            createRobotMethod.setAccessible(true);
            glassRobot = createRobotMethod.invoke(application);
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void robotDestroy() {
        if (glassRobot != null) {
            waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS, () -> {
                try {
                    getRobot().getClass().getMethod("destroy").invoke(glassRobot);
                }
                catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                glassRobot = null;
            });
        }
    }

    @Override
    public void keyPress(KeyCode key) {
        asyncFx(() -> getRobot().getClass().getMethod("keyPress", int.class)
                .invoke(getRobot(), convertToKeyCodeId(key)));
    }

    @Override
    public void keyRelease(KeyCode key) {
        asyncFx(() -> getRobot().getClass().getMethod("keyRelease", int.class)
                .invoke(getRobot(), convertToKeyCodeId(key)));
    }

    @Override
    public Point2D getMouseLocation() {
        // Note the current JavaFX version (10.0.2) behavior below is quite inconsistent (no scaling on
        // set, but scaling on read) - this might change in the future.
        // Please keep backwards compatibility to the latest version with this behavior in this case.
        if (PlatformAdapter.getOs() == PlatformAdapter.OS.UNIX &&
                JavaVersionAdapter.currentVersion().isJava9Compatible()) {
            return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS,
                () -> new Point2D((int) getRobot().getClass().getMethod("getMouseX").invoke(
                        getRobot()) / JavaVersionAdapter.getScreenScaleX(),
                        (int) getRobot().getClass().getMethod("getMouseY").invoke(
                                getRobot()) / JavaVersionAdapter.getScreenScaleY()));
        }
        else {
            return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS,
                () -> new Point2D((int) getRobot().getClass().getMethod("getMouseX").invoke(glassRobot),
                        (int) getRobot().getClass().getMethod("getMouseY").invoke(glassRobot)));
        }
    }

    @Override
    public void mouseMove(Point2D location) {
        final Rectangle2D scaled = new Rectangle2D(location.getX(), location.getY(), 0, 0);
        asyncFx(() -> getRobot().getClass().getMethod("mouseMove", int.class, int.class)
                .invoke(getRobot(), (int) scaled.getMinX(), (int) scaled.getMinY()));
    }

    @Override
    public void mousePress(MouseButton button) {
        asyncFx(() -> getRobot().getClass().getMethod("mousePress", int.class)
                .invoke(getRobot(), convertToButtonId(button)));
    }

    @Override
    public void mouseRelease(MouseButton button) {
        asyncFx(() -> getRobot().getClass().getMethod("mouseRelease", int.class)
                .invoke(getRobot(), convertToButtonId(button)));
    }

    @Override
    public Color getCapturePixelColor(Point2D location) {
        final Rectangle2D scaled = new Rectangle2D(location.getX(), location.getY(), 0, 0);
        return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS, () -> {
            int glassColor = (int) getRobot().getClass().getMethod("getPixelColor", int.class, int.class)
                    .invoke(getRobot(), (int) scaled.getMinX(), (int) scaled.getMinY());
            return convertFromGlassColor(glassColor);
        });
    }

    @Override
    protected Image getScreenCapture(Rectangle2D region, boolean raw) {
        return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS, () -> convertFromGlassPixels(
                getRobot().getClass().getMethod("getScreenCapture", int.class, int.class, int.class,
                        int.class, boolean.class).invoke(glassRobot,
                        (int) region.getMinX(), (int) region.getMinY(),
                        (int) region.getWidth(), (int) region.getHeight(), raw)));
    }

    private int convertToButtonId(MouseButton button) {
        try {
            switch (button) {
                case PRIMARY:
                    return getRobot().getClass().getField("MOUSE_LEFT_BTN").getInt(null);
                case SECONDARY:
                    return getRobot().getClass().getField("MOUSE_RIGHT_BTN").getInt(null);
                case MIDDLE:
                    return getRobot().getClass().getField("MOUSE_MIDDLE_BTN").getInt(null);
                default:
                    throw new IllegalArgumentException("MouseButton: " + button + " not supported by GlassRobot");
            }
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("MouseButton: " + button + " not supported by GlassRobot");
        }
    }

    private Color convertFromGlassColor(int color) {
        int alpha = (color >> 24) & 0xFF;
        int red   = (color >> 16) & 0xFF;
        int green = (color >>  8) & 0xFF;
        int blue  =  color        & 0xFF;
        return new Color(red / 255d, green / 255d, blue / 255d, alpha / 255d);
    }

    private Image convertFromGlassPixels(Object glassPixels) {
        try {
            int width = (int) glassPixels.getClass().getMethod("getWidth").invoke(glassPixels);
            int height = (int) glassPixels.getClass().getMethod("getHeight").invoke(glassPixels);
            WritableImage image = new WritableImage(width, height);
            IntBuffer intBuffer = (IntBuffer) glassPixels.getClass().getMethod("getPixels").invoke(glassPixels);
            writeIntBufferToImage(intBuffer, image);
            return image;
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeIntBufferToImage(IntBuffer intBuffer, WritableImage image) {
        PixelWriter pixelWriter = image.getPixelWriter();
        double width = image.getWidth();
        double height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = intBuffer.get();
                pixelWriter.setArgb(x, y, argb);
            }
        }
    }
}
