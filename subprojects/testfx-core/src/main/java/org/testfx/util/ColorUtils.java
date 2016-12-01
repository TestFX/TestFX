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
package org.testfx.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javafx.scene.paint.Color;

/**
 * Provides utilities for working with JavaFX Colors.
 *
 * @see <a href="https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#typecolor">Named Colors</a>
 */
public class ColorUtils {
    private static Map<RGB, String> namedColors = new HashMap<>();

    static {
        namedColors.put(new RGB(0xF0, 0xF8, 0xFF), "AliceBlue");
        namedColors.put(new RGB(0xFA, 0xEB, 0xD7), "AntiqueWhite");
        namedColors.put(new RGB(0x00, 0xFF, 0xFF), "Aqua");
        namedColors.put(new RGB(0x7F, 0xFF, 0xD4), "Aquamarine");
        namedColors.put(new RGB(0xF0, 0xFF, 0xFF), "Azure");
        namedColors.put(new RGB(0xF5, 0xF5, 0xDC), "Beige");
        namedColors.put(new RGB(0xFF, 0xE4, 0xC4), "Bisque");
        namedColors.put(new RGB(0x00, 0x00, 0x00), "Black");
        namedColors.put(new RGB(0xFF, 0xEB, 0xCD), "BlanchedAlmond");
        namedColors.put(new RGB(0x00, 0x00, 0xFF), "Blue");
        namedColors.put(new RGB(0x8A, 0x2B, 0xE2), "BlueViolet");
        namedColors.put(new RGB(0xA5, 0x2A, 0x2A), "Brown");
        namedColors.put(new RGB(0xDE, 0xB8, 0x87), "BurlyWood");
        namedColors.put(new RGB(0x5F, 0x9E, 0xA0), "CadetBlue");
        namedColors.put(new RGB(0x7F, 0xFF, 0x00), "Chartreuse");
        namedColors.put(new RGB(0xD2, 0x69, 0x1E), "Chocolate");
        namedColors.put(new RGB(0xFF, 0x7F, 0x50), "Coral");
        namedColors.put(new RGB(0x64, 0x95, 0xED), "CornflowerBlue");
        namedColors.put(new RGB(0xFF, 0xF8, 0xDC), "Cornsilk");
        namedColors.put(new RGB(0xDC, 0x14, 0x3C), "Crimson");
        namedColors.put(new RGB(0x00, 0xFF, 0xFF), "Cyan");
        namedColors.put(new RGB(0x00, 0x00, 0x8B), "DarkBlue");
        namedColors.put(new RGB(0x00, 0x8B, 0x8B), "DarkCyan");
        namedColors.put(new RGB(0xB8, 0x86, 0x0B), "DarkGoldenRod");
        namedColors.put(new RGB(0xA9, 0xA9, 0xA9), "DarkGray");
        namedColors.put(new RGB(0x00, 0x64, 0x00), "DarkGreen");
        namedColors.put(new RGB(0xBD, 0xB7, 0x6B), "DarkKhaki");
        namedColors.put(new RGB(0x8B, 0x00, 0x8B), "DarkMagenta");
        namedColors.put(new RGB(0x55, 0x6B, 0x2F), "DarkOliveGreen");
        namedColors.put(new RGB(0xFF, 0x8C, 0x00), "DarkOrange");
        namedColors.put(new RGB(0x99, 0x32, 0xCC), "DarkOrchid");
        namedColors.put(new RGB(0x8B, 0x00, 0x00), "DarkRed");
        namedColors.put(new RGB(0xE9, 0x96, 0x7A), "DarkSalmon");
        namedColors.put(new RGB(0x8F, 0xBC, 0x8F), "DarkSeaGreen");
        namedColors.put(new RGB(0x48, 0x3D, 0x8B), "DarkSlateBlue");
        namedColors.put(new RGB(0x2F, 0x4F, 0x4F), "DarkSlateGray");
        namedColors.put(new RGB(0x00, 0xCE, 0xD1), "DarkTurquoise");
        namedColors.put(new RGB(0x94, 0x00, 0xD3), "DarkViolet");
        namedColors.put(new RGB(0xFF, 0x14, 0x93), "DeepPink");
        namedColors.put(new RGB(0x00, 0xBF, 0xFF), "DeepSkyBlue");
        namedColors.put(new RGB(0x69, 0x69, 0x69), "DimGray");
        namedColors.put(new RGB(0x1E, 0x90, 0xFF), "DodgerBlue");
        namedColors.put(new RGB(0xB2, 0x22, 0x22), "FireBrick");
        namedColors.put(new RGB(0xFF, 0xFA, 0xF0), "FloralWhite");
        namedColors.put(new RGB(0x22, 0x8B, 0x22), "ForestGreen");
        namedColors.put(new RGB(0xFF, 0x00, 0xFF), "Fuchsia");
        namedColors.put(new RGB(0xDC, 0xDC, 0xDC), "Gainsboro");
        namedColors.put(new RGB(0xF8, 0xF8, 0xFF), "GhostWhite");
        namedColors.put(new RGB(0xFF, 0xD7, 0x00), "Gold");
        namedColors.put(new RGB(0xDA, 0xA5, 0x20), "GoldenRod");
        namedColors.put(new RGB(0x80, 0x80, 0x80), "Gray");
        namedColors.put(new RGB(0x00, 0x80, 0x00), "Green");
        namedColors.put(new RGB(0xAD, 0xFF, 0x2F), "GreenYellow");
        namedColors.put(new RGB(0xF0, 0xFF, 0xF0), "HoneyDew");
        namedColors.put(new RGB(0xFF, 0x69, 0xB4), "HotPink");
        namedColors.put(new RGB(0xCD, 0x5C, 0x5C), "IndianRed");
        namedColors.put(new RGB(0x4B, 0x00, 0x82), "Indigo");
        namedColors.put(new RGB(0xFF, 0xFF, 0xF0), "Ivory");
        namedColors.put(new RGB(0xF0, 0xE6, 0x8C), "Khaki");
        namedColors.put(new RGB(0xE6, 0xE6, 0xFA), "Lavender");
        namedColors.put(new RGB(0xFF, 0xF0, 0xF5), "LavenderBlush");
        namedColors.put(new RGB(0x7C, 0xFC, 0x00), "LawnGreen");
        namedColors.put(new RGB(0xFF, 0xFA, 0xCD), "LemonChiffon");
        namedColors.put(new RGB(0xAD, 0xD8, 0xE6), "LightBlue");
        namedColors.put(new RGB(0xF0, 0x80, 0x80), "LightCoral");
        namedColors.put(new RGB(0xE0, 0xFF, 0xFF), "LightCyan");
        namedColors.put(new RGB(0xFA, 0xFA, 0xD2), "LightGoldenRodYellow");
        namedColors.put(new RGB(0xD3, 0xD3, 0xD3), "LightGray");
        namedColors.put(new RGB(0x90, 0xEE, 0x90), "LightGreen");
        namedColors.put(new RGB(0xFF, 0xB6, 0xC1), "LightPink");
        namedColors.put(new RGB(0xFF, 0xA0, 0x7A), "LightSalmon");
        namedColors.put(new RGB(0x20, 0xB2, 0xAA), "LightSeaGreen");
        namedColors.put(new RGB(0x87, 0xCE, 0xFA), "LightSkyBlue");
        namedColors.put(new RGB(0x77, 0x88, 0x99), "LightSlateGray");
        namedColors.put(new RGB(0xB0, 0xC4, 0xDE), "LightSteelBlue");
        namedColors.put(new RGB(0xFF, 0xFF, 0xE0), "LightYellow");
        namedColors.put(new RGB(0x00, 0xFF, 0x00), "Lime");
        namedColors.put(new RGB(0x32, 0xCD, 0x32), "LimeGreen");
        namedColors.put(new RGB(0xFA, 0xF0, 0xE6), "Linen");
        namedColors.put(new RGB(0xFF, 0x00, 0xFF), "Magenta");
        namedColors.put(new RGB(0x80, 0x00, 0x00), "Maroon");
        namedColors.put(new RGB(0x66, 0xCD, 0xAA), "MediumAquaMarine");
        namedColors.put(new RGB(0x00, 0x00, 0xCD), "MediumBlue");
        namedColors.put(new RGB(0xBA, 0x55, 0xD3), "MediumOrchid");
        namedColors.put(new RGB(0x93, 0x70, 0xDB), "MediumPurple");
        namedColors.put(new RGB(0x3C, 0xB3, 0x71), "MediumSeaGreen");
        namedColors.put(new RGB(0x7B, 0x68, 0xEE), "MediumSlateBlue");
        namedColors.put(new RGB(0x00, 0xFA, 0x9A), "MediumSpringGreen");
        namedColors.put(new RGB(0x48, 0xD1, 0xCC), "MediumTurquoise");
        namedColors.put(new RGB(0xC7, 0x15, 0x85), "MediumVioletRed");
        namedColors.put(new RGB(0x19, 0x19, 0x70), "MidnightBlue");
        namedColors.put(new RGB(0xF5, 0xFF, 0xFA), "MintCream");
        namedColors.put(new RGB(0xFF, 0xE4, 0xE1), "MistyRose");
        namedColors.put(new RGB(0xFF, 0xE4, 0xB5), "Moccasin");
        namedColors.put(new RGB(0xFF, 0xDE, 0xAD), "NavajoWhite");
        namedColors.put(new RGB(0x00, 0x00, 0x80), "Navy");
        namedColors.put(new RGB(0xFD, 0xF5, 0xE6), "OldLace");
        namedColors.put(new RGB(0x80, 0x80, 0x00), "Olive");
        namedColors.put(new RGB(0x6B, 0x8E, 0x23), "OliveDrab");
        namedColors.put(new RGB(0xFF, 0xA5, 0x00), "Orange");
        namedColors.put(new RGB(0xFF, 0x45, 0x00), "OrangeRed");
        namedColors.put(new RGB(0xDA, 0x70, 0xD6), "Orchid");
        namedColors.put(new RGB(0xEE, 0xE8, 0xAA), "PaleGoldenRod");
        namedColors.put(new RGB(0x98, 0xFB, 0x98), "PaleGreen");
        namedColors.put(new RGB(0xAF, 0xEE, 0xEE), "PaleTurquoise");
        namedColors.put(new RGB(0xDB, 0x70, 0x93), "PaleVioletRed");
        namedColors.put(new RGB(0xFF, 0xEF, 0xD5), "PapayaWhip");
        namedColors.put(new RGB(0xFF, 0xDA, 0xB9), "PeachPuff");
        namedColors.put(new RGB(0xCD, 0x85, 0x3F), "Peru");
        namedColors.put(new RGB(0xFF, 0xC0, 0xCB), "Pink");
        namedColors.put(new RGB(0xDD, 0xA0, 0xDD), "Plum");
        namedColors.put(new RGB(0xB0, 0xE0, 0xE6), "PowderBlue");
        namedColors.put(new RGB(0x80, 0x00, 0x80), "Purple");
        namedColors.put(new RGB(0xFF, 0x00, 0x00), "Red");
        namedColors.put(new RGB(0xBC, 0x8F, 0x8F), "RosyBrown");
        namedColors.put(new RGB(0x41, 0x69, 0xE1), "RoyalBlue");
        namedColors.put(new RGB(0x8B, 0x45, 0x13), "SaddleBrown");
        namedColors.put(new RGB(0xFA, 0x80, 0x72), "Salmon");
        namedColors.put(new RGB(0xF4, 0xA4, 0x60), "SandyBrown");
        namedColors.put(new RGB(0x2E, 0x8B, 0x57), "SeaGreen");
        namedColors.put(new RGB(0xFF, 0xF5, 0xEE), "SeaShell");
        namedColors.put(new RGB(0xA0, 0x52, 0x2D), "Sienna");
        namedColors.put(new RGB(0xC0, 0xC0, 0xC0), "Silver");
        namedColors.put(new RGB(0x87, 0xCE, 0xEB), "SkyBlue");
        namedColors.put(new RGB(0x6A, 0x5A, 0xCD), "SlateBlue");
        namedColors.put(new RGB(0x70, 0x80, 0x90), "SlateGray");
        namedColors.put(new RGB(0xFF, 0xFA, 0xFA), "Snow");
        namedColors.put(new RGB(0x00, 0xFF, 0x7F), "SpringGreen");
        namedColors.put(new RGB(0x46, 0x82, 0xB4), "SteelBlue");
        namedColors.put(new RGB(0xD2, 0xB4, 0x8C), "Tan");
        namedColors.put(new RGB(0x00, 0x80, 0x80), "Teal");
        namedColors.put(new RGB(0xD8, 0xBF, 0xD8), "Thistle");
        namedColors.put(new RGB(0xFF, 0x63, 0x47), "Tomato");
        namedColors.put(new RGB(0x40, 0xE0, 0xD0), "Turquoise");
        namedColors.put(new RGB(0xEE, 0x82, 0xEE), "Violet");
        namedColors.put(new RGB(0xF5, 0xDE, 0xB3), "Wheat");
        namedColors.put(new RGB(0xFF, 0xFF, 0xFF), "White");
        namedColors.put(new RGB(0xF5, 0xF5, 0xF5), "WhiteSmoke");
        namedColors.put(new RGB(0xFF, 0xFF, 0x00), "Yellow");
        namedColors.put(new RGB(0x9A, 0xCD, 0x32), "YellowGreen");
    }

    /**
     * Get the color name that is closest to the given RGB color value.
     *
     * @param r the red component of the color (0 - 255)
     * @param g the green component of the color (0 - 255)
     * @param b the blue component of the color (0 - 255)
     * @return the name of the color that is the closest to the given RGB
     * color value.
     */
    private static String getClosestNamedColor(int r, int g, int b) {
        Map.Entry<RGB, String> closestEntry = namedColors.entrySet().iterator().next();
        double minDistanceSeen = Integer.MAX_VALUE;
        for (Map.Entry<RGB, String> namedColor : namedColors.entrySet()) {
            double distance = calculateColorDistSq(Color.color(
                    r / 255.0, g / 255.0, b / 255.0), namedColor.getKey().getColor());
            if (distance < minDistanceSeen) {
                minDistanceSeen = distance;
                closestEntry = namedColor;
            }
        }

        return closestEntry.getValue();
    }

    /**
     * Get the color name of the color that is closest to the given hex color
     * value (as a 6-digit hex String).
     *
     * @param hexString the String containing 6 hex-digits representing a
     * color value ("000000" to "FFFFFF")
     * @return the name of the color that is the closest to the given hex
     * String color value
     */
    public static String getClosestNamedColor(String hexString) {
        int hexColor = Integer.parseInt(hexString, 16);
        int r = (hexColor & 0xFF0000) >> 16;
        int g = (hexColor & 0xFF00) >> 8;
        int b = hexColor & 0xFF;
        return getClosestNamedColor(r, g, b);
    }

    /**
     * Get the named color that has exactly the given RGB color value or
     * {@literal null} if no such named color exists.
     *
     * @param r the red component of the color (0 - 255)
     * @param g the green component of the color (0 - 255)
     * @param b the blue component of the color (0 - 255)
     * @return an Optional<String> that either contains the name of the color
     * with the exact same color value or nothing if no such named color exists
     */
    public static Optional<String> getNamedColor(int r, int g, int b) {
        return Optional.ofNullable(namedColors.get(new RGB(r, g, b)));
    }

    /**
     * Get the color name of the color that is exactly equal to the given hex color
     * value (as a 6-digit hex String).
     *
     * @param hexString the String containing 6 hex-digits representing a
     * color value ("000000" to "FFFFFF")
     * @return an Optional<String> that either contains the name of the color
     * with the exact same color value or nothing if no such named color exists
     */
    public static Optional<String> getNamedColor(String hexString) {
        int hexColor = Integer.parseInt(hexString, 16);
        int r = (hexColor & 0xFF0000) >> 16;
        int g = (hexColor & 0xFF00) >> 8;
        int b = hexColor & 0xFF;
        return getNamedColor(r, g, b);
    }

    /**
     * Calculates the distance between two Colors.
     *
     * @param color0 the first color
     * @param color1 the second color
     * @return the distance between the two colors
     */
    public static double calculateColorDistSq(Color color0,
                                              Color color1) {
        double diffRed = color0.getRed() - color1.getRed();
        double diffGreen = color0.getGreen() - color1.getGreen();
        double diffBlue = color0.getBlue() - color1.getBlue();
        return (diffRed * diffRed) + (diffGreen * diffGreen) + (diffBlue * diffBlue);
    }

    private static class RGB {
        private final int r;
        private final int g;
        private final int b;

        private RGB(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public Color getColor() {
            return Color.color(r / 255.0, g / 255.0, b / 255.0);
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }

            if (object == null || object.getClass() != getClass()) {
                return false;
            }

            RGB other = (RGB) object;

            return r == other.r && g == other.g && b == other.b;
        }

        @Override
        public int hashCode() {
            return Objects.hash(r, g, b);
        }
    }
}
