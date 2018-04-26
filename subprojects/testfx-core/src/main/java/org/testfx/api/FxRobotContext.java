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
package org.testfx.api;

import javafx.geometry.Pos;

import org.testfx.robot.BaseRobot;
import org.testfx.robot.ClickRobot;
import org.testfx.robot.DragRobot;
import org.testfx.robot.KeyboardRobot;
import org.testfx.robot.MouseRobot;
import org.testfx.robot.MoveRobot;
import org.testfx.robot.ScrollRobot;
import org.testfx.robot.SleepRobot;
import org.testfx.robot.TypeRobot;
import org.testfx.robot.WriteRobot;
import org.testfx.robot.impl.BaseRobotImpl;
import org.testfx.robot.impl.ClickRobotImpl;
import org.testfx.robot.impl.DragRobotImpl;
import org.testfx.robot.impl.KeyboardRobotImpl;
import org.testfx.robot.impl.MouseRobotImpl;
import org.testfx.robot.impl.MoveRobotImpl;
import org.testfx.robot.impl.ScrollRobotImpl;
import org.testfx.robot.impl.SleepRobotImpl;
import org.testfx.robot.impl.TypeRobotImpl;
import org.testfx.robot.impl.WriteRobotImpl;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.finder.WindowFinder;
import org.testfx.service.locator.BoundsLocator;
import org.testfx.service.locator.PointLocator;
import org.testfx.service.locator.impl.BoundsLocatorImpl;
import org.testfx.service.locator.impl.PointLocatorImpl;
import org.testfx.service.support.CaptureSupport;
import org.testfx.service.support.impl.CaptureSupportImpl;

/**
 * Stores the robot implementations, the window and node finders, position calculators, and capture support for
 * {@link FxRobot}.
 */
public class FxRobotContext {

    private final WindowFinder windowFinder;
    private final NodeFinder nodeFinder;
    private final BoundsLocator boundsLocator;
    private final PointLocator pointLocator;
    private final BaseRobot baseRobot;
    private final MouseRobot mouseRobot;
    private final KeyboardRobot keyboardRobot;
    private final MoveRobot moveRobot;
    private final SleepRobot sleepRobot;
    private final ClickRobot clickRobot;
    private final DragRobot dragRobot;
    private final ScrollRobot scrollRobot;
    private final TypeRobot typeRobot;
    private final WriteRobot writeRobot;
    private final CaptureSupport captureSupport;
    private Pos pointPosition;

    public FxRobotContext() {
        windowFinder = FxService.serviceContext().getWindowFinder();
        nodeFinder = FxService.serviceContext().getNodeFinder();
        boundsLocator = new BoundsLocatorImpl();
        pointLocator = new PointLocatorImpl(boundsLocator);
        baseRobot = new BaseRobotImpl();
        keyboardRobot = new KeyboardRobotImpl(baseRobot);
        mouseRobot = new MouseRobotImpl(baseRobot);
        sleepRobot = new SleepRobotImpl();
        typeRobot = new TypeRobotImpl(keyboardRobot, sleepRobot);
        writeRobot = new WriteRobotImpl(baseRobot, sleepRobot, windowFinder);
        moveRobot = new MoveRobotImpl(baseRobot, mouseRobot, sleepRobot);
        clickRobot = new ClickRobotImpl(mouseRobot, moveRobot, sleepRobot);
        dragRobot = new DragRobotImpl(mouseRobot, moveRobot);
        scrollRobot = new ScrollRobotImpl(mouseRobot);
        captureSupport = new CaptureSupportImpl(baseRobot);
        pointPosition = Pos.CENTER;
    }

    public WindowFinder getWindowFinder() {
        return windowFinder;
    }

    public NodeFinder getNodeFinder() {
        return nodeFinder;
    }

    public Pos getPointPosition() {
        return pointPosition;
    }

    public void setPointPosition(Pos pointPosition) {
        this.pointPosition = pointPosition;
    }

    public BoundsLocator getBoundsLocator() {
        return boundsLocator;
    }

    public PointLocator getPointLocator() {
        return pointLocator;
    }

    public BaseRobot getBaseRobot() {
        return baseRobot;
    }

    public MouseRobot getMouseRobot() {
        return mouseRobot;
    }

    public KeyboardRobot getKeyboardRobot() {
        return keyboardRobot;
    }

    public MoveRobot getMoveRobot() {
        return moveRobot;
    }

    public SleepRobot getSleepRobot() {
        return sleepRobot;
    }

    public ClickRobot getClickRobot() {
        return clickRobot;
    }

    public DragRobot getDragRobot() {
        return dragRobot;
    }

    public ScrollRobot getScrollRobot() {
        return scrollRobot;
    }

    public TypeRobot getTypeRobot() {
        return typeRobot;
    }

    public WriteRobot getWriteRobot() {
        return writeRobot;
    }

    public CaptureSupport getCaptureSupport() {
        return captureSupport;
    }

}
