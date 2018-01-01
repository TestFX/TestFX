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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.testfx.service.finder.WindowFinder;

import static org.testfx.internal.JavaVersionAdapter.getWindows;

public class WindowFinderImpl implements WindowFinder {

    private Window lastTargetWindow;

    @Override
    public Window targetWindow() {
        return lastTargetWindow;
    }

    @Override
    public void targetWindow(Window window) {
        lastTargetWindow = window;
    }

    @Override
    public void targetWindow(Predicate<Window> predicate) {
        targetWindow(window(predicate));
    }

    @Override
    public List<Window> listWindows() {
        return fetchWindowsInQueue();
    }

    @Override
    public List<Window> listTargetWindows() {
        return fetchWindowsByProximityTo(lastTargetWindow);
    }

    @Override
    public Window window(Predicate<Window> predicate) {
        return fetchWindowsByProximityTo(lastTargetWindow).stream()
            .filter(predicate)
            .findFirst()
            .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void targetWindow(int windowIndex) {
        targetWindow(window(windowIndex));
    }

    @Override
    public void targetWindow(String stageTitleRegex) {
        targetWindow(window(stageTitleRegex));
    }

    @Override
    public void targetWindow(Pattern stageTitlePattern) {
        targetWindow(window(stageTitlePattern));
    }

    @Override
    public void targetWindow(Scene scene) {
        targetWindow(window(scene));
    }

    @Override
    public void targetWindow(Node node) {
        targetWindow(window(node));
    }

    @Override
    public Window window(int windowIndex) {
        List<Window> windows = fetchWindowsByProximityTo(lastTargetWindow);
        return windows.get(windowIndex);
    }

    @Override
    public Window window(String stageTitleRegex) {
        return window(hasStageTitlePredicate(stageTitleRegex));
    }

    @Override
    public Window window(Pattern stageTitlePattern) {
        return window(hasStageTitlePredicate(stageTitlePattern.toString()));
    }

    @Override
    public Window window(Scene scene) {
        return scene.getWindow();
    }

    @Override
    public Window window(Node node) {
        return window(node.getScene());
    }

    @SuppressWarnings("deprecation")
    private List<Window> fetchWindowsInQueue() {
        return Collections.unmodifiableList(getWindows());
    }

    private List<Window> fetchWindowsByProximityTo(Window targetWindow) {
        List<Window> windows = fetchWindowsInQueue();
        return orderWindowsByProximityTo(targetWindow, windows);
    }

    private List<Window> orderWindowsByProximityTo(Window targetWindow, List<Window> windows) {
        List<Window> copy = new ArrayList<>(windows);
        copy.sort(Comparator.comparingInt(w -> calculateWindowProximityTo(targetWindow, w)));
        return Collections.unmodifiableList(copy);
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

    private Predicate<Window> hasStageTitlePredicate(String stageTitleRegex) {
        return window -> window instanceof Stage &&
            hasStageTitle((Stage) window, stageTitleRegex);
    }

    private boolean hasStageTitle(Stage stage, String stageTitleRegex) {
        return stage.getTitle() != null && stage.getTitle().matches(stageTitleRegex);
    }

}
