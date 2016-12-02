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

    public Window targetWindow();
    public void targetWindow(Window window);
    public void targetWindow(Predicate<Window> predicate);

    // Convenience methods:
    public void targetWindow(int windowIndex);
    public void targetWindow(String stageTitleRegex);
    public void targetWindow(Pattern stageTitlePattern);
    public void targetWindow(Scene scene);
    public void targetWindow(Node node);

    //---------------------------------------------------------------------------------------------
    // METHODS FOR WINDOW LOOKUP.
    //---------------------------------------------------------------------------------------------

    public List<Window> listWindows();
    public List<Window> listTargetWindows();
    public Window window(Predicate<Window> predicate);

    // Convenience methods:
    public Window window(int windowIndex);
    public Window window(String stageTitleRegex);
    public Window window(Pattern stageTitlePattern);
    public Window window(Scene scene);
    public Window window(Node node);

}
