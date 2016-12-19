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
package org.testfx.api;

import javafx.geometry.Pos;

import org.testfx.api.annotation.Unstable;
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
@Unstable(reason = "class was recently added")
public class FxRobotContext {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private WindowFinder windowFinder;
    private NodeFinder nodeFinder;

    private Pos pointPosition;
    private BoundsLocator boundsLocator;
    private PointLocator pointLocator;

    private BaseRobot baseRobot;
    private MouseRobot mouseRobot;
    private KeyboardRobot keyboardRobot;
    private MoveRobot moveRobot;
    private SleepRobot sleepRobot;

    private ClickRobot clickRobot;
    private DragRobot dragRobot;
    private ScrollRobot scrollRobot;
    private TypeRobot typeRobot;
    private WriteRobot writeRobot;

    private CaptureSupport captureSupport;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

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
    }

    //---------------------------------------------------------------------------------------------
    // GETTER AND SETTER.
    //---------------------------------------------------------------------------------------------

    public WindowFinder getWindowFinder() {
        return windowFinder;
    }

    public void setWindowFinder(WindowFinder windowFinder) {
        this.windowFinder = windowFinder;
    }

    public NodeFinder getNodeFinder() {
        return nodeFinder;
    }

    public void setNodeFinder(NodeFinder nodeFinder) {
        this.nodeFinder = nodeFinder;
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

    public void setBoundsLocator(BoundsLocator boundsLocator) {
        this.boundsLocator = boundsLocator;
    }

    public PointLocator getPointLocator() {
        return pointLocator;
    }

    public void setPointLocator(PointLocator pointLocator) {
        this.pointLocator = pointLocator;
    }

    public BaseRobot getBaseRobot() {
        return baseRobot;
    }

    public void setBaseRobot(BaseRobot baseRobot) {
        this.baseRobot = baseRobot;
    }

    public MouseRobot getMouseRobot() {
        return mouseRobot;
    }

    public void setMouseRobot(MouseRobot mouseRobot) {
        this.mouseRobot = mouseRobot;
    }

    public KeyboardRobot getKeyboardRobot() {
        return keyboardRobot;
    }

    public void setKeyboardRobot(KeyboardRobot keyboardRobot) {
        this.keyboardRobot = keyboardRobot;
    }

    public MoveRobot getMoveRobot() {
        return moveRobot;
    }

    public void setMoveRobot(MoveRobot moveRobot) {
        this.moveRobot = moveRobot;
    }

    public SleepRobot getSleepRobot() {
        return sleepRobot;
    }

    public void setSleepRobot(SleepRobot sleepRobot) {
        this.sleepRobot = sleepRobot;
    }

    public ClickRobot getClickRobot() {
        return clickRobot;
    }

    public void setClickRobot(ClickRobot clickRobot) {
        this.clickRobot = clickRobot;
    }

    public DragRobot getDragRobot() {
        return dragRobot;
    }

    public void setDragRobot(DragRobot dragRobot) {
        this.dragRobot = dragRobot;
    }

    public ScrollRobot getScrollRobot() {
        return scrollRobot;
    }

    public void setScrollRobot(ScrollRobot scrollRobot) {
        this.scrollRobot = scrollRobot;
    }

    public TypeRobot getTypeRobot() {
        return typeRobot;
    }

    public void setTypeRobot(TypeRobot typeRobot) {
        this.typeRobot = typeRobot;
    }

    public WriteRobot getWriteRobot() {
        return writeRobot;
    }

    public void setWriteRobot(WriteRobot writeRobot) {
        this.writeRobot = writeRobot;
    }

    public CaptureSupport getCaptureSupport() {
        return captureSupport;
    }

    public void setCaptureSupport(CaptureSupport captureSupport) {
        this.captureSupport = captureSupport;
    }

}
