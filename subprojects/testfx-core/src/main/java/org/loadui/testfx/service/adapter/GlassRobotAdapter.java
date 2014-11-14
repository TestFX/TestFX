/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.loadui.testfx.service.adapter;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import com.google.common.collect.ImmutableMap;
import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Robot;

import static org.loadui.testfx.utils.WaitForAsyncUtils.asyncFx;
import static org.loadui.testfx.utils.WaitForAsyncUtils.waitForAsyncFx;

// PROBLEMS:
// - we need to wait until Application.GetApplication() is available.
// - we need to call robot methods within JavaFX thread and wait for results.
// - retrieving the mouse location seems unreliable.
// - retrieving screenshots in headless mode returns transparent images.

public class GlassRobotAdapter {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final int BYTE_BUFFER_BYTES_PER_COMPONENT = 1;
    private static final int INT_BUFFER_BYTES_PER_COMPONENT = 4;

    private static final Map<MouseButton, Integer> GLASS_BUTTONS = ImmutableMap.of(
        MouseButton.PRIMARY, Robot.MOUSE_LEFT_BTN,
        MouseButton.SECONDARY, Robot.MOUSE_RIGHT_BTN,
        MouseButton.MIDDLE, Robot.MOUSE_MIDDLE_BTN
    );

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private Robot glassRobot;

    private Point2D mouseLocation = Point2D.ZERO;

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    // ROBOT.

    public void robotCreate() {
        waitForAsyncFx(10000, () -> {
            glassRobot = createGlassRobot();
        });
    }

    public void robotDestroy() {
        waitForAsyncFx(10000, () -> {
            glassRobot.destroy();
        });
    }

    public Robot getRobotInstance() {
        if (glassRobot == null) {
            robotCreate();
        }
        return glassRobot;
    }

    // KEY.

    public void keyPress(KeyCode key) {
        asyncFx(() -> {
            getRobotInstance().keyPress(convertToKeyCodeId(key));
        });
    }

    public void keyRelease(KeyCode key) {
        asyncFx(() -> {
            getRobotInstance().keyRelease(convertToKeyCodeId(key));
        });
    }

    // MOUSE.

    public Point2D getMouseLocation() {
        //return mouseLocation;
        return waitForAsyncFx(1000, () -> {
            Robot robotInstance = getRobotInstance();
            return convertFromCoordinates(robotInstance.getMouseX(), robotInstance.getMouseY());
        });

    }

    public void mouseMove(Point2D location) {
        mouseLocation = location;
        asyncFx(() -> {
            getRobotInstance().mouseMove((int) location.getX(), (int) location.getY());
        });
    }

    public void mousePress(MouseButton button) {
        asyncFx(() -> {
            getRobotInstance().mousePress(convertToButtonId(button));
        });
    }

    public void mouseRelease(MouseButton button) {
        asyncFx(() -> {
            getRobotInstance().mouseRelease(convertToButtonId(button));
        });
    }

    public void mouseWheel(int wheelAmount) {
        asyncFx(() -> {
            getRobotInstance().mouseWheel(wheelAmount);
        });
    }

    // CAPTURE.

    public Color getCapturePixelColor(Point2D location) {
        int glassColor = getRobotInstance().getPixelColor(
            (int) location.getX(), (int) location.getY()
        );
        return convertFromGlassColor(glassColor);
    }

    public Image getCaptureRegion(Rectangle2D region) {
        Pixels glassPixels = getRobotInstance().getScreenCapture(
            (int) region.getMinX(), (int) region.getMinY(),
            (int) region.getWidth(), (int) region.getHeight()
        );
        return convertFromGlassPixels(glassPixels);
    }

    // TIMER.

    /**
     * Block until events in the queue are processed.
     */
    public void timerWaitForIdle() {
        throw new UnsupportedOperationException();
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Robot createGlassRobot() {
        return Application.GetApplication().createRobot();
    }

    @SuppressWarnings("deprecation")
    private int convertToKeyCodeId(KeyCode keyCode) {
        return keyCode.impl_getCode();
    }

    private Point2D convertFromCoordinates(int x,
                                           int y) {
        return new Point2D(x, y);
    }

    private int convertToButtonId(MouseButton button) {
        return GLASS_BUTTONS.get(button);
    }

    private Color convertFromGlassColor(int glassColor) {
        WritableImage image = new WritableImage(1, 1);
        image.getPixelWriter().setArgb(0, 0, glassColor);
        return image.getPixelReader().getColor(0, 0);
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

    private void writeIntBufferToImage(IntBuffer intBuffer,
                                       WritableImage image) {
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

    private void writeByteBufferToImage(ByteBuffer byteBuffer,
                                        WritableImage image) {
        throw new UnsupportedOperationException("Writing from byte buffer is not supported.");
    }

}
