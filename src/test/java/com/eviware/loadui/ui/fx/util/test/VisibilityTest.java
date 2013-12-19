package com.eviware.loadui.ui.fx.util.test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.layout.VBoxBuilder;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.exceptions.NoNodesFoundException;
import org.loadui.testfx.exceptions.NoNodesVisibleException;

import static org.loadui.testfx.FXTestUtils.isNodeVisible;
import static org.loadui.testfx.GuiTest.find;

/**
 * TestFX should not find/click invisible nodes.
 */
public class VisibilityTest extends GuiTest {

    @Test(expected=NoNodesVisibleException.class)
    public void nodeNotInScene_should_notBeFound()
    {
        Node n = find("#node-not-in-scene");
        System.out.println( isNodeVisible(n) );
    }

    @Test(expected=NoNodesVisibleException.class)
    public void invisibleNode_should_notBeFound()
    {
        find("#invisible-node");
    }

    @Test(expected=NoNodesFoundException.class)
    public void nonExistingNode_should_notBeFound()
    {
        find("#non-existing-node");
    }

    @Test
    public void shouldClickNodeThatIsMostlyOutsideTheScene()
    {
        click("#node-mostly-outside-of-scene");
    }

    @Override
    protected Parent getRootNode() {
        Button nodeNotInScene = ButtonBuilder.create().id("node-not-in-scene").translateX(1500).build();
        Button invisibleNode = ButtonBuilder.create().id("invisible-node").visible(false).build();
        Button nodeMostlyOutside = ButtonBuilder.create().id("node-mostly-outside-of-scene").text("Mostly outside").translateX(330).build();
        return StackPaneBuilder.create().children(nodeNotInScene, invisibleNode, nodeMostlyOutside).build();
    }
}
