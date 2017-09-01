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
package org.testfx.robot;

import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;


public interface MouseRobot {

    /**
     * Presses given buttons, until explicitly released via {@link #release(MouseButton...)}. Once pressed,
     * calls {@link org.testfx.util.WaitForAsyncUtils#waitForFxEvents()}.
     * <em>Note:</em> passing in an empty {@code MouseButton[]} will call {@code press(MouseButton.PRIMARY)}.
     *
     * @param buttons the mouse buttons
     */
    void press(MouseButton... buttons);

    /**
     * Presses given buttons, until explicitly released via {@link #release(MouseButton...)}. Once pressed,
     * {@link org.testfx.util.WaitForAsyncUtils#waitForFxEvents()} is not called.
     * <em>Note:</em> passing in an empty {@code MouseButton[]} will call {@code press(MouseButton.PRIMARY)}.
     *
     * @param buttons the mouse buttons
     */
    void pressNoWait(MouseButton... buttons);

    /**
     * Gets the buttons that have been pressed but not yet released.
     * @return an unmodifiable set of the pressed buttons
     */
    Set<MouseButton> getPressedButtons();

    /**
     * Releases given buttons. Once pressed, calls {@link org.testfx.util.WaitForAsyncUtils#waitForFxEvents()}.
     * <em>Note:</em> passing in an empty {@code MouseButton[]} will release all pressed {@code MouseButton}s.
     *
     * @param buttons the mouse buttons
     */
    void release(MouseButton... buttons);

    /**
     * Releases given buttons. Once pressed, {@link org.testfx.util.WaitForAsyncUtils#waitForFxEvents()} is not called.
     * <em>Note:</em> passing in an empty {@code MouseButton[]} will release all pressed {@code MouseButton}s.
     *
     * @param buttons the mouse buttons
     */
    void releaseNoWait(MouseButton... buttons);

    /**
     * Moves mouse to given location. Once moved, calls {@link org.testfx.util.WaitForAsyncUtils#waitForFxEvents()}.
     *
     * @param location the location to move
     */
    void move(Point2D location);

    /**
     * Moves mouse to given location. Once moved, {@link org.testfx.util.WaitForAsyncUtils#waitForFxEvents()} is
     * not called.
     *
     * @param location the location to move
     */
    void moveNoWait(Point2D location);

    /**
     * Scrolls mouse wheel by the given amount. Once scrolled, calls
     * {@link org.testfx.util.WaitForAsyncUtils#waitForFxEvents()}.
     *
     * @param wheelAmount the amount to scroll
     */
    void scroll(int wheelAmount);

    /**
     * Scrolls mouse wheel by the given amount. Once scrolled,
     * {@link org.testfx.util.WaitForAsyncUtils#waitForFxEvents()} is not called.
     *
     * @param wheelAmount the amount to scroll
     */
    void scrollNoWait(int wheelAmount);

}
