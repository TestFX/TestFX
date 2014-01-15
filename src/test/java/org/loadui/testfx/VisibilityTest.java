package org.loadui.testfx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.layout.VBoxBuilder;
import org.junit.Test;
import org.loadui.testfx.exceptions.NoNodesFoundException;
import org.loadui.testfx.exceptions.NoNodesVisibleException;

import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.Commons.hasText;
import static org.hamcrest.Matchers.is;

/**
 * TestFX should not find/click invisible nodes.
 */
public class VisibilityTest extends GuiTest {

    @Test(expected=NoNodesVisibleException.class)
    public void nodeNotInScene_should_notBeFound()
    {
        find("#node-not-in-scene");
    }

    @Test(expected=NoNodesVisibleException.class)
    public void nodeNotInScene_should_notBeFound_2()
    {
        find("Node not in scene");
    }

    @Test(expected=NoNodesVisibleException.class)
    public void invisibleNode_should_notBeFound()
    {
        find("#invisible-node");
    }

    @Test(expected=NoNodesVisibleException.class)
    public void nodeInInvisibleContainer_should_notBeFound()
    {
        find("#node-in-invisible-container");
    }

    @Test(expected=NoNodesFoundException.class)
    public void nonExistingNode_should_notBeFound()
    {
        find("#non-existing-node");
    }

    @Test
    public void shouldClickNodeThatIsMostlyOutsideTheScene()
    {
        String target = "#node-mostly-outside-of-scene";
        click(target);
        verifyThat(target, hasText("Clicked"));
    }
    
    @Test
    public void shouldFindVisibleTwinOnly()
    {
        verifyThat(findAll("Twin").size(), is(1));
        verifyThat(find("#twin").isVisible(), is(true));
    }

    @Override
    protected Parent getRootNode() {
        Button nodeNotInScene = ButtonBuilder.create().text("Node not in scene").id("node-not-in-scene").translateX(1500).build();
        Button invisibleNode = ButtonBuilder.create().id("invisible-node").visible(false).build();
        Button nodeInInvisibleContainer = ButtonBuilder.create().id("node-in-invisible-container").text("In invisible container").build();
        StackPane invisibleContainer = StackPaneBuilder.create().children(nodeInInvisibleContainer).visible(false).build();
        final Button nodeMostlyOutside = ButtonBuilder.create().id("node-mostly-outside-of-scene").text("Mostly outside").translateX(570).build();
        nodeMostlyOutside.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent _) {
                nodeMostlyOutside.setText("Clicked");
            }
        });
        Button visibleTwin = ButtonBuilder.create().text("Twin").id("twin").build();
        Button invisibleTwin = ButtonBuilder.create().text("Twin").id("twin").visible(false).build();
        return VBoxBuilder.create().children(invisibleTwin, visibleTwin, nodeNotInScene, invisibleNode, nodeMostlyOutside, invisibleContainer).build();
    }
}

