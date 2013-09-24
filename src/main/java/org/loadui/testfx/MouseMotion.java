package org.loadui.testfx;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import org.hamcrest.Matcher;

/**
 * A builder class for advanced mouse movements.
 *
 * Example usage: drag("#nodeA").via(nodeB).to("Node C")
 *
 */
public class MouseMotion
{
    private final MouseButton[] buttons;
    private GuiTest controller;

    MouseMotion(GuiTest controller, MouseButton... buttons)
    {
        this.controller = controller;
        this.buttons = buttons;
    }

    /*---------------- To ----------------*/

    /**
     * Specifies that the ongoing drag-n-drop operation should end at the given coordinates.
     *
     * @param x
     * @param y
     */
    public GuiTest to( double x, double y )
    {
        controller.move(x, y);
        return controller.release(buttons);
    }

    /**
     * Ends the ongoing drag-n-drop operation at the given target.
     *
     * @param query
     */
    public GuiTest to( String query )
    {
        controller.move(query);
        return controller.release(buttons);
    }

    public GuiTest to( Node node )
    {
        controller.move(node);
        return controller.release(buttons);
    }

    public GuiTest to( Matcher<Node> matcher )
    {
        controller.move(matcher);
        return controller.release(buttons);
    }

    public GuiTest to( Point2D point )
    {
        controller.move(point);
        return controller.release(buttons);
    }

	 public GuiTest to( GuiTest.OffsetTarget node )
	 {
        controller.move(node);
		  return controller.release(buttons);
	 }


    /*---------------- Via ----------------*/

    public MouseMotion via( double x, double y )
    {
        controller.move(x, y);
        return this;
    }

    public MouseMotion via( String query )
    {
        controller.move(query);
        return this;
    }

    public MouseMotion via( Node node )
    {
        controller.move(node);
        return this;
    }

    public MouseMotion via( Matcher<Node> matcher )
    {
        controller.move(matcher);
        return this;
    }


    /*---------------- By ----------------*/

    public MouseMotion by( double x, double y )
    {
        controller.moveBy(x, y);
        return this;
    }

    /**
     * Waits still, while keeping the mouse pressed.
     *
     * @param ms time in milliseconds
     */
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

    /**
     * Releases the mouse button and thereby ends the ongoing drag-n-drop operation.
     */
    public GuiTest drop()
    {
        return controller.release(buttons);
    }
}
