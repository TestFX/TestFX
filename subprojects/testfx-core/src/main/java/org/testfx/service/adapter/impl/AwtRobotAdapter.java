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

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import org.testfx.internal.JavaVersionAdapter;
import org.testfx.internal.PlatformAdapter;
import org.testfx.internal.PlatformAdapter.OS;
import org.testfx.service.adapter.RobotAdapter;

import static org.testfx.internal.JavaVersionAdapter.convertToKeyCodeId;

public class AwtRobotAdapter implements RobotAdapter<Robot> {

    private Robot awtRobot;

    @Override
    public void robotCreate() {
        if (GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance()) {
            throw new RuntimeException("can not create awt robot as environment is headless");
        }
        // Initialize AWT toolkit.
        Toolkit.getDefaultToolkit();
        awtRobot = createAwtRobot();
    }

    @Override
    public void robotDestroy() {
        awtRobot = null;
    }

    @Override
    public void keyPress(KeyCode key) {
        if (key == KeyCode.COMMAND) {
            key = KeyCode.META;
        }
        useRobot().keyPress(convertToKeyCodeId(key));
    }

    @Override
    public void keyRelease(KeyCode key) {
        if (key == KeyCode.COMMAND) {
            key = KeyCode.META;
        }
        useRobot().keyRelease(convertToKeyCodeId(key));
    }

    @Override
    public Point2D getMouseLocation() {
        Point awtPoint = MouseInfo.getPointerInfo().getLocation();
        return scaleInverseRect(new Point2D(awtPoint.getX(), awtPoint.getY()));
    }

    @Override
    public void mouseMove(Point2D location) {
        final Rectangle2D scaled = scaleRect(new Rectangle2D(location.getX(), location.getY(), 0, 0));
        useRobot().mouseMove((int) scaled.getMinX(), (int) scaled.getMinY());
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
        //captureRegion does scaling already
        final Rectangle2D region = new Rectangle2D(location.getX(), location.getY(), 2, 2);
        Image image = getCaptureRegion(region);
        return image.getPixelReader().getColor(0, 0);
    }

    @Override
    public Image getCaptureRegion(Rectangle2D region) {
        final Rectangle2D scaled = scaleRect(region);
        Rectangle awtRectangle = new Rectangle(
                (int) scaled.getMinX(), (int) scaled.getMinY(),
                (int) scaled.getWidth(), (int) scaled.getHeight());

        BufferedImage awtBufferedImage = useRobot().createScreenCapture(awtRectangle);
        BufferedImage out;
        if (scaleRequired()) {
            double scaleX = region.getWidth() / scaled.getWidth();
            double scaleY = region.getHeight() / scaled.getHeight();
            int w = (int)(awtBufferedImage.getWidth() * scaleX);
            int h = (int)(awtBufferedImage.getHeight() * scaleY);
            out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            AffineTransform at = new AffineTransform();
            at.scale(scaleX, scaleY);
            AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            out = scaleOp.filter(awtBufferedImage, out);
        } else {
            out = awtBufferedImage;
        }
        return SwingFXUtils.toFXImage(out, null);
    }


    public Image getCaptureRegionRaw(Rectangle2D region) {
        final Rectangle2D scaled = scaleRect(region);
        Rectangle awtRectangle = new Rectangle(
                (int) scaled.getMinX(), (int) scaled.getMinY(),
                (int) scaled.getWidth(), (int) scaled.getHeight());
        BufferedImage awtBufferedImage = useRobot().createScreenCapture(awtRectangle);
        return SwingFXUtils.toFXImage(awtBufferedImage, null);
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

    private int convertToAwtButton(MouseButton button) {
        switch (button) {
            case PRIMARY: return InputEvent.BUTTON1_DOWN_MASK;
            case MIDDLE: return InputEvent.BUTTON2_DOWN_MASK;
            case SECONDARY: return InputEvent.BUTTON3_DOWN_MASK;
            default: throw new IllegalArgumentException("MouseButton: " + button + " not supported by awt robot");
        }
    }

    private Rectangle2D scaleRect(Rectangle2D rect) {
        if (!scaleRequired()) {
            return rect;
        }
        double factorX = JavaVersionAdapter.getScreenScaleX();
        double factorY = JavaVersionAdapter.getScreenScaleY();
        return new Rectangle2D(rect.getMinX() * factorX, rect.getMinY() * factorY, rect.getWidth() * factorX,
                rect.getHeight() * factorY);
    }

    private Point2D scaleInverseRect(Point2D pt) {
        if (!scaleRequired()) {
            return pt;
        }
        double factorX = JavaVersionAdapter.getScreenScaleX();
        double factorY = JavaVersionAdapter.getScreenScaleY();
        return new Point2D(pt.getX() / factorX, pt.getY() / factorY);
    }

    private boolean scaleRequired() {
        if (JavaVersionAdapter.getScreenScaleX() == 1.0 && JavaVersionAdapter.getScreenScaleY() == 1.0) {
            // Just to prevent unnecessary computations.
            return false;
        } else {
            if (Boolean.getBoolean("testfx.awt.scale")) {
                return true;
            }
            if (PlatformAdapter.getOs() == OS.WINDOWS) {
                return false;
            }
            // Do not remove, if not testing on headed macOS with Java 10+. Beware: macOS with AWT robot is
            // not covered by CI builds!
            return PlatformAdapter.getOs() != OS.MAC;
        }
    }


}
