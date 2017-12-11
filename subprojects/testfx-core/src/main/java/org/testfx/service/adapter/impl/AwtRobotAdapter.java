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

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import org.testfx.api.annotation.Unstable;
import org.testfx.service.adapter.RobotAdapter;

import static org.testfx.internal.JavaVersionAdapter.convertToKeyCodeId;

@Unstable
public class AwtRobotAdapter implements RobotAdapter<Robot> {

    private Robot awtRobot;

    @Override
    public void robotCreate() {
        if (isAwtEnvironmentHeadless()) {
            throw new RuntimeException("environment is headless");
        }
        initializeAwtToolkit();
        awtRobot = createAwtRobot();
    }

    @Override
    public void robotDestroy() {
        awtRobot = null;
    }

    @Override
    public Robot getRobotInstance() {
        return awtRobot;
    }

    @Override
    public void keyPress(KeyCode key) {
        useRobot().keyPress(convertToKeyCodeId(key));
    }

    @Override
    public void keyRelease(KeyCode key) {
        useRobot().keyRelease(convertToKeyCodeId(key));
    }

    @Override
    public Point2D getMouseLocation() {
        return convertFromAwtPoint(MouseInfo.getPointerInfo().getLocation());
    }

    @Override
    public void mouseMove(Point2D location) {
        useRobot().mouseMove((int) location.getX(), (int) location.getY());
    }

    @Override
    public void mousePress(MouseButton button) {
        useRobot().mousePress(convertToAwtButton(button));
    }

    @Override
    public void mouseRelease(MouseButton button) {
        useRobot().mouseRelease(convertToAwtButton(button));
    }

    @Override
    public void mouseWheel(int wheelAmount) {
        useRobot().mouseWheel(wheelAmount);
    }

    @Override
    public Color getCapturePixelColor(Point2D location) {
        Rectangle2D region = new Rectangle2D(location.getX(), location.getY(), 1, 1);
        Image image = getCaptureRegion(region);
        return image.getPixelReader().getColor(0, 0);
    }

    @Override
    public Image getCaptureRegion(Rectangle2D region) {
        Rectangle awtRectangle = convertToAwtRectangle(region);
        BufferedImage awtBufferedImage = useRobot().createScreenCapture(awtRectangle);
        return convertFromAwtBufferedImage(awtBufferedImage);
    }

    private Robot useRobot() {
        if (awtRobot == null) {
            robotCreate();
        }
        return awtRobot;
    }

    private Robot createAwtRobot() {
        try {
            return new Robot();
        }
        catch (AWTException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void initializeAwtToolkit() {
        Toolkit.getDefaultToolkit();
    }

    private boolean isAwtEnvironmentHeadless() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance();
    }

    private Point2D convertFromAwtPoint(Point awtPoint) {
        return new Point2D(awtPoint.getX(), awtPoint.getY());
    }

    private Image convertFromAwtBufferedImage(BufferedImage awtBufferedImage) {
        return SwingFXUtils.toFXImage(awtBufferedImage, null);
    }

    private int convertToAwtButton(MouseButton button) {
        switch (button) {
            case PRIMARY: return InputEvent.BUTTON1_DOWN_MASK;
            case MIDDLE: return InputEvent.BUTTON2_DOWN_MASK;
            case SECONDARY: return InputEvent.BUTTON3_DOWN_MASK;
            default: throw new IllegalArgumentException("MouseButton: " + button + " not supported by AwtRobot");
        }
    }

    private Rectangle convertToAwtRectangle(Rectangle2D rectangle) {
        return new Rectangle(
            (int) rectangle.getMinX(), (int) rectangle.getMinY(),
            (int) rectangle.getWidth(), (int) rectangle.getHeight()
        );
    }

}
