package org.loadui.testfx.utils;

import java.awt.*;
import java.awt.event.InputEvent;

import static org.loadui.testfx.utils.FXTestUtils.releaseButtons;

public class UserInputDetector implements Runnable {

    public final static UserInputDetector instance = new UserInputDetector();
    private volatile boolean wasTilted = false;
    private volatile Thread testThread;
    private volatile Point lastPoint = null;

    static {
        Thread t = new Thread(instance);
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                Point currentPoint = MouseInfo.getPointerInfo().getLocation();
                if (lastPoint != null) {
                    assertPointsAreEqual(currentPoint, lastPoint);
                }
                lastPoint = currentPoint;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }

    public void assertPointsAreEqual(Point p1, Point p2) {
        if (! equals(p1, p2, 3)) {
            userInputDetected();
        }
    }

    private static boolean equals(Point p1, Point p2, int errorMargin) {
        return Math.abs(p2.getX() - p1.getX()) < errorMargin
            && Math.abs(p2.getY() - p1.getY()) < errorMargin;
    }

    public synchronized void reset() {
        lastPoint = null;
    }

    private void userInputDetected() {
        System.out.println("[TestFX] User mouse movement detected. Aborting test.");
        wasTilted = true;
        testThread.stop();
        releaseButtons();
    }

    public void setTestThread(Thread testThread) {
        this.testThread = testThread;
    }

    public boolean hasDetectedUserInput() {
        return wasTilted;
    }
}
