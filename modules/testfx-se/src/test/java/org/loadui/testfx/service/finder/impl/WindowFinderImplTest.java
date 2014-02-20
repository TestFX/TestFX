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

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.framework.app.StageSetupCallback;
import org.loadui.testfx.framework.junit.AppRobotTestBase;

import java.util.List;

public class WindowFinderImplTest extends AppRobotTestBase {

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
        setupApplication();
        setupStages(new StageSetupCallback() {
            @Override
            public void setupStages(Stage primaryStage) {
                primaryStage.setScene(new Scene(new Region(), 600, 400));
                setupStagesClass();
            }
        });
    }

    @Before
    public void setup() {
        windowFinder = new WindowFinderImpl();
    }

    @AfterClass
    public static void cleanupClass() throws Throwable {
        invokeAndWait(new Runnable() {
            @Override
            public void run() {
                cleanupStagesClass();
            }
        });
    }

    public static void setupStagesClass() {
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

    public static void cleanupStagesClass() {
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
        MatcherAssert.assertThat(windows, Matchers.hasItems((Window) window));
        MatcherAssert.assertThat(windows, Matchers.hasItems((Window) windowInWindow));
        MatcherAssert.assertThat(windows, Matchers.hasItems((Window) windowInWindowInWindow));
        MatcherAssert.assertThat(windows, Matchers.hasItems((Window) otherWindow));
    }

    @Test
    public void listOrderedWindows() {
        // TODO: Assert that ordering of windows is correct.
        // when:
        List<Window> orderedWindows = windowFinder.listOrderedWindows();

        // then:
        MatcherAssert.assertThat(orderedWindows, Matchers.hasItems((Window) window));
        MatcherAssert.assertThat(orderedWindows, Matchers.hasItems((Window) windowInWindow));
        MatcherAssert.assertThat(orderedWindows, Matchers.hasItems((Window) windowInWindowInWindow));
        MatcherAssert.assertThat(orderedWindows, Matchers.hasItems((Window) otherWindow));
    }

    @Test
    public void target_window() {
        // when:
        windowFinder.target(window);

        // then:
        MatcherAssert.assertThat(windowFinder.target(), Matchers.is((Window) window));
    }

    @Test
    public void target_windowIndex() {
        // when:
        windowFinder.target(1);

        // then:
        MatcherAssert.assertThat(windowFinder.target(), Matchers.is((Window) window));
    }

    @Test
    public void target_stageTitleRegex() {
        // when:
        windowFinder.target("window");

        // then:
        MatcherAssert.assertThat(windowFinder.target(), Matchers.is((Window) window));
    }

    @Test
    public void target_scene() {
        // when:
        windowFinder.target(scene);

        // then:
        MatcherAssert.assertThat(windowFinder.target(), Matchers.is((Window) otherWindow));
    }

    @Test
    public void window_windowIndex() {
        // TODO: Assert that it throws an exception of index is out of range.
        // expect:
        MatcherAssert.assertThat(windowFinder.window(1), Matchers.is((Window) window));
        MatcherAssert.assertThat(windowFinder.window(2), Matchers.is((Window) windowInWindow));
        MatcherAssert.assertThat(windowFinder.window(3), Matchers.is((Window) windowInWindowInWindow));
        MatcherAssert.assertThat(windowFinder.window(4), Matchers.is((Window) otherWindow));
    }

    @Test
    public void window_stageTitleRegex() {
        // TODO: Assert that it thrown an exception of stage title regex does not match.
        // TODO: Assert that stages without title do not throw a NPE.
        // expect:
        MatcherAssert.assertThat(windowFinder.window("window"), Matchers.is((Window) window));
        MatcherAssert.assertThat(windowFinder.window("windowInWindow"), Matchers.is((Window) windowInWindow));
        MatcherAssert.assertThat(windowFinder.window("windowInWindowInWindow"), Matchers.is((Window) windowInWindowInWindow));
        MatcherAssert.assertThat(windowFinder.window("otherWindow"), Matchers.is((Window) otherWindow));
    }

    @Test
    public void window_scene() {
        // expect:
        MatcherAssert.assertThat(windowFinder.window(scene), Matchers.is((Window) otherWindow));
    }

}
