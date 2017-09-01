/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2017 The TestFX Contributors
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
import org.testfx.api.annotation.Unstable;
import org.testfx.service.adapter.RobotAdapter;

import static org.testfx.util.WaitForAsyncUtils.asyncFx;
import static org.testfx.util.WaitForAsyncUtils.waitForAsyncFx;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@Unstable
public class GlassRobotAdapter implements RobotAdapter<Robot> {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    public static final int RETRIEVAL_TIMEOUT_IN_MILLIS = 10000;

    public static final int BYTE_BUFFER_BYTES_PER_COMPONENT = 1;
    public static final int INT_BUFFER_BYTES_PER_COMPONENT = 4;

    public static final Map<MouseButton, Integer> GLASS_BUTTONS = ImmutableMap.of(
        MouseButton.PRIMARY, Robot.MOUSE_LEFT_BTN,
        MouseButton.SECONDARY, Robot.MOUSE_RIGHT_BTN,
        MouseButton.MIDDLE, Robot.MOUSE_MIDDLE_BTN
    );

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private Robot glassRobot;

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    // ROBOT.

    @Override
    public void robotCreate() {
        waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS, () -> glassRobot = createGlassRobot());
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

    // KEY.

    @Override
    public void keyPress(KeyCode key) {
        asyncFx(() -> useRobot().keyPress(convertToKeyCodeId(key)));
    }

    @Override
    public void keyRelease(KeyCode key) {
        asyncFx(() -> useRobot().keyRelease(convertToKeyCodeId(key)));
    }

    // MOUSE.

    @Override
    public Point2D getMouseLocation() {
        return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS, () -> convertFromCoordinates(
                useRobot().getMouseX(), useRobot().getMouseY()));
    }

    @Override
    public void mouseMove(Point2D location) {
        asyncFx(() -> useRobot().mouseMove((int) location.getX(), (int) location.getY()));
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

    // CAPTURE.

    @Override
    public Color getCapturePixelColor(Point2D location) {
        return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS, () -> {
            int glassColor = useRobot().getPixelColor(
                (int) location.getX(), (int) location.getY()
            );
            return convertFromGlassColor(glassColor);
        });
    }

    @Override
    public Image getCaptureRegion(Rectangle2D region) {
        return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS, () -> {
            Pixels glassPixels = useRobot().getScreenCapture(
                (int) region.getMinX(), (int) region.getMinY(),
                (int) region.getWidth(), (int) region.getHeight()
            );
            return convertFromGlassPixels(glassPixels);
        });
    }

    // TIMER.

    @Override
    public void timerWaitForIdle() {
        waitForFxEvents();
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Robot useRobot() {
        if (glassRobot == null) {
            robotCreate();
        }
        return glassRobot;
    }

    private Robot createGlassRobot() {
        return Application.GetApplication().createRobot();
    }

    private int convertToKeyCodeId(KeyCode keyCode) {
        return keyCode.getCode();
    }

    private Point2D convertFromCoordinates(int pointX,
                                           int pointY) {
        return new Point2D(pointX, pointY);
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
