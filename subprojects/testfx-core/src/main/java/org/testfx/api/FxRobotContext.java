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
package org.testfx.api;

import javafx.geometry.Pos;

import org.loadui.testfx.robots.BaseRobot;
import org.loadui.testfx.robots.ClickRobot;
import org.loadui.testfx.robots.DragRobot;
import org.loadui.testfx.robots.KeyboardRobot;
import org.loadui.testfx.robots.MouseRobot;
import org.loadui.testfx.robots.MoveRobot;
import org.loadui.testfx.robots.ScrollRobot;
import org.loadui.testfx.robots.SleepRobot;
import org.loadui.testfx.robots.TypeRobot;
import org.loadui.testfx.robots.WriteRobot;
import org.loadui.testfx.robots.impl.BaseRobotImpl;
import org.loadui.testfx.robots.impl.ClickRobotImpl;
import org.loadui.testfx.robots.impl.DragRobotImpl;
import org.loadui.testfx.robots.impl.KeyboardRobotImpl;
import org.loadui.testfx.robots.impl.MouseRobotImpl;
import org.loadui.testfx.robots.impl.MoveRobotImpl;
import org.loadui.testfx.robots.impl.ScrollRobotImpl;
import org.loadui.testfx.robots.impl.SleepRobotImpl;
import org.loadui.testfx.robots.impl.TypeRobotImpl;
import org.loadui.testfx.robots.impl.WriteRobotImpl;
import org.loadui.testfx.service.finder.NodeFinder;
import org.loadui.testfx.service.finder.WindowFinder;
import org.loadui.testfx.service.locator.BoundsLocator;
import org.loadui.testfx.service.locator.PointLocator;
import org.loadui.testfx.service.locator.impl.BoundsLocatorImpl;
import org.loadui.testfx.service.locator.impl.PointLocatorImpl;

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

}
