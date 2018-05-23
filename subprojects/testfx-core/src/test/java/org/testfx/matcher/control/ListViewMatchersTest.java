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
package org.testfx.matcher.control;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.util.WaitForAsyncUtils;

import static javafx.collections.FXCollections.observableArrayList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListViewMatchersTest extends FxRobot {

    @Rule
    public TestRule rule = new TestFXRule();

    ListView<String> listView;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            listView = new ListView<>();
            listView.setItems(observableArrayList("alice", "bob", "carol", "dave"));
            listView.setPlaceholder(new Label("Empty!"));
            return new StackPane(listView);
        });
        FxToolkit.showStage();
    }

    @Test
    public void hasListCell() {
        assertThat(listView, ListViewMatchers.hasListCell("alice"));
    }

    @Test
    public void hasListCell_with_null_fails() {
        assertThatThrownBy(() -> assertThat(listView, ListViewMatchers.hasListCell(null)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: ListView has list cell \"null\"\n     " +
                        "but: was [alice, bob, carol, dave]");
    }

    @Test
    public void hasListCell_fails() {
        assertThatThrownBy(() -> assertThat(listView, ListViewMatchers.hasListCell("foobar")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: ListView has list cell \"foobar\"\n     " +
                        "but: was [alice, bob, carol, dave]");
    }

    @Test
    public void hasItems() {
        assertThat(listView, ListViewMatchers.hasItems(4));
    }

    @Test
    public void hasItems_fails() {
        assertThatThrownBy(() -> assertThat(listView, ListViewMatchers.hasItems(0)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: ListView has exactly 0 items\n     " +
                        "but: was 4");
    }

    @Test
    public void isEmpty() {
        // given:
        Platform.runLater(() -> listView.getItems().clear());
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(listView, ListViewMatchers.isEmpty());
    }

    @Test
    public void isEmpty_fails() {
        assertThatThrownBy(() -> assertThat(listView, ListViewMatchers.isEmpty()))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: ListView is empty (contains no items)\n     " +
                        "but: was contains 4 items");
    }

    @Test
    public void hasPlaceholder() {
        assertThat(listView, ListViewMatchers.hasPlaceholder(new Label("Empty!")));
    }

    @Test
    public void hasPlaceholder_fails() {
        assertThatThrownBy(() -> assertThat(listView, ListViewMatchers.hasPlaceholder(new Label("foobar"))))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: ListView has labeled placeholder containing text: \"foobar\"\n     " +
                        "but: was labeled placeholder containing text: \"Empty!\"");
    }

    @Test
    public void hasVisiblePlaceholder() {
        assertThat(listView, ListViewMatchers.hasVisiblePlaceholder(new Label("Empty!")));
    }

    @Test
    public void hasVisiblePlaceholder_fails_whenPlaceHolderHasWrongText() {
        assertThatThrownBy(() -> assertThat(listView, ListViewMatchers.hasVisiblePlaceholder(new Label("foobar"))))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: ListView has visible labeled placeholder containing text: \"foobar\"\n" +
                        "     but: was visible labeled placeholder containing text: \"Empty!\"");
    }

    @Test
    public void hasVisiblePlaceholder_fails_whenPlaceHolderIsInvisible() {
        // when:
        listView.getPlaceholder().setVisible(false);
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThatThrownBy(() -> assertThat(listView, ListViewMatchers.hasVisiblePlaceholder(new Label("Empty!"))))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: ListView has visible labeled placeholder containing text: \"Empty!\"\n" +
                        "     but: was invisible labeled placeholder containing text: \"Empty!\"");
    }
}
