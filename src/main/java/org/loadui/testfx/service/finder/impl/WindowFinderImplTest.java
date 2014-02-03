package org.loadui.testfx.service.finder.impl;

import java.util.List;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;

import org.hamcrest.Matchers;
import static org.hamcrest.MatcherAssert.assertThat;

public class WindowFinderImplTest extends GuiTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    Stage window;
    Stage windowInWindow;
    Stage windowInWindowInWindow;
    Stage otherWindow;

    Scene scene;

    WindowFinderImpl windowFinder;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setup() {
        windowFinder = new WindowFinderImpl();
    }

    @After
    public void cleanup() throws Throwable {
        FXTestUtils.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                window.close();
                windowInWindow.close();
                windowInWindowInWindow.close();
                otherWindow.close();
            }
        }, 10);
    }

    @Override
    protected Parent getRootNode() {
        window = new Stage();
        window.setTitle("window");

        windowInWindow = new Stage();
        windowInWindow.setTitle("windowInWindow");
        windowInWindow.initOwner(window);

        windowInWindowInWindow = new Stage();
        windowInWindowInWindow.setTitle("windowInWindowInWindow");
        windowInWindowInWindow.initOwner(windowInWindow);

        otherWindow = new Stage();
        otherWindow.setTitle("otherWindow");
        scene = new Scene(new Region(), 600, 400);
        otherWindow.setScene(scene);

        window.show();
        windowInWindow.show();
        windowInWindowInWindow.show();
        otherWindow.show();

        return new AnchorPane();
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void listWindows() {
        // TODO: Assert that ordering of windows is correct.
        // when:
        List<Window> windows = windowFinder.listWindows();
        windows = windows.subList(0, windows.size());

        // then:
        assertThat(windows, Matchers.hasItems((Window) window));
        assertThat(windows, Matchers.hasItems((Window) windowInWindow));
        assertThat(windows, Matchers.hasItems((Window) windowInWindowInWindow));
        assertThat(windows, Matchers.hasItems((Window) otherWindow));
    }

    @Test
    public void listOrderedWindows() {
        // TODO: Assert that ordering of windows is correct.
        // when:
        List<Window> orderedWindows = windowFinder.listOrderedWindows();

        // then:
        assertThat(orderedWindows, Matchers.hasItems((Window) window));
        assertThat(orderedWindows, Matchers.hasItems((Window) windowInWindow));
        assertThat(orderedWindows, Matchers.hasItems((Window) windowInWindowInWindow));
        assertThat(orderedWindows, Matchers.hasItems((Window) otherWindow));
    }

    @Test
    public void target_window() {
        // when:
        windowFinder.target(window);

        // then:
        assertThat(windowFinder.target(), Matchers.is((Window) window));
    }

    @Test
    public void target_windowIndex() {
        // when:
        windowFinder.target(1);

        // then:
        assertThat(windowFinder.target(), Matchers.is((Window) window));
    }

    @Test
    public void target_stageTitleRegex() {
        // when:
        windowFinder.target("window");

        // then:
        assertThat(windowFinder.target(), Matchers.is((Window) window));
    }

    @Test
    public void target_scene() {
        // when:
        windowFinder.target(scene);

        // then:
        assertThat(windowFinder.target(), Matchers.is((Window) otherWindow));
    }

    @Test
    public void window_windowIndex() {
        // TODO: Assert that it throws an exception of index is out of range.
        // expect:
        assertThat(windowFinder.window(1), Matchers.is((Window) window));
        assertThat(windowFinder.window(2), Matchers.is((Window) windowInWindow));
        assertThat(windowFinder.window(3), Matchers.is((Window) windowInWindowInWindow));
        assertThat(windowFinder.window(4), Matchers.is((Window) otherWindow));
    }

    @Test
    public void window_stageTitleRegex() {
        // TODO: Assert that it thrown an exception of stage title regex does not match.
        // expect:
        assertThat(windowFinder.window("window"), Matchers.is((Window) window));
        assertThat(windowFinder.window("windowInWindow"), Matchers.is((Window) windowInWindow));
        assertThat(windowFinder.window("windowInWindowInWindow"), Matchers.is((Window) windowInWindowInWindow));
        assertThat(windowFinder.window("otherWindow"), Matchers.is((Window) otherWindow));
    }

    @Test
    public void window_scene() {
        // expect:
        assertThat(windowFinder.window(scene), Matchers.is((Window) otherWindow));
    }

}
