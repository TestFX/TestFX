package org.loadui.testfx.utils;

import com.google.common.collect.ImmutableMap;
import javafx.scene.input.KeyCode;

import java.util.Map;

public class KeyCodeUtils {

    private static final Map<Character, KeyCode> KEY_CODES = ImmutableMap.<Character, KeyCode>builder()
            .put( ',', KeyCode.COMMA ).put( ';', KeyCode.SEMICOLON ).put( '.', KeyCode.PERIOD ).put( ':', KeyCode.COLON )
            .put( '_', KeyCode.UNDERSCORE ).put( '!', KeyCode.EXCLAMATION_MARK ).put( '"', KeyCode.QUOTEDBL )
            .put( '#', KeyCode.POUND ).put( '&', KeyCode.AMPERSAND ).put( '/', KeyCode.SLASH )
            .put( '(', KeyCode.LEFT_PARENTHESIS ).put( ')', KeyCode.RIGHT_PARENTHESIS ).put( '=', KeyCode.EQUALS ).build();

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