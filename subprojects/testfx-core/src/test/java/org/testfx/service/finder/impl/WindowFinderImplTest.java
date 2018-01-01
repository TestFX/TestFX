/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2018 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.service.finder.impl;

import java.util.List;
import java.util.concurrent.TimeoutException;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;

import static org.hamcrest.MatcherAssert.assertThat;

public class WindowFinderImplTest {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    Stage window;
    Stage windowInWindow;
    Stage windowInWindowInWindow;
    Stage otherWindow;
    Scene scene;
    WindowFinderImpl windowFinder;

    @After
    public void cleanup() throws TimeoutException {
        FxToolkit.setupFixture(this::cleanupStages);
    }

    @Before
    public void setup() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.showStage();
        FxToolkit.setupScene(() -> new Scene(new Region(), 600, 400));
        FxToolkit.setupFixture(this::setupStages);
        windowFinder = new WindowFinderImpl();
    }

    public void setupStages() {
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

    public void cleanupStages() {
        window.close();
        windowInWindow.close();
        windowInWindowInWindow.close();
        otherWindow.close();
    }

    @Test
    public void listWindows() {
        // TODO: Assert that ordering of windows is correct.
        // when:
        List<Window> windows = windowFinder.listWindows();

        // then:
        assertThat(windows, CoreMatchers.hasItems((Window) window));
        assertThat(windows, CoreMatchers.hasItems((Window) windowInWindow));
        assertThat(windows, CoreMatchers.hasItems((Window) windowInWindowInWindow));
        assertThat(windows, CoreMatchers.hasItems((Window) otherWindow));
    }

    @Test
    public void listTargetWindows() {
        // TODO: Assert that ordering of windows is correct.
        // when:
        List<Window> orderedWindows = windowFinder.listTargetWindows();

        // then:
        assertThat(orderedWindows, CoreMatchers.hasItems((Window) window));
        assertThat(orderedWindows, CoreMatchers.hasItems((Window) windowInWindow));
        assertThat(orderedWindows, CoreMatchers.hasItems((Window) windowInWindowInWindow));
        assertThat(orderedWindows, CoreMatchers.hasItems((Window) otherWindow));
    }

    @Test
    public void targetWindow_window() {
        // when:
        windowFinder.targetWindow(window);

        // then:
        assertThat(windowFinder.targetWindow(), CoreMatchers.is(window));
    }

    @Test
    public void targetWindow_windowIndex() {
        // when:
        windowFinder.targetWindow(1);

        // then:
        assertThat(windowFinder.targetWindow(), CoreMatchers.is(window));
    }

    @Test
    public void targetWindow_stageTitle() {
        // when:
        windowFinder.targetWindow("window");

        // then:
        assertThat(windowFinder.targetWindow(), CoreMatchers.is(window));
    }

    @Test
    public void targetWindow_scene() {
        // when:
        windowFinder.targetWindow(scene);

        // then:
        assertThat(windowFinder.targetWindow(), CoreMatchers.is(otherWindow));
    }

    @Test
    public void window_windowIndex() {
        // TODO: Assert that it throws an exception of index is out of range.
        // expect:
        assertThat(windowFinder.window(1), CoreMatchers.is(window));
        assertThat(windowFinder.window(2), CoreMatchers.is(windowInWindow));
        assertThat(windowFinder.window(3), CoreMatchers.is(windowInWindowInWindow));
        assertThat(windowFinder.window(4), CoreMatchers.is(otherWindow));
    }

    @Test
    public void window_stageTitle() {
        // TODO: Assert that it thrown an exception of stage title regex does not match.
        // TODO: Assert that stages without title do not throw a NPE.
        // expect:
        assertThat(windowFinder.window("window"), CoreMatchers.is(window));
        assertThat(windowFinder.window("windowInWindow"), CoreMatchers.is(windowInWindow));
        assertThat(windowFinder.window("windowInWindowInWindow"), CoreMatchers.is(windowInWindowInWindow));
        assertThat(windowFinder.window("otherWindow"), CoreMatchers.is(otherWindow));
    }

    @Test
    public void window_scene() {
        // expect:
        assertThat(windowFinder.window(scene), CoreMatchers.is(otherWindow));
    }

}
