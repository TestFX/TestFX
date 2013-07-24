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

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.*;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Labeled;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.transform;

public class GuiTest
{
	@Deprecated
	public static GuiTest wrap( ScreenController controller )
	{
		return new GuiTest();
	}

	private static Window lastSeenWindow = null;

	public static <T extends Window> T targetWindow( T window )
	{
		if( window instanceof Stage )
		{
			Stage stage = (Stage) window;
			stage.toFront();
		}
		lastSeenWindow = window;

		return window;
	}

	public static Object offset( Object target, double offsetX, double offsetY )
	{
		return new OffsetTarget( target, offsetX, offsetY );
	}

	@SuppressWarnings("deprecation")
	public static List<Window> getWindows()
	{
		return Lists.reverse( Lists.newArrayList( Window.impl_getWindows() ) );
	}

	public static Window getWindowByIndex( int index )
	{
		return getWindows().get( index );
	}

	public static Stage findStageByTitle( final String titleRegex )
	{
		return Iterables.find( Iterables.filter( getWindows(), Stage.class ), new Predicate<Stage>()
		{
			@Override
			public boolean apply( Stage input )
			{
				return input.getTitle().matches( titleRegex );
			}
		} );
	}

	public static Set<Node> findAll( String selector, Object parent )
	{
		try
		{
			if( parent instanceof String )
			{
				final String titleRegex = (String) parent;
				return findAll( selector, targetWindow( findStageByTitle( titleRegex ) ).getScene() );
			} else if( parent instanceof Node )
			{
				Node node = (Node) parent;
				targetWindow( node.getScene().getWindow() );
				return node.lookupAll( selector );
			} else if( parent instanceof Scene )
			{
				Scene scene = (Scene) parent;
				targetWindow( scene.getWindow() );
				return findAll( selector, scene.getRoot() );
			} else if( parent instanceof Window )
			{
				return findAll( selector, targetWindow( (Window) parent ).getScene() );
			}
		} catch( Exception e )
		{
			//Ignore, something went wrong with checking a window, so return an empty set.
		}

		return Collections.emptySet();
	}

	public static Set<Node> findAll( String selector )
	{
		Set<Node> results = Sets.newLinkedHashSet();
		results.addAll( findAll( selector, lastSeenWindow ) );
		final Predicate<Window> isDescendant = new Predicate<Window>()
		{
			@Override
			public boolean apply( Window input )
			{
				Window parent = null;
				if( input instanceof Stage )
				{
					parent = ((Stage) input).getOwner();
				} else if( input instanceof PopupWindow )
				{
					parent = ((PopupWindow) input).getOwnerWindow();
				}

				return parent == lastSeenWindow || parent != null && apply( parent );
			}
		};
		Iterable<Window> descendants = Iterables.filter( getWindows(), isDescendant );
		Iterable<Window> rest = Iterables.filter( getWindows(), Predicates.not( isDescendant ) );
		for( Window descendant : concat( descendants, rest ) )
		{
			results.addAll( findAll( selector, descendant ) );
		}

		return results;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Node> T find( String selector, Object parent )
	{
		return checkNotNull( (T) getFirst( findAll( selector, parent ), null ),
				"Query [%s] select [%s] resulted in no nodes found!", parent, selector );
	}

	@SuppressWarnings("unchecked")
	public static <T extends Node> T find( final String query )
	{
		T nodeFoundByCss = findByCssSelector( query );
		if( nodeFoundByCss != null )
			return nodeFoundByCss;

		T foundNode = (T) find( new HasLabel( query ) );
		if( foundNode == null )
		{
			throw new RuntimeException( "Query "+query+" resulted in no nodes found! Screenshot saved as " + captureScreenshot().getAbsolutePath() );
		}

		return foundNode;
	}

	private static File captureScreenshot()
	{
		File screenshot = new File( "/screenshot.png" );
		BufferedImage image = null;
		try
		{
			image = new Robot().createScreenCapture( new Rectangle( Toolkit.getDefaultToolkit().getScreenSize() ) );
			ImageIO.write( image, "png", new File( "/screenshot.png" ) );
		} catch( Exception e )
		{
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return screenshot;
	}

	private static class HasLabel<Node> implements Predicate<Node>
	{
		private final String label;

		HasLabel( String label )
		{
			this.label = label;
		}

		@Override
		public boolean apply( Node node )
		{
			return (node instanceof Labeled) && label.equals( ((Labeled) node).getText() );
		}
	};

	private static <T extends Node> T findByCssSelector( final String selector )
	{
		Set<Node> locallyFound = findAll( selector );
		Iterable<Node> globallyFound = concat( transform( getWindows(),
				new Function<Window, Iterable<Node>>()
				{
					@Override
					public Iterable<Node> apply( Window input )
					{
						return findAll( selector, input );
					}
				} ) );

		return (T) getFirst( locallyFound, getFirst( globallyFound, null ) );
	}

	@SuppressWarnings("unchecked")
	public static <T extends Node> T find( final Predicate<Node> predicate )
	{
		Iterable<Node> globallyFound = concat( transform( getWindows(),
				new Function<Window, Iterable<Node>>()
				{
					@Override
					public Iterable<Node> apply( Window input )
					{
						return findAll( predicate, input.getScene().getRoot() );
					}
				} ) );

		return (T) getFirst( globallyFound, null );
	}

	public static Iterable<Node> findAll( Predicate<Node> predicate, Node parent )
	{
		ImmutableList.Builder<Iterable<Node>> found = ImmutableList.builder();
		if( predicate.apply( parent ) )
		{
			found.add( Collections.singleton( parent ) );
		}
		if( parent instanceof Parent )
		{
			for( Node child : ((Parent) parent).getChildrenUnmodifiable() )
			{
				found.add( findAll( predicate, child ) );
			}
		}

		return concat( found.build() );
	}

	private final ScreenController controller;
	private final Set<MouseButton> pressedButtons = new HashSet<>();
	private final Set<KeyCode> pressedKeys = new HashSet<>();

	public GuiTest()
	{
		this.controller = new FXScreenController();
	}

	public GuiTest sleep( long ms )
	{
		try
		{
			Thread.sleep( ms );
		} catch( InterruptedException e )
		{
			throw new RuntimeException( e );
		}
		return this;
	}

	public GuiTest target( Object window )
	{
		if( window instanceof Window )
		{
			targetWindow( (Window) window );
		} else if( window instanceof String )
		{
			targetWindow( findStageByTitle( (String) window ) );
		} else if( window instanceof Number )
		{
			targetWindow( getWindowByIndex( ((Number) window).intValue() ) );
		} else if( window instanceof Class<?> )
		{
			targetWindow( Iterables.find( getWindows(), Predicates.instanceOf( (Class<?>) window ) ) );
		} else
		{
			Preconditions.checkArgument( false, "Unable to identify Window based on the given argument: %s", window );
		}

		return this;
	}

	public GuiTest click( MouseButton... buttons )
	{
		if( buttons.length == 0 )
		{
			return click( MouseButton.PRIMARY );
		}

		press( buttons );
		return release( buttons );
	}

	public GuiTest click( Object target, MouseButton... buttons )
	{
		move( target );
		return click( buttons );
	}

	public GuiTest eraseCharacters( int characters )
	{
		for( int i = 0; i < characters; i++ )
		{
			type( KeyCode.BACK_SPACE );
		}
		return this;
	}

	public GuiTest doubleClick( MouseButton... buttons )
	{
		return click( buttons ).click( buttons );
	}

	public GuiTest doubleClick( Object target, MouseButton... buttons )
	{
		return click( target, buttons ).click( target, buttons );
	}

	public MouseMotion drag( Object source, MouseButton... buttons )
	{
		move( source );
		press( buttons );

		return new MouseMotion( buttons );
	}

	public GuiTest move( double x, double y )
	{
		controller.move( x, y );
		return this;
	}

	public GuiTest move( Object target )
	{
		Point2D point = pointFor( target );
		//Since moving takes time, only do it if we're not already at the desired point.
		if( !controller.getMouse().equals( point ) )
		{
			move( point.getX(), point.getY() );
		}
		//If the target has moved while we were moving the mouse, update to the new position:
		point = pointFor( target );
		controller.position( point.getX(), point.getY() );
		return this;
	}

	public GuiTest moveBy( double x, double y )
	{
		Point2D mouse = controller.getMouse();
		controller.move( mouse.getX() + x, mouse.getY() + y );
		return this;
	}

	public GuiTest press( MouseButton... buttons )
	{
		if( buttons.length == 0 )
		{
			return press( MouseButton.PRIMARY );
		}

		for( MouseButton button : buttons )
		{
			if( pressedButtons.add( button ) )
			{
				controller.press( button );
			}
		}
		return this;
	}

	public GuiTest release( MouseButton... buttons )
	{
		if( buttons.length == 0 )
		{
			for( MouseButton button : pressedButtons )
			{
				controller.release( button );
			}
			pressedButtons.clear();
		} else
		{
			for( MouseButton button : buttons )
			{
				if( pressedButtons.remove( button ) )
				{
					controller.release( button );
				}
			}
		}
		return this;
	}

	public GuiTest scroll( int amount )
	{
		controller.scroll( amount );
		return this;
	}

	public GuiTest type( Object target, String text )
	{
		click( target );
		return type( text );
	}

	public GuiTest type( String text )
	{
		for( int i = 0; i < text.length(); i++ )
		{
			type( text.charAt( i ) );
			try
			{
				Thread.sleep( 25 );
			} catch( InterruptedException e )
			{
			}
		}

		return this;
	}

	private static final Map<Character, KeyCode> KEY_CODES = ImmutableMap.<Character, KeyCode>builder()
			.put( ',', KeyCode.COMMA ).put( ';', KeyCode.SEMICOLON ).put( '.', KeyCode.PERIOD ).put( ':', KeyCode.COLON )
			.put( '_', KeyCode.UNDERSCORE ).put( '!', KeyCode.EXCLAMATION_MARK ).put( '"', KeyCode.QUOTEDBL )
			.put( '#', KeyCode.POUND ).put( '&', KeyCode.AMPERSAND ).put( '/', KeyCode.SLASH )
			.put( '(', KeyCode.LEFT_PARENTHESIS ).put( ')', KeyCode.RIGHT_PARENTHESIS ).put( '=', KeyCode.EQUALS ).build();

	@SuppressWarnings("deprecation")
	private static KeyCode findKeyCode( char character )
	{
		if( KEY_CODES.containsKey( character ) )
		{
			return KEY_CODES.get( character );
		}

		KeyCode keyCode = KeyCode.getKeyCode( String.valueOf( Character.toUpperCase( character ) ) );
		if( keyCode != null )
		{
			return keyCode;
		}

		for( KeyCode code : KeyCode.values() )
		{
			if( (char) code.impl_getCode() == character )
			{
				return code;
			}
		}

		throw new IllegalArgumentException( "No KeyCode found for character: " + character );
	}

	public GuiTest type( char character )
	{
		KeyCode keyCode = findKeyCode( character );

		if( !Character.isUpperCase( character ) )
		{
			return type( keyCode );
		} else
		{
			KeyCode[] modifiers = new KeyCode[]{KeyCode.SHIFT};
			press( modifiers );
			type( keyCode );
			return release( modifiers );
		}
	}

	public GuiTest push( KeyCode... keys )
	{
		return type( keys );
	}

	public GuiTest push( char character )
	{
		return type( character );
	}

	public GuiTest type( KeyCode... keys )
	{
		press( keys );
		return release( keys );
	}

	public GuiTest press( KeyCode... keys )
	{
		for( KeyCode key : keys )
		{
			if( pressedKeys.add( key ) )
			{
				controller.press( key );
			}
		}
		return this;
	}

	public GuiTest release( KeyCode... keys )
	{
		if( keys.length == 0 )
		{
			for( KeyCode button : pressedKeys )
			{
				controller.release( button );
			}
			pressedKeys.clear();
		} else
		{
			for( KeyCode key : keys )
			{
				if( pressedKeys.remove( key ) )
				{
					controller.release( key );
				}
			}
		}
		return this;
	}

	private Pos nodePosition = Pos.CENTER;

	public GuiTest pos( Pos pos )
	{
		nodePosition = pos;
		return this;
	}

	public GuiTest closeCurrentWindow()
	{
		this.press( KeyCode.ALT ).press( KeyCode.F4 ).release( KeyCode.F4 ).release( KeyCode.ALT );
		return this;
	}

	private Point2D pointForBounds( Bounds bounds )
	{
		double x = 0;
		switch( nodePosition.getHpos() )
		{
			case LEFT:
				x = bounds.getMinX();
				break;
			case CENTER:
				x = (bounds.getMinX() + bounds.getMaxX()) / 2;
				break;
			case RIGHT:
				x = bounds.getMaxX();
				break;
		}

		double y = 0;
		switch( nodePosition.getVpos() )
		{
			case TOP:
				y = bounds.getMinY();
				break;
			case CENTER:
			case BASELINE:
				y = (bounds.getMinY() + bounds.getMaxY()) / 2;
				break;
			case BOTTOM:
				y = bounds.getMaxY();
				break;
		}

		return new Point2D( x, y );
	}

	private static Bounds sceneBoundsToScreenBounds( Bounds sceneBounds, Scene scene )
	{
		Window window = targetWindow( scene.getWindow() );
		return new BoundingBox( window.getX() + scene.getX() + sceneBounds.getMinX(), window.getY() + scene.getY()
				+ sceneBounds.getMinY(), sceneBounds.getWidth(), sceneBounds.getHeight() );
	}

	@SuppressWarnings("unchecked")
	public Point2D pointFor( Object target )
	{
		if( target instanceof Point2D )
		{
			return (Point2D) target;
		} else if( target instanceof Bounds )
		{
			return pointForBounds( (Bounds) target );
		} else if( target instanceof String )
		{
			return pointFor( find( (String) target ) );
		} else if( target instanceof Node )
		{
			Node node = (Node) target;
			return pointFor( sceneBoundsToScreenBounds( node.localToScene( node.getBoundsInLocal() ), node.getScene() ) );
		} else if( target instanceof Scene )
		{
			Scene scene = (Scene) target;
			return pointFor( sceneBoundsToScreenBounds( new BoundingBox( 0, 0, scene.getWidth(), scene.getHeight() ),
					scene ) );
		} else if( target instanceof Window )
		{
			Window window = targetWindow( (Window) target );
			return pointFor( new BoundingBox( window.getX(), window.getY(), window.getWidth(), window.getHeight() ) );
		} else if( target instanceof Predicate )
		{
			return pointFor( find( (Predicate<Node>) target ) );
		} else if( target instanceof Iterable<?> )
		{
			return pointFor( Iterables.get( (Iterable<?>) target, 0 ) );
		} else if( target instanceof OffsetTarget )
		{
			OffsetTarget offset = (OffsetTarget) target;
			Pos oldPos = nodePosition;
			Point2D targetPoint = pos( Pos.TOP_LEFT ).pointFor( offset.target );
			pos( oldPos );
			return new Point2D( targetPoint.getX() + offset.offsetX, targetPoint.getY() + offset.offsetY );
		}

		throw new IllegalArgumentException( "Unable to get coordinates for: " + target );
	}

	private static class OffsetTarget
	{
		private final Object target;
		private final double offsetX;
		private final double offsetY;

		private OffsetTarget( Object target, double offsetX, double offsetY )
		{
			this.target = target;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
		}
	}

	public class MouseMotion
	{
		private final MouseButton[] buttons;

		private MouseMotion( MouseButton... buttons )
		{
			this.buttons = buttons;
		}

		public GuiTest to( double x, double y )
		{
			move( x, y );
			return release( buttons );
		}

		public GuiTest to( Object target )
		{
			move( target );
			return release( buttons );
		}

		public MouseMotion via( double x, double y )
		{
			move( x, y );
			return this;
		}

		public MouseMotion via( Object target )
		{
			move( target );
			return this;
		}

		public MouseMotion by( double x, double y )
		{
			moveBy( x, y );
			return this;
		}

		public MouseMotion sleep( long ms )
		{
			try
			{
				Thread.sleep( ms );
			} catch( InterruptedException e )
			{
				throw new RuntimeException( e );
			}
			return this;
		}


		public GuiTest drop()
		{
			return release( buttons );
		}
	}
}
