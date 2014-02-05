/*
 * Copyright 2013 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.loadui.testfx.service.finder.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.utils.FXTestUtils;
import org.loadui.testfx.utils.FxLauncherUtils;
import org.loadui.testfx.framework.FxRobot;

import org.hamcrest.Matchers;
import static org.hamcrest.MatcherAssert.assertThat;

public class WindowFinderImplTest extends FxRobot {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    static Stage window;
    static Stage windowInWindow;
    static Stage windowInWindowInWindow;
    static Stage otherWindow;
    static Scene scene;

    WindowFinderImpl windowFinder;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupSpec() throws Throwable {
        FxLauncherUtils.launchOnce(10, TimeUnit.SECONDS);
        FXTestUtils.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                setupStages();
            }
        }, 10);
    }

    @Before
    public void setup() {
        windowFinder = new WindowFinderImpl();
    }

    @AfterClass
    public static void cleanupSpec() throws Throwable {
        FXTestUtils.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                cleanupStages();
            }
        }, 10);
    }

    public static void setupStages() {
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
    }

    public static void cleanupStages() {
        window.close();
        windowInWindow.close();
        windowInWindowInWindow.close();
        otherWindow.close();
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void listWindows() {
        // TODO: Assert that ordering of windows is correct.
        // when:
        List<Window> windows = windowFinder.listWindows();

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
        // TODO: Assert that stages without title do not throw a NPE.
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
