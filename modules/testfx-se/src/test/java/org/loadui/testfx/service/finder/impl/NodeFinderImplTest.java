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

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.hamcrest.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.loadui.testfx.exceptions.NoNodesFoundException;
import org.loadui.testfx.exceptions.NoNodesVisibleException;
import org.loadui.testfx.framework.app.StageSetupCallback;
import org.loadui.testfx.framework.junit.AppRobotTestBase;
import org.loadui.testfx.service.finder.WindowFinder;

import java.util.List;

public class NodeFinderImplTest extends AppRobotTestBase {

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

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupClass() throws Throwable {
        AppRobotTestBase.setupApplication();
        AppRobotTestBase.setupStages(new StageSetupCallback() {
            @Override
            public void setupStages(Stage primaryStage) {
                primaryStage.setScene(new Scene(new Region(), 600, 400));
                setupStagesClass();
            }
        });
    }

    @Before
    public void setup() {
        windowFinder = new WindowFinderStub();
        windowFinder.windows = Lists.<Window>newArrayList(window, otherWindow, twinWindow);
        nodeFinder = new NodeFinderImpl(windowFinder);
    }

    @AfterClass
    public static void cleanupClass() throws Throwable {
        AppRobotTestBase.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                cleanupStagesClass();
            }
        });
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
        MatcherAssert.assertThat(nodeFinder.node("#firstId"), Matchers.is(firstIdLabel));
        MatcherAssert.assertThat(nodeFinder.node("#secondId"), Matchers.is(secondIdLabel));
        MatcherAssert.assertThat(nodeFinder.node(".thirdClass"), Matchers.is(thirdClassLabel));
    }

    @Test
    public void node_string_labelQuery() {
        // expect:
        MatcherAssert.assertThat(nodeFinder.node("first"), Matchers.is(firstIdLabel));
        MatcherAssert.assertThat(nodeFinder.node("second"), Matchers.is(secondIdLabel));
        MatcherAssert.assertThat(nodeFinder.node("third"), Matchers.is(thirdClassLabel));
    }

    @Test(expected=NoNodesFoundException.class)
    public void node_string_cssQuery_nonExistentNode() {
        // expect:
        MatcherAssert.assertThat(nodeFinder.node("#nonExistentNode"), Matchers.is(Matchers.nullValue()));
    }

    @Test(expected=NoNodesVisibleException.class)
    public void node_string_cssQuery_invisibleNode() {
        // expect:
        MatcherAssert.assertThat(nodeFinder.node("#invisibleNode"), Matchers.is(Matchers.nullValue()));
    }

    @Test
    public void node_string_cssQuery_twinNodes() {
        System.out.println(nodeFinder.node("#twin"));
        // TODO: test node in invisible container.
    }

    @Test(expected=NoNodesFoundException.class)
    public void node_string_labelQuery_nonExistentNode() {
        // expect:
        MatcherAssert.assertThat(nodeFinder.nodes("nonExistent"), Matchers.is(Matchers.nullValue()));
    }

    @Test(expected=NoNodesVisibleException.class)
    public void node_string_labelQuery_invisibleNode() {
        // expect:
        MatcherAssert.assertThat(nodeFinder.nodes("invisible"), Matchers.is(Matchers.nullValue()));
    }

    @Test
    public void node_predicate() {
        // given:
        Predicate<Node> predicate = createNodePredicate(createLabelTextPredicate("first"));

        // expect:
        MatcherAssert.assertThat(nodeFinder.node(predicate), Matchers.is(firstIdLabel));
    }

    @Test
    public void node_matcher() {
        // given:
        Matcher<Object> matcher = createObjectMatcher(createLabelTextMatcher("first"));

        // expect:
        MatcherAssert.assertThat(nodeFinder.node(matcher), Matchers.is(firstIdLabel));
    }

    @Test
    public void nodes_string_cssQuery() {
        // expect:
        MatcherAssert.assertThat(nodeFinder.nodes(".sub"), Matchers.contains(subLabel, subSubLabel));
    }

    @Test(expected=NoNodesFoundException.class)
    public void nodes_string_cssQuery_nonExistentNode() {
        // expect:
        MatcherAssert.assertThat(nodeFinder.nodes("#nonExistentNode"), Matchers.is(Matchers.nullValue()));
    }

    @Test(expected=NoNodesVisibleException.class)
    public void nodes_string_cssQuery_invisibleNode() {
        // expect:
        MatcherAssert.assertThat(nodeFinder.nodes("#invisibleNode"), Matchers.is(Matchers.nullValue()));
    }

    @Test
    public void nodes_string_cssQuery_parentNode() {
        // expect:
        MatcherAssert.assertThat(nodeFinder.nodes(".sub", otherPane), Matchers.contains(subLabel, subSubLabel));
        MatcherAssert.assertThat(nodeFinder.nodes(".sub", otherSubPane), Matchers.contains(subSubLabel));
    }

    @Test
    public void nodes_string_labelQuery_parentNode() {
        // expect:
        MatcherAssert.assertThat(nodeFinder.nodes("#subLabel", otherPane), Matchers.contains(subLabel));
        MatcherAssert.assertThat(nodeFinder.nodes("#subSubLabel", otherSubPane), Matchers.contains(subSubLabel));
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
