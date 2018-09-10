package org.testfx.cases;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * A simple test case with a VBox as container for elements under test.
 * 
 */
public class InternalContainerTest extends InternalJUnitTest<VBox> {

    VBox parent;

    @Override
    public VBox createComponent() {
        parent = new VBox();
        // empty containers initialize at (0,0)
        parent.setMinSize(10, 10);
        return parent;
    }

    /**
     * Adds the given nodes to the VBox.
     * 
     * @param nodes the nodes to add
     */
    public void addAll(Node... nodes) {
        interact(() -> {
            parent.getChildren().addAll(nodes);
            getTestStage().sizeToScene();
        });
    }

    /**
     * Clears all elements in the VBox.
     */
    public void clear() {
        interact(() -> {
            parent.getChildren().clear();
            getTestStage().sizeToScene();
        });
    }

}
