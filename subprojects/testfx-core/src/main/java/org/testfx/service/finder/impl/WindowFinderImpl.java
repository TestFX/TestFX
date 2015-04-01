/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.service.finder.impl;

import java.util.List;
import javafx.scene.Scene;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.testfx.api.annotation.Unstable;
import org.testfx.service.finder.WindowFinder;

@Unstable
public class WindowFinderImpl implements WindowFinder {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    private Window lastTargetWindow;

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public Window target() {
        return lastTargetWindow;
    }

    @Override
    public void target(Window window) {
        lastTargetWindow = window;
    }

    @Override
    public void target(int windowIndex) {
        target(window(windowIndex));
    }

    @Override
    public void target(String stageTitleRegex) {
        target(window(stageTitleRegex));
    }

    @Override
    public void target(Scene scene) {
        target(window(scene));
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<Window> listWindows() {
        List<Window> windows = Lists.newArrayList(Window.impl_getWindows());
        return ImmutableList.copyOf(Lists.reverse(windows));
    }

    @Override
    public List<Window> listOrderedWindows() {
        List<Window> windows = listWindows();
        List<Window> orderedWindows = orderWindowsByProximityTo(lastTargetWindow, windows);
        return orderedWindows;
    }

    @Override
    public Window window(int windowIndex) {
        List<Window> windows = listWindows();
        return windows.get(windowIndex);
    }

    @Override
    public Window window(String stageTitleRegex) {
        List<Window> windows = listWindows();
        return Iterables.find(windows, hasStageTitlePredicate(stageTitleRegex));
    }

    @Override
    public Window window(Scene scene) {
        return scene.getWindow();
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private List<Window> orderWindowsByProximityTo(Window targetWindow, List<Window> windows) {
        return Ordering.natural()
            .onResultOf(calculateWindowProximityFunction(targetWindow))
            .immutableSortedCopy(windows);
    }

    private Function<Window, Integer> calculateWindowProximityFunction(final Window targetWindow) {
        return window -> calculateWindowProximityTo(targetWindow, window);
    }

    private int calculateWindowProximityTo(Window targetWindow, Window window) {
        if (window == targetWindow) {
            return 0;
        }
        if (isOwnerOf(window, targetWindow)) {
            return 1;
        }
        return 2;
    }

    private boolean isOwnerOf(Window window, Window targetWindow) {
        Window ownerWindow = retrieveOwnerOf(window);
        if (ownerWindow == targetWindow) {
            return true;
        }
        return ownerWindow != null && isOwnerOf(ownerWindow, targetWindow);
    }

    private Window retrieveOwnerOf(Window window) {
        if (window instanceof Stage) {
            return ((Stage) window).getOwner();
        }
        if (window instanceof PopupWindow) {
            return ((PopupWindow) window).getOwnerWindow();
        }
        return null;
    }

    private Predicate<Window> hasStageTitlePredicate(final String stageTitleRegex) {
        return window -> window instanceof Stage &&
            hasStageTitle((Stage) window, stageTitleRegex);
    }

    private boolean hasStageTitle(Stage stage, String stageTitleRegex) {
        return stage.getTitle() != null && stage.getTitle().matches(stageTitleRegex);
    }

}
