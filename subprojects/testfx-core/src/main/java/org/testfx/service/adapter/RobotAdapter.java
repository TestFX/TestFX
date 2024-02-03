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
package org.testfx.service.adapter;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

/**
 * The common interface for all RobotAdapters.<br>
 * These are the minimum set of functions required from a robot to provide the
 * full functionality use in the higher level api.
 *
 * <h2>Threading</h2>
 * The caller of the Adapter usually doesn't run on the FX-Application Thread.
 * If required, the implementation of this class will schedule the actions on the
 * FX-Application Thread.
 *
 */
public interface RobotAdapter<T> {

    /**
     * Creates a robot.
     */
    void robotCreate();

    /**
     * Destroys the robot
     */
    void robotDestroy();

    /**
     * Function used to make the robot press a key. The key must be a physical
     * existing key on the keyboard.
     *
     * @param key the key to press (must exist on a keyboard)
     */
    void keyPress(KeyCode key);

    /**
     * Function used to make the robot release a key. The key must be a physical
     * existing key on the keyboard.
     *
     * @param key the key to press (must exist on a keyboard)
     */
    void keyRelease(KeyCode key);

    /**
     * Returns the current position of the cursor in JavaFx coordinates
     *
     * @return the current position of the mouse cursor
     */
    Point2D getMouseLocation();

    /**
     * Moves the mouse cursor to the given position in JavaFx coordinates
     *
     * @param location the location in JavaFx coordinates to move the cursor to
     */
    void mouseMove(Point2D location);

    /**
     * Makes the robot press a mouse button.
     *
     * @param button the button to press
     */
    void mousePress(MouseButton button);

    /**
     * Makes the robot release a mouse button.
     *
     * @param button the button to release
     */
    void mouseRelease(MouseButton button);

    /**
     * Makes the robot to simulate a action of the mouse wheel.<br>
     * Negative values indicate movement up/away from the user, positive values
     * indicate movement down/towards the user.
     *
     * @param wheelAmount the amount to scroll
     */
    void mouseWheel(int wheelAmount);

    /**
     * Gets the color of a pixel at the given JavaFx coordinates. The returned Color
     * is in the JavaFx color space. //TODO Due to technical reasons, there might be
     * a deviation in color.
     *
     * @param location of the pixel in JavaFx coordinates, to retrieve the color for
     * @return the Color of the given Pixel in the JavaFx color space
     */
    Color getCapturePixelColor(Point2D location);

    /**
     * Captures a region of the screen. The returned Image is in the JavaFx color
     * space. //TODO Due to technical reasons, there might be a deviation.
     *
     * @param region the region to capture in JavaFx coordinates
     * @return a image of the region
     */
    Image getCaptureRegion(Rectangle2D region);
}
