/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.service.adapter.impl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

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

import org.testfx.api.annotation.Unstable;

import static org.testfx.util.WaitForAsyncUtils.asyncFx;

@Unstable
public class JavafxRobotAdapter {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------
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

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    // ROBOT.

    public void robotCreate(Scene scene) {
        this.scene = scene;
    }

    // KEY.

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

    // MOUSE.

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

    // CAPTURE.
    public CompletableFuture<Color> getCapturePixelColor(Point2D location) {
        CompletableFuture<Color> captureColorFutureResult = new CompletableFuture<>();
        Platform.runLater(() -> {
            WritableImage snapshot = scene.snapshot(null);
            captureColorFutureResult.complete(snapshot.getPixelReader().getColor(
                    (int) location.getX(), (int) location.getY()));
        });
        return captureColorFutureResult;
    }

    public CompletableFuture<Image> getCaptureRegion(Rectangle2D region) {
        CompletableFuture<Image> captureRegionFutureResult = new CompletableFuture<>();
        Platform.runLater(() -> scene.snapshot(result -> {
            ImageView imageView = new ImageView(result.getImage());
            imageView.setViewport(region);
            Pane pane = new Pane(imageView);
            Scene offScreenScene = new Scene(pane);
            WritableImage croppedImage = imageView.snapshot(null, null);
            captureRegionFutureResult.complete(croppedImage);
            return null;
        }, null));
        return captureRegionFutureResult;
    }

    // TIMER.

    /**
     * Block until events in the queue are processed.
     */
    public void timerWaitForIdle() {
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(latch::countDown);
        while (true) {
            try {
                latch.await();
                break;
            }
            catch (InterruptedException ignore) {
            }
        }
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

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
            if (button == MouseButton.PRIMARY) {
                isButton1Pressed = pressed;
            }
            else if (button == MouseButton.MIDDLE) {
                isButton2Pressed = pressed;
            }
            else if (button == MouseButton.SECONDARY) {
                isButton3Pressed = pressed;
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
