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
package com.eviware.loadui.ui.fx.util.test;

import static com.eviware.loadui.ui.fx.util.test.GuiTest.find;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.SceneBuilder;
import javafx.scene.control.*;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;

import javafx.util.Duration;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.eviware.loadui.test.categories.GUITest;
import com.google.common.util.concurrent.SettableFuture;

@Category( GUITest.class )
public class ControllerApiTest
{
	private static final GuiTest controller = GuiTest.wrap( new FXScreenController() );
	private static final SettableFuture<Stage> stageFuture = SettableFuture.create();
	private static Stage stage;

	public static class ControllerApiTestApp extends Application
	{
		@Override
		public void start( Stage primaryStage ) throws Exception
		{
			primaryStage.setScene( SceneBuilder
					.create().width( 500 )
					.root(
							VBoxBuilder
									.create()
									.children( ButtonBuilder.create().id( "button1" ).text( "Button 1" ).build(),
											ButtonBuilder.create().id( "button2" ).text( "Button 2" ).build(),
											TextFieldBuilder.create().id( "text" ).build() ).build() ).build() );
			primaryStage.show();

			stageFuture.set( primaryStage );
		}
	}

	@BeforeClass
	public static void createWindow() throws Throwable
	{
		FXTestUtils.launchApp( ControllerApiTestApp.class );
		stage = stageFuture.get( 5, TimeUnit.SECONDS );
		FXTestUtils.bringToFront( stage );
		GuiTest.targetWindow( stage );
	}

	@Test
	public void shouldTypeString()
	{
		String text = "H3llo W0rld.";

		TextField textField = find( "#text" );
		controller.type( "#text", text );

		assertThat( textField.getText(), is( text ) );
	}

	@Test
	public void shouldClickNodes_whenGivenCSS()
	{
		final AtomicInteger counter = setupCountingButtons();

		controller.click( "#button1" ).click( "#button2" ).click( "#button1" );

		assertThat( counter.get(), is( 3 ) );
	}

	@Test
	public void shouldClickNodes_whenGivenLabel()
	{
		final AtomicInteger counter = setupCountingButtons();

		controller.click( "Button 1" ).click( "Button 2" ).click( "Button 1" );

		assertThat( counter.get(), is( 3 ) );
	}

	private AtomicInteger setupCountingButtons()
	{
		final AtomicInteger counter = new AtomicInteger();
		Button button1 = find( "#button1" );
		button1.setOnAction( new EventHandler<ActionEvent>()
		{
			@Override
			public void handle( ActionEvent _ )
			{
				counter.incrementAndGet();
			}
		} );

		Button button2 = find( "#button2" );
		button2.setOnAction( new EventHandler<ActionEvent>()
		{
			@Override
			public void handle( ActionEvent _ )
			{
				counter.set( counter.get() * 2 );
			}
		} );
		return counter;
	}

	@Test
	public void shouldHandleFastClicks() throws Exception
	{
		final AtomicInteger counter = setupCountingButtons();
		for( int i=1; i<30; i++ )
		{
			controller.click( "#button1" );
			assertEquals( i, counter.get() );
		}
	}

	@Test
	public void shouldHandleTimelines() throws Exception
	{
		setupTimelineButton();
		Button button1 = find( "#button1" );
		Button button2 = find( "#button2" );
		controller.click( "#button1" );
		long startTime = System.currentTimeMillis();
		controller.click( "#button2" );
		for( int i=0; i<10; i++ )
		{
			long elapsedTime = System.currentTimeMillis() - startTime;
			assertEquals( elapsedTime/5, button1.getTranslateX(), 15);
			controller.sleep( 20 );
		}
		controller.move( 0,0 ).click( "#button2" );
		for( int i=0; i<20; i++ )
		{
			long elapsedTime = System.currentTimeMillis() - startTime;
			assertEquals( elapsedTime/5, Double.parseDouble( button2.getText()), 15);
			controller.sleep( 20 );
		}
	}

	private void setupTimelineButton()
	{
		final Button button1 = find( "#button1" );

		final Timeline timeline = new Timeline();
		timeline.setCycleCount( 1 );
		final KeyValue kv = new KeyValue( button1.translateXProperty(), 500);
		final KeyFrame kf = new KeyFrame( Duration.millis( 2500 ), kv);
		timeline.getKeyFrames().add( kf );

		button1.setOnAction( new EventHandler<ActionEvent>()
		{
			@Override
			public void handle( ActionEvent _ )
			{
				timeline.play();
			}
		} );

		final Button button2 = find( "#button2" );
		button2.setMinWidth( 140 );
		Platform.runLater( new Runnable()
		{
			@Override
			public void run()
			{
				button2.textProperty().bind( button1.translateXProperty().asString() );
			}
		} );
	}
}
