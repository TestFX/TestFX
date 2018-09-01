package org.testfx.robot.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import org.testfx.robot.BaseRobot;

public class DelayedRobot implements BaseRobot {

    public static ScheduledExecutorService sExe = Executors.newScheduledThreadPool(0);

    final BaseRobot robot;
    long delay = 500;

    public DelayedRobot(BaseRobot peer, long delay) {
        robot = peer;
        this.delay = delay;
    }

    @Override
    public void pressKeyboard(KeyCode key) {
        delayedCall(() -> robot.pressKeyboard(key));
    }

    @Override
    public void releaseKeyboard(KeyCode key) {
        delayedCall(() -> robot.releaseKeyboard(key));
    }

    @Override
    public void typeKeyboard(Scene scene, KeyCode key, String character) {
        delayedCall(() -> robot.typeKeyboard(scene, key, character));
    }

    @Override
    public Point2D retrieveMouse() {
        return robot.retrieveMouse();
    }

    @Override
    public void moveMouse(Point2D point) {
        delayedCall(() -> robot.moveMouse(point));
    }

    @Override
    public void scrollMouse(int amount) {
        delayedCall(() -> robot.scrollMouse(amount));
    }

    @Override
    public void pressMouse(MouseButton button) {
        delayedCall(() -> robot.pressMouse(button));
    }

    @Override
    public void releaseMouse(MouseButton button) {
        delayedCall(() -> robot.releaseMouse(button));
    }

    @Override
    public Image captureRegion(Rectangle2D region) {
        return robot.captureRegion(region);
    }

    protected void delayedCall(Runnable f) {
        sExe.schedule(f, delay, TimeUnit.MILLISECONDS);
    }

}
