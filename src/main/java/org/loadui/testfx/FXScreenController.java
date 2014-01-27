package org.loadui.testfx;

import com.google.common.collect.ImmutableMap;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import org.loadui.testfx.utils.FXTestUtils;
import org.loadui.testfx.utils.UserInputDetector;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Map;

public class FXScreenController implements ScreenController
{
    private static final Map<MouseButton, Integer> BUTTONS = ImmutableMap.of( MouseButton.PRIMARY,
            InputEvent.BUTTON1_MASK, MouseButton.MIDDLE, InputEvent.BUTTON2_MASK, MouseButton.SECONDARY,
            InputEvent.BUTTON3_MASK );

    private final Robot robot;
    private long moveTime = 175;

    public FXScreenController()
    {
        try
        {
            robot = new Robot();
        }
        catch( AWTException e )
        {
            throw new IllegalArgumentException( e );
        }

    }

    @Override
    public Point2D getMouse()
    {
        Point awtPoint = MouseInfo.getPointerInfo().getLocation();
        return new Point2D( awtPoint.getX(), awtPoint.getY() );
    }

    @Override
    public void position( double x, double y )
    {
        robot.mouseMove((int) x, (int) y);
    }

    @Override
    public void move( double x, double y )
    {
        // Calculate how far we need to go
        Point position = MouseInfo.getPointerInfo().getLocation();
        double distanceX = x - position.getX();
        double distanceY = y - position.getY();
        double distance = Math.sqrt( Math.pow(distanceX, 2) + Math.pow(distanceY, 2) );
        
        // The maximum time for the movement is "moveTime". Far movements will make the cursor go faster.
        // In order to be not too slow on small distances, the minimum speed is 1 pixel per millisecond.
        double totalTime = moveTime;
        if (distance < totalTime) {
            totalTime = Math.max(1, distance);
        }

        double speedX = distanceX / totalTime;
        double speedY = distanceY / totalTime;
        for (int time = 0; time < totalTime; time++) {

            int newX = position.x + (int) (speedX * time);
            int newY = position.y + (int) (speedY * time);
            robot.mouseMove(newX, newY);

            try
            {
                Thread.sleep( 1 );
            }
            catch( InterruptedException e )
            {
                return;
            }

            Point currentCursorLocation = MouseInfo.getPointerInfo().getLocation();
            Point expectedCursorLocation = new Point(newX, newY);
            UserInputDetector.instance.assertPointsAreEqual(currentCursorLocation, expectedCursorLocation);

        }

        // We should be less than one step away from the target
        // => Make one last step to hit it.
        robot.mouseMove((int) x, (int) y);
        FXTestUtils.awaitEvents();
    }

    @Override
    public void press( MouseButton button )
    {
        if( button == null )
        {
            return;
        }
        robot.mousePress( BUTTONS.get( button ) );
        FXTestUtils.awaitEvents();
    }

    @Override
    public void release( MouseButton button )
    {
        if( button == null )
        {
            return;
        }
        robot.mouseRelease( BUTTONS.get( button ) );
        FXTestUtils.awaitEvents();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void press( KeyCode key )
    {
        robot.keyPress( key.impl_getCode() );
        FXTestUtils.awaitEvents();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void release( KeyCode key )
    {
        robot.keyRelease( key.impl_getCode() );
        FXTestUtils.awaitEvents();
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void pressNoWait( KeyCode key )
    {
        robot.keyPress( key.impl_getCode() );
    }

    @SuppressWarnings("deprecation")
    @Override
    public void releaseNoWait( KeyCode key )
    {
        robot.keyRelease( key.impl_getCode() );
    }
    
    @Override
    public void scroll( int amount )
    {
        robot.mouseWheel( amount );
        FXTestUtils.awaitEvents();
    }
}
