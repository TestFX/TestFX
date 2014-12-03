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
package org.testfx.robot.impl;

import java.util.List;
import javafx.geometry.Point2D;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.testfx.robot.BaseRobot;
import org.testfx.robot.MouseRobot;
import org.testfx.robot.MoveRobot;
import org.testfx.robot.SleepRobot;
import org.testfx.service.query.PointQuery;

public class MoveRobotImpl implements MoveRobot {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final long SLEEP_AFTER_MOVEMENT_STEP_IN_MILLIS = 1;

    private static final long MIN_POINT_OFFSET_COUNT = 1;
    private static final long MAX_POINT_OFFSET_COUNT = 200;

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private BaseRobot baseRobot;
    private MouseRobot mouseRobot;
    private SleepRobot sleepRobot;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public MoveRobotImpl(BaseRobot baseRobot,
                         MouseRobot mouseRobot,
                         SleepRobot sleepRobot) {
        this.baseRobot = baseRobot;
        this.mouseRobot = mouseRobot;
        this.sleepRobot = sleepRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void moveTo(PointQuery pointQuery) {
        // Since moving takes time, only do it if we're not already at the desired point.
        Point2D sourcePoint = retrieveMouseLocation();
        Point2D targetPoint = pointQuery.query();
        if (sourcePoint != targetPoint) {
            moveMouseStepwiseBetween(sourcePoint, targetPoint);
        }

        // If the target has moved while we were moving the mouse, update to the new position.
        Point2D finalPoint = pointQuery.query();
        moveMouseDirectlyTo(finalPoint);
    }

    @Override
    public void moveBy(double x,
                       double y) {
        Point2D sourcePoint = retrieveMouseLocation();
        Point2D targetPoint = new Point2D(sourcePoint.getX() + x, sourcePoint.getY() + y);
        moveMouseStepwiseBetween(sourcePoint, targetPoint);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Point2D retrieveMouseLocation() {
        return baseRobot.retrieveMouse();
    }

    private void moveMouseDirectlyTo(Point2D targetPoint) {
        mouseRobot.move(targetPoint);
    }

    private void moveMouseStepwiseBetween(Point2D sourcePoint,
                                          Point2D targetPoint) {
        double pointDistance = calculateDistanceBetween(sourcePoint, targetPoint);
        int pointOffsetCount = (int) limitValueBetween(
            pointDistance, MIN_POINT_OFFSET_COUNT, MAX_POINT_OFFSET_COUNT
        );
        List<Point2D> points = interpolatePointsBetween(
            sourcePoint, targetPoint, pointOffsetCount
        );
        for (Point2D point : Iterables.limit(points, points.size() - 1)) {
            mouseRobot.moveNoWait(point);
            sleepRobot.sleep(SLEEP_AFTER_MOVEMENT_STEP_IN_MILLIS);
        }
        mouseRobot.move(targetPoint);
    }

    private List<Point2D> interpolatePointsBetween(Point2D sourcePoint,
                                                   Point2D targetPoint,
                                                   int pointOffsetCount) {
        List<Point2D> points = Lists.newArrayList();
        for (int pointOffset = 0; pointOffset <= pointOffsetCount; pointOffset++) {
            double factor = (double) pointOffset / (double) pointOffsetCount;
            Point2D point = interpolatePointBetween(sourcePoint, targetPoint, factor);
            points.add(point);
        }
        return ImmutableList.copyOf(points);
    }

    private double calculateDistanceBetween(Point2D point0,
                                            Point2D point1) {
        double x = point0.getX() - point1.getX();
        double y = point0.getY() - point1.getY();
        return Math.sqrt((x * x) + (y * y));
    }

    private double limitValueBetween(double value,
                                     double minValue,
                                     double maxValue) {
        return Math.max(minValue, Math.min(maxValue, value));
    }

    private Point2D interpolatePointBetween(Point2D point0,
                                            Point2D point1,
                                            double factor) {
        double x = interpolateValuesBetween(point0.getX(), point1.getX(), factor);
        double y = interpolateValuesBetween(point0.getY(), point1.getY(), factor);
        return new Point2D(x, y);
    }

    private double interpolateValuesBetween(double value0,
                                            double value1,
                                            double factor) {
        return value0 + ((value1 - value0) * factor);
    }

}
