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

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

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

import com.google.common.collect.ImmutableMap;

public class FXScreenController implements ScreenController
{
	private static final Map<MouseButton, Integer> BUTTONS = ImmutableMap.of( MouseButton.PRIMARY,
			InputEvent.BUTTON1_MASK, MouseButton.MIDDLE, InputEvent.BUTTON2_MASK, MouseButton.SECONDARY,
			InputEvent.BUTTON3_MASK );

	private final DoubleProperty mouseXProperty = new SimpleDoubleProperty();
	private final DoubleProperty mouseYProperty = new SimpleDoubleProperty();
	private final Robot robot;
	private long moveTime = 500;

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
		final CountDownLatch done = new CountDownLatch( 1 );

		Platform.runLater( new Runnable()
		{
			@Override
			public void run()
			{
				new Timeline( new KeyFrame( new Duration( moveTime ), new EventHandler<ActionEvent>()
				{
					@Override
					public void handle( ActionEvent arg0 )
					{
						done.countDown();
					}
				}, new KeyValue( mouseXProperty, x, Interpolator.EASE_BOTH ), new KeyValue( mouseYProperty, y,
						Interpolator.EASE_BOTH ) ) ).playFromStart();
			}
		} );

		try
		{
			done.await();
			FXTestUtils.awaitEvents();
		}
		catch( InterruptedException e )
		{
			throw new RuntimeException( e );
		}
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
