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
package org.loadui.testfx.utils.keymaps;

import java.awt.im.InputContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.input.KeyCode;

public final class KeyCharMap implements Cloneable, Serializable {

    private volatile static KeyCharMap defaultKeyCharMap = initDefault();

    private static Map<String, KeyCharMap> cachedCharMap;

    private final String keyCharMapName;

    private final Map<Character, KeyChar> keyCharMap;

    /**
     * Gets the current value of the keyboard layout reading default locale of
     * the Java Virtual Machine.
     *
     * @return the default characted map for the current keyboard layout
     */
    public static KeyCharMap getDefault() {
        return defaultKeyCharMap;
    }

    public static KeyCharMap getInstance(final String keyboardLayoutName) {
        KeyCharMap ret = cachedCharMap.get(keyboardLayoutName);
        if ((ret == null) && (!cachedCharMap.containsKey(keyboardLayoutName))) {
            ret = initKeyCharMap(keyboardLayoutName);
        }
        return ret;
    }

    private static KeyCharMap initDefault() {
        String name = InputContext.getInstance().getLocale().toString();
        return initKeyCharMap(name);
    }

    private static KeyCharMap initKeyCharMap(final String name) {
        URL url = KeyCharMap.class.getResource("/keycharmaps/keycharmap_"
                + name + ".tsv");
        Map<Character, KeyChar> map = null;
        KeyCharMap ret = null;
        if (url != null) {
            map = readKeyCharMapFile(url);
            if (map != null) {
                ret = new KeyCharMap(name, map);
            }
        }
        return ret;
    }

    /**
     * Private constructor to prevent instanciation
     * 
     * @param map
     */
    private KeyCharMap(final String keyCharMapName,
            final Map<Character, KeyChar> map) {
        this.keyCharMapName = keyCharMapName;
        this.keyCharMap = map;
        if (cachedCharMap == null) {
            cachedCharMap = new HashMap<>();
        }
        cachedCharMap.put(this.keyCharMapName, this);
    }

    static private Map<Character, KeyChar> readKeyCharMapFile(
            final URL keyCharMapFile) {
        Map<Character, KeyChar> keyCharMap = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                keyCharMapFile.openStream(), Charset.forName("UTF-8")))) {
            keyCharMap = new HashMap<>();
            String line = null;
            reader.readLine(); // Header (column names)
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\t");
                    if (parts.length >= 3) {
                        KeyChar c = new KeyChar(parts[0].charAt(0),
                                Integer.parseInt(parts[1]),
                                getKeyCodeByName(parts[2]));
                        // Is it a dead key ?
                        if (c.isDeadKey()) {
                            // So another combination of key maybe needed (if
                            // not, whitespace is pressed)
                            if (parts.length == 5) {
                                c.setExtraKey(Integer.parseInt(parts[3]),
                                        getKeyCodeByName(parts[4]));
                            } else {
                                c.setExtraKey(0, KeyCode.SPACE);
                            }
                        }
                        keyCharMap.put(c.getCharacter(), c);
                    }
                }
            }
        } catch (IOException ioe) {
            System.err.print("Cannot load keycharmap file : "
                    + keyCharMapFile.toString());
            keyCharMap = null;
        }
        return keyCharMap;
    }

    private static KeyCode getKeyCodeByName(final String name) {
        KeyCode keyCode = KeyCode.getKeyCode(name);
        // Workaround bug JavaFX RT-37431
        if (name.equals("Dead Circumflex")) {
            keyCode = KeyCode.DEAD_CIRCUMFLEX;
        }
        return keyCode;
    }

    public Map<Character, KeyChar> getKeyCharMap() {
        return Collections.unmodifiableMap(keyCharMap);
    }

    public KeyChar getKeyChar(final char character) {
        return keyCharMap.get(character);
    }
}
