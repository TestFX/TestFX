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
package org.loadui.testfx.utils;

import java.util.Map;
import javafx.scene.input.KeyCode;

import com.google.common.collect.ImmutableMap;

public class KeyCodeUtils {

    private static final Map<Character, KeyCode> KEY_CODES = ImmutableMap.<Character, KeyCode>builder()
            .put(',', KeyCode.COMMA).put(';', KeyCode.SEMICOLON).put('.', KeyCode.PERIOD).put(':', KeyCode.COLON)
            .put('_', KeyCode.UNDERSCORE).put('!', KeyCode.EXCLAMATION_MARK).put('"', KeyCode.QUOTEDBL)
            .put('#', KeyCode.POUND).put('&', KeyCode.AMPERSAND).put('/', KeyCode.SLASH)
            .put('(', KeyCode.LEFT_PARENTHESIS).put(')', KeyCode.RIGHT_PARENTHESIS).put('=', KeyCode.EQUALS).build();

    @SuppressWarnings("deprecation")
    public static KeyCode findKeyCode(char character) {
        if (KEY_CODES.containsKey(character)) {
            return KEY_CODES.get(character);
        }

        KeyCode keyCode = KeyCode.getKeyCode(String.valueOf(Character.toUpperCase(character)));
        if (keyCode != null) {
            return keyCode;
        }

        for (KeyCode code : KeyCode.values()) {
            if ((char) code.impl_getCode() == character) {
                return code;
            }
        }

        throw new IllegalArgumentException("No KeyCode found for character: " + character);
    }
}
