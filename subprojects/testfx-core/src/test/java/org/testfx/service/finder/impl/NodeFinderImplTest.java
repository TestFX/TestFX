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
package org.testfx.service.finder.impl;

import java.util.List;
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

import com.google.common.collect.Lists;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testfx.api.FxToolkit;
import org.testfx.service.finder.NodeFinderException;
import org.testfx.service.finder.WindowFinder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assume.assumeThat;

public class NodeFinderImplTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    static Stage window;
    static Stage otherWindow;
    static Stage twinWindow;

    static Pane pane;
    static Node firstIdLabel;
    static Node secondIdLabel;
    static Node thirdClassLabel;
    static Node invisibleNode;

    static Pane otherPane;
    static Node subLabel;
    static Pane otherSubPane;
    static Node subSubLabel;

    static Pane twinPane;
    static Node visibleTwin;
    static Node invisibleTwin;

    WindowFinderStub windowFinder;
    NodeFinderImpl nodeFinder;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupScene(() -> new Scene(new Region(), 600, 400));
        FxToolkit.setupFixture(() -> setupStagesClass());
    }

    @AfterClass
    public static void cleanupSpec() throws Exception {
        FxToolkit.setupFixture(() -> cleanupStagesClass());
    }

    @Before
    public void setup() {
        assumeThat(System.getProperty("java.specification.version"), is("1.8"));

        windowFinder = new WindowFinderStub();
        windowFinder.windows = Lists.<Window>newArrayList(window, otherWindow, twinWindow);
        nodeFinder = new NodeFinderImpl(windowFinder);
    }

    public static void setupStagesClass() {
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

    public static void cleanupStagesClass() {
        window.close();
        otherWindow.close();
        twinWindow.close();
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

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
    @Ignore("error is only used for robots")
    public void node_string_cssQuery_nonExistentNode() {
        // expect:
        thrown.expect(NodeFinderException.class);
        thrown.expectMessage("No matching nodes were found.");
        assertThat(nodeFinder.lookup("#nonExistentNode").query(), is(nullValue()));
    }

    @Test
    @Ignore("error is only used for robots")
    public void node_string_cssQuery_invisibleNode() {
        // expect:
        thrown.expect(NodeFinderException.class);
        thrown.expectMessage("Matching nodes were found, but none of them are visible.");
        assertThat(nodeFinder.lookup("#invisibleNode").query(), is(nullValue()));
    }

    //@Test
    //public void node_string_cssQuery_twinNodes() {
    //    System.out.println(nodeFinder.node("#twin"));
    //    // TODO: test node in invisible container.
    //}

    @Test
    @Ignore("error is only used for robots")
    public void node_string_labelQuery_nonExistentNode() {
        // expect:
        thrown.expect(NodeFinderException.class);
        thrown.expectMessage("No matching nodes were found.");
        assertThat(nodeFinder.lookup("nonExistent").query(), is(nullValue()));
    }

    @Test
    @Ignore("error is only used for robots")
    public void node_string_labelQuery_invisibleNode() {
        // expect:
        thrown.expect(NodeFinderException.class);
        thrown.expectMessage("Matching nodes were found, but none of them are visible.");
        assertThat(nodeFinder.lookup("invisible").query(), is(nullValue()));
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
        assertThat(nodeFinder.lookup(".sub").queryAll(), contains(subLabel, subSubLabel));
    }

    @Test
    @Ignore("error is only used for robots")
    public void nodes_string_cssQuery_nonExistentNode() {
        // expect:
        thrown.expect(NodeFinderException.class);
        thrown.expectMessage("No matching nodes were found.");
        assertThat(nodeFinder.lookup("#nonExistentNode").query(), is(nullValue()));
    }

    @Test
    @Ignore("error is only used for robots")
    public void nodes_string_cssQuery_invisibleNode() {
        // expect:
        thrown.expect(NodeFinderException.class);
        thrown.expectMessage("Matching nodes were found, but none of them are visible.");
        assertThat(nodeFinder.lookup("#invisibleNode").query(), is(nullValue()));
    }

    @Test
    public void nodes_string_cssQuery_parentNode() {
        // expect:
        assertThat(nodeFinder.from(otherPane).lookup(".sub").queryAll(), contains(subLabel, subSubLabel));
        assertThat(nodeFinder.from(otherSubPane).lookup(".sub").queryAll(), contains(subSubLabel));
    }

    @Test
    public void nodes_string_labelQuery_parentNode() {
        // expect:
        assertThat(nodeFinder.from(otherPane).lookup("#subLabel").queryAll(), contains(subLabel));
        assertThat(nodeFinder.from(otherSubPane).lookup("#subSubLabel").queryAll(), contains(subSubLabel));
    }

    //---------------------------------------------------------------------------------------------
    // HELPER METHODS.
    //---------------------------------------------------------------------------------------------

    public Predicate<? extends Node> createLabelTextPredicate(final String labelText) {
        return new Predicate<Label>() {
            @Override
            public boolean test(Label label) {
                return labelText.equals(label.getText());
            }
        };
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

    //---------------------------------------------------------------------------------------------
    // HELPER CLASSES.
    //---------------------------------------------------------------------------------------------

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
