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
package org.testfx.robot.impl;

import javafx.scene.input.MouseButton;

import org.testfx.api.annotation.Unstable;
import org.testfx.robot.DragRobot;
import org.testfx.robot.MouseRobot;
import org.testfx.robot.MoveRobot;
import org.testfx.service.query.PointQuery;

@Unstable
public class DragRobotImpl implements DragRobot {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public MouseRobot mouseRobot;
    public MoveRobot moveRobot;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public DragRobotImpl(MouseRobot mouseRobot,
                          MoveRobot moveRobot) {
        this.mouseRobot = mouseRobot;
        this.moveRobot = moveRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void drag(MouseButton... buttons) {
        mouseRobot.press(buttons);
    }

    @Override
    public void drag(PointQuery pointQuery,
                     MouseButton... buttons) {
        moveRobot.moveTo(pointQuery);
        drag(buttons);
    }

    @Override
    public void drop() {
        mouseRobot.release();
    }

    @Override
    public void dropTo(PointQuery pointQuery) {
        moveRobot.moveTo(pointQuery);
        drop();
    }

    @Override
    public void dropBy(double x,
                       double y) {
        moveRobot.moveBy(x, y);
        drop();
    }

}
