/*
 * Copyright 2013 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.loadui.testfx.service.finder.impl;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.exceptions.NoNodesFoundException;
import org.loadui.testfx.exceptions.NoNodesVisibleException;
import org.loadui.testfx.service.finder.WindowFinder;
import org.loadui.testfx.utils.FXTestUtils;

import org.hamcrest.Matchers;
import static org.hamcrest.MatcherAssert.assertThat;

public class NodeFinderImplTest extends GuiTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    Stage window;
    Stage otherWindow;

    Pane pane;
    Node firstIdLabel;
    Node secondIdLabel;
    Node thirdClassLabel;
    Node invisibleNode;

    Pane otherPane;
    Node subLabel;
    Pane otherSubPane;
    Node subSubLabel;

    WindowFinderStub windowFinder;
    NodeFinderImpl nodeFinder;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setup() {
        windowFinder = new WindowFinderStub();
        windowFinder.windows = Lists.<Window>newArrayList(window, otherWindow);
        nodeFinder = new NodeFinderImpl(windowFinder);
    }

    @After
    public void cleanup() throws Throwable {
        FXTestUtils.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                window.close();
                otherWindow.close();
            }
        }, 10);
    }

    @Override
    protected Parent getRootNode() {
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

        window = new Stage();
        window.setTitle("window");
        window.setScene(new Scene(pane, 600, 400));
        otherWindow = new Stage();
        otherWindow.setTitle("otherWindow");
        otherWindow.setScene(new Scene(otherPane, 600, 400));
        window.show();
        otherWindow.show();

        return new AnchorPane();
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void node_string_cssQuery() {
        // expect:
        assertThat(nodeFinder.node("#firstId"), Matchers.is(firstIdLabel));
        assertThat(nodeFinder.node("#secondId"), Matchers.is(secondIdLabel));
        assertThat(nodeFinder.node(".thirdClass"), Matchers.is(thirdClassLabel));
    }

    @Test
    public void node_string_labelQuery() {
        // expect:
        assertThat(nodeFinder.node("first"), Matchers.is(firstIdLabel));
        assertThat(nodeFinder.node("second"), Matchers.is(secondIdLabel));
        assertThat(nodeFinder.node("third"), Matchers.is(thirdClassLabel));
    }

    @Test(expected=NoNodesFoundException.class)
    public void node_string_cssQuery_nonExistentNode() {
        // expect:
        assertThat(nodeFinder.node("#nonExistentNode"), Matchers.is(Matchers.nullValue()));
    }

    @Test(expected=NoNodesVisibleException.class)
    public void node_string_cssQuery_invisibleNode() {
        // expect:
        assertThat(nodeFinder.node("#invisibleNode"), Matchers.is(Matchers.nullValue()));
    }

    @Test(expected=NoNodesFoundException.class)
    public void node_string_labelQuery_nonExistentNode() {
        // expect:
        assertThat(nodeFinder.nodes("nonExistent"), Matchers.is(Matchers.nullValue()));
    }

    @Test(expected=NoNodesVisibleException.class)
    public void node_string_labelQuery_invisibleNode() {
        // expect:
        assertThat(nodeFinder.nodes("invisible"), Matchers.is(Matchers.nullValue()));
    }

    @Test
    public void node_predicate() {
        // given:
        Predicate<Node> predicate = createNodePredicate(createLabelTextPredicate("first"));

        // expect:
        assertThat(nodeFinder.node(predicate), Matchers.is(firstIdLabel));
    }

    @Test
    public void node_matcher() {
        // given:
        Matcher<Object> matcher = createObjectMatcher(createLabelTextMatcher("first"));

        // expect:
        assertThat(nodeFinder.node(matcher), Matchers.is(firstIdLabel));
    }

    @Test
    public void nodes_string_cssQuery() {
        // expect:
        assertThat(nodeFinder.nodes(".sub"), Matchers.contains(subLabel, subSubLabel));
    }

    @Test(expected=NoNodesFoundException.class)
    public void nodes_string_cssQuery_nonExistentNode() {
        // expect:
        assertThat(nodeFinder.nodes("#nonExistentNode"), Matchers.is(Matchers.nullValue()));
    }

    @Test(expected=NoNodesVisibleException.class)
    public void nodes_string_cssQuery_invisibleNode() {
        // expect:
        assertThat(nodeFinder.nodes("#invisibleNode"), Matchers.is(Matchers.nullValue()));
    }

    @Test
    public void nodes_string_cssQuery_parentNode() {
        // expect:
        assertThat(nodeFinder.nodes(".sub", otherPane), Matchers.contains(subLabel, subSubLabel));
        assertThat(nodeFinder.nodes(".sub", otherSubPane), Matchers.contains(subSubLabel));
    }

    @Test
    public void nodes_string_labelQuery_parentNode() {
        // expect:
        assertThat(nodeFinder.nodes("#subLabel", otherPane), Matchers.contains(subLabel));
        assertThat(nodeFinder.nodes("#subSubLabel", otherSubPane), Matchers.contains(subSubLabel));
    }

    //---------------------------------------------------------------------------------------------
    // HELPER METHODS.
    //---------------------------------------------------------------------------------------------

    public Predicate<? extends Node> createLabelTextPredicate(final String labelText) {
        return new Predicate<Label>() {
            @Override
            public boolean apply(Label label) {
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
        public Window target() {
            return targetWindow;
        }

        @Override
        public void target(Window window) {
            targetWindow = window;
        }

        @Override
        public void target(int windowIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void target(String stageTitleRegex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void target(Scene scene) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Window> listWindows() {
            return windows;
        }

        @Override
        public List<Window> listOrderedWindows() {
            return windows;
        }

        @Override
        public Window window(int windowIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Window window(String stageTitleRegex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Window window(Scene scene) {
            throw new UnsupportedOperationException();
        }
    }

}
