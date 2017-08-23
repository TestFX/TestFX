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
package org.testfx.util;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import org.hamcrest.Matcher;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.service.support.FiredEvents;

/**
 * Utility class for displaying additional info or running code when a test fails using
 * {@link org.testfx.api.FxAssert#verifyThat(Node, Matcher)} or its related methods.
 *
 * <p>
 *     Quickly chain things together using {@link #compose(Function[])} or use standard handlers like
 *     {@link #informedErrorMessage(String, boolean, FxRobot, boolean, boolean)}. Any {@code tab} parameters in methods
 *     are there to specify what to insert as a tab. This could be a tab or a number of spaces. Default tab
 *     value is three spaces (e.g. {@code "   "}).
 * </p>
 */
public final class DebugUtils {

    // simulate tab character with 3 spaces
    // as it is easier to read the list of fired events
    // on Travis CI's console when error message is thrown
    private static final String THREE_SPACES = "   ";

    private DebugUtils() {
        throw new IllegalStateException("Cannot initialize DebugUtils");
    }

    /**
     * Composes multiple functions together into one. The ones to run first should be the first in the array,
     * and the ones to run last should be at the end of the array.
     */
    @SafeVarargs
    public static Function<StringBuilder, StringBuilder> compose(Function<StringBuilder, StringBuilder>... functions) {
        if (functions.length == 0) {
            return Function.identity();
        } else if (functions.length == 1) {
            return functions[0];
        } else {
            // reverse array due to composition
            return Arrays.stream(functions)
                    // flip arguments so that functions will be run in the order
                    // in which they appear in the given array
                    .reduce((accumulated, nextFunction) -> nextFunction.compose(accumulated))
                    .orElse(Function.identity());
        }
    }

    /**
     * Ignores the StringBuilder and runs the given code block
     */
    public static Function<StringBuilder, StringBuilder> runCode(Runnable runnable) {
        return sb -> {
            runnable.run();
            return sb;
        };
    }

    /**
     * Inserts a header on a newline; useful for specifying a section in the message
     */
    public static Function<StringBuilder, StringBuilder> insertHeader(String headerText) {
        return insertHeader(headerText, THREE_SPACES);
    }

    public static Function<StringBuilder, StringBuilder> insertHeader(String headerText, String tab) {
        return sb -> sb.append("\n\n").append(tab).append(headerText);
    }

    /**
     * Shows the keys that were pressed when the test failed.
     */
    public static Function<StringBuilder, StringBuilder> showKeysPressedAtTestFailure(FxRobot robot) {
        return showKeysPressedAtTestFailure(robot, THREE_SPACES);
    }

    public static Function<StringBuilder, StringBuilder> showKeysPressedAtTestFailure(FxRobot robot, String tab) {
        return sb -> {
            Set<KeyCode> keysPressed = robot.robotContext().getKeyboardRobot().getPressedKeys();
            return sb
                    .append("\n").append(tab).append("Keys pressed at test failure:")
                    .append("\n").append(tab).append(tab).append(keysPressed);
        };
    }

    /**
     * Shows the {@link MouseButton}s that were pressed when the test failed.
     */
    public static Function<StringBuilder, StringBuilder> showMouseButtonsPressedAtTestFailure(FxRobot robot) {
        return showMouseButtonsPressedAtTestFailure(robot, THREE_SPACES);
    }

    public static Function<StringBuilder, StringBuilder> showMouseButtonsPressedAtTestFailure(FxRobot robot,
                                                                                              String tab) {
        return sb -> {
            Set<MouseButton> mouseButtons = robot.robotContext().getMouseRobot().getPressedButtons();
            return sb
                    .append("\n").append(tab).append("Mouse Buttons pressed at test failure:")
                    .append("\n").append(tab).append(tab).append(mouseButtons);
        };
    }

    /**
     * Shows all events that were fired since the start of the test. Note: only events stored in
     * {@link org.testfx.api.FxToolkitContext#getFiredEvents()} will be shown
     */
    public static Function<StringBuilder, StringBuilder> showFiredEvents() {
        return showFiredEvents(THREE_SPACES);
    }

    public static Function<StringBuilder, StringBuilder> showFiredEvents(String tab) {
        return sb -> {
            List<Event> firedEvents = FxToolkit.toolkitContext().getFiredEvents();
            return showFiredEvents(firedEvents, tab).apply(sb);
        };
    }

    /**
     * Shows all events that were fired since the start of the test.
     */
    public static Function<StringBuilder, StringBuilder> showFiredEvents(FiredEvents events) {
        return showFiredEvents(events, THREE_SPACES);
    }

    public static Function<StringBuilder, StringBuilder> showFiredEvents(FiredEvents events, String tab) {
        return showFiredEvents(events.getEvents(), tab);
    }

    public static Function<StringBuilder, StringBuilder> showFiredEvents(List<Event> events) {
        return showFiredEvents(events, THREE_SPACES);
    }

    public static Function<StringBuilder, StringBuilder> showFiredEvents(List<Event> events, String tab) {
        return sb -> {
            sb.append("\n").append(tab).append("Fired events since test began:");

            events.stream()
                    .map(Event::toString)
                    .forEach(e -> sb.append("\n").append(tab).append(tab).append(e));

            return sb;
        };
    }

    /**
     * Convenience method for {@link #informedErrorMessage(String, boolean, FxRobot, boolean, boolean)} with all
     * booleans set to {@code true} and the header text set to {@code "Context:"}.
     */
    public static Function<StringBuilder, StringBuilder> informedErrorMessage(FxRobot robot) {
        return informedErrorMessage(robot, "Context:");
    }

    /**
     * Convenience method for {@link #informedErrorMessage(String, boolean, FxRobot, boolean, boolean)} with all
     * booleans set to {@code true}.
     */
    public static Function<StringBuilder, StringBuilder> informedErrorMessage(FxRobot robot, String headerText) {
        return informedErrorMessage(headerText, true, robot, true, true);
    }

    /**
     * Convenience method for composing {@link #showFiredEvents()}, {@link #showKeysPressedAtTestFailure(FxRobot)},
     * and {@link #showMouseButtonsPressedAtTestFailure(FxRobot)} together, depending on what the booleans are.
     */
    public static Function<StringBuilder, StringBuilder> informedErrorMessage(String headerText,
                                                                              boolean showFiredEvents,
                                                                              FxRobot robot,
                                                                              boolean showKeysPressed,
                                                                              boolean showMouseButtonsPressed) {
        Function<StringBuilder, StringBuilder> function = Function.identity();
        if (headerText != null) {
            function = insertHeader(headerText).compose(function);
        }
        if (showKeysPressed) {
            function = showKeysPressedAtTestFailure(robot, THREE_SPACES).compose(function);
        }
        if (showMouseButtonsPressed) {
            function = showMouseButtonsPressedAtTestFailure(robot, THREE_SPACES).compose(function);
        }
        if (showFiredEvents) {
            function = showFiredEvents(THREE_SPACES).compose(function);
        }
        return function;
    }

}
