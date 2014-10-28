package org.testfx.api.demo;

import java.util.concurrent.TimeoutException;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.framework.robot.impl.FxRobotImpl;
import org.testfx.api.FxLifecycle;

public class SimpleLabelTest extends FxRobotImpl {

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupSpec() throws TimeoutException {
        FxLifecycle.registerPrimaryStage();
    }

    @Before
    public void setup() throws TimeoutException {
        FxLifecycle.setupStage(stage -> {
            Scene scene = new Scene(createSceneRoot(this.getClass()), 300, 100);
            stage.setScene(scene);
            stage.show();
        });
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void should_move_to_label() {
        moveTo(".label").sleep(1000);
    }

    //---------------------------------------------------------------------------------------------
    // HELPER METHODS.
    //---------------------------------------------------------------------------------------------

    private Region createSceneRoot(Class<?> cls) {
        return new StackPane(new Label(cls.getSimpleName()));
    }

}
