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

import java.awt.MouseInfo;
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
import org.loadui.testfx.robots.KeyboardRobot;
import org.loadui.testfx.robots.MouseRobot;
import org.loadui.testfx.service.locator.BoundsLocator;
import org.loadui.testfx.service.locator.PointLocator;
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

public abstract class GuiTest implements SceneProvider {

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

    /*---------------- Click ----------------*/

    public GuiTest click(MouseButton... buttons) {
        if (buttons.length == 0) {
            return click(MouseButton.PRIMARY);
        }
        press(buttons);
        return release(buttons);
    }

    /**
     * Clicks the first Node matching the given query.
     *
     * @param query   either CSS selector, label of node or class name.
     * @param buttons
     */
    public GuiTest click(String query, MouseButton... buttons) {
        move(query);
        return click(buttons);
    }

    public GuiTest click(Node node, MouseButton... buttons) {
        move(node);
        return click(buttons);
    }

    public <T extends Node> GuiTest click(Predicate<T> p, MouseButton... buttons) {
        move(p);
        return click(buttons);
    }

    public GuiTest click(Point2D point, MouseButton... buttons) {
        move(point);
        return click(buttons);
    }

    public GuiTest click(Bounds bounds, MouseButton... buttons) {
        move(bounds);
        return click(buttons);
    }

    public GuiTest click(Scene scene, MouseButton... buttons) {
        move(scene);
        return click(buttons);
    }

    public GuiTest click(Window window, MouseButton... buttons) {
        move(window);
        return click(buttons);
    }

    public GuiTest click(Matcher<Node> matcher, MouseButton... buttons) {
        move(matcher);
        return click(buttons);
    }

    public GuiTest click(Iterable<?> iterable, MouseButton... buttons) {
        move(iterable);
        return click(buttons);
    }

    public GuiTest click(OffsetTarget target, MouseButton... buttons) {
        move(target);
        return click(buttons);
    }


    /*---------------- Right-click ----------------*/

    public GuiTest rightClick() {
        return click(MouseButton.SECONDARY);
    }

    /**
     * Right-clicks a given target.
     */
    public GuiTest rightClick(String query) {
        return click(query, MouseButton.SECONDARY);
    }

    public GuiTest rightClick(Node node) {
        return click(node, MouseButton.SECONDARY);
    }

    public GuiTest rightClick(Matcher<Node> matcher) {
        return click(matcher, MouseButton.SECONDARY);
    }

    public <T extends Node> GuiTest rightClick(Predicate<T> predicate) {
        return click(predicate, MouseButton.SECONDARY);
    }

    public GuiTest rightClick(Scene scene) {
        return click(scene, MouseButton.SECONDARY);
    }

    public GuiTest rightClick(Window window) {
        return click(window, MouseButton.SECONDARY);
    }

    public GuiTest rightClick(Point2D point) {
        return click(point, MouseButton.SECONDARY);
    }

    public GuiTest rightClick(Bounds bounds) {
        return click(bounds, MouseButton.SECONDARY);
    }

    public GuiTest rightClick(OffsetTarget target) {
        return click(target, MouseButton.SECONDARY);
    }

    public GuiTest rightClick(Iterable<?> iterable) {
        return click(iterable, MouseButton.SECONDARY);
    }

    /*---------------- Double-click ----------------*/

    public GuiTest doubleClick() {
        return click().click().sleep(50);
    }

    /**
     * Double-clicks a given target.
     *
     * @param query
     */
    public GuiTest doubleClick(String query) {
        return click(query).click().sleep(50);
    }

    public GuiTest doubleClick(Node node) {
        return click(node).click().sleep(50);
    }

    public GuiTest doubleClick(Matcher<Node> matcher) {
        return click(matcher).click().sleep(50);
    }

    public <T extends Node> GuiTest doubleClick(Predicate<T> predicate) {
        return click(predicate).click().sleep(50);
    }

    public GuiTest doubleClick(Scene scene) {
        return click(scene).click().sleep(50);
    }

    public GuiTest doubleClick(Window window) {
        return click(window).click().sleep(50);
    }

    public GuiTest doubleClick(Point2D point) {
        return click(point).click().sleep(50);
    }

    public GuiTest doubleClick(Bounds bounds) {
        return click(bounds).click().sleep(50);
    }

    public GuiTest doubleClick(OffsetTarget target) {
        return click(target).click().sleep(50);
    }

    public GuiTest doubleClick(Iterable<?> iterable) {
        return click(iterable).click().sleep(50);
    }

    public GuiTest doubleClick(MouseButton button) {
        return click(button).click(button).sleep(50);
    }


    /*---------------- Other  ----------------*/


    public GuiTest eraseCharacters(int characters) {
        for (int i = 0; i < characters; i++) {
            type(KeyCode.BACK_SPACE);
        }
        return this;
    }


    public MouseMotion drag(Object source, MouseButton... buttons) {
        move(source);
        press(buttons);

        return new MouseMotion(this, buttons);
    }

    /**
     * Moves the mouse cursor to the given coordinates.
     *
     * @param x
     * @param y
     */
    public GuiTest move(double x, double y) {
        screenRobot.moveMouseLinearTo(x, y);
        return this;
    }

    public GuiTest move(Object target) {
        Point2D point = pointForObject(target);

        //Since moving takes time, only do it if we're not already at the desired point.
        if (!screenRobot.getMouseLocation().equals(point)) {
            move(point.getX(), point.getY());
        }
        //If the target has moved while we were moving the mouse, update to the new position:
        point = pointForObject(target);
        screenRobot.moveMouseTo(point.getX(), point.getY());
        return this;
    }

    /**
     * Moves the mouse cursor relatively to its current position.
     *
     * @param x
     * @param y
     */
    public GuiTest moveBy(double x, double y) {
        Point2D mouse = screenRobot.getMouseLocation();
        screenRobot.moveMouseLinearTo(mouse.getX() + x, mouse.getY() + y);
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

    public Point2D pointFor(Point2D point) {
        return point;
    }

    public Point2D pointFor(Bounds bounds) {
        return pointLocator.pointFor(bounds, nodePosition);
    }

    public Point2D pointFor(Node node) {
        targetWindow(node.getScene().getWindow());
        return pointLocator.pointFor(node, nodePosition);
    }

    public Point2D pointFor(Scene scene) {
        targetWindow(scene.getWindow());
        return pointLocator.pointFor(scene, nodePosition);
    }

    public Point2D pointFor(Window window) {
        targetWindow(window);
        return pointLocator.pointFor(window, nodePosition);
    }

    public Point2D pointFor(String query) {
        Node node = find(query);
        return pointFor(node);
    }

    public Point2D pointFor(Matcher<Object> matcher) {
        Node node = find(matcher);
        return pointFor(node);
    }

    public Point2D pointFor(Predicate<Node> predicate) {
        Node node = find(predicate);
        return pointFor(node);
    }

    public Point2D pointFor(Iterable<?> iterable) {
        Node node = (Node) Iterables.get(iterable, 0);
        return pointFor(node);
    }

    public Point2D pointFor(OffsetTarget offsetTarget) {
        Pos oldNodePosition = nodePosition;
        pos(Pos.TOP_LEFT);
        Point2D targetPoint = pointForObject(offsetTarget.target);
        pos(oldNodePosition);
        return new Point2D(
            targetPoint.getX() + offsetTarget.offsetX,
            targetPoint.getY() + offsetTarget.offsetY
        );
    }

    @SuppressWarnings("unchecked")
    private Point2D pointForObject(Object target) {
        if (target instanceof Point2D) {
            return pointFor((Point2D) target);
        }
        else if (target instanceof Bounds) {
            return pointFor((Bounds) target);
        }
        else if (target instanceof String) {
            return pointFor((String) target);
        }
        else if (target instanceof Node) {
            return pointFor((Node) target);
        }
        else if (target instanceof Scene) {
            return pointFor((Scene) target);
        }
        else if (target instanceof Window) {
            return pointFor((Window) target);
        }
        else if (target instanceof Matcher) {
            return pointFor((Matcher<Object>) target);
        }
        else if (target instanceof Predicate) {
            return pointFor((Predicate<Node>) target);
        }
        else if (target instanceof Iterable<?>) {
            return pointFor((Iterable<?>) target);
        }
        else if (target instanceof OffsetTarget) {
            return pointFor((OffsetTarget) target);
        }
        throw new IllegalArgumentException("Unable to get coordinates for: " + target);
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
