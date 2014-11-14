package org.loadui.testfx.service.adapter;

import java.util.concurrent.TimeoutException;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxLifecycle;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.loadui.testfx.utils.WaitForAsyncUtils.waitForFxEvents;

public class GlassRobotAdapterTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public GlassRobotAdapter robotAdapter;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupSpec() throws TimeoutException {
        FxLifecycle.registerPrimaryStage();
    }

    @Before
    public void setup() throws TimeoutException {
        robotAdapter = new GlassRobotAdapter();
        FxLifecycle.setupStage(stage -> {
            Parent sceneRoot = createSceneRoot(this.getClass());
            Scene scene = new Scene(sceneRoot, 300, 100);
            stage.setScene(scene);
            stage.show();
        });
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void robotCreate() {
        robotAdapter.robotCreate();
    }

    @Test
    public void getMouseLocation() {
        // when:
        Point2D mouseLocation = robotAdapter.getMouseLocation();

        // then:
        assertThat(mouseLocation.getX(), Matchers.is(greaterThanOrEqualTo(0.0)));
        assertThat(mouseLocation.getY(), Matchers.is(greaterThanOrEqualTo(0.0)));
    }

    @Test
    public void mouseMove() {
        // given:
        robotAdapter.mouseMove(new Point2D(100, 200));
        waitForFxEvents();

        // when:
        Point2D mouseLocation = robotAdapter.getMouseLocation();

        // then:
        assertThat(mouseLocation.getX(), Matchers.is(100.0));
        assertThat(mouseLocation.getY(), Matchers.is(200.0));
    }

    //---------------------------------------------------------------------------------------------
    // HELPER METHODS.
    //---------------------------------------------------------------------------------------------

    private Region createSceneRoot(Class<?> cls) {
        return new StackPane(new Label(cls.getSimpleName()));
    }

}
