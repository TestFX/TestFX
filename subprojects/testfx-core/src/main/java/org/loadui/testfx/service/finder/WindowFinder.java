/*
 * Copyright 2013-2014 SmartBear Software
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
package org.loadui.testfx.service.finder;

import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Window;

public interface WindowFinder {
    public Window target();
    public void target(Window window);
    public void target(int windowIndex);
    public void target(String stageTitleRegex);
    public void target(Scene scene);
    //public void target(Node node);

    public List<Window> listWindows();
    public List<Window> listOrderedWindows();

    public Window window(int windowIndex);
    public Window window(String stageTitleRegex);
    public Window window(Scene scene);
    //public Window window(Node node);
}
