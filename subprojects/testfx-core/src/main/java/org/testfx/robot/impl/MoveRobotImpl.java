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
package org.testfx.robot.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.geometry.Point2D;

import org.testfx.robot.BaseRobot;
import org.testfx.robot.Motion;
import org.testfx.robot.MouseRobot;
import org.testfx.robot.MoveRobot;
import org.testfx.robot.SleepRobot;
import org.testfx.service.query.PointQuery;

public class MoveRobotImpl implements MoveRobot {

    private static final long SLEEP_AFTER_MOVEMENT_STEP_IN_MILLIS = 1;
    private static final long MIN_POINT_OFFSET_COUNT = 1;
    private static final long MAX_POINT_OFFSET_COUNT;

    static {
        int maxOffsetCount;
        try {
            maxOffsetCount = Integer.getInteger("testfx.robot.move_max_count", 200);
        }
        catch (NumberFormatException e) {
            System.err.println("\"testfx.robot.move_max_count\" property must be a number but was: \"" +
                    System.getProperty("testfx.robot.move_max_count") + "\".\nUsing default of \"200\".");
            e.printStackTrace();
            maxOffsetCount = 200;
        }
        MAX_POINT_OFFSET_COUNT = maxOffsetCount;
    }
    
    private final BaseRobot baseRobot;
    private final MouseRobot mouseRobot;
    private final SleepRobot sleepRobot;

    public MoveRobotImpl(BaseRobot baseRobot, MouseRobot mouseRobot, SleepRobot sleepRobot) {
        this.baseRobot = baseRobot;
        this.mouseRobot = mouseRobot;
        this.sleepRobot = sleepRobot;
    }

    @Override
    public void moveTo(PointQuery pointQuery, Motion motion) {
        // Since moving takes time, only do it if we're not already at the desired point.
        Point2D sourcePoint = baseRobot.retrieveMouse();

        if (motion == Motion.DEFAULT && pointQuery.queryMotion().isPresent()) {
            // The user explicitly requested a non-default type of motion, so honor it.
            motion = pointQuery.queryMotion().get();
        }
        Point2D targetPoint = pointQuery.query();
        if (sourcePoint != targetPoint) {
            moveMouseStepwiseBetween(sourcePoint, targetPoint, motion);
        }

        // If the target has moved while we were moving the mouse, update to the new position.
        Point2D finalPoint = pointQuery.query();
        mouseRobot.move(finalPoint);
    }

    @Override
    public void moveBy(double x, double y, Motion motion) {

        Point2D sourcePoint = baseRobot.retrieveMouse();
        Point2D targetPoint = new Point2D(sourcePoint.getX() + x, sourcePoint.getY() + y);
        moveMouseStepwiseBetween(sourcePoint, targetPoint, motion);
    }

    private void moveMouseStepwiseBetween(Point2D sourcePoint,
                                          Point2D targetPoint,
                                          Motion motion) {
        if (motion == Motion.DEFAULT) {
            motion = Motion.DIRECT;
        }
        double directDistance = sourcePoint.distance(targetPoint);
        double horizontalDistance = sourcePoint.distance(targetPoint.getX(), sourcePoint.getY());
        double verticalDistance = sourcePoint.distance(sourcePoint.getX(), targetPoint.getY());

        int totalStepsCount = (int) limitValueBetween(
                directDistance, MIN_POINT_OFFSET_COUNT, MAX_POINT_OFFSET_COUNT
        );

        List<Point2D> path = new ArrayList<>(totalStepsCount);
        double percentHorizontal = horizontalDistance / (horizontalDistance +
                verticalDistance);
        int horizontalStepsCount = (int) (totalStepsCount * percentHorizontal);
        int verticalStepsCount = totalStepsCount - horizontalStepsCount;
        switch (motion) {
            case DIRECT:
                path = interpolatePointsBetween(sourcePoint, targetPoint, totalStepsCount);
                break;
            case HORIZONTAL_FIRST: {
                Point2D intermediate = new Point2D(targetPoint.getX(), sourcePoint.getY());
                path = Stream.concat(
                        interpolatePointsBetween(
                                sourcePoint, intermediate, horizontalStepsCount).stream(),
                        interpolatePointsBetween(
                                intermediate, targetPoint, verticalStepsCount).stream())
                        .collect(Collectors.toList());
                break;
            }
            case VERTICAL_FIRST: {
                Point2D intermediate = new Point2D(sourcePoint.getX(), targetPoint.getY());
                path = Stream.concat(
                        interpolatePointsBetween(
                                sourcePoint, intermediate, verticalStepsCount).stream(),
                        interpolatePointsBetween(
                                intermediate, targetPoint, horizontalStepsCount).stream())
                        .collect(Collectors.toList());
                break;
            }
        }

        for (Point2D point : path.stream().limit(path.size() - 1).collect(Collectors.toList())) {
            // TODO(mike): Why is the limiting necessary?
            mouseRobot.moveNoWait(point);
            sleepRobot.sleep(SLEEP_AFTER_MOVEMENT_STEP_IN_MILLIS);
        }
        mouseRobot.move(targetPoint);
    }

    private List<Point2D> interpolatePointsBetween(Point2D sourcePoint,
                                                   Point2D targetPoint,
                                                   int pointOffsetCount) {
        List<Point2D> points = new ArrayList<>();
        for (int pointOffset = 1; pointOffset <= pointOffsetCount; pointOffset++) {
            double factor = (double) pointOffset / (double) pointOffsetCount;
            Point2D point = interpolatePointBetween(sourcePoint, targetPoint, factor);
            points.add(point);
        }
        return Collections.unmodifiableList(points);
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
