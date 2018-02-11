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
package org.testfx.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import javafx.scene.paint.Color;
import javafx.util.Pair;

/**
 * Contains {@link javafx.scene.paint.Color} utility methods.
 *
 * @see <a href="https://docs.oracle.com/javase/9/docs/api/javafx/scene/doc-files/cssref.html#typecolor">Named Colors</a>
 */
public final class ColorUtils {
    private static final List<Pair<String, Color>> NAMED_COLORS = new ArrayList<>(140);
    static {
        NAMED_COLORS.add(new Pair<>("ALICEBLUE", Color.ALICEBLUE));
        NAMED_COLORS.add(new Pair<>("ANTIQUEWHITE", Color.ANTIQUEWHITE));
        NAMED_COLORS.add(new Pair<>("AQUA", Color.AQUA));
        NAMED_COLORS.add(new Pair<>("AQUAMARINE", Color.AQUAMARINE));
        NAMED_COLORS.add(new Pair<>("AZURE", Color.AZURE));
        NAMED_COLORS.add(new Pair<>("BEIGE", Color.BEIGE));
        NAMED_COLORS.add(new Pair<>("BISQUE", Color.BISQUE));
        NAMED_COLORS.add(new Pair<>("BLACK", Color.BLACK));
        NAMED_COLORS.add(new Pair<>("BLANCHEDALMOND", Color.BLANCHEDALMOND));
        NAMED_COLORS.add(new Pair<>("BLUE", Color.BLUE));
        NAMED_COLORS.add(new Pair<>("BLUEVIOLET", Color.BLUEVIOLET));
        NAMED_COLORS.add(new Pair<>("BROWN", Color.BROWN));
        NAMED_COLORS.add(new Pair<>("BURLYWOOD", Color.BURLYWOOD));
        NAMED_COLORS.add(new Pair<>("CADETBLUE", Color.CADETBLUE));
        NAMED_COLORS.add(new Pair<>("CHARTREUSE", Color.CHARTREUSE));
        NAMED_COLORS.add(new Pair<>("CHOCOLATE", Color.CHOCOLATE));
        NAMED_COLORS.add(new Pair<>("CORAL", Color.CORAL));
        NAMED_COLORS.add(new Pair<>("CORNFLOWERBLUE", Color.CORNFLOWERBLUE));
        NAMED_COLORS.add(new Pair<>("CORNSILK", Color.CORNSILK));
        NAMED_COLORS.add(new Pair<>("CRIMSON", Color.CRIMSON));
        NAMED_COLORS.add(new Pair<>("CYAN", Color.CYAN));
        NAMED_COLORS.add(new Pair<>("DARKBLUE", Color.DARKBLUE));
        NAMED_COLORS.add(new Pair<>("DARKCYAN", Color.DARKCYAN));
        NAMED_COLORS.add(new Pair<>("DARKGOLDENROD", Color.DARKGOLDENROD));
        NAMED_COLORS.add(new Pair<>("DARKGRAY", Color.DARKGRAY));
        NAMED_COLORS.add(new Pair<>("DARKGREEN", Color.DARKGREEN));
        NAMED_COLORS.add(new Pair<>("DARKKHAKI", Color.DARKKHAKI));
        NAMED_COLORS.add(new Pair<>("DARKMAGENTA", Color.DARKMAGENTA));
        NAMED_COLORS.add(new Pair<>("DARKOLIVEGREEN", Color.DARKOLIVEGREEN));
        NAMED_COLORS.add(new Pair<>("DARKORANGE", Color.DARKORANGE));
        NAMED_COLORS.add(new Pair<>("DARKORCHID", Color.DARKORCHID));
        NAMED_COLORS.add(new Pair<>("DARKRED", Color.DARKRED));
        NAMED_COLORS.add(new Pair<>("DARKSALMON", Color.DARKSALMON));
        NAMED_COLORS.add(new Pair<>("DARKSEAGREEN", Color.DARKSEAGREEN));
        NAMED_COLORS.add(new Pair<>("DARKSLATEBLUE", Color.DARKSLATEBLUE));
        NAMED_COLORS.add(new Pair<>("DARKSLATEGRAY", Color.DARKSLATEGRAY));
        NAMED_COLORS.add(new Pair<>("DARKTURQUOISE", Color.DARKTURQUOISE));
        NAMED_COLORS.add(new Pair<>("DARKVIOLET", Color.DARKVIOLET));
        NAMED_COLORS.add(new Pair<>("DEEPPINK", Color.DEEPPINK));
        NAMED_COLORS.add(new Pair<>("DEEPSKYBLUE", Color.DEEPSKYBLUE));
        NAMED_COLORS.add(new Pair<>("DIMGRAY", Color.DIMGRAY));
        NAMED_COLORS.add(new Pair<>("DODGERBLUE", Color.DODGERBLUE));
        NAMED_COLORS.add(new Pair<>("FIREBRICK", Color.FIREBRICK));
        NAMED_COLORS.add(new Pair<>("FLORALWHITE", Color.FLORALWHITE));
        NAMED_COLORS.add(new Pair<>("FORESTGREEN", Color.FORESTGREEN));
        NAMED_COLORS.add(new Pair<>("FUCHSIA", Color.FUCHSIA));
        NAMED_COLORS.add(new Pair<>("GAINSBORO", Color.GAINSBORO));
        NAMED_COLORS.add(new Pair<>("GHOSTWHITE", Color.GHOSTWHITE));
        NAMED_COLORS.add(new Pair<>("GOLD", Color.GOLD));
        NAMED_COLORS.add(new Pair<>("GOLDENROD", Color.GOLDENROD));
        NAMED_COLORS.add(new Pair<>("GRAY", Color.GRAY));
        NAMED_COLORS.add(new Pair<>("GREEN", Color.GREEN));
        NAMED_COLORS.add(new Pair<>("GREENYELLOW", Color.GREENYELLOW));
        NAMED_COLORS.add(new Pair<>("HONEYDEW", Color.HONEYDEW));
        NAMED_COLORS.add(new Pair<>("HOTPINK", Color.HOTPINK));
        NAMED_COLORS.add(new Pair<>("INDIANRED", Color.INDIANRED));
        NAMED_COLORS.add(new Pair<>("INDIGO", Color.INDIGO));
        NAMED_COLORS.add(new Pair<>("IVORY", Color.IVORY));
        NAMED_COLORS.add(new Pair<>("KHAKI", Color.KHAKI));
        NAMED_COLORS.add(new Pair<>("LAVENDER", Color.LAVENDER));
        NAMED_COLORS.add(new Pair<>("LAVENDERBLUSH", Color.LAVENDERBLUSH));
        NAMED_COLORS.add(new Pair<>("LAWNGREEN", Color.LAWNGREEN));
        NAMED_COLORS.add(new Pair<>("LEMONCHIFFON", Color.LEMONCHIFFON));
        NAMED_COLORS.add(new Pair<>("LIGHTBLUE", Color.LIGHTBLUE));
        NAMED_COLORS.add(new Pair<>("LIGHTCORAL", Color.LIGHTCORAL));
        NAMED_COLORS.add(new Pair<>("LIGHTCYAN", Color.LIGHTCYAN));
        NAMED_COLORS.add(new Pair<>("LIGHTGOLDENRODYELLOW", Color.LIGHTGOLDENRODYELLOW));
        NAMED_COLORS.add(new Pair<>("LIGHTGRAY", Color.LIGHTGRAY));
        NAMED_COLORS.add(new Pair<>("LIGHTGREEN", Color.LIGHTGREEN));
        NAMED_COLORS.add(new Pair<>("LIGHTPINK", Color.LIGHTPINK));
        NAMED_COLORS.add(new Pair<>("LIGHTSALMON", Color.LIGHTSALMON));
        NAMED_COLORS.add(new Pair<>("LIGHTSEAGREEN", Color.LIGHTSEAGREEN));
        NAMED_COLORS.add(new Pair<>("LIGHTSKYBLUE", Color.LIGHTSKYBLUE));
        NAMED_COLORS.add(new Pair<>("LIGHTSLATEGRAY", Color.LIGHTSLATEGRAY));
        NAMED_COLORS.add(new Pair<>("LIGHTSTEELBLUE", Color.LIGHTSTEELBLUE));
        NAMED_COLORS.add(new Pair<>("LIGHTYELLOW", Color.LIGHTYELLOW));
        NAMED_COLORS.add(new Pair<>("LIME", Color.LIME));
        NAMED_COLORS.add(new Pair<>("LIMEGREEN", Color.LIMEGREEN));
        NAMED_COLORS.add(new Pair<>("LINEN", Color.LINEN));
        NAMED_COLORS.add(new Pair<>("MAGENTA", Color.MAGENTA));
        NAMED_COLORS.add(new Pair<>("MAROON", Color.MAROON));
        NAMED_COLORS.add(new Pair<>("MEDIUMAQUAMARINE", Color.MEDIUMAQUAMARINE));
        NAMED_COLORS.add(new Pair<>("MEDIUMBLUE", Color.MEDIUMBLUE));
        NAMED_COLORS.add(new Pair<>("MEDIUMORCHID", Color.MEDIUMORCHID));
        NAMED_COLORS.add(new Pair<>("MEDIUMPURPLE", Color.MEDIUMPURPLE));
        NAMED_COLORS.add(new Pair<>("MEDIUMSEAGREEN", Color.MEDIUMSEAGREEN));
        NAMED_COLORS.add(new Pair<>("MEDIUMSLATEBLUE", Color.MEDIUMSLATEBLUE));
        NAMED_COLORS.add(new Pair<>("MEDIUMSPRINGGREEN", Color.MEDIUMSPRINGGREEN));
        NAMED_COLORS.add(new Pair<>("MEDIUMTURQUOISE", Color.MEDIUMTURQUOISE));
        NAMED_COLORS.add(new Pair<>("MEDIUMVIOLETRED", Color.MEDIUMVIOLETRED));
        NAMED_COLORS.add(new Pair<>("MIDNIGHTBLUE", Color.MIDNIGHTBLUE));
        NAMED_COLORS.add(new Pair<>("MINTCREAM", Color.MINTCREAM));
        NAMED_COLORS.add(new Pair<>("MISTYROSE", Color.MISTYROSE));
        NAMED_COLORS.add(new Pair<>("MOCCASIN", Color.MOCCASIN));
        NAMED_COLORS.add(new Pair<>("NAVAJOWHITE", Color.NAVAJOWHITE));
        NAMED_COLORS.add(new Pair<>("NAVY", Color.NAVY));
        NAMED_COLORS.add(new Pair<>("OLDLACE", Color.OLDLACE));
        NAMED_COLORS.add(new Pair<>("OLIVE", Color.OLIVE));
        NAMED_COLORS.add(new Pair<>("OLIVEDRAB", Color.OLIVEDRAB));
        NAMED_COLORS.add(new Pair<>("ORANGE", Color.ORANGE));
        NAMED_COLORS.add(new Pair<>("ORANGERED", Color.ORANGERED));
        NAMED_COLORS.add(new Pair<>("ORCHID", Color.ORCHID));
        NAMED_COLORS.add(new Pair<>("PALEGOLDENROD", Color.PALEGOLDENROD));
        NAMED_COLORS.add(new Pair<>("PALEGREEN", Color.PALEGREEN));
        NAMED_COLORS.add(new Pair<>("PALETURQUOISE", Color.PALETURQUOISE));
        NAMED_COLORS.add(new Pair<>("PALEVIOLETRED", Color.PALEVIOLETRED));
        NAMED_COLORS.add(new Pair<>("PAPAYAWHIP", Color.PAPAYAWHIP));
        NAMED_COLORS.add(new Pair<>("PEACHPUFF", Color.PEACHPUFF));
        NAMED_COLORS.add(new Pair<>("PERU", Color.PERU));
        NAMED_COLORS.add(new Pair<>("PINK", Color.PINK));
        NAMED_COLORS.add(new Pair<>("PLUM", Color.PLUM));
        NAMED_COLORS.add(new Pair<>("POWDERBLUE", Color.POWDERBLUE));
        NAMED_COLORS.add(new Pair<>("PURPLE", Color.PURPLE));
        NAMED_COLORS.add(new Pair<>("RED", Color.RED));
        NAMED_COLORS.add(new Pair<>("ROSYBROWN", Color.ROSYBROWN));
        NAMED_COLORS.add(new Pair<>("ROYALBLUE", Color.ROYALBLUE));
        NAMED_COLORS.add(new Pair<>("SADDLEBROWN", Color.SADDLEBROWN));
        NAMED_COLORS.add(new Pair<>("SALMON", Color.SALMON));
        NAMED_COLORS.add(new Pair<>("SANDYBROWN", Color.SANDYBROWN));
        NAMED_COLORS.add(new Pair<>("SEAGREEN", Color.SEAGREEN));
        NAMED_COLORS.add(new Pair<>("SEASHELL", Color.SEASHELL));
        NAMED_COLORS.add(new Pair<>("SIENNA", Color.SIENNA));
        NAMED_COLORS.add(new Pair<>("SILVER", Color.SILVER));
        NAMED_COLORS.add(new Pair<>("SKYBLUE", Color.SKYBLUE));
        NAMED_COLORS.add(new Pair<>("SLATEBLUE", Color.SLATEBLUE));
        NAMED_COLORS.add(new Pair<>("SLATEGRAY", Color.SLATEGRAY));
        NAMED_COLORS.add(new Pair<>("SNOW", Color.SNOW));
        NAMED_COLORS.add(new Pair<>("SPRINGGREEN", Color.SPRINGGREEN));
        NAMED_COLORS.add(new Pair<>("STEELBLUE", Color.STEELBLUE));
        NAMED_COLORS.add(new Pair<>("TAN", Color.TAN));
        NAMED_COLORS.add(new Pair<>("TEAL", Color.TEAL));
        NAMED_COLORS.add(new Pair<>("THISTLE", Color.THISTLE));
        NAMED_COLORS.add(new Pair<>("TOMATO", Color.TOMATO));
        NAMED_COLORS.add(new Pair<>("TURQUOISE", Color.TURQUOISE));
        NAMED_COLORS.add(new Pair<>("VIOLET", Color.VIOLET));
        NAMED_COLORS.add(new Pair<>("WHEAT", Color.WHEAT));
        NAMED_COLORS.add(new Pair<>("WHITE", Color.WHITE));
        NAMED_COLORS.add(new Pair<>("WHITESMOKE", Color.WHITESMOKE));
        NAMED_COLORS.add(new Pair<>("YELLOW", Color.YELLOW));
        NAMED_COLORS.add(new Pair<>("YELLOWGREEN", Color.YELLOWGREEN));
    }

    private ColorUtils() {}

    /**
     * Returns the named color that is closest to the given color, supplied as an RGB
     * color value.
     *
     * @param r the red component of the color (0 - 255)
     * @param g the green component of the color (0 - 255)
     * @param b the blue component of the color (0 - 255)
     * @return the named color that is closest to the given RGB color value
     */
    private static String getClosestNamedColor(int r, int g, int b) {
        checkColorTriple(r, g, b);
        Pair<String, Color> closestColorPair = NAMED_COLORS.get(0); // Pick something to start with.
        double minDistanceSeen = Integer.MAX_VALUE;
        for (Pair<String, Color> namedColorPair : NAMED_COLORS) {
            double distance = calculateColorDistSq(Color.color(
                    r / 255.0, g / 255.0, b / 255.0), namedColorPair.getValue());
            if (distance < minDistanceSeen) {
                minDistanceSeen = distance;
                closestColorPair = namedColorPair;
            }
        }

        return closestColorPair.getKey();
    }

    /**
     * Returns the named color that is closest to the given hex code color value.
     * <p>
     * Only the first 6 bytes of {@code hexColor} are taken into account. That is two bytes
     * for each red, blue, and green component.
     *
     * @param hexColor the {@code Integer} hex color value of the named color to return
     * @return the named color that is closest to the given {@code hexString} color
     */
    public static String getClosestNamedColor(Integer hexColor) {
        Objects.requireNonNull(hexColor, "hexColor must not be null");
        int r = (hexColor & 0xFF0000) >> 16;
        int g = (hexColor & 0xFF00) >> 8;
        int b = hexColor & 0xFF;
        return getClosestNamedColor(r, g, b);
    }

    /**
     * Returns the named color that is closest to the given {@code color}.
     *
     * @param color the color to find the closest named color for
     * @return the named color that is closest to the given {@code color}
     */
    public static Color getClosestNamedColor(Color color) {
        Objects.requireNonNull(color, "color must not be null");
        return Color.valueOf(getClosestNamedColor((int) (color.getRed() * 255d), (int) (color.getGreen() * 255d),
                (int) (color.getBlue() * 255d)));
    }

    /**
     * Returns an {@code Optional} with a value of the named color that has exactly
     * the given RGB color value, if it exists. Otherwise returns an empty {@code Optional}.
     *
     * @param r the red component of the color (0 - 255)
     * @param g the green component of the color (0 - 255)
     * @param b the blue component of the color (0 - 255)
     * @return an {@code Optional<String>} that either contains the name of the color
     * with the exact same color value or nothing (i.e. an empty {@code Optional} if
     * no such named color exists
     */
    private static Optional<String> getNamedColor(int r, int g, int b) {
        checkColorTriple(r, g, b);
        for (Pair<String, Color> namedColorPair : NAMED_COLORS) {
            if (Color.rgb(r, g, b).equals(namedColorPair.getValue())) {
                return Optional.of(namedColorPair.getKey());
            }
        }
        return Optional.empty();
    }

    /**
     * Returns an {@code Optional} with a value of the named {@code Color} that has
     * the given {@code name}, if it exists. Otherwise returns an empty {@code Optional}.
     *
     * @param name the name of the named {@code Color} to return
     * @return an {@code Optional<Color>} that either contains the named color
     * with the given {@code name} or nothing (i.e. an empty {@code Optional}
     * if no such named color exists
     */
    public static Optional<Color> getNamedColor(String name) {
        Objects.requireNonNull(name, "name must not be null");
        for (Pair<String, Color> namedColorPair : NAMED_COLORS) {
            if (name.toUpperCase(Locale.US).equals(namedColorPair.getKey())) {
                return Optional.of(namedColorPair.getValue());
            }
        }

        return Optional.empty();
    }

    /**
     * Returns an {@code Optional} with a value of the named {@code Color} that is equal
     * to the given {@code color}, if it exists. Otherwise returns an empty {@code Optional}.
     *
     * @param color the color of the named color to return
     * @return an {@code Optional<Color>} that either contains the named color
     * that is equal to the given {@code color} or nothing (i.e. an empty {@code Optional})
     * if no such named color exists
     */
    public static Optional<Color> getNamedColor(Color color) {
        Objects.requireNonNull(color, "color must not be null");
        for (Pair<String, Color> namedColorPair : NAMED_COLORS) {
            if (color.equals(namedColorPair.getValue())) {
                return Optional.of(namedColorPair.getValue());
            }
        }

        return Optional.empty();
    }

    /**
     * Returns the named color that is exactly equal to the given hex code color value.
     * <p>
     * Only the first 6 bytes of {@code hexColor} are taken into account. That is two bytes
     * for each red, blue, and green component.
     *
     * @param hexColor the {@code Integer} hex color value of the named color to return
     * @return an {@code Optional<String>} that either contains the named color that equals
     * the given {@code hexColor} or nothing if no such named color exists
     */
    public static Optional<String> getNamedColor(Integer hexColor) {
        Objects.requireNonNull(hexColor, "hexColor must not be null");
        int r = (hexColor & 0xFF0000) >> 16;
        int g = (hexColor & 0xFF00) >> 8;
        int b = hexColor & 0xFF;
        return getNamedColor(r, g, b);
    }

    /**
     * Calculates and returns the distance between two {@code Color}s, using a simple
     * Euclidean metric.
     *
     * @param color0 the first color
     * @param color1 the second color
     * @return the distance between the two colors
     */
    public static double calculateColorDistSq(Color color0, Color color1) {
        Objects.requireNonNull(color0, "color0 must not be null");
        Objects.requireNonNull(color1, "color1 must not be null");
        double diffRed = color0.getRed() - color1.getRed();
        double diffGreen = color0.getGreen() - color1.getGreen();
        double diffBlue = color0.getBlue() - color1.getBlue();
        return (diffRed * diffRed) + (diffGreen * diffGreen) + (diffBlue * diffBlue);
    }

    private static void checkColorTriple(int r, int g, int b) {
        if (r < 0 || r > 255) {
            throw new IllegalArgumentException("r must be in range [0, 255] but was: " + r);
        }
        if (g < 0 || g > 255) {
            throw new IllegalArgumentException("g must be in range [0, 255] but was: " + g);
        }
        if (b < 0 || b > 255) {
            throw new IllegalArgumentException("b must be in range [0, 255] but was: " + b);
        }
    }

}
