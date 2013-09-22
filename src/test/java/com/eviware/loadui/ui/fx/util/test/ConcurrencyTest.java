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

import static org.loadui.testfx.Assertions.assertNodeExists;
import static org.loadui.testfx.GuiTest.find;
import static org.loadui.testfx.Matchers.hasLabel;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import org.loadui.testfx.FXScreenController;
import org.loadui.testfx.FXTestUtils;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;
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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.common.util.concurrent.SettableFuture;

@Ignore
@Category( TestFX.class )
public class ConcurrencyTest
{
	private static final GuiTest controller = GuiTest.wrap( new FXScreenController() );
	private static final SettableFuture<Stage> stageFuture = SettableFuture.create();
	private static Stage stage;

	public static class ConcurrencyTestApp extends Application
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
		FXTestUtils.launchApp( ConcurrencyTestApp.class );
		stage = stageFuture.get( 25, TimeUnit.SECONDS );
		FXTestUtils.bringToFront( stage );
		GuiTest.targetWindow( stage );
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
