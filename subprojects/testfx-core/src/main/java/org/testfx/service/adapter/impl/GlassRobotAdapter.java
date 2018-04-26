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
package org.testfx.service.adapter.impl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Robot;
import org.testfx.internal.JavaVersionAdapter;
import org.testfx.service.adapter.RobotAdapter;

import static org.testfx.internal.JavaVersionAdapter.convertToKeyCodeId;
import static org.testfx.util.WaitForAsyncUtils.asyncFx;
import static org.testfx.util.WaitForAsyncUtils.waitForAsyncFx;

public class GlassRobotAdapter implements RobotAdapter<Robot> {

    private static final int RETRIEVAL_TIMEOUT_IN_MILLIS = 10000;
    private static final int BYTE_BUFFER_BYTES_PER_COMPONENT = 1;
    private static final int INT_BUFFER_BYTES_PER_COMPONENT = 4;

    private Robot glassRobot;

    @Override
    public void robotCreate() {
        waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS,
            () -> glassRobot = Application.GetApplication().createRobot());
    }

    @Override
    public void robotDestroy() {
        if (glassRobot != null) {
            waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS, () -> {
                glassRobot.destroy();
                glassRobot = null;
            });
        }
    }

    @Override
    public Robot getRobotInstance() {
        return glassRobot;
    }

    @Override
    public void keyPress(KeyCode key) {
        asyncFx(() -> useRobot().keyPress(convertToKeyCodeId(key)));
    }

    @Override
    public void keyRelease(KeyCode key) {
        asyncFx(() -> useRobot().keyRelease(convertToKeyCodeId(key)));
    }

    @Override
    public Point2D getMouseLocation() {
        return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS,
            () -> new Point2D(useRobot().getMouseX(), useRobot().getMouseY()));
    }

    @Override
    public void mouseMove(Point2D location) {
        asyncFx(() -> useRobot().mouseMove((int) (location.getX() / JavaVersionAdapter.getScreenScaleX()),
                (int) (location.getY() / JavaVersionAdapter.getScreenScaleY())));
    }

    @Override
    public void mousePress(MouseButton button) {
        asyncFx(() -> useRobot().mousePress(convertToButtonId(button)));
    }

    @Override
    public void mouseRelease(MouseButton button) {
        asyncFx(() -> useRobot().mouseRelease(convertToButtonId(button)));
    }

    @Override
    public void mouseWheel(int wheelAmount) {
        asyncFx(() -> useRobot().mouseWheel(wheelAmount));
    }

    @Override
    public Color getCapturePixelColor(Point2D location) {
        return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS, () -> {
            int glassColor = useRobot().getPixelColor(
                    (int) (location.getX() / JavaVersionAdapter.getScreenScaleX()),
                    (int) (location.getY() / JavaVersionAdapter.getScreenScaleY()));
            return convertFromGlassColor(glassColor);
        });
    }

    @Override
    public Image getCaptureRegion(Rectangle2D region) {
        return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS, () -> {
            Pixels glassPixels = useRobot().getScreenCapture(
                    (int) region.getMinX(), (int) region.getMinY(),
                    (int) region.getWidth(), (int) region.getHeight(),
                    Math.abs(JavaVersionAdapter.getScreenScaleX() - 1) <= 0.001 ||
                            Math.abs(JavaVersionAdapter.getScreenScaleY() - 1) <= 0.001);
            return convertFromGlassPixels(glassPixels);
        });
    }

    private Robot useRobot() {
        if (glassRobot == null) {
            robotCreate();
        }
        return glassRobot;
    }

    private int convertToButtonId(MouseButton button) {
        switch (button) {
            case PRIMARY: return Robot.MOUSE_LEFT_BTN;
            case SECONDARY: return Robot.MOUSE_RIGHT_BTN;
            case MIDDLE: return Robot.MOUSE_MIDDLE_BTN;
            default: throw new IllegalArgumentException("MouseButton: " + button + " not supported by GlassRobot");
        }
    }

    private Color convertFromGlassColor(int color) {
        int alpha = (color >> 24) & 0xFF;
        int red   = (color >> 16) & 0xFF;
        int green = (color >>  8) & 0xFF;
        int blue  =  color        & 0xFF;
        return new Color(red / 255d, green / 255d, blue / 255d, alpha / 255d);
    }

    private Image convertFromGlassPixels(Pixels glassPixels) {
        int width = glassPixels.getWidth();
        int height = glassPixels.getHeight();
        WritableImage image = new WritableImage(width, height);

        int bytesPerComponent = glassPixels.getBytesPerComponent();
        if (bytesPerComponent == INT_BUFFER_BYTES_PER_COMPONENT) {
            IntBuffer intBuffer = (IntBuffer) glassPixels.getPixels();
            writeIntBufferToImage(intBuffer, image);
        }
        else if (bytesPerComponent == BYTE_BUFFER_BYTES_PER_COMPONENT) {
            ByteBuffer byteBuffer = (ByteBuffer) glassPixels.getPixels();
            writeByteBufferToImage(byteBuffer, image);
        }

        return image;
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

    private void writeByteBufferToImage(ByteBuffer byteBuffer, WritableImage image) {
        // Note: Would love to know if screen captures are ever written this way by any
        // Glass robot implementation in OpenJFX.
        throw new UnsupportedOperationException("writing from byte buffer is not supported");
    }

}
