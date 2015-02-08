package org.testfx.cases.acceptance;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import com.google.common.base.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.Assertions;
import org.testfx.api.FxToolkit;

public class SceneRootAssertionTest {

    // FIXTURES.
    StackPane stackPane;
    Label label;

    // FIXTURE METHODS.
    @Before
    public void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupSceneRoot(() -> {
            label = new Label("foobar");
            stackPane = new StackPane(label);
            return stackPane;
        });
    }

    // FEATURE METHODS.
    @Test
    public void should_have_stage_root_with_label() {
        Assertions.verifyThat(stackPane, hasChild(label));
    }

    // HELPER METHODS.
    private Predicate<Parent> hasChild(Node node) {
        return (Parent parent) -> {
            return parent.getChildrenUnmodifiable().contains(node);
        };
    }

}
