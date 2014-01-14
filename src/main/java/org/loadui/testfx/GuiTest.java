/*
 * Copyright 2013 SmartBear Software
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the Licence for the specific language governing permissions and limitations
 * under the Licence.
 */
package org.loadui.testfx;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.loadui.testfx.exceptions.NoNodesFoundException;
import org.loadui.testfx.exceptions.NoNodesVisibleException;
import org.loadui.testfx.framework.ScreenRobotImpl;
import org.loadui.testfx.framework.ScreenRobot;
import org.loadui.testfx.robots.ClickRobot;
import org.loadui.testfx.robots.DragRobot;
import org.loadui.testfx.robots.KeyboardRobot;
import org.loadui.testfx.robots.MouseRobot;
import org.loadui.testfx.robots.MoveRobot;
import org.loadui.testfx.service.locator.BoundsLocator;
import org.loadui.testfx.service.locator.PointLocator;
import org.loadui.testfx.service.locator.PointQuery;
import org.loadui.testfx.service.stage.SceneProvider;
import org.loadui.testfx.service.stage.StageRetriever;
import org.loadui.testfx.service.stage.impl.StageRetrieverImpl;
import org.loadui.testfx.utils.KeyCodeUtils;
import org.loadui.testfx.utils.TestUtils;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Sets.filter;
import static org.loadui.testfx.controls.Commons.hasText;
import static org.loadui.testfx.utils.FXTestUtils.flattenSets;
import static org.loadui.testfx.utils.FXTestUtils.isVisible;

public abstract class GuiTest implements SceneProvider, ClickRobot, DragRobot, MoveRobot {

    @Before
    public void setupStage() throws Throwable {
        StageRetriever stageRetriever = new StageRetrieverImpl();
        stageRetriever.retrieveWithScene(this);
    }

    protected abstract Parent getRootNode();

    // Runs in JavaFX Application Thread.
    public Scene setupScene(Parent sceneRootNode) {
        return SceneBuilder.create()
            .width(600)
            .height(400)
            .root(sceneRootNode).build();
    }

    // Runs in JavaFX Application Thread.
    public Parent setupSceneRoot() {
        return getRootNode();
    }

    private static Window lastSeenWindow = null;

    public static <T extends Window> T targetWindow(T window) {
        lastSeenWindow = window;
        return window;
    }

    public static OffsetTarget offset(Object target, double offsetX, double offsetY) {
        return new OffsetTarget(target, offsetX, offsetY);
    }

    @SuppressWarnings("deprecation")
    public static List<Window> getWindows() {
        return Lists.reverse(Lists.newArrayList(Window.impl_getWindows()));
    }

    public static Window getWindowByIndex(int index) {
        return getWindows().get(index);
    }

    public static Stage findStageByTitle(final String titleRegex) {
        return Iterables.find(Iterables.filter(getWindows(), Stage.class), new Predicate<Stage>() {
            @Override
            public boolean apply(Stage input) {
                return input.getTitle().matches(titleRegex);
            }
        });
    }

    private static Set<Node> findAll(String query, Object parent) {
        try {
            if (parent instanceof String) {
                final String titleRegex = (String) parent;
                return findAll(query, targetWindow(findStageByTitle(titleRegex)).getScene());
            }
            else if (parent instanceof Node) {
                Node node = (Node) parent;
                targetWindow(node.getScene().getWindow());
                return findAllRecursively(query, node);
            }
            else if (parent instanceof Scene) {
                Scene scene = (Scene) parent;
                targetWindow(scene.getWindow());
                return findAllRecursively(query, scene.getRoot());
            }
            else if (parent instanceof Window) {
                return findAll(query, targetWindow((Window) parent).getScene());
            }
        }
        catch (NoNodesVisibleException e) {
            throw e;
        }
        catch (Exception e) {
            //Ignore, something went wrong with checking a window, so return an empty set.
        }

        return Collections.emptySet();
    }

    private static Set<Node> findAllRecursively(String query, Node node) {
        Set<Node> foundNodes;
        if (query.startsWith(".") || query.startsWith("#")) {
            foundNodes = node.lookupAll(query);
        }
        else {
            foundNodes = findAll(hasText(query), node);
        }
        return foundNodes;
    }

    private static <T extends Node> Set<T> getVisibleNodes(Set<T> foundNodes) {
        Set<T> visibleNodes = filter(foundNodes, isVisible);
        if (visibleNodes.isEmpty())
            throw new NoNodesVisibleException("Matching nodes were found, but none of them were visible. Screenshot saved as " + captureScreenshot().getAbsolutePath() + ".");
        return visibleNodes;
    }

    private static void assertNodesFound(Object query, Collection<? extends Node> foundNodes) {
        if (foundNodes.isEmpty())
            throw new NoNodesFoundException("No nodes matched '" + query + "'. Screenshot saved as " + captureScreenshot().getAbsolutePath() + ".");
    }

    public static Set<Node> findAll(String query) {
        Set<Node> foundNodes = findAllRecursively(query);
        assertNodesFound(query, foundNodes);
        return getVisibleNodes(foundNodes);
    }

    private static Set<Node> findAllRecursively(String query) {
        Set<Node> results = Sets.newLinkedHashSet();
        results.addAll(findAll(query, lastSeenWindow));
        final Predicate<Window> isDescendant = new Predicate<Window>() {
            @Override
            public boolean apply(Window input) {
                Window parent = null;
                if (input instanceof Stage) {
                    parent = ((Stage) input).getOwner();
                }
                else if (input instanceof PopupWindow) {
                    parent = ((PopupWindow) input).getOwnerWindow();
                }

                return parent == lastSeenWindow || parent != null && apply(parent);
            }
        };
        Iterable<Window> descendants = Iterables.filter(getWindows(), isDescendant);
        Iterable<Window> rest = Iterables.filter(getWindows(), Predicates.not(isDescendant));
        for (Window descendant : ImmutableList.copyOf(concat(descendants, rest))) {
            results.addAll(findAll(query, descendant));
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> T find(String selector, Object parent) {
        return checkNotNull((T) getFirst(findAll(selector, parent), null),
            "Query [%s] select [%s] resulted in no nodes found!", parent, selector);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> T find(final String query) {
        T foundNode = null;
        boolean isCssQuery = query.startsWith(".") || query.startsWith("#");

        if (isCssQuery) {
            foundNode = findByCssSelector(query);
            if (foundNode == null)
                throw new NoNodesFoundException("No nodes matched the CSS query '" + query + "'! Screenshot saved as " + captureScreenshot().getAbsolutePath());
        }
        else {
            foundNode = (T) find(hasText(query));
            if (foundNode == null)
                throw new NoNodesFoundException("No nodes found with label '" + query + "'! Screenshot saved as " + captureScreenshot().getAbsolutePath());
        }

        return foundNode;
    }

    public static boolean exists(final String query) {
        return selectorExists(query) || labelExists(query);
    }

    private static boolean labelExists(String query) {
        return find(hasText(query)) != null;
    }

    private static boolean selectorExists(String query) {
        return findByCssSelector(query) != null;
    }

    /**
     * Returns a Callable that calculates the number of nodes that matches the given query.
     *
     * @param nodeQuery a CSS query or the label of a node.
     * @return
     */
    public static Callable<Integer> numberOf(final String nodeQuery) {
        return new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return findAll(nodeQuery).size();
            }
        };
    }

    public static File captureScreenshot() {
        File screenshot = new File("screenshot" + new Date().getTime() + ".png");
        try {
            BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image, "png", screenshot);
        }
        catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return screenshot;
    }

    public static void waitUntil(final Node node, final Matcher<Object> condition, int timeoutInSeconds) {
        TestUtils.awaitCondition(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return condition.matches(node);
            }
        }, timeoutInSeconds);
    }

    /**
     * Waits until the provided node fulfills the given condition.
     *
     * @param node
     * @param condition
     */
    public static void waitUntil(final Node node, final Matcher<Object> condition) {
        waitUntil(node, condition, 15);
    }

    public static <T> void waitUntil(final T value, final Matcher<? super T> condition) {
        waitUntil(value, condition, 15);
    }

    public static <T> void waitUntil(final Callable<T> callable, final Matcher<? super T> condition) {
        TestUtils.awaitCondition(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return condition.matches(callable.call());
            }
        }, 15);
    }

    public static <T> void waitUntil(final T value, final Matcher<? super T> condition, int timeoutInSeconds) {
        TestUtils.awaitCondition(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return condition.matches(value);
            }
        }, timeoutInSeconds);
    }

    public static <T extends Node> void waitUntil(final T node, final Predicate<T> condition) {
        waitUntil(node, condition, 15);
    }

    public static <T extends Node> void waitUntil(final T node, final Predicate<T> condition, int timeoutInSeconds) {
        TestUtils.awaitCondition(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return condition.apply(node);
            }
        }, timeoutInSeconds);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Node> T findByCssSelector(final String selector) {
        Set<Node> locallyFound = findAll(selector);
        Iterable<Node> globallyFound = concat(transform(getWindows(),
            new Function<Window, Iterable<Node>>() {
                @Override
                public Iterable<Node> apply(Window input) {
                    return findAll(selector, input);
                }
            }));

        Iterables.addAll(locallyFound, globallyFound);
        assertNodesFound(selector, locallyFound);
        Set<Node> visibleNodes = getVisibleNodes(locallyFound);

        return (T) getFirst(visibleNodes, null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> T find(final Matcher<Object> matcher) {
        Iterable<Set<Node>> found = transform(getWindows(),
            new Function<Window, Set<Node>>() {
                @Override
                public Set<Node> apply(Window input) {
                    return findAllRecursively(matcher, input.getScene().getRoot());
                }
            });

        Set<Node> foundFlattened = flattenSets(found);

        assertNodesFound(matcher, foundFlattened);
        return (T) getFirst(getVisibleNodes(foundFlattened), null);
    }

    public static <T extends Node> T find(final Predicate<T> predicate) {
        Iterable<Set<T>> found = transform(getWindows(),
            new Function<Window, Set<T>>() {
                @Override
                public Set<T> apply(Window input) {
                    return findAllRecursively(predicate, input.getScene().getRoot());
                }
            });

        Set<T> foundFlattened = flattenSets(found);

        assertNodesFound(predicate, foundFlattened);
        return getFirst(getVisibleNodes(foundFlattened), null);
    }

    /**
     * Returns all child nodes of parent, that matches the given matcher.
     *
     * @param matcher
     * @param parent
     * @return found nodes
     */
    public static Set<Node> findAll(Matcher<Object> matcher, Node parent) {
        Set<Node> foundNodes = findAllRecursively(matcher, parent);
        assertNodesFound(matcher, foundNodes);
        return getVisibleNodes(foundNodes);
    }

    private static Set<Node> findAllRecursively(Matcher<Object> matcher, Node parent) {
        Set<Node> found = new HashSet<Node>();
        if (matcher.matches(parent)) {
            found.add(parent);
        }
        if (parent instanceof Parent) {
            for (Node child : ((Parent) parent).getChildrenUnmodifiable()) {
                found.addAll(findAllRecursively(matcher, child));
            }
        }
        return ImmutableSet.copyOf(found);
    }

    public static <T extends Node> Set<T> findAll(Predicate<T> predicate, Node parent) {
        Set<T> foundNodes = findAllRecursively(predicate, parent);
        assertNodesFound(predicate, foundNodes);
        return getVisibleNodes(foundNodes);
    }

    private static <T extends Node> Set<T> findAllRecursively(Predicate<T> predicate, Node parent) {
        Set<T> found = new HashSet<T>();
        try {
            @SuppressWarnings("unchecked")
            T node = (T) parent;
            if (predicate.apply(node)) {
                found.add(node);
            }
        }
        catch (ClassCastException e) {
            // Do nothing.
        }
        if (parent instanceof Parent) {
            for (Node child : ((Parent) parent).getChildrenUnmodifiable()) {
                found.addAll(findAllRecursively(predicate, child));
            }
        }
        return ImmutableSet.copyOf(found);
    }

    private final ScreenRobot screenRobot;
    private final BoundsLocator boundsLocator;
    private final PointLocator pointLocator;
    private final MouseRobot mouseRobot;
    private final KeyboardRobot keyboardRobot;

    public GuiTest() {
        screenRobot = new ScreenRobotImpl();
        boundsLocator = new BoundsLocator();
        pointLocator = new PointLocator(boundsLocator);
        mouseRobot = new MouseRobot(screenRobot);
        keyboardRobot = new KeyboardRobot(screenRobot);
    }

    /**
     * Same as Thread.sleep(), but without checked exceptions.
     *
     * @param ms time in milliseconds
     */
    public GuiTest sleep(long ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public GuiTest sleep(long value, TimeUnit unit) {
        return sleep(unit.toMillis(value));
    }

    public GuiTest target(Object window) {
        if (window instanceof Window) {
            targetWindow((Window) window);
        }
        else if (window instanceof String) {
            targetWindow(findStageByTitle((String) window));
        }
        else if (window instanceof Number) {
            targetWindow(getWindowByIndex(((Number) window).intValue()));
        }
        else if (window instanceof Class<?>) {
            targetWindow(Iterables.find(getWindows(), Predicates.instanceOf((Class<?>) window)));
        }
        else {
            Preconditions.checkArgument(false, "Unable to identify Window based on the given argument: %s", window);
        }

        return this;
    }

    /*---------------- Other  ----------------*/


    public GuiTest eraseCharacters(int characters) {
        for (int i = 0; i < characters; i++) {
            type(KeyCode.BACK_SPACE);
        }
        return this;
    }

    /**
     * Presses and holds a mouse button, until explicitly released.
     *
     * @param buttons defaults to the primary mouse button.
     */
    public GuiTest press(MouseButton... buttons) {
        mouseRobot.press(buttons);
        return this;
    }

    /**
     * Releases a pressed mouse button.
     *
     * @param buttons defaults to the primary mouse button.
     */
    public GuiTest release(MouseButton... buttons) {
        mouseRobot.release(buttons);
        return this;
    }


    /*---------------- Scroll ----------------*/

    @Deprecated
    public GuiTest scroll(int amount) {
        for (int x = 0; x < Math.abs(amount); x++) {
            screenRobot.scrollMouse(Integer.signum(amount));
        }
        return this;
    }

    /**
     * Scrolls the mouse-wheel a given number of notches in a direction.
     *
     * @param amount    the number of notches to scroll
     * @param direction
     * @return
     */
    public GuiTest scroll(int amount, VerticalDirection direction) {
        for (int x = 0; x < Math.abs(amount); x++) {
            screenRobot.scrollMouse(directionToInteger(direction));
        }
        return this;
    }

    /**
     * Scrolls the mouse-wheel one notch in the given direction.
     *
     * @param direction
     * @return
     */
    public GuiTest scroll(VerticalDirection direction) {
        return scroll(1, direction);
    }

    private int directionToInteger(VerticalDirection direction) {
        if (direction == VerticalDirection.UP)
            return -1;
        return 1;
    }


    /*---------------- Type ----------------*/

    /**
     * Types the given text on the keyboard.
     * Note: Typing depends on the operating system keyboard layout!
     *
     * @param text
     */
    public GuiTest type(String text) {
        for (int i = 0; i < text.length(); i++) {
            type(text.charAt(i));
            try {
                Thread.sleep(25);
            }
            catch (InterruptedException e) {
            }
        }
        return this;
    }

    public GuiTest type(char character) {
        KeyCode keyCode = KeyCodeUtils.findKeyCode(character);

        if (!Character.isUpperCase(character)) {
            return type(keyCode);
        }
        else {
            KeyCode[] modifiers = new KeyCode[]{KeyCode.SHIFT};
            press(modifiers);
            type(keyCode);
            return release(modifiers);
        }
    }

    /**
     * Alias for type( ).
     *
     * @param keys
     */
    public GuiTest push(KeyCode... keys) {
        return type(keys);
    }

    /**
     * Alias for type().
     *
     * @param character
     */
    public GuiTest push(char character) {
        return type(character);
    }

    public GuiTest type(KeyCode... keys) {
        press(keys);
        return release(keys);
    }

    /**
     * Presses and holds a given key, until explicitly released.
     *
     * @param keys
     */
    public GuiTest press(KeyCode... keys) {
        keyboardRobot.press(keys);
        return this;
    }

    /**
     * Releases a given key.
     *
     * @param keys
     */
    public GuiTest release(KeyCode... keys) {
        keyboardRobot.release(keys);
        return this;
    }

    private Pos nodePosition = Pos.CENTER;

    public GuiTest pos(Pos pos) {
        nodePosition = pos;
        return this;
    }

    /**
     * Closes the front-most window using the Alt+F4 keyboard shortcut.
     *
     * @return
     */
    public GuiTest closeCurrentWindow() {
        this.push(KeyCode.ALT, KeyCode.F4).sleep(100);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF CLICK ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    public GuiTest click(MouseButton... buttons) {
        if (buttons.length == 0) {
            return click(MouseButton.PRIMARY);
        }
        mouseRobot.press(buttons);
        mouseRobot.release(buttons);
        return this;
    }

    @Override
    public GuiTest click(double x, double y, MouseButton... buttons) {
        moveTo(x, y);
        click(buttons);
        return this;
    }

    @Override
    public GuiTest click(Point2D point, MouseButton... buttons) {
        moveTo(point);
        click(buttons);
        return this;
    }

    @Override
    public GuiTest click(Bounds bounds, MouseButton... buttons) {
        moveTo(bounds);
        click(buttons);
        return this;
    }

    @Override
    public GuiTest click(Node node, MouseButton... buttons) {
        moveTo(node);
        click(buttons);
        return this;
    }

    @Override
    public GuiTest click(Scene scene, MouseButton... buttons) {
        moveTo(scene);
        click(buttons);
        return this;
    }

    @Override
    public GuiTest click(Window window, MouseButton... buttons) {
        moveTo(window);
        click(buttons);
        return this;
    }

    @Override
    public GuiTest click(String query, MouseButton... buttons) {
        moveTo(query);
        click(buttons);
        return this;
    }

    @Override
    public GuiTest click(Matcher<Object> matcher, MouseButton... buttons) {
        moveTo(matcher);
        click(buttons);
        return this;
    }

    @Override
    public <T extends Node> GuiTest click(Predicate<T> predicate, MouseButton... buttons) {
        moveTo(predicate);
        click(buttons);
        return this;
    }

    @Override
    public GuiTest rightClick() {
        click(MouseButton.SECONDARY);
        return this;
    }

    @Override
    public GuiTest rightClick(double x, double y) {
        click(x, y, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public GuiTest rightClick(Point2D point) {
        click(point, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public GuiTest rightClick(Bounds bounds) {
        click(bounds, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public GuiTest rightClick(Node node) {
        click(node, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public GuiTest rightClick(Scene scene) {
        click(scene, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public GuiTest rightClick(Window window) {
        click(window, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public GuiTest rightClick(String query) {
        click(query, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public GuiTest rightClick(Matcher<Object> matcher) {
        click(matcher, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public <T extends Node> GuiTest rightClick(Predicate<T> predicate) {
        click(predicate, MouseButton.SECONDARY);
        return this;
    }

    @Override
    public GuiTest doubleClick(MouseButton... buttons) {
        click(buttons).click().sleep(50);
        return this;
    }

    @Override
    public GuiTest doubleClick(double x, double y, MouseButton... buttons) {
        click(x, y, buttons).click().sleep(50);
        return this;
    }

    @Override
    public GuiTest doubleClick(Point2D point, MouseButton... buttons) {
        click(point, buttons).click().sleep(50);
        return this;
    }

    @Override
    public GuiTest doubleClick(Bounds bounds, MouseButton... buttons) {
        click(bounds, buttons).click().sleep(50);
        return this;
    }

    @Override
    public GuiTest doubleClick(Node node, MouseButton... buttons) {
        click(node, buttons).click().sleep(50);
        return this;
    }

    @Override
    public GuiTest doubleClick(Scene scene, MouseButton... buttons) {
        click(scene, buttons).click().sleep(50);
        return this;
    }

    @Override
    public GuiTest doubleClick(Window window, MouseButton... buttons) {
        click(window, buttons).click().sleep(50);
        return this;
    }

    @Override
    public GuiTest doubleClick(String query, MouseButton... buttons) {
        click(query, buttons).click().sleep(50);
        return this;
    }

    @Override
    public GuiTest doubleClick(Matcher<Object> matcher, MouseButton... buttons) {
        click(matcher, buttons).click().sleep(50);
        return this;
    }

    @Override
    public <T extends Node> GuiTest doubleClick(Predicate<T> predicate, MouseButton... buttons) {
        click(predicate, buttons).click().sleep(50);
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF DRAG ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    public GuiTest drag(MouseButton... buttons) {
        mouseRobot.press(buttons);
        return this;
    }

    @Override
    public GuiTest drag(double x, double y, MouseButton... buttons) {
        moveTo(x, y);
        drag(buttons);
        return this;
    }

    @Override
    public GuiTest drag(Point2D point, MouseButton... buttons) {
        moveTo(point);
        drag(buttons);
        return this;
    }

    @Override
    public GuiTest drag(Bounds bounds, MouseButton... buttons) {
        moveTo(bounds);
        drag(buttons);
        return this;
    }

    @Override
    public GuiTest drag(Node node, MouseButton... buttons) {
        moveTo(node);
        drag(buttons);
        return this;
    }

    @Override
    public GuiTest drag(Scene scene, MouseButton... buttons) {
        moveTo(scene);
        drag(buttons);
        return this;
    }

    @Override
    public GuiTest drag(Window window, MouseButton... buttons) {
        moveTo(window);
        drag(buttons);
        return this;
    }

    @Override
    public GuiTest drag(String query, MouseButton... buttons) {
        moveTo(query);
        drag(buttons);
        return this;
    }

    @Override
    public GuiTest drag(Matcher<Object> matcher, MouseButton... buttons) {
        moveTo(matcher);
        drag(buttons);
        return this;
    }

    @Override
    public GuiTest drag(Predicate<Node> predicate, MouseButton... buttons) {
        moveTo(predicate);
        drag(buttons);
        return this;
    }

    @Override
    public GuiTest drop() {
        mouseRobot.release();
        return this;
    }

    @Override
    public GuiTest dropTo(double x, double y) {
        moveTo(x, y);
        drop();
        return this;
    }

    @Override
    public GuiTest dropTo(Point2D point) {
        moveTo(point);
        drop();
        return this;
    }

    @Override
    public GuiTest dropTo(Bounds bounds) {
        moveTo(bounds);
        drop();
        return this;
    }

    @Override
    public GuiTest dropTo(Node node) {
        moveTo(node);
        drop();
        return this;
    }

    @Override
    public GuiTest dropTo(Scene scene) {
        moveTo(scene);
        drop();
        return this;
    }

    @Override
    public GuiTest dropTo(Window window) {
        moveTo(window);
        drop();
        return this;
    }

    @Override
    public GuiTest dropTo(String query) {
        moveTo(query);
        drop();
        return this;
    }

    @Override
    public GuiTest dropTo(Matcher<Object> matcher) {
        moveTo(matcher);
        drop();
        return this;
    }

    @Override
    public GuiTest dropTo(Predicate<Node> predicate) {
        moveTo(predicate);
        drop();
        return this;
    }

    @Override
    public GuiTest dropBy(double x, double y) {
        moveBy(x, y);
        drop();
        return this;
    }

    //---------------------------------------------------------------------------------------------
    // IMPLEMENTATION OF MOVE ROBOT.
    //---------------------------------------------------------------------------------------------

    @Override
    public GuiTest moveTo(double x, double y) {
        moveToImpl(pointFor(new Point2D(x, y)));
        return this;
    }

    @Override
    public GuiTest moveTo(Point2D point) {
        moveToImpl(pointFor(point));
        return this;
    }

    @Override
    public GuiTest moveTo(Bounds bounds) {
        moveToImpl(pointFor(bounds));
        return this;
    }

    @Override
    public GuiTest moveTo(Node node) {
        moveToImpl(pointFor(node));
        return this;
    }

    @Override
    public GuiTest moveTo(Scene scene) {
        moveToImpl(pointFor(scene));
        return this;
    }

    @Override
    public GuiTest moveTo(Window window) {
        moveToImpl(pointFor(window));
        return this;
    }

    @Override
    public GuiTest moveTo(String query) {
        moveToImpl(pointFor(query));
        return this;
    }

    @Override
    public GuiTest moveTo(Matcher<Object> matcher) {
        moveToImpl(pointFor(matcher));
        return this;
    }

    @Override
    public <T extends Node> GuiTest moveTo(Predicate<T> predicate) {
        moveToImpl(pointFor(predicate));
        return this;
    }

    @Override
    public GuiTest moveBy(double x, double y) {
        Point2D mouseLocation = screenRobot.getMouseLocation();
        Point2D targetPoint = new Point2D(mouseLocation.getX() + x, mouseLocation.getY() + y);
        screenRobot.moveMouseLinearTo(targetPoint.getX(), targetPoint.getY());
        return this;
    }

    private void moveToImpl(PointQuery pointQuery) {
        // Since moving takes time, only do it if we're not already at the desired point.
        Point2D point = pointQuery.atPosition(nodePosition);
        if (!isPointAtMouseLocation(point)) {
            screenRobot.moveMouseLinearTo(point.getX(), point.getY());
        }

        // If the target has moved while we were moving the mouse, update to the new position.
        Point2D endPoint = pointQuery.atPosition(nodePosition);
        screenRobot.moveMouseTo(endPoint.getX(), endPoint.getY());
    }

    private boolean isPointAtMouseLocation(Point2D point) {
        Point2D mouseLocation = screenRobot.getMouseLocation();
        return mouseLocation.equals(point);
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    public PointQuery pointFor(Point2D point) {
        return pointLocator.pointFor(point);
    }

    public PointQuery pointFor(Bounds bounds) {
        return pointLocator.pointFor(bounds);
    }

    public PointQuery pointFor(Node node) {
        targetWindow(node.getScene().getWindow());
        return pointLocator.pointFor(node);
    }

    public PointQuery pointFor(Scene scene) {
        targetWindow(scene.getWindow());
        return pointLocator.pointFor(scene);
    }

    public PointQuery pointFor(Window window) {
        targetWindow(window);
        return pointLocator.pointFor(window);
    }

    public PointQuery pointFor(String query) {
        Node node = find(query);
        return pointFor(node);
    }

    public PointQuery pointFor(Matcher<Object> matcher) {
        Node node = find(matcher);
        return pointFor(node);
    }

    public <T extends Node> PointQuery pointFor(Predicate<T> predicate) {
        Node node = find(predicate);
        return pointFor(node);
    }

    static class OffsetTarget {
        private final Object target;
        private final double offsetX;
        private final double offsetY;

        private OffsetTarget(Object target, double offsetX, double offsetY) {
            this.target = target;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }
    }

}
