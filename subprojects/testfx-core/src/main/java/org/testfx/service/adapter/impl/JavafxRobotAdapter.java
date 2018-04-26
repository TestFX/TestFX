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

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import org.testfx.service.adapter.RobotAdapter;

import static org.testfx.util.WaitForAsyncUtils.asyncFx;

/**
 * A {@link RobotAdapter} implementation that uses the only the JavaFX public API.
 * <p>
 * Developer's Note: Forcing the type parameter to be {@code JavafxRobotAdapter} is a bit
 * of a kludge. Ideally we would use a {@code JavafxRobot} instead but such a type does
 * not exist.
 */
public class JavafxRobotAdapter implements RobotAdapter<JavafxRobotAdapter> {
    private Scene scene;

    private boolean isShiftDown;
    private boolean isControlDown;
    private boolean isAltDown;
    private boolean isMetaDown;

    private MouseButton lastButtonPressed;
    private boolean isButton1Pressed;
    private boolean isButton2Pressed;
    private boolean isButton3Pressed;
    private double sceneMouseX;
    private double sceneMouseY;
    private double screenMouseX;
    private double screenMouseY;

    public void robotCreate(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void robotCreate() {
        // NO-OP
    }

    @Override
    public void robotDestroy() {
        // NO-OP
    }

    @Override
    public JavafxRobotAdapter getRobotInstance() {
        return null;
    }

    public void keyPress(KeyCode key) {
        asyncFx(() -> Event.fireEvent(getEventTarget(scene), createKeyEvent(
                KeyEvent.KEY_PRESSED, key, "")));
    }

    public void keyRelease(KeyCode key) {
        asyncFx(() -> Event.fireEvent(getEventTarget(scene), createKeyEvent(
                KeyEvent.KEY_RELEASED, key, "")));
    }

    public void keyType(KeyCode key,
                        String character) {
        asyncFx(() -> Event.fireEvent(getEventTarget(scene), createKeyEvent(
                KeyEvent.KEY_TYPED, key, character)));
    }

    public Point2D getMouseLocation() {
        throw new UnsupportedOperationException();
    }

    public void mouseMove(Point2D location) {
        asyncFx(() -> Event.fireEvent(getEventTarget(scene), createMouseEvent(MouseEvent.MOUSE_MOVED,
                (int) location.getX(), (int) location.getY(), lastButtonPressed, 0)));
    }

    public void mousePress(MouseButton button,
                           int clickCount) {
        asyncFx(() -> Event.fireEvent(getEventTarget(scene), createMouseEvent(MouseEvent.MOUSE_PRESSED,
                sceneMouseX, sceneMouseY, button, clickCount)));
    }

    public void mouseRelease(MouseButton button,
                             int clickCount) {
        asyncFx(() -> Event.fireEvent(getEventTarget(scene), createMouseEvent(MouseEvent.MOUSE_RELEASED,
                sceneMouseX, sceneMouseY, button, clickCount)));
    }

    public void mouseClick(MouseButton button,
                           int clickCount) {
        asyncFx(() -> Event.fireEvent(getEventTarget(scene), createMouseEvent(MouseEvent.MOUSE_CLICKED,
                sceneMouseX, sceneMouseY, button, clickCount)));
    }

    public void mousePress(MouseButton button) {
        mousePress(button, 1);
    }

    public void mouseRelease(MouseButton button) {
        mouseRelease(button, 1);
    }

    public void mouseClick(MouseButton button) {
        mouseClick(button, 1);
    }

    public void mouseDrag(MouseButton button) {
        asyncFx(() -> Event.fireEvent(getEventTarget(scene), createMouseEvent(MouseEvent.MOUSE_DRAGGED,
                sceneMouseX, sceneMouseY, button, 0)));
    }

    public void mouseWheel(int wheelAmount) {
        asyncFx(() -> Event.fireEvent(getEventTarget(scene), createScrollEvent(wheelAmount)));
    }

    public Color getCapturePixelColor(Point2D location) {
        if (!Platform.isFxApplicationThread()) {
            throw new RuntimeException("JavafxRobotAdapter#getCapturePixelColor(..) must be called on JavaFX " +
                    "application thread but was: " + Thread.currentThread());
        }
        WritableImage snapshot = scene.snapshot(null);
        return snapshot.getPixelReader().getColor((int) location.getX(), (int) location.getY());
    }

    public Image getCaptureRegion(Rectangle2D region) {
        if (!Platform.isFxApplicationThread()) {
            throw new RuntimeException("JavafxRobotAdapter#getCaptureRegion(..) must be called on JavaFX " +
                    "application thread but was: " + Thread.currentThread());
        }
        ImageView imageView = new ImageView(scene.snapshot(null));
        imageView.setViewport(region);
        new Scene(new Pane(imageView)); // The imageView must be in an off-screen Scene to be snapshotted
        return imageView.snapshot(null, null);
    }

    private EventTarget getEventTarget(Scene scene) {
        return scene.getFocusOwner() != null ? scene.getFocusOwner() : scene;
    }

    private KeyEvent createKeyEvent(EventType<KeyEvent> eventType, KeyCode keyCode, String character) {
        boolean pressed = eventType == KeyEvent.KEY_PRESSED;
        if (keyCode == KeyCode.SHIFT) {
            isShiftDown = pressed;
        }
        if (keyCode == KeyCode.CONTROL) {
            isControlDown = pressed;
        }
        if (keyCode == KeyCode.ALT) {
            isAltDown = pressed;
        }
        if (keyCode == KeyCode.META) {
            isMetaDown = pressed;
        }

        boolean typed = eventType == KeyEvent.KEY_TYPED;
        String keyChar = typed ? character : KeyEvent.CHAR_UNDEFINED;
        String keyText = typed ? "" : keyCode.getName();
        return new KeyEvent(eventType, keyChar, keyText, keyCode, isShiftDown, isControlDown, isAltDown, isMetaDown);
    }

    private MouseEvent createMouseEvent(EventType<MouseEvent> eventType, double x, double y, MouseButton mouseButton,
                                        int clickCount) {
        screenMouseX = scene.getWindow().getX() + scene.getX() + x;
        screenMouseY = scene.getWindow().getY() + scene.getY() + y;
        sceneMouseX = x;
        sceneMouseY = y;

        MouseButton button = mouseButton;
        EventType<MouseEvent> type = eventType;
        if (type == MouseEvent.MOUSE_PRESSED || type == MouseEvent.MOUSE_RELEASED) {
            boolean pressed = type == MouseEvent.MOUSE_PRESSED;
            switch (button) {
                case PRIMARY:
                    isButton1Pressed = pressed;
                    break;
                case MIDDLE:
                    isButton2Pressed = pressed;
                    break;
                case SECONDARY:
                    isButton3Pressed = pressed;
                    break;
            }
            if (pressed) {
                lastButtonPressed = button;
            }
            else {
                if (!(isButton1Pressed || isButton2Pressed || isButton3Pressed)) {
                    lastButtonPressed = MouseButton.NONE;
                }
            }
        }
        else if (type == MouseEvent.MOUSE_MOVED) {
            boolean someButtonPressed = isButton1Pressed || isButton2Pressed || isButton3Pressed;
            if (someButtonPressed) {
                type = MouseEvent.MOUSE_DRAGGED;
                button = MouseButton.NONE;
            }
        }

        return new MouseEvent(type, (int) sceneMouseX, (int) sceneMouseY, (int) screenMouseX, (int) screenMouseY,
                button, clickCount, isShiftDown, isControlDown, isAltDown, isMetaDown, isButton1Pressed,
                isButton2Pressed, isButton3Pressed, false, button == MouseButton.SECONDARY, false, null);
    }

    private ScrollEvent createScrollEvent(int wheelAmount) {
        screenMouseX = scene.getWindow().getX() + scene.getX() + sceneMouseX;
        screenMouseY = scene.getWindow().getY() + scene.getY() + sceneMouseY;

        return new ScrollEvent(ScrollEvent.SCROLL, (int) sceneMouseX, (int) sceneMouseY, (int) screenMouseX,
                (int)screenMouseY, isShiftDown, isControlDown, isAltDown, isMetaDown, false, false, 0,
                wheelAmount * 40, 0, 0, ScrollEvent.HorizontalTextScrollUnits.NONE, 0,
                ScrollEvent.VerticalTextScrollUnits.NONE, 0, 0, null);
    }

}
