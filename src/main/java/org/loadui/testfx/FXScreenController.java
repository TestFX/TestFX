package org.loadui.testfx;

import com.google.common.collect.ImmutableMap;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.loadui.testfx.utils.FXTestUtils;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Map;

public class FXScreenController implements ScreenController
{
    private static final Map<MouseButton, Integer> BUTTONS = ImmutableMap.of( MouseButton.PRIMARY,
            InputEvent.BUTTON1_MASK, MouseButton.MIDDLE, InputEvent.BUTTON2_MASK, MouseButton.SECONDARY,
            InputEvent.BUTTON3_MASK );

    private final DoubleProperty mouseXProperty = new SimpleDoubleProperty();
    private final DoubleProperty mouseYProperty = new SimpleDoubleProperty();
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

        final ChangeListener<Number> mouseChangeListener = new ChangeListener<Number>()
        {
            @Override
            public void changed( ObservableValue<? extends Number> value, Number oldNum, Number newNum )
            {
                robot.mouseMove( mouseXProperty.intValue(), mouseYProperty.intValue() );
                System.out.println("!");
            }
        };

        mouseXProperty.addListener( mouseChangeListener );
        mouseYProperty.addListener( mouseChangeListener );
    }

    @Override
    public Point2D getMouse()
    {
        return new Point2D( mouseXProperty.doubleValue(), mouseYProperty.doubleValue() );
    }

    @Override
    public void position( double x, double y )
    {
        mouseXProperty.set( x );
        mouseYProperty.set( y );
    }

    @Override
    public void move( final double x, final double y )
    {
        Point currentMousePosition = MouseInfo.getPointerInfo().getLocation();
        mouseXProperty.set( currentMousePosition.getX() );
        mouseYProperty.set( currentMousePosition.getY() );

        boolean reachedTarget = false;
        while(!reachedTarget)
        {
            currentMousePosition = MouseInfo.getPointerInfo().getLocation();

            int directionX = Integer.compare( (int)x, (int)currentMousePosition.getX() );
            int directionY = Integer.compare( (int)y, (int)currentMousePosition.getY() );

            if(directionX == 0 && directionY == 0)
                reachedTarget = true;
            else
            {
                robot.mouseMove( (int)currentMousePosition.getX() + directionX, (int)currentMousePosition.getY() + directionY );
            }
            try
            {
                Thread.sleep( 1 );
            }
            catch( InterruptedException e )
            {
                e.printStackTrace();
            }
        }

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

    @Override
    public void press( KeyCode key )
    {
        robot.keyPress( key.impl_getCode() );
        FXTestUtils.awaitEvents();
    }

    @Override
    public void release( KeyCode key )
    {
        robot.keyRelease( key.impl_getCode() );
        FXTestUtils.awaitEvents();
    }

    @Override
    public void scroll( int amount )
    {
        robot.mouseWheel( amount );
        FXTestUtils.awaitEvents();
    }
}
