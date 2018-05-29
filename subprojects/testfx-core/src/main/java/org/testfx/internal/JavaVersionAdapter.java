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
package org.testfx.internal;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Window;

/**
 * Provides a consistent API regardless of what Java version (8+) is used.
 */
public final class JavaVersionAdapter {

    private JavaVersionAdapter() {}

    private static JavaVersion currentJavaVersion;

    public static int convertToKeyCodeId(KeyCode keyCode) {
        Method getCode;

        if (currentVersion().isJava8()) {
            try {
                getCode = keyCode.getClass().getMethod("impl_getCode");
            }
            catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        } else if (currentVersion().isJava9Compatible()) {
            try {
                getCode = keyCode.getClass().getMethod("getCode");
            }
            catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("bad java version: " + currentVersion());
        }

        try {
            return (int) getCode.invoke(keyCode);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Window> getWindows() {
        List<Window> windows;

        if (currentVersion().isJava8()) {
            try {
                windows = new ArrayList<>();
                ((Iterator<Window>) Window.class.getMethod("impl_getWindows").invoke(null))
                        .forEachRemaining(windows::add);
                Collections.reverse(windows);
            }
            catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else if (currentVersion().isJava9Compatible()) {
            try {
                windows = new ArrayList<>((ObservableList<Window>) Window.class.getMethod("getWindows").invoke(null));
            }
            catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("bad java version: " + currentVersion());
        }

        return windows;
    }

    public static boolean isNotVisible(Node node) {
        if (currentVersion().isJava8()) {
            try {
                boolean treeVisible = (boolean) node.getClass().getMethod("impl_isTreeVisible").invoke(node);
                return !node.isVisible() || !treeVisible;
            }
            catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

        } else if (currentVersion().isJava9Compatible()) {
            return !node.isVisible();
        } else {
            throw new RuntimeException("bad java version: " + currentVersion());
        }
    }

    public static double getScreenScaleX() {
        if (currentVersion().isJava8()) {
            if (GraphicsEnvironment.isHeadless()) {
                return 1d;
            }
            Rectangle primaryScreenRect = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration().getBounds();
            double realWidth = primaryScreenRect.getWidth();
            double scaledWidth = Screen.getPrimary().getBounds().getWidth();
            if (Math.abs(realWidth - scaledWidth) <= 0.001) {
                return 1d;
            }
            return realWidth / scaledWidth;
        } else if (currentVersion().isJava9Compatible()) {
            try {
                return (double) Screen.class.getMethod("getOutputScaleX").invoke(Screen.getPrimary());
            }
            catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("bad java version: " + currentVersion());
        }
    }

    public static double getScreenScaleY() {
        if (currentVersion().isJava8()) {
            if (GraphicsEnvironment.isHeadless()) {
                return 1d;
            }
            Rectangle primaryScreenRect = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration().getBounds();
            double realHeight = primaryScreenRect.getHeight();
            double scaledHeight = Screen.getPrimary().getBounds().getHeight();
            if (Math.abs(realHeight - scaledHeight) <= 0.001) {
                return 1d;
            }
            return realHeight / scaledHeight;
        } else if (currentVersion().isJava9Compatible()) {
            try {
                return (double) Screen.class.getMethod("getOutputScaleY").invoke(Screen.getPrimary());
            }
            catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("bad java version: " + currentVersion());
        }
    }

    // The following is copied from Gradle:

    /*
     * Copyright 2009 the original author or authors.
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *      http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    private static JavaVersion currentVersion() {
        if (currentJavaVersion == null) {
            currentJavaVersion = toVersion(System.getProperty("java.version"));
        }
        return currentJavaVersion;
    }

    private static JavaVersion toVersion(String javaVersion) throws IllegalArgumentException {
        Objects.requireNonNull(javaVersion, "javaVersion must not be null");

        int firstNonVersionCharIndex = javaVersion.length();
        for (int i = 0; i < javaVersion.length(); ++i) {
            char c = javaVersion.charAt(i);
            if (c >= '0' && c <= '9' || c == '.') {
                continue;
            }
            firstNonVersionCharIndex = i;
            break;
        }

        String[] versionStrings = javaVersion.substring(0, firstNonVersionCharIndex).split("\\.");
        List<Integer> result = new ArrayList<>();
        for (String versionString : versionStrings) {
            try {
                result.add(Integer.parseInt(versionString));
            }
            catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }

        if (result.get(0) == 1 && result.size() > 1) {
            return getVersionForMajor(result.get(1));
        } else {
            return getVersionForMajor(result.get(0));
        }
    }

    private static JavaVersion getVersionForMajor(int major) {
        return major >= JavaVersion.values().length ? JavaVersion.VERSION_HIGHER : JavaVersion.values()[major - 1];
    }

    /**
     * An enumeration of Java versions.
     * Before 9: http://www.oracle.com/technetwork/java/javase/versioning-naming-139433.html
     * 9+: http://openjdk.java.net/jeps/223
     */
    private enum JavaVersion {
        VERSION_1_1, VERSION_1_2, VERSION_1_3, VERSION_1_4,
        VERSION_1_5, VERSION_1_6, VERSION_1_7, VERSION_1_8,
        VERSION_1_9, VERSION_1_10, VERSION_11, VERSION_HIGHER;
        // Since Java 9, version should be X instead of 1.X
        // However, to keep backward compatibility, we change from 11
        private static final int FIRST_MAJOR_VERSION_ORDINAL = 10;
        private final String versionName;

        JavaVersion() {
            versionName = ordinal() >= FIRST_MAJOR_VERSION_ORDINAL ? getMajorVersion() : "1." + getMajorVersion();
        }

        public String getMajorVersion() {
            return String.valueOf(ordinal() + 1);
        }

        private boolean isJava8() {
            return this == VERSION_1_8;
        }

        public boolean isJava9Compatible() {
            return this.compareTo(VERSION_1_9) >= 0;
        }

        @Override
        public String toString() {
            return versionName;
        }
    }
}
