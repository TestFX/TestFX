/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2017 The TestFX Contributors
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
package org.testfx.service.finder;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

public interface WindowFinder {

    //---------------------------------------------------------------------------------------------
    // METHODS FOR WINDOW TARGETING.
    //---------------------------------------------------------------------------------------------

    /**
     * Returns the last stored target window
     */
    Window targetWindow();

    /**
     * Stores the given window as the target window
     */
    void targetWindow(Window window);

    /**
     * Stores the first window that is closes by proximity to the last stored target window and passes the
     * given predicate as the new last target window or throws a {@link java.util.NoSuchElementException}
     * if none exist.
     */
    void targetWindow(Predicate<Window> predicate);

    // Convenience methods:

    /**
     * Stores the window returned from {@link #window(int)} as the new last target window
     */
    void targetWindow(int windowIndex);

    /**
     * Stores the stage whose title matches the given regex as the new last target window.
     */
    void targetWindow(String stageTitleRegex);

    /**
     * Stores the stage whose title matches the given regex as the new last target window.
     */
    void targetWindow(Pattern stageTitlePattern);

    /**
     * Stores the given scene's window as the new last target window
     */
    void targetWindow(Scene scene);

    /**
     * Stores the given node's scene's window as the new last target window
     */
    void targetWindow(Node node);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR WINDOW LOOKUP.
    //---------------------------------------------------------------------------------------------

    /**
     * Calls {@link org.testfx.service.adapter.JavaVersionAdapter#getWindows()}
     */
    List<Window> listWindows();

    /**
     * Returns a list of windows that are ordered by proximity to the last target window
     */
    List<Window> listTargetWindows();

    /**
     * Calls {@link #listTargetWindows()} and returns the first window that passes the predicate or throws
     * a {@link java.util.NoSuchElementException} if none exist.
     */
    Window window(Predicate<Window> predicate);

    // Convenience methods:
    /**
     * Returns the window at the given index from the list of windows that are ordered by proximity to the last stored
     * target window
     */
    Window window(int windowIndex);

    /**
     * Returns the stage whose title matches the given regex
     */
    Window window(String stageTitleRegex);

    /**
     * Returns the stage whose title matches the given regex
     */
    Window window(Pattern stageTitlePattern);

    /**
     * Returns the scene's window
     */
    Window window(Scene scene);

    /**
     * Returns the node's scene's window
     */
    Window window(Node node);

}
