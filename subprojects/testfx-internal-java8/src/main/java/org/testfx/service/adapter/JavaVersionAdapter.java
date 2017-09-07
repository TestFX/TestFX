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
package org.testfx.service.adapter;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.stage.Window;

import com.google.common.collect.Lists;

/**
 * Provides consistent API for TestFX-Core subproject regardless of whether Java 8 or Java 9 is being used.
 */
public final class JavaVersionAdapter {

    @SuppressWarnings("deprecated")
    public static int convertToKeyCodeId(KeyCode keyCode) {
        return keyCode.impl_getCode();
    }

    @SuppressWarnings("deprecated")
    public static List<Window> getWindows() {
        return Lists.reverse(Lists.newArrayList(Window.impl_getWindows()));
    }

    @SuppressWarnings("deprecated")
    public static boolean isNotVisible(Node node) {
        return !node.isVisible() || !node.impl_isTreeVisible();
    }

}
