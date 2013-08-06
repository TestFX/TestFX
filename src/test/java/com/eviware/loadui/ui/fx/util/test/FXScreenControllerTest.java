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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.loadui.testfx.FXScreenController;
import org.loadui.testfx.FXTestUtils;
import org.loadui.testfx.categories.TestFX;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.SceneBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.common.util.concurrent.SettableFuture;

@Category( TestFX.class )
public class FXScreenControllerTest
{
	private static final SettableFuture<Stage> stageFuture = SettableFuture.create();
	private static final FXScreenController controller = new FXScreenController();
	private static Stage stage;

	public static class FXScreenControllerTestApp extends Application
	{
		@Override
		public void start( Stage primaryStage ) throws Exception
		{
			primaryStage.setScene( SceneBuilder
					.create()
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
		FXTestUtils.launchApp( FXScreenControllerTestApp.class );
		stage = stageFuture.get( 5, TimeUnit.SECONDS );
		FXTestUtils.bringToFront( stage );
	}

	@Before
	public void setup() throws Exception
	{
	}

	@Test
	public void shouldClickButtons() throws InterruptedException
	{
		Button button1 = ( Button )stage.getScene().lookup( "#button1" );
		Button button2 = ( Button )stage.getScene().lookup( "#button2" );

		final CountDownLatch pressed1 = new CountDownLatch( 1 );

		button1.setOnAction( new EventHandler<ActionEvent>()
		{
			@Override
			public void handle( ActionEvent event )
			{
				pressed1.countDown();
			}
		} );

		Bounds sceneBounds = button1.localToScene( button1.getBoundsInLocal() );
		controller.move( stage.getX() + stage.getScene().getX() + sceneBounds.getMinX() + 10, stage.getY()
				+ stage.getScene().getY() + sceneBounds.getMinY() + 10 );

		controller.press( MouseButton.PRIMARY );
		Thread.sleep( 100 );
		controller.release( MouseButton.PRIMARY );

		assertTrue( pressed1.await( 2, TimeUnit.SECONDS ) );

		final CountDownLatch pressed2 = new CountDownLatch( 3 );

		button2.setOnAction( new EventHandler<ActionEvent>()
		{
			@Override
			public void handle( ActionEvent event )
			{
				pressed2.countDown();
			}
		} );

		sceneBounds = button2.localToScene( button2.getBoundsInLocal() );
		controller.move( stage.getX() + stage.getScene().getX() + sceneBounds.getMinX() + 10, stage.getY()
				+ stage.getScene().getY() + sceneBounds.getMinY() + 10 );

		controller.press( MouseButton.PRIMARY );
		Thread.sleep( 100 );
		controller.release( MouseButton.PRIMARY );
		Thread.sleep( 100 );
		controller.press( MouseButton.PRIMARY );
		Thread.sleep( 100 );
		controller.release( MouseButton.PRIMARY );
		Thread.sleep( 100 );
		controller.press( MouseButton.PRIMARY );
		Thread.sleep( 100 );
		controller.release( MouseButton.PRIMARY );

		assertTrue( pressed2.await( 2, TimeUnit.SECONDS ) );
	}

	@Test
	public void shouldTypeKeys() throws InterruptedException
	{
		TextField textField = ( TextField )stage.getScene().lookup( "#text" );
		textField.requestFocus();
		FXTestUtils.awaitEvents();
		assertTrue( textField.isFocused() );

		controller.press( KeyCode.A );
		controller.release( KeyCode.A );
		Thread.sleep( 100 );
		controller.press( KeyCode.SHIFT );
		controller.press( KeyCode.B );
		controller.release( KeyCode.B );
		controller.release( KeyCode.SHIFT );
		Thread.sleep( 100 );
		controller.press( KeyCode.C );
		controller.release( KeyCode.C );
		Thread.sleep( 100 );

		FXTestUtils.awaitEvents();

		assertThat( textField.getText(), is( "aBc" ) );
	}
}
