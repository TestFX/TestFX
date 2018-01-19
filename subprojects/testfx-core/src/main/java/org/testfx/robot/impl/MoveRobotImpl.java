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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.geometry.Point2D;

import org.testfx.api.annotation.Unstable;
import org.testfx.robot.BaseRobot;
import org.testfx.robot.Motion;
import org.testfx.robot.MouseRobot;
import org.testfx.robot.MoveRobot;
import org.testfx.robot.SleepRobot;
import org.testfx.service.query.PointQuery;

@Unstable
public class MoveRobotImpl implements MoveRobot {

    private static final long SLEEP_AFTER_MOVEMENT_STEP_IN_MILLIS = 1;

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
        final int discreteSteps = limitValueBetween((int) sourcePoint.distance(targetPoint), 1, 200);

        List<Point2D> path = new ArrayList<>(discreteSteps);
        switch (motion) {
            case DIRECT:
                path = interpolatePointsBetween(sourcePoint, targetPoint, discreteSteps);
                break;
            case HORIZONTAL_FIRST: {
                // ratio of the horizontal to the vertical side of the right-triangle (determines how much time should
                // be spent traversing in each direction)
                double horizontalDistance = sourcePoint.distance(targetPoint.getX(), sourcePoint.getY());
                double verticalDistance = sourcePoint.distance(sourcePoint.getX(), targetPoint.getY());
                double percentHorizontal = horizontalDistance / (horizontalDistance + verticalDistance);
                // the point where the horizontal path stops and the vertical path starts
                Point2D intermediate = new Point2D(targetPoint.getX(), sourcePoint.getY());
                path = Stream.concat(
                        interpolatePointsBetween(
                                sourcePoint, intermediate, (int) (discreteSteps * percentHorizontal)).stream(),
                        interpolatePointsBetween(
                                intermediate, targetPoint, (int) (discreteSteps * (1 - percentHorizontal))).stream())
                        .collect(Collectors.toList());
                break;
            }
            case VERTICAL_FIRST: {
                // ratio of the vertical to the horizontal side of the right-triangle
                double horizontalDistance = sourcePoint.distance(targetPoint.getX(), sourcePoint.getY());
                double verticalDistance = sourcePoint.distance(sourcePoint.getX(), targetPoint.getY());
                double percentVertical = verticalDistance / (horizontalDistance + verticalDistance);
                // the point where the vertical path stops and the horizontal path starts
                Point2D intermediate = new Point2D(sourcePoint.getX(), targetPoint.getY());
                path = Stream.concat(
                        interpolatePointsBetween(
                                sourcePoint, intermediate, (int) (discreteSteps * percentVertical)).stream(),
                        interpolatePointsBetween(
                                intermediate, targetPoint, (int) (discreteSteps * (1 - percentVertical))).stream())
                        .collect(Collectors.toList());
                break;
            }
        }

        // Remove the last (final) point of the path, which is the target point, because we explicitly call
        // mouseRobot.move(targetPoint) after looping through the points of the path.
        path.remove(path.size() - 1);
        for (Point2D point : path) {
            mouseRobot.moveNoWait(point);
            sleepRobot.sleep(SLEEP_AFTER_MOVEMENT_STEP_IN_MILLIS);
        }
        mouseRobot.move(targetPoint);
    }

    private List<Point2D> interpolatePointsBetween(Point2D sourcePoint,
                                                   Point2D targetPoint,
                                                   int discreteSteps) {
        if (discreteSteps <= 0) {
            throw new IllegalArgumentException("discreteSteps must be strictly greater (>) " +
                    "than 0 but was: " + discreteSteps);
        }
        List<Point2D> path = new ArrayList<>(discreteSteps);
        for (int pointOffset = 0; pointOffset <= discreteSteps; pointOffset++) {
            double factor = (double) pointOffset / (double) discreteSteps;
            Point2D point = interpolatePointBetween(sourcePoint, targetPoint, factor);
            path.add(point);
        }
        return path;
    }

    private int limitValueBetween(int value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException(String.format(
                    "min (%s) must be less than or equal to (>=) max (%s)", min, max));
        }
        return Math.min(Math.max(value, min), max);
    }

    private Point2D interpolatePointBetween(Point2D point0, Point2D point1, double factor) {
        double x = lerp(point0.getX(), point1.getX(), factor);
        double y = lerp(point0.getY(), point1.getY(), factor);
        return new Point2D(x, y);
    }

    private double lerp(double value0, double value1, double factor) {
        return value0 + ((value1 - value0) * factor);
    }

}
