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
package org.testfx.service.finder.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.service.finder.WindowFinder;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NodeFinderImplTest {

    @Rule
    public TestRule rule = RuleChain.outerRule(new TestFXRule()).around(exception = ExpectedException.none());
    public ExpectedException exception;

    Stage window;
    Stage otherWindow;
    Stage twinWindow;

    Pane pane;
    Node firstIdLabel;
    Node secondIdLabel;
    Node thirdClassLabel;
    Node invisibleNode;

    Pane otherPane;
    Node subLabel;
    Pane otherSubPane;
    Node subSubLabel;

    Pane twinPane;
    Node visibleTwin;
    Node invisibleTwin;

    WindowFinderStub windowFinder;
    NodeFinderImpl nodeFinder;

    @After
    public void cleanup() throws TimeoutException {
        FxToolkit.setupFixture(this::cleanupStages);
    }

    @Before
    public void setup() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupScene(() -> new Scene(new Region(), 600, 400));
        FxToolkit.setupFixture(this::setupStages);
        windowFinder = new WindowFinderStub();
        windowFinder.windows = new ArrayList<>();
        windowFinder.windows.add(window);
        windowFinder.windows.add(otherWindow);
        windowFinder.windows.add(twinWindow);
        nodeFinder = new NodeFinderImpl(windowFinder);
    }

    public void setupStages() {
        pane = new VBox();
        firstIdLabel = new Label("first");
        firstIdLabel.setId("firstId");
        secondIdLabel = new Label("second");
        secondIdLabel.setId("secondId");
        thirdClassLabel = new Label("third");
        thirdClassLabel.getStyleClass().add("thirdClass");
        invisibleNode = new Label("invisible");
        invisibleNode.setId("invisibleNode");
        invisibleNode.setVisible(false);
        pane.getChildren().setAll(firstIdLabel, secondIdLabel, thirdClassLabel, invisibleNode);

        otherPane = new VBox();
        otherSubPane = new VBox();
        subLabel = new Label("sub");
        subLabel.setId("subLabel");
        subLabel.getStyleClass().add("sub");
        subSubLabel = new Label("subSub");
        subSubLabel.setId("subSubLabel");
        subSubLabel.getStyleClass().add("sub");
        otherPane.getChildren().setAll(subLabel, otherSubPane);
        otherSubPane.getChildren().setAll(subSubLabel);

        twinPane = new VBox();
        visibleTwin = new Button("Twin");
        visibleTwin.setId("twin");
        invisibleTwin = new Button("Twin");
        invisibleTwin.setId("twin");
        invisibleTwin.setVisible(false);
        twinPane.getChildren().setAll(invisibleTwin, visibleTwin);

        window = new Stage();
        window.setTitle("window");
        window.setScene(new Scene(pane, 600, 400));
        otherWindow = new Stage();
        otherWindow.setTitle("otherWindow");
        otherWindow.setScene(new Scene(otherPane, 600, 400));
        twinWindow = new Stage();
        twinWindow.setTitle("twinWindow");
        twinWindow.setScene(new Scene(twinPane, 600, 400));
        window.show();
        otherWindow.show();
        twinWindow.show();
    }

    public void cleanupStages() {
        window.close();
        otherWindow.close();
        twinWindow.close();
    }

    @Test
    public void node_string_cssQuery() {
        // expect:
        assertThat(nodeFinder.lookup("#firstId").query(), is(firstIdLabel));
        assertThat(nodeFinder.lookup("#secondId").query(), is(secondIdLabel));
        assertThat(nodeFinder.lookup(".thirdClass").query(), is(thirdClassLabel));
    }

    @Test
    public void node_string_labelQuery() {
        // expect:
        assertThat(nodeFinder.lookup("first").query(), is(firstIdLabel));
        assertThat(nodeFinder.lookup("second").query(), is(secondIdLabel));
        assertThat(nodeFinder.lookup("third").query(), is(thirdClassLabel));
    }

    @Test
    public void node_predicate() {
        // given:
        Predicate<Node> predicate = createNodePredicate(createLabelTextPredicate("first"));

        // expect:
        assertThat(nodeFinder.lookup(predicate).query(), is(firstIdLabel));
    }

    @Test
    public void node_matcher() {
        // given:
        Matcher<Object> matcher = createObjectMatcher(createLabelTextMatcher("first"));

        // expect:
        assertThat(nodeFinder.lookup(matcher).query(), is(firstIdLabel));
    }

    @Test
    public void nodes_string_cssQuery() {
        // expect:
        assertThat(nodeFinder.lookup(".sub").queryAll(), hasItems(subLabel, subSubLabel));
    }

    @Test
    public void nodes_string_cssQuery_parentNode() {
        // expect:
        assertThat(nodeFinder.from(otherPane).lookup(".sub").queryAll(), hasItems(subLabel, subSubLabel));
        assertThat(nodeFinder.from(otherSubPane).lookup(".sub").queryAll(), hasItems(subSubLabel));
    }

    @Test
    public void nodes_string_labelQuery_parentNode() {
        // expect:
        assertThat(nodeFinder.from(otherPane).lookup("#subLabel").queryAll(), hasItem(subLabel));
        assertThat(nodeFinder.from(otherSubPane).lookup("#subSubLabel").queryAll(), hasItem(subSubLabel));
    }

    public Predicate<? extends Node> createLabelTextPredicate(final String labelText) {
        return (Predicate<Label>) label -> labelText.equals(label.getText());
    }

    public Matcher<? extends Node> createLabelTextMatcher(final String labelText) {
        return new TypeSafeMatcher<Label>() {
            @Override
            public boolean matchesSafely(Label label) {
                return labelText.equals(label.getText());
            }

            @Override
            public void describeTo(Description description) {}
        };
    }

    @SuppressWarnings("unchecked")
    public Predicate<Node> createNodePredicate(final Predicate predicate) {
        return (Predicate<Node>) predicate;
    }

    public Matcher<Object> createObjectMatcher(final Matcher matcher) {
        return new BaseMatcher<Object>() {
            @Override
            public boolean matches(Object item) {
                try {
                    return matcher.matches(item);
                }
                catch (ClassCastException ignore) {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {}
        };
    }

    public static class WindowFinderStub implements WindowFinder {
        public Window targetWindow;
        public List<Window> windows;

        @Override
        public Window targetWindow() {
            return targetWindow;
        }

        @Override
        public void targetWindow(Window window) {
            targetWindow = window;
        }

        @Override
        public List<Window> listWindows() {
            return windows;
        }

        @Override
        public List<Window> listTargetWindows() {
            return windows;
        }

        @Override
        public void targetWindow(Predicate<Window> predicate) {}

        @Override
        public void targetWindow(int windowIndex) {}

        @Override
        public void targetWindow(String stageTitleRegex) {}

        @Override
        public void targetWindow(Pattern stageTitlePattern) {}

        @Override
        public void targetWindow(Scene scene) {}

        @Override
        public void targetWindow(Node node) {}

        @Override
        public Window window(Predicate<Window> predicate) {
            return null;
        }

        @Override
        public Window window(int windowIndex) {
            return null;
        }

        @Override
        public Window window(String stageTitleRegex) {
            return null;
        }

        @Override
        public Window window(Pattern stageTitlePattern) {
            return null;
        }

        @Override
        public Window window(Scene scene) {
            return null;
        }

        @Override
        public Window window(Node node) {
            return null;
        }
    }

}
