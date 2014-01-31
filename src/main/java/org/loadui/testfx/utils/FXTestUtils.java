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
package org.loadui.testfx.utils;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.fail;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.google.common.util.concurrent.SettableFuture;

import org.loadui.testfx.GuiTest;

public class FXTestUtils
{

    public static final int TIMEOUT = 5;

    public static void bringToFront( final Stage stage ) throws Exception
	{
		invokeAndWait( new Runnable()
		{
			@Override
			public void run()
			{
				//stage.setIconified( true );
				//stage.setIconified( false );
				stage.toBack();
				stage.toFront();
			}
		}, 5 );
		Thread.sleep( 250 );
	}

	/**
	 * Attempts to wait for events in the JavaFX event thread to complete, as
	 * well as any new events triggered by them.
	 */
	public static void awaitEvents()
	{
		try
		{
			for( int i = 0; i < 5; i++ )
			{
				final Semaphore sem = new Semaphore( 0 );
				Platform.runLater( new Runnable()
				{
					@Override
					public void run()
					{
						sem.release();
					}
				} );

                boolean wasAcquired = sem.tryAcquire(TIMEOUT, SECONDS);
                if(!wasAcquired)
                {
                    System.out.println("[TestFX] WARNING: GUI locked for more than "+ TIMEOUT +" seconds - timing out!");
                    break;
                }
                try
				{
					Thread.sleep( 10 );
				}
				catch( InterruptedException e )
				{
				}
			}
		}
		catch( Throwable e )
		{
			throw new RuntimeException( e );
		}
	}

	/**
	 * Runs the given Callable in the JavaFX thread, waiting for it to complete
	 * before returning. Also attempts to wait for any other JavaFX events that
	 * may have been queued in the Callable to complete. If any Exception is
	 * thrown during execution of the Callable, that exception will be re-thrown
	 * from invokeAndWait.
	 * 
	 * @param task
	 * @param timeoutInSeconds
	 * @throws Throwable
	 */
	public static void invokeAndWait( final Callable<?> task, int timeoutInSeconds ) throws Exception
	{
		final SettableFuture<Void> future = SettableFuture.create();

		Platform.runLater( new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					task.call();
					future.set( null );
				}
				catch( Throwable e )
				{
					future.setException( e );
				}
			}
		} );

		try
		{
			future.get( timeoutInSeconds, SECONDS );
			awaitEvents();
		}
		catch( ExecutionException e )
		{
			if( e.getCause() instanceof Exception )
			{
				throw ( Exception )e.getCause();
			}
			else
			{
				throw e;
			}
		}
	}

	/**
	 * @see invokeAndWait(Runnable, int)
	 * 
	 * @param task
	 * @param timeoutInSeconds
	 * @throws Throwable
	 */
	public static void invokeAndWait( final Runnable task, int timeoutInSeconds ) throws Exception
	{
		invokeAndWait( new Callable<Void>()
		{
			@Override
			public Void call() throws Exception
			{
				task.run();

				return null;
			}
		}, timeoutInSeconds );
	}

	/**
	 * Launches a JavaFX App in a new Thread.
	 * 
	 * @param appClass
	 * @param args
	 */
	public static void launchApp( final Class<? extends Application> appClass, final String... args )
	{
		new Thread( new Runnable()
		{
			@Override
			public void run()
			{
				Application.launch( appClass, args );
			}
		} ).start();
	}

	public static void printGraph( Node root )
	{
		printGraph( root, "" );
	}
	
	public static void failIfExists( String selector )
	{
		try
		{
			GuiTest.find(selector);
			fail( "Selector shouldn't have found anything: " + selector );
		}
		catch( Exception e )
		{
			// expected
		}
	}

	public static <T extends Node> T getOrFail( String selector )
	{
		try
		{
			return GuiTest.find( selector );
		}
		catch( Exception e )
		{
			fail( "Cannot find anything with selector: " + selector );
			return null;
		}
	}

	private static void printGraph( Node root, String indent )
	{
		System.out.println( indent + root );
		if( root instanceof Parent )
		{
			indent += "  ";
			for( Node child : ( ( Parent )root ).getChildrenUnmodifiable() )
			{
				printGraph( child, indent );
			}
		}
	}

    public static final Predicate<Node> isVisible = new Predicate<Node>(){
        @Override
        public boolean apply(javafx.scene.Node node) {
            return isNodeVisible(node);
        }
    };

    @SuppressWarnings("deprecation")
    public static boolean isNodeVisible(Node node)
    {
        if(!node.isVisible() || !node.impl_isTreeVisible())
            return false;
        return isNodeWithinSceneBounds(node);
    }

    public static boolean isNodeWithinSceneBounds(Node node)
    {
        Scene scene = node.getScene();
        Bounds nodeBounds = node.localToScene( node.getBoundsInLocal() );
        return nodeBounds.intersects( 0, 0, scene.getWidth(), scene.getHeight() );
    }

    public static Bounds intersection(Bounds b1, Bounds b2)
    {
        Rectangle r1 = toRectangle(b1);
        Rectangle r2 = toRectangle(b2);
        Rectangle i = r1.intersection(r2);
        return new BoundingBox(i.getMinX(), i.getMinY(), i.getWidth(), i.getHeight());
    }

    private static Rectangle toRectangle(Bounds b1) {
        return new Rectangle((int)b1.getMinX(), (int)b1.getMinY(), (int)b1.getWidth(), (int)b1.getHeight());
    }

    public static <T extends Node> Set<T> flattenSets(Iterable<Set<T>> found) {
        ImmutableSet.Builder<T> sb = ImmutableSet.builder();
        for( Set<T> set : found )
            sb.addAll(set);
        return sb.build();
    }

    public static void releaseButtons()
    {
        try {
            Robot robot = new Robot();
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        } catch (AWTException e) {
            System.out.println("[TestFX] Failed releasing keys.");
            e.printStackTrace();
        }
    }
}
