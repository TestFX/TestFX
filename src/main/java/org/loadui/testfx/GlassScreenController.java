/*
 * Copyright 2013 SmartBear Software
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the Licence for the specific language governing permissions and limitations
 * under the Licence.
 */
package org.loadui.testfx;

import com.google.common.collect.ImmutableMap;
import com.sun.glass.ui.Robot;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class GlassScreenController implements ScreenController
{
	private static final Map<MouseButton, Integer> BUTTONS = ImmutableMap.of( MouseButton.PRIMARY,
			1, MouseButton.MIDDLE, InputEvent.BUTTON2_MASK, MouseButton.SECONDARY,
			InputEvent.BUTTON3_MASK );

	private final DoubleProperty mouseXProperty = new SimpleDoubleProperty();
	private final DoubleProperty mouseYProperty = new SimpleDoubleProperty();
	private Robot robot;
	private long moveTime = 200;

	public GlassScreenController()
	{
	}

    private Robot getRobot()
    {
        if( robot == null )
        {
            robot = com.sun.glass.ui.Application.GetApplication().createRobot();

            final ChangeListener<Number> mouseChangeListener = new ChangeListener<Number>()
            {
                @Override
                public void changed( ObservableValue<? extends Number> value, Number oldNum, Number newNum )
                {
						 Platform.runLater( new Runnable()
						 {
							 @Override
							 public void run()
							 {
								 robot.mouseMove( mouseXProperty.intValue(), mouseYProperty.intValue() );
							 }
						 } );
                }
            };

            mouseXProperty.addListener( mouseChangeListener );
            mouseYProperty.addListener( mouseChangeListener );
        }
        return robot;
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
		getRobot();

		Point currentMousePosition = MouseInfo.getPointerInfo().getLocation();
		mouseXProperty.set( currentMousePosition.getX() );
		mouseYProperty.set( currentMousePosition.getY() );

		final CountDownLatch done = new CountDownLatch( 1 );


				new Timeline( new KeyFrame( new Duration( moveTime ), new EventHandler<ActionEvent>()
				{
					@Override
					public void handle( ActionEvent arg0 )
					{
						done.countDown();
					}
				}, new KeyValue( mouseXProperty, x, Interpolator.EASE_BOTH ), new KeyValue( mouseYProperty, y,
						Interpolator.EASE_BOTH ) ) ).playFromStart();

		try
		{
			done.await();
			Thread.sleep( 100 );
		}
		catch( InterruptedException e )
		{
			throw new RuntimeException( e );
		}
	}

	@Override
	public void press( final MouseButton button ) {
		if( button == null )
		{
            return;
		}
        final CountDownLatch done = new CountDownLatch( 1 );
        Platform.runLater( new Runnable() {
            @Override
            public void run() {
                getRobot().mousePress(BUTTONS.get(button));
                done.countDown();
            }
        });
        try {
            done.await();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

	@Override
	public void release( final MouseButton button )
	{
		if( button == null )
		{
			return;
		}
        final CountDownLatch done = new CountDownLatch( 1 );
        Platform.runLater( new Runnable() {
            @Override
            public void run() {
                done.countDown();
                getRobot().mouseRelease(BUTTONS.get(button));
            }
        });
        try {
            done.await();
			  	Thread.sleep( 100 );
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

	@Override
	public void press( final KeyCode key )
	{
        final CountDownLatch done = new CountDownLatch( 1 );
        Platform.runLater( new Runnable() {
            @Override
            public void run() {
                done.countDown();
                getRobot().keyPress(key.impl_getCode());
            }
        });
        try {
            done.await();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

	@Override
	public void release( final KeyCode key )
	{
        final CountDownLatch done = new CountDownLatch( 1 );
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                done.countDown();
                getRobot().keyRelease(key.impl_getCode());
            }
        });
        try {
            done.await();
			   Thread.sleep( 100 );
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

	@Override
	public void scroll( final int amount )
	{
        final CountDownLatch done = new CountDownLatch( 1 );
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                done.countDown();
                getRobot().mouseWheel(amount);
            }
        });
        try {
            done.await();
			   Thread.sleep( 50 );
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
	}
}
