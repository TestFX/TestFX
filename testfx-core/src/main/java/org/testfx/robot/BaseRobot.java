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
package org.testfx.robot;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public interface BaseRobot {

    public void pressKeyboard(KeyCode key);

    public void releaseKeyboard(KeyCode key);

    public void typeKeyboard(Scene scene,
                             KeyCode key,
                             String character);

    public Point2D retrieveMouse();

    public void moveMouse(Point2D point);

    public void scrollMouse(int amount);

    public void pressMouse(MouseButton button);

    public void releaseMouse(MouseButton button);

    public Image captureRegion(Rectangle2D region);

    public void awaitEvents();

}
