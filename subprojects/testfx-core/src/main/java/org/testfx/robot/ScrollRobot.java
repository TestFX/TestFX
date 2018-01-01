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
package org.testfx.robot;

import javafx.geometry.HorizontalDirection;
import javafx.geometry.VerticalDirection;

public interface ScrollRobot {

    /**
     * Scrolls vertically by {@code amount} (in terms of ticks of a mouse wheel). If
     * {@code amount} is positive we scroll up, if it's negative we scroll down.
     *
     * @param amount the number of scroll ticks to scroll
     */
    void scroll(int amount);

    /**
     * Scrolls vertically by {@code amount} (in terms of ticks of a mouse wheel) in given direction.
     *
     * @param positiveAmount the number of scroll ticks to scroll vertically
     * @param direction the vertical direction in which to scroll (up or down)
     */
    void scroll(int positiveAmount, VerticalDirection direction);

    /**
     * Scrolls up by {@code amount} (in terms of ticks of a mouse wheel).
     *
     * @param positiveAmount the number of scroll ticks to scroll up
     */
    void scrollUp(int positiveAmount);

    /**
     * Scrolls down by {@code amount} (in terms of ticks of a wheel).
     *
     * @param positiveAmount the number of scroll ticks to scroll down
     */
    void scrollDown(int positiveAmount);

    /**
     * Scrolls horizontally by {@code amount} (in terms of ticks of a mouse wheel) in given direction.
     *
     * @param positiveAmount the number of scroll ticks to scroll horizontally
     * @param direction the horizontal direction in which to scroll (left or right)
     */
    void scroll(int positiveAmount, HorizontalDirection direction);

    /**
     * Scrolls left by {@code amount} (in terms of ticks of a wheel).
     *
     * @param positiveAmount the number of scroll ticks to scroll left
     */
    void scrollLeft(int positiveAmount);

    /**
     * Scrolls right by {@code amount} (in terms of ticks of a wheel).
     *
     * @param positiveAmount the number of scroll ticks to scroll right
     */
    void scrollRight(int positiveAmount);
}
