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
package org.loadui.testfx.robots.impl;

import java.util.List;
import javafx.geometry.Point2D;

import com.google.common.collect.Lists;
import org.loadui.testfx.robots.MoveRobot;
import org.loadui.testfx.robots.ScreenRobot;
import org.loadui.testfx.service.query.PointQuery;

public class MoveRobotImpl implements MoveRobot {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final long MIN_POINTS = 1;
    private static final long MAX_POINTS = 200;

    private static final long SLEEP_DURATION = 1;

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public ScreenRobot screenRobot;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public MoveRobotImpl(ScreenRobot screenRobot) {
        this.screenRobot = screenRobot;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void moveTo(PointQuery pointQuery) {
        // Since moving takes time, only do it if we're not already at the desired point.
        Point2D sourcePoint = retrieveMouseLocation();
        Point2D targetPoint = pointQuery.query();
        if (!isPointAtMouseLocation(targetPoint)) {
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

    private boolean isPointAtMouseLocation(Point2D point) {
        return retrieveMouseLocation() == point;
    }

    private Point2D retrieveMouseLocation() {
        return screenRobot.retrieveMouse();
    }

    private void moveMouseDirectlyTo(Point2D targetPoint) {
        screenRobot.moveMouse(targetPoint);
        screenRobot.awaitEvents();
    }

    private void moveMouseStepwiseBetween(Point2D sourcePoint,
                                          Point2D targetPoint) {
        double pointDistance = calculateDistanceBetween(sourcePoint, targetPoint);
        int maxPointOffset = (int) limitValueBetween(pointDistance, MIN_POINTS, MAX_POINTS);
        List<Point2D> points = interpolatePointsBetween(sourcePoint, targetPoint, maxPointOffset);
        for (Point2D point : points) {
            screenRobot.moveMouse(point);
            try {
                Thread.sleep(SLEEP_DURATION);
            }
            catch (InterruptedException ignore) {
                return;
            }
        }
        screenRobot.awaitEvents();
    }

    private List<Point2D> interpolatePointsBetween(Point2D sourcePoint,
                                                   Point2D targetPoint,
                                                   int maxPointOffset) {
        List<Point2D> points = Lists.newArrayList();
        for (int pointOffset = 0; pointOffset <= maxPointOffset; pointOffset++) {
            double factor = (double) pointOffset / (double) maxPointOffset;
            Point2D point = interpolatePointBetween(sourcePoint, targetPoint, factor);
            points.add(point);
        }
        return points;
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
