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

import java.util.ArrayList;

import javafx.scene.paint.Color;

/**
 * Provides utilities for working with JavaFX Colors.
 *
 * @see <a href="https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html#typecolor">Named Colors</a>
 */
public class ColorUtils
{
    private static ArrayList<NamedColor> namedColors = new ArrayList<>();
    static {
        namedColors.add(new NamedColor("AliceBlue", 0xF0, 0xF8, 0xFF));
        namedColors.add(new NamedColor("AntiqueWhite", 0xFA, 0xEB, 0xD7));
        namedColors.add(new NamedColor("Aqua", 0x00, 0xFF, 0xFF));
        namedColors.add(new NamedColor("Aquamarine", 0x7F, 0xFF, 0xD4));
        namedColors.add(new NamedColor("Azure", 0xF0, 0xFF, 0xFF));
        namedColors.add(new NamedColor("Beige", 0xF5, 0xF5, 0xDC));
        namedColors.add(new NamedColor("Bisque", 0xFF, 0xE4, 0xC4));
        namedColors.add(new NamedColor("Black", 0x00, 0x00, 0x00));
        namedColors.add(new NamedColor("BlanchedAlmond", 0xFF, 0xEB, 0xCD));
        namedColors.add(new NamedColor("Blue", 0x00, 0x00, 0xFF));
        namedColors.add(new NamedColor("BlueViolet", 0x8A, 0x2B, 0xE2));
        namedColors.add(new NamedColor("Brown", 0xA5, 0x2A, 0x2A));
        namedColors.add(new NamedColor("BurlyWood", 0xDE, 0xB8, 0x87));
        namedColors.add(new NamedColor("CadetBlue", 0x5F, 0x9E, 0xA0));
        namedColors.add(new NamedColor("Chartreuse", 0x7F, 0xFF, 0x00));
        namedColors.add(new NamedColor("Chocolate", 0xD2, 0x69, 0x1E));
        namedColors.add(new NamedColor("Coral", 0xFF, 0x7F, 0x50));
        namedColors.add(new NamedColor("CornflowerBlue", 0x64, 0x95, 0xED));
        namedColors.add(new NamedColor("Cornsilk", 0xFF, 0xF8, 0xDC));
        namedColors.add(new NamedColor("Crimson", 0xDC, 0x14, 0x3C));
        namedColors.add(new NamedColor("Cyan", 0x00, 0xFF, 0xFF));
        namedColors.add(new NamedColor("DarkBlue", 0x00, 0x00, 0x8B));
        namedColors.add(new NamedColor("DarkCyan", 0x00, 0x8B, 0x8B));
        namedColors.add(new NamedColor("DarkGoldenRod", 0xB8, 0x86, 0x0B));
        namedColors.add(new NamedColor("DarkGray", 0xA9, 0xA9, 0xA9));
        namedColors.add(new NamedColor("DarkGreen", 0x00, 0x64, 0x00));
        namedColors.add(new NamedColor("DarkKhaki", 0xBD, 0xB7, 0x6B));
        namedColors.add(new NamedColor("DarkMagenta", 0x8B, 0x00, 0x8B));
        namedColors.add(new NamedColor("DarkOliveGreen", 0x55, 0x6B, 0x2F));
        namedColors.add(new NamedColor("DarkOrange", 0xFF, 0x8C, 0x00));
        namedColors.add(new NamedColor("DarkOrchid", 0x99, 0x32, 0xCC));
        namedColors.add(new NamedColor("DarkRed", 0x8B, 0x00, 0x00));
        namedColors.add(new NamedColor("DarkSalmon", 0xE9, 0x96, 0x7A));
        namedColors.add(new NamedColor("DarkSeaGreen", 0x8F, 0xBC, 0x8F));
        namedColors.add(new NamedColor("DarkSlateBlue", 0x48, 0x3D, 0x8B));
        namedColors.add(new NamedColor("DarkSlateGray", 0x2F, 0x4F, 0x4F));
        namedColors.add(new NamedColor("DarkTurquoise", 0x00, 0xCE, 0xD1));
        namedColors.add(new NamedColor("DarkViolet", 0x94, 0x00, 0xD3));
        namedColors.add(new NamedColor("DeepPink", 0xFF, 0x14, 0x93));
        namedColors.add(new NamedColor("DeepSkyBlue", 0x00, 0xBF, 0xFF));
        namedColors.add(new NamedColor("DimGray", 0x69, 0x69, 0x69));
        namedColors.add(new NamedColor("DodgerBlue", 0x1E, 0x90, 0xFF));
        namedColors.add(new NamedColor("FireBrick", 0xB2, 0x22, 0x22));
        namedColors.add(new NamedColor("FloralWhite", 0xFF, 0xFA, 0xF0));
        namedColors.add(new NamedColor("ForestGreen", 0x22, 0x8B, 0x22));
        namedColors.add(new NamedColor("Fuchsia", 0xFF, 0x00, 0xFF));
        namedColors.add(new NamedColor("Gainsboro", 0xDC, 0xDC, 0xDC));
        namedColors.add(new NamedColor("GhostWhite", 0xF8, 0xF8, 0xFF));
        namedColors.add(new NamedColor("Gold", 0xFF, 0xD7, 0x00));
        namedColors.add(new NamedColor("GoldenRod", 0xDA, 0xA5, 0x20));
        namedColors.add(new NamedColor("Gray", 0x80, 0x80, 0x80));
        namedColors.add(new NamedColor("Green", 0x00, 0x80, 0x00));
        namedColors.add(new NamedColor("GreenYellow", 0xAD, 0xFF, 0x2F));
        namedColors.add(new NamedColor("HoneyDew", 0xF0, 0xFF, 0xF0));
        namedColors.add(new NamedColor("HotPink", 0xFF, 0x69, 0xB4));
        namedColors.add(new NamedColor("IndianRed", 0xCD, 0x5C, 0x5C));
        namedColors.add(new NamedColor("Indigo", 0x4B, 0x00, 0x82));
        namedColors.add(new NamedColor("Ivory", 0xFF, 0xFF, 0xF0));
        namedColors.add(new NamedColor("Khaki", 0xF0, 0xE6, 0x8C));
        namedColors.add(new NamedColor("Lavender", 0xE6, 0xE6, 0xFA));
        namedColors.add(new NamedColor("LavenderBlush", 0xFF, 0xF0, 0xF5));
        namedColors.add(new NamedColor("LawnGreen", 0x7C, 0xFC, 0x00));
        namedColors.add(new NamedColor("LemonChiffon", 0xFF, 0xFA, 0xCD));
        namedColors.add(new NamedColor("LightBlue", 0xAD, 0xD8, 0xE6));
        namedColors.add(new NamedColor("LightCoral", 0xF0, 0x80, 0x80));
        namedColors.add(new NamedColor("LightCyan", 0xE0, 0xFF, 0xFF));
        namedColors.add(new NamedColor("LightGoldenRodYellow", 0xFA, 0xFA, 0xD2));
        namedColors.add(new NamedColor("LightGray", 0xD3, 0xD3, 0xD3));
        namedColors.add(new NamedColor("LightGreen", 0x90, 0xEE, 0x90));
        namedColors.add(new NamedColor("LightPink", 0xFF, 0xB6, 0xC1));
        namedColors.add(new NamedColor("LightSalmon", 0xFF, 0xA0, 0x7A));
        namedColors.add(new NamedColor("LightSeaGreen", 0x20, 0xB2, 0xAA));
        namedColors.add(new NamedColor("LightSkyBlue", 0x87, 0xCE, 0xFA));
        namedColors.add(new NamedColor("LightSlateGray", 0x77, 0x88, 0x99));
        namedColors.add(new NamedColor("LightSteelBlue", 0xB0, 0xC4, 0xDE));
        namedColors.add(new NamedColor("LightYellow", 0xFF, 0xFF, 0xE0));
        namedColors.add(new NamedColor("Lime", 0x00, 0xFF, 0x00));
        namedColors.add(new NamedColor("LimeGreen", 0x32, 0xCD, 0x32));
        namedColors.add(new NamedColor("Linen", 0xFA, 0xF0, 0xE6));
        namedColors.add(new NamedColor("Magenta", 0xFF, 0x00, 0xFF));
        namedColors.add(new NamedColor("Maroon", 0x80, 0x00, 0x00));
        namedColors.add(new NamedColor("MediumAquaMarine", 0x66, 0xCD, 0xAA));
        namedColors.add(new NamedColor("MediumBlue", 0x00, 0x00, 0xCD));
        namedColors.add(new NamedColor("MediumOrchid", 0xBA, 0x55, 0xD3));
        namedColors.add(new NamedColor("MediumPurple", 0x93, 0x70, 0xDB));
        namedColors.add(new NamedColor("MediumSeaGreen", 0x3C, 0xB3, 0x71));
        namedColors.add(new NamedColor("MediumSlateBlue", 0x7B, 0x68, 0xEE));
        namedColors.add(new NamedColor("MediumSpringGreen", 0x00, 0xFA, 0x9A));
        namedColors.add(new NamedColor("MediumTurquoise", 0x48, 0xD1, 0xCC));
        namedColors.add(new NamedColor("MediumVioletRed", 0xC7, 0x15, 0x85));
        namedColors.add(new NamedColor("MidnightBlue", 0x19, 0x19, 0x70));
        namedColors.add(new NamedColor("MintCream", 0xF5, 0xFF, 0xFA));
        namedColors.add(new NamedColor("MistyRose", 0xFF, 0xE4, 0xE1));
        namedColors.add(new NamedColor("Moccasin", 0xFF, 0xE4, 0xB5));
        namedColors.add(new NamedColor("NavajoWhite", 0xFF, 0xDE, 0xAD));
        namedColors.add(new NamedColor("Navy", 0x00, 0x00, 0x80));
        namedColors.add(new NamedColor("OldLace", 0xFD, 0xF5, 0xE6));
        namedColors.add(new NamedColor("Olive", 0x80, 0x80, 0x00));
        namedColors.add(new NamedColor("OliveDrab", 0x6B, 0x8E, 0x23));
        namedColors.add(new NamedColor("Orange", 0xFF, 0xA5, 0x00));
        namedColors.add(new NamedColor("OrangeRed", 0xFF, 0x45, 0x00));
        namedColors.add(new NamedColor("Orchid", 0xDA, 0x70, 0xD6));
        namedColors.add(new NamedColor("PaleGoldenRod", 0xEE, 0xE8, 0xAA));
        namedColors.add(new NamedColor("PaleGreen", 0x98, 0xFB, 0x98));
        namedColors.add(new NamedColor("PaleTurquoise", 0xAF, 0xEE, 0xEE));
        namedColors.add(new NamedColor("PaleVioletRed", 0xDB, 0x70, 0x93));
        namedColors.add(new NamedColor("PapayaWhip", 0xFF, 0xEF, 0xD5));
        namedColors.add(new NamedColor("PeachPuff", 0xFF, 0xDA, 0xB9));
        namedColors.add(new NamedColor("Peru", 0xCD, 0x85, 0x3F));
        namedColors.add(new NamedColor("Pink", 0xFF, 0xC0, 0xCB));
        namedColors.add(new NamedColor("Plum", 0xDD, 0xA0, 0xDD));
        namedColors.add(new NamedColor("PowderBlue", 0xB0, 0xE0, 0xE6));
        namedColors.add(new NamedColor("Purple", 0x80, 0x00, 0x80));
        namedColors.add(new NamedColor("Red", 0xFF, 0x00, 0x00));
        namedColors.add(new NamedColor("RosyBrown", 0xBC, 0x8F, 0x8F));
        namedColors.add(new NamedColor("RoyalBlue", 0x41, 0x69, 0xE1));
        namedColors.add(new NamedColor("SaddleBrown", 0x8B, 0x45, 0x13));
        namedColors.add(new NamedColor("Salmon", 0xFA, 0x80, 0x72));
        namedColors.add(new NamedColor("SandyBrown", 0xF4, 0xA4, 0x60));
        namedColors.add(new NamedColor("SeaGreen", 0x2E, 0x8B, 0x57));
        namedColors.add(new NamedColor("SeaShell", 0xFF, 0xF5, 0xEE));
        namedColors.add(new NamedColor("Sienna", 0xA0, 0x52, 0x2D));
        namedColors.add(new NamedColor("Silver", 0xC0, 0xC0, 0xC0));
        namedColors.add(new NamedColor("SkyBlue", 0x87, 0xCE, 0xEB));
        namedColors.add(new NamedColor("SlateBlue", 0x6A, 0x5A, 0xCD));
        namedColors.add(new NamedColor("SlateGray", 0x70, 0x80, 0x90));
        namedColors.add(new NamedColor("Snow", 0xFF, 0xFA, 0xFA));
        namedColors.add(new NamedColor("SpringGreen", 0x00, 0xFF, 0x7F));
        namedColors.add(new NamedColor("SteelBlue", 0x46, 0x82, 0xB4));
        namedColors.add(new NamedColor("Tan", 0xD2, 0xB4, 0x8C));
        namedColors.add(new NamedColor("Teal", 0x00, 0x80, 0x80));
        namedColors.add(new NamedColor("Thistle", 0xD8, 0xBF, 0xD8));
        namedColors.add(new NamedColor("Tomato", 0xFF, 0x63, 0x47));
        namedColors.add(new NamedColor("Turquoise", 0x40, 0xE0, 0xD0));
        namedColors.add(new NamedColor("Violet", 0xEE, 0x82, 0xEE));
        namedColors.add(new NamedColor("Wheat", 0xF5, 0xDE, 0xB3));
        namedColors.add(new NamedColor("White", 0xFF, 0xFF, 0xFF));
        namedColors.add(new NamedColor("WhiteSmoke", 0xF5, 0xF5, 0xF5));
        namedColors.add(new NamedColor("Yellow", 0xFF, 0xFF, 0x00));
        namedColors.add(new NamedColor("YellowGreen", 0x9A, 0xCD, 0x32));
    }

    /**
     * Get the color name that is closest to the given RGB color.
     *
     * @param r the red component of the color (0 - 255)
     * @param g the green component of the color (0 - 255)
     * @param b the blue component of the color (0 - 255)
     * @return the name of the color that is the closest to the given RGB color
     */
    private static String getColorNameFromRgb(int r, int g, int b) {
        NamedColor closestMatch = namedColors.get(0);
        double minDistanceSeen = Integer.MAX_VALUE;
        for (NamedColor namedColor : namedColors) {
            double distance = calculateColorDistSq(Color.color(r / 255.0, g / 255.0, b / 255.0),
                    namedColor.getColor());
            if (distance < minDistanceSeen) {
                minDistanceSeen = distance;
                closestMatch = namedColor;
            }
        }

        return closestMatch.name;
    }

    /**
     * Get the color name of the color that is closest to the given hex color
     * (as an int).
     *
     * @param hexColor the color as an int from 0x000000 - 0xFFFFFF
     * @return the name of the color that is closest to the given hex color
     */
    public static String getColorNameFromHex(int hexColor) {
        int r = (hexColor & 0xFF0000) >> 16;
        int g = (hexColor & 0xFF00) >> 8;
        int b = (hexColor & 0xFF);
        return getColorNameFromRgb(r, g, b);
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

    /**
     * Get the color name of the color that is closest to the given hex color
     * (as a 6-digit hex String).
     *
     * @param hexString the String containing 6 hex-digits representing a
     * color ("000000" to "FFFFFF")
     * @return the name of the color that is the closest to the given hex
     * String color.
     */
    public static String getColorNameFromHex(String hexString) {
        int hexColor = Integer.parseInt(hexString, 16);
        int r = (hexColor & 0xFF0000) >> 16;
        int g = (hexColor & 0xFF00) >> 8;
        int b = (hexColor & 0xFF);
        return getColorNameFromRgb(r, g, b);
    }

    private static class NamedColor
    {
        private final int r;
        private final int g;
        private final int b;
        private final String name;

        private NamedColor(String name, int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.name = name;
        }

        public Color getColor()
        {
            return Color.color(r / 255.0, g / 255.0, b / 255.0);
        }
    }
}
