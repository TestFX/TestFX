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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Screen;
import javafx.stage.Window;

import org.hamcrest.Matcher;
import org.testfx.api.FxRobot;
import org.testfx.api.FxService;
import org.testfx.api.FxToolkit;
import org.testfx.service.support.CaptureSupport;
import org.testfx.service.support.FiredEvents;

/**
 * Utility class for displaying additional info, running code, or capturing an image of a test whenever
 * a test fails using {@link org.testfx.api.FxAssert#verifyThat(Node, Matcher)} or its related methods.
 * <p>
 * It is possible to create completely customized error messages by chaining functions together using
 * {@link #compose(Function[])} or one can use the standard provided handlers such as
 * {@link #informedErrorMessage(FxRobot)}, {@link #informedErrorMessage(FxRobot, String)}, etc.
 * All {@code indent} parameters in methods are used to specify the spacing to insert in order to
 * offset values. The default indent value is three spaces (e.g. {@code "   "}).
 * <p>
 * When a test fails, an image of the screen at the time of failure can be captured and saved to a PNG file.
 * {@code DebugUtils} provides support for a number of image sizes: the full screen, a window, an area of the
 * screen (i.e. bounds), or an individual node. Use the convenience methods prefixed by "save" (e.g.
 * {@link #saveScreenshot(String)}) to capture and save images. The image path will be printed using
 * {@link #insertContent(String, Object)} and will appear in the error message. For your own custom handler, use
 * {@link #saveTestImage(Function, Supplier, String)}, one of the "capture"-prefixed methods, and a
 * {@code Supplier<Path>} that determines where to save the image to.
 * <p>
 * This class uses the concept of combining functions together in order to add additional information
 * to the error message of an {@link AssertionError} thrown by a failing test.
 * <p>
 * Example:
 * <pre>{@code
 * // Template:
 * compose(
 *      // insert a header for the section of error information
 *      insertHeader(headerText),
 *      // insert the specific content that falls under this header
 *      insertContent(contentHeading, content),
 *      insertContent(contentHeading2, contentIterable),
 *      insertContent(contentHeading3, contentArray),
 *      insertContent(contentHeading4, contentStream),
 *
 *      // insert a header for the section of error information
 *      insertHeader(headerText),
 *      // insert the specific content that falls under this header
 *      insertContent(contentHeading5, contentStream2),
 *      insertContent(contentHeading6, contentStream3));
 *
 * // Example:
 * compose(
 *      insertHeader("Context:"),
 *      showKeysPressedAtTestFailure(this),
 *      showMouseButtonsPressedAtTestFailure(this),
 *      showFiredEvents(),
 *      saveScreenshot());
 *
 * // is equivalent to:
 *
 * informedErrorMessage(this);
 *
 * // as it uses all of the default arguments.
 * }</pre>
 */
public final class DebugUtils {

    /**
     * The default indentation to use for spacing between items of the error messages, defaults to
     * three spaces.
     */
    private static final String DEFAULT_INDENT = "   ";
    private static final AtomicInteger DEFAULT_PHOTO_NUMBER = new AtomicInteger(0);

    private DebugUtils() {}

    /**
     * Composes multiple functions together into one. The functions are called in the order that they appear
     * in the array. That is, {@code functions[0]} is run first, {@code functions[1]} is run second, etc.
     */
    @SafeVarargs
    public static Function<StringBuilder, StringBuilder> compose(Function<StringBuilder, StringBuilder>... functions) {
        switch (functions.length) {
            case 0:
                return Function.identity();
            case 1:
                return functions[0];
            default:
                return Arrays.stream(functions)
                        // flip arguments so that functions will be run in the order
                        // in which they appear in the given array
                        .reduce((accumulated, nextFunction) -> nextFunction.compose(accumulated))
                        .orElse(Function.identity());
        }
    }

    /**
     * Ignores the given {@code StringBuilder} and just runs the given code block.
     */
    public static Function<StringBuilder, StringBuilder> runCode(Runnable runnable) {
        return sb -> {
            runnable.run();
            return sb;
        };
    }

    /**
     * Inserts a header on a newline followed by the default indent; useful for specifying a section
     * in the error message.
     */
    public static Function<StringBuilder, StringBuilder> insertHeader(String headerText) {
        return insertHeader(headerText, DEFAULT_INDENT);
    }

    /**
     * Inserts a header on a newline followed by the given {@code indent}; useful for specifying a
     * section in the error message.
     */
    public static Function<StringBuilder, StringBuilder> insertHeader(String headerText, String indent) {
        return sb -> sb.append("\n\n").append(indent).append(headerText);
    }

    /**
     * Inserts the heading on a newline followed by two default indents followed by the given {@code contentHeading}.
     * Then, inserts a newline followed by three default indents followed by the content itself.
     */
    public static Function<StringBuilder, StringBuilder> insertContent(String contentHeading, Object content) {
        return insertContent(contentHeading, content, DEFAULT_INDENT);
    }

    /**
     * Inserts the heading on a newline followed by two of the given {@code indents} followed by the given
     * {@code contentHeading}. Then, inserts a newline followed by three of the given {@code indents} followed
     * by the content itself.
     */
    public static Function<StringBuilder, StringBuilder> insertContent(String contentHeading, Object content,
                                                                       String indent) {
        return sb -> sb.append("\n").append(indent).append(indent)
                .append(contentHeading).append("\n")
                .append(indent).append(indent).append(indent)
                .append(content);
    }

    /**
     * Inserts the heading on a newline followed by two default indents followed by the given {@code contentHeading}.
     * Then, for each item in the given iterable, inserts a newline followed by three default indents followed by the
     * list item itself.
     */
    public static Function<StringBuilder, StringBuilder> insertContent(String contentHeading, Iterable<?> contentIter) {
        return insertContent(contentHeading, contentIter, DEFAULT_INDENT);
    }

    /**
     * Inserts the heading on a newline followed by two of the given {@code indents} followed by the given
     * {@code contentHeading}. Then, for each item in the given iterable, inserts a newline followed by three of
     * the given indents followed by the list item itself.
     */
    public static Function<StringBuilder, StringBuilder> insertContent(String contentHeading, Iterable<?> contentIter,
                                                                       String indent) {
        return sb -> {
            sb.append("\n").append(indent).append(indent).append(contentHeading);
            contentIter.forEach(content ->
                sb.append("\n").append(indent).append(indent).append(indent).append(content));
            return sb;
        };
    }

    /**
     * Inserts the heading on a newline followed by two default indents followed by the given {@code contentHeading}.
     * Then, for each item in the given array, inserts a newline followed by three default indents followed by the
     * array item itself.
     */
    public static Function<StringBuilder, StringBuilder> insertContent(String contentHeading, Object[] contentArray) {
        return insertContent(contentHeading, contentArray, DEFAULT_INDENT);
    }

    /**
     * Inserts the heading on a newline followed by two of the given indents followed by the given
     * {@code contentHeading}. Then, for each item in the given array, inserts a newline followed by three of the
     * given indents followed by the array item itself.
     */
    public static Function<StringBuilder, StringBuilder> insertContent(String contentHeading, Object[] contentArray,
                                                                       String indent) {
        return insertContent(contentHeading, Stream.of(contentArray), indent);
    }

    /**
     * Inserts the heading on a newline followed by two default indents followed by the given {@code contentHeading}.
     * Then, for each item in the given stream, inserts a newline followed by three default indents followed by the
     * item itself.
     */
    public static Function<StringBuilder, StringBuilder> insertContent(String contentHeading, Stream<?> contentStream) {
        return insertContent(contentHeading, contentStream, DEFAULT_INDENT);
    }

    /**
     * Inserts the heading on a newline followed by two of the given indents followed by the given
     * {@code contentHeading}. Then, for each item in the given stream, inserts a newline followed by three of
     * the given indents followed by the item itself.
     */
    public static Function<StringBuilder, StringBuilder> insertContent(String contentHeading, Stream<?> contentStream,
                                                                       String indent) {
        return sb -> {
            sb.append("\n").append(indent).append(indent).append(contentHeading);
            contentStream.forEach(content ->
                sb.append("\n").append(indent).append(indent).append(indent).append(content));
            return sb;
        };
    }

    /**
     * Via {@link #insertContent(String, Object)}: shows the keys that were pressed when the test failed.
     */
    public static Function<StringBuilder, StringBuilder> showKeysPressedAtTestFailure(FxRobot robot) {
        return showKeysPressedAtTestFailure(robot, DEFAULT_INDENT);
    }

    public static Function<StringBuilder, StringBuilder> showKeysPressedAtTestFailure(FxRobot robot, String indent) {
        return sb -> {
            Set<KeyCode> keysPressed = robot.robotContext().getKeyboardRobot().getPressedKeys();
            return insertContent("Keys pressed at test failure:", keysPressed, indent)
                .apply(sb);
        };
    }

    /**
     * Via {@link #insertContent(String, Object)}: shows the {@link MouseButton}s
     * that were pressed when the test failed.
     */
    public static Function<StringBuilder, StringBuilder> showMouseButtonsPressedAtTestFailure(FxRobot robot) {
        return showMouseButtonsPressedAtTestFailure(robot, DEFAULT_INDENT);
    }

    public static Function<StringBuilder, StringBuilder> showMouseButtonsPressedAtTestFailure(FxRobot robot,
                                                                                              String indent) {
        return sb -> {
            Set<MouseButton> mouseButtons = robot.robotContext().getMouseRobot().getPressedButtons();
            return insertContent("Mouse Buttons pressed at test failure:", mouseButtons, indent)
                .apply(sb);
        };
    }

    /**
     * Via {@link #insertContent(String, Object)}: shows all events that were fired since the start of the test.
     * Note: only events stored in {@link org.testfx.api.FxToolkitContext#getFiredEvents()} will be shown.
     */
    public static Function<StringBuilder, StringBuilder> showFiredEvents() {
        return showFiredEvents(DEFAULT_INDENT);
    }

    public static Function<StringBuilder, StringBuilder> showFiredEvents(String indent) {
        return sb -> {
            List<Event> firedEvents = FxToolkit.toolkitContext().getFiredEvents();
            return showFiredEvents(firedEvents, indent).apply(sb);
        };
    }

    /**
     * Via {@link #insertContent(String, Object)}: shows all events that were fired since the start of the test.
     */
    public static Function<StringBuilder, StringBuilder> showFiredEvents(FiredEvents events) {
        return showFiredEvents(events, DEFAULT_INDENT);
    }

    public static Function<StringBuilder, StringBuilder> showFiredEvents(FiredEvents events, String indent) {
        return showFiredEvents(events.getEvents(), indent);
    }

    public static Function<StringBuilder, StringBuilder> showFiredEvents(List<Event> events) {
        return showFiredEvents(events, DEFAULT_INDENT);
    }

    public static Function<StringBuilder, StringBuilder> showFiredEvents(List<Event> events, String indent) {
        return insertContent("Fired events since test began:", events, indent);
    }

    public static Function<CaptureSupport, Image> captureScreenshot() {
        return captureScreenshot(Screen.getPrimary());
    }

    public static Function<CaptureSupport, Image> captureScreenshot(int screenIndex) {
        return captureScreenshot(Screen.getScreens().get(screenIndex));
    }

    public static Function<CaptureSupport, Image> captureScreenshot(Screen screen) {
        return captureBounds(screen.getBounds());
    }

    /**
     * Captures the registered stage.
     */
    public static Function<CaptureSupport, Image> captureWindow() {
        return captureWindow(FxToolkit.toolkitContext().getRegisteredStage());
    }

    public static Function<CaptureSupport, Image> captureWindow(Window window) {
        return captureBounds(mapToRect2D(window));
    }

    public static Function<CaptureSupport, Image> captureBounds(Bounds bounds) {
        return captureBounds(mapToRect2D(bounds));
    }

    public static Function<CaptureSupport, Image> captureBounds(Rectangle2D bounds) {
        return captureSupport -> captureSupport.captureRegion(bounds);
    }

    public static Function<CaptureSupport, Image> captureNode(Node node) {
        return captureSupport -> captureSupport.captureNode(node);
    }

    /**
     * Returns {@link #defaultImagePath(String, int)} with "testfx-test" as the test name and
     * {@link AtomicInteger#getAndIncrement() gets and increments} the next photo number.
     */
    public static Supplier<Path> defaultImagePath() {
        return defaultImagePath("testfx-test", DEFAULT_PHOTO_NUMBER.getAndIncrement());
    }

    /**
     * Returns {@code () -> Paths.get(testName + " - 0.png");}
     */
    public static Supplier<Path> defaultImagePath(String testName) {
        return defaultImagePath(testName, 0);
    }

    /**
     * Returns {@code () -> Paths.get(testName + " - " photoNumber + ".png");}
     */
    public static Supplier<Path> defaultImagePath(String testName, int photoNumber) {
        return () -> Paths.get(testName + " - " + photoNumber + ".png");
    }

    /**
     * Saves the captured primary screen image using {@link #defaultImagePath()} and the {@link #DEFAULT_INDENT}.
     */
    public static Function<StringBuilder, StringBuilder> saveScreenshot() {
        return saveScreenshot(defaultImagePath(), DEFAULT_INDENT);
    }

    /**
     * Saves the captured screenshot of the primary screen to "testName - 0.png"
     * (e.g. "button_has_label - 0.png").
     */
    public static Function<StringBuilder, StringBuilder> saveScreenshot(String testName) {
        return saveScreenshot(testName, 0);
    }

    /**
     * Saves the captured screenshot of the primary screen to "testName - photoNumber.png"
     * (e.g. "button_has_label - 2.png").
     */
    public static Function<StringBuilder, StringBuilder> saveScreenshot(String testName, int photoNumber) {
        return saveScreenshot(defaultImagePath(testName, photoNumber), DEFAULT_INDENT);
    }

    /**
     * Saves the captured screenshot of the primary screen to the supplied path.
     */
    public static Function<StringBuilder, StringBuilder> saveScreenshot(Supplier<Path> capturedImagePath,
                                                                        String indent) {
        return saveTestImage(captureScreenshot(), capturedImagePath, indent);
    }

    /**
     * Saves the captured screenshot of the screen to "testName - photoNumber.png" (e.g. "button_has_label - 2.png").
     */
    public static Function<StringBuilder, StringBuilder> saveScreenshot(Screen screen,
                                                                        String testName, int photoNumber) {
        return saveScreenshot(screen, defaultImagePath(testName, photoNumber), DEFAULT_INDENT);
    }

    /**
     * Saves the captured screenshot of the screen to the supplied path.
     */
    public static Function<StringBuilder, StringBuilder> saveScreenshot(Screen screen,
                                                                        Supplier<Path> capturedImagePath,
                                                                        String indent) {
        return saveTestImage(captureScreenshot(screen), capturedImagePath, indent);
    }

    /**
     * Saves the captured screenshot of the screen at the given index in {@link Screen#getScreens()}}
     * to "testName - photoNumber.png" (e.g. "button_has_label - 2.png").
     */
    public static Function<StringBuilder, StringBuilder> saveScreenshot(int screenIndex,
                                                                        String testName, int photoNumber) {
        return saveScreenshot(screenIndex, defaultImagePath(testName, photoNumber), DEFAULT_INDENT);
    }

    /**
     * Saves the captured screenshot of the screen at the given index in {@link Screen#getScreens()}}
     * to the supplied path.
     */
    public static Function<StringBuilder, StringBuilder> saveScreenshot(int screenIndex,
                                                                        Supplier<Path> capturedImagePath,
                                                                        String indent) {
        return saveTestImage(captureScreenshot(screenIndex), capturedImagePath, indent);
    }

    /**
     * Saves the captured registered stage image using {@link #defaultImagePath()} and the {@link #DEFAULT_INDENT}.
     */
    public static Function<StringBuilder, StringBuilder> saveWindow() {
        return saveWindow(defaultImagePath(), DEFAULT_INDENT);
    }

    /**
     * Saves the captured registered stage "testName - photoNumber.png" (e.g. "button_has_label - 2.png").
     */
    public static Function<StringBuilder, StringBuilder> saveWindow(String testName, int photoNumber) {
        return saveWindow(defaultImagePath(testName, photoNumber), DEFAULT_INDENT);
    }

    /**
     * Saves the captured registered stage to the supplied path.
     */
    public static Function<StringBuilder, StringBuilder> saveWindow(Supplier<Path> capturedImagePath, String indent) {
        return saveWindow(FxToolkit.toolkitContext().getRegisteredStage(), capturedImagePath, indent);
    }

    /**
     * Saves the captured window to "testName - photoNumber.png" (e.g. "button_has_label - 2.png").
     */
    public static Function<StringBuilder, StringBuilder> saveWindow(Window window,
                                                                    String testName, int photoNumber) {
        return saveWindow(window, defaultImagePath(testName, photoNumber), DEFAULT_INDENT);
    }

    /**
     * Saves the captured window to the supplied path.
     */
    public static Function<StringBuilder, StringBuilder> saveWindow(Window window, Supplier<Path> capturedImagePath,
                                                                    String indent) {
        return saveTestImage(captureWindow(window), capturedImagePath, indent);
    }

    /**
     * Saves the captured image based on the given bounds using {@link #defaultImagePath()}
     * and the {@link #DEFAULT_INDENT}.
     */
    public static Function<StringBuilder, StringBuilder> saveBounds(Bounds bounds) {
        return saveBounds(bounds, defaultImagePath(), DEFAULT_INDENT);
    }

    /**
     * Saves the captured image based on the given bounds to "testName - photoNumber.png"
     * (e.g. "button_has_label - 2.png").
     */
    public static Function<StringBuilder, StringBuilder> saveBounds(Bounds bounds, String testName, int photoNumber) {
        return saveBounds(bounds, defaultImagePath(testName, photoNumber), DEFAULT_INDENT);
    }

    /**
     * Saves the captured image based on the given bounds to the supplied path.
     */
    public static Function<StringBuilder, StringBuilder> saveBounds(Bounds bounds, Supplier<Path> capturedImagePath,
                                                                    String indent) {
        return saveTestImage(captureBounds(bounds), capturedImagePath, indent);
    }

    /**
     * Saves the captured image based on the given bounds using {@link #defaultImagePath()}
     * and the {@link #DEFAULT_INDENT}.
     */
    public static Function<StringBuilder, StringBuilder> saveBounds(Rectangle2D bounds) {
        return saveBounds(bounds, defaultImagePath(), DEFAULT_INDENT);
    }

    /**
     * Saves the captured image based on the given bounds to "testName - photoNumber.png"
     * (e.g. "button_has_label - 2.png").
     */
    public static Function<StringBuilder, StringBuilder> saveBounds(Rectangle2D bounds,
                                                                    String testName, int photoNumber) {
        return saveBounds(bounds, defaultImagePath(testName, photoNumber), DEFAULT_INDENT);
    }

    /**
     * Saves the captured image based on the given bounds to the supplied path.
     */
    public static Function<StringBuilder, StringBuilder> saveBounds(Rectangle2D bounds,
                                                                    Supplier<Path> capturedImagePath, String indent) {
        return saveTestImage(captureBounds(bounds), capturedImagePath, indent);
    }

    /**
     * Saves the captured node using {@link #defaultImagePath()} and the {@link #DEFAULT_INDENT}.
     */
    public static Function<StringBuilder, StringBuilder> saveNode(Node node) {
        return saveNode(node, defaultImagePath(), DEFAULT_INDENT);
    }

    /**
     * Saves the captured node image to "testName - photoNumber.png" (e.g. "button_has_label - 2.png").
     */
    public static Function<StringBuilder, StringBuilder> saveNode(Node node, String testName, int photoNumber) {
        return saveNode(node, defaultImagePath(testName, photoNumber), DEFAULT_INDENT);
    }

    /**
     * Saves the captured node to the supplied path.
     */
    public static Function<StringBuilder, StringBuilder> saveNode(Node node,
                                                                  Supplier<Path> capturedImagePath,
                                                                  String indent) {
        return saveTestImage(captureNode(node), capturedImagePath, indent);
    }

    /**
     * Saves the captured image to the supplied path.
     */
    public static Function<StringBuilder, StringBuilder> saveTestImage(Function<CaptureSupport, Image> captureImage,
                                                                       Supplier<Path> capturedImagePath,
                                                                       String indent) {
        return sb -> {
            CaptureSupport captureSupport = FxService.serviceContext().getCaptureSupport();
            Image errorImage = captureImage.apply(captureSupport);

            Path path = capturedImagePath.get();
            captureSupport.saveImage(errorImage, path);
            insertContent("Test image saved at:", path.toAbsolutePath().toString(), indent).apply(sb);
            return sb;
        };
    }

    /**
     * Convenience method for {@link #informedErrorMessage(String, boolean, boolean, FxRobot, boolean, boolean)}
     * with all booleans set to {@code true} and the header text set to {@code "Context:"}.
     */
    public static Function<StringBuilder, StringBuilder> informedErrorMessage(FxRobot robot) {
        return informedErrorMessage(robot, "Context:");
    }

    /**
     * Convenience method for {@link #informedErrorMessage(String, boolean, boolean, FxRobot, boolean, boolean)}
     * with all booleans set to {@code true}.
     */
    public static Function<StringBuilder, StringBuilder> informedErrorMessage(FxRobot robot, String headerText) {
        return informedErrorMessage(headerText, true, true, robot, true, true);
    }

    /**
     * Convenience method for {@link #insertHeader(String)} using "Context:" as the header text and then, via
     * {@link #insertContent(String, Object)}, composes {@link #showKeysPressedAtTestFailure(FxRobot)},
     * {@link #showMouseButtonsPressedAtTestFailure(FxRobot)}, and {@link #showFiredEvents()} together in
     * their given order, depending on what the booleans are.
     */
    public static Function<StringBuilder, StringBuilder> informedErrorMessage(String headerText,
                                                                              boolean takeScreenshot,
                                                                              boolean showFiredEvents,
                                                                              FxRobot robot,
                                                                              boolean showKeysPressed,
                                                                              boolean showMouseButtonsPressed) {
        Function<StringBuilder, StringBuilder> function = Function.identity();
        if (headerText != null) {
            function = insertHeader(headerText).compose(function);
        }
        if (takeScreenshot) {
            function = saveScreenshot().compose(function);
        }
        if (showKeysPressed) {
            function = showKeysPressedAtTestFailure(robot, DEFAULT_INDENT).compose(function);
        }
        if (showMouseButtonsPressed) {
            function = showMouseButtonsPressedAtTestFailure(robot, DEFAULT_INDENT).compose(function);
        }
        if (showFiredEvents) {
            function = showFiredEvents(DEFAULT_INDENT).compose(function);
        }
        return function;
    }

    private static Rectangle2D mapToRect2D(Bounds bounds) {
        return new Rectangle2D(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
    }

    private static Rectangle2D mapToRect2D(Window window) {
        return new Rectangle2D(window.getX(), window.getY(), window.getWidth(), window.getHeight());
    }

}
