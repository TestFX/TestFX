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
package org.testfx.robot;

import javafx.scene.input.MouseButton;

import org.testfx.service.query.PointQuery;

public interface ClickRobot {

    /**
     * Clicks whatever is under the mouse.
     *
     * @param buttons the mouse buttons to click
     */
    void clickOn(MouseButton... buttons);

    /**
     * Moves mouse directly to {@link PointQuery#query()} and clicks whatever is under it.
     *
     * @param pointQuery the pointQuery
     * @param buttons the mouse buttons to click
     */
    default void clickOn(PointQuery pointQuery, MouseButton... buttons) {
        clickOn(pointQuery, Motion.DEFAULT, buttons);
    }

    /**
     * Moves mouse to {@link PointQuery#query()} using the specified {@code motion}
     * (see: {@link Motion}) and clicks whatever is under it.
     *
     * @param pointQuery the pointQuery
     * @param motion the type of motion to use for movement
     * @param buttons the mouse buttons to click
     */
    void clickOn(PointQuery pointQuery, Motion motion, MouseButton... buttons);

    /**
     * Double-clicks whatever is under the mouse.
     *
     * @param buttons the mouse buttons to click
     */
    void doubleClickOn(MouseButton... buttons);

    /**
     * Moves mouse directly to {@link PointQuery#query()} and double-clicks whatever
     * is under it.
     *
     * @param pointQuery the pointQuery
     * @param buttons the mouse buttons to click
     */
    default void doubleClickOn(PointQuery pointQuery, MouseButton... buttons) {
        doubleClickOn(pointQuery, Motion.DEFAULT, buttons);
    }

    /**
     * Moves mouse to {@link PointQuery#query()} using the specified {@code motion}
     * (see: {@link Motion} and double-clicks whatever is under it.
     *
     * @param pointQuery the pointQuery
     * @param motion the type of motion to use for movement
     * @param buttons the mouse buttons to click
     */
    void doubleClickOn(PointQuery pointQuery, Motion motion, MouseButton... buttons);

}
