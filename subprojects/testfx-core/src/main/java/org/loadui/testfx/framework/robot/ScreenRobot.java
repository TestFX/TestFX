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
package org.loadui.testfx.framework.robot;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public interface ScreenRobot {
    public Point2D getMouseLocation();

    public Image captureRegion(Rectangle2D region);

    public void moveMouseTo(double x, double y);

    public void moveMouseLinearTo(double x, double y);

    public void scrollMouse(int amount);

    public void pressMouse(MouseButton button);

    public void releaseMouse(MouseButton button);

    public void pressKey(KeyCode key);

    public void releaseKey(KeyCode key);
}
