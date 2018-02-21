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
package org.testfx.assertions.api;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.util.WaitForAsyncUtils;

import static javafx.collections.FXCollections.observableArrayList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testfx.assertions.api.Assertions.assertThat;

public class ListViewAssertTest extends FxRobot {

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
        assertThat(listView).hasListCell("alice");
    }

    @Test
    public void hasListCell_with_null_fails() {
        assertThatThrownBy(() -> assertThat(listView).hasListCell(null))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: ListView has list cell \"null\"\n     " +
                        "but: was [alice, bob, carol, dave]");
    }

    @Test
    public void hasListCell_fails() {
        assertThatThrownBy(() -> assertThat(listView).hasListCell("foobar"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: ListView has list cell \"foobar\"\n     " +
                        "but: was [alice, bob, carol, dave]");
    }

    @Test
    public void doesNotHaveListCell() {
        assertThat(listView).doesNotHaveListCell("ebert");
    }

    @Test
    public void doesNotHaveListCell_fails() {
        assertThatThrownBy(() -> assertThat(listView).doesNotHaveListCell("alice"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: ListView has list cell \"alice\" to be false\n     " +
                        "but: was [alice, bob, carol, dave]");
    }

    @Test
    public void hasExactlyNumItems() {
        assertThat(listView).hasExactlyNumItems(4);
    }

    @Test
    public void hasExactlyNumItems_fails() {
        assertThatThrownBy(() -> assertThat(listView).hasExactlyNumItems(1))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: ListView has exactly 1 item\n     " +
                        "but: was 4");
    }

    @Test
    public void doesNotHaveExactlyNumItems() {
        assertThat(listView).doesNotHaveExactlyNumItems(3);
    }

    @Test
    public void doesNotHaveExactlyNumItems_fails() {
        assertThatThrownBy(() -> assertThat(listView).doesNotHaveExactlyNumItems(4))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: ListView has exactly 4 items to be false\n     " +
                        "but: was 4");
    }

    @Test
    public void isEmpty() {
        // when:
        Platform.runLater(() -> listView.getItems().clear());
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(listView).isEmpty();
    }

    @Test
    public void isEmpty_fails() {
        assertThatThrownBy(() -> assertThat(listView).isEmpty())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: ListView is empty (contains no items)\n     " +
                        "but: was contains 4 items");
    }

    @Test
    public void isNotEmpty() {
        // when:
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(listView).isNotEmpty();
    }

    @Test
    public void isNotEmpty_fails() {
        // when:
        Platform.runLater(() -> listView.getItems().clear());
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThatThrownBy(() -> assertThat(listView).isNotEmpty())
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: ListView is empty (contains no items) to be false\n     " +
                        "but: was empty");
    }

    @Test
    public void hasPlaceholder() {
        assertThat(listView).hasPlaceholder(new Label("Empty!"));
    }

    @Test
    public void hasPlaceholder_fails() {
        assertThatThrownBy(() -> assertThat(listView).hasPlaceholder(new Label("foobar")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: ListView has labeled placeholder containing text: \"foobar\"\n     " +
                        "but: was labeled placeholder containing text: \"Empty!\"");
    }

    @Test
    public void doesNotHavePlaceholder() {
        assertThat(listView).doesNotHavePlaceholder(new Label("cat"));
    }

    @Test
    public void doesNotHavePlaceholder_fails() {
        assertThatThrownBy(() -> assertThat(listView).doesNotHavePlaceholder(new Label("Empty!")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: ListView has labeled placeholder containing text: \"Empty!\" to be false\n" +
                        "     but: was labeled placeholder containing text: \"Empty!\"");
    }

    @Test
    public void hasVisiblePlaceholder() {
        assertThat(listView).hasVisiblePlaceholder(new Label("Empty!"));
    }

    @Test
    public void hasVisiblePlaceholder_fails_whenPlaceHolderHasWrongText() {
        assertThatThrownBy(() -> assertThat(listView).hasVisiblePlaceholder(new Label("foobar")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: ListView has visible labeled placeholder containing text: \"foobar\"\n     " +
                        "but: was visible labeled placeholder containing text: \"Empty!\"");
    }

    @Test
    public void hasVisiblePlaceholder_fails_whenPlaceHolderIsInvisible() {
        // when:
        listView.getPlaceholder().setVisible(false);
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThatThrownBy(() -> assertThat(listView).hasVisiblePlaceholder(new Label("Empty!")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: ListView has visible labeled placeholder containing text: \"Empty!\"\n     " +
                        "but: was invisible labeled placeholder containing text: \"Empty!\"");
    }

    @Test
    public void doesNotHaveVisiblePlaceholder_when_wrong_text() {
        // when:
        listView.getPlaceholder().setVisible(false);
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(listView).doesNotHaveVisiblePlaceholder(new Label("foobar"));
    }

    @Test
    public void doesNotHaveVisiblePlaceholder_when_invisible() {
        // when:
        listView.getPlaceholder().setVisible(false);
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(listView).doesNotHaveVisiblePlaceholder(new Label("Empty!"));
    }

    @Test
    public void doesNotHaveVisiblePlaceholder_fails_same_text() {
        assertThatThrownBy(() -> assertThat(listView).doesNotHaveVisiblePlaceholder(new Label("Empty!")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: ListView has visible labeled placeholder containing text: \"Empty!\" to be" +
                        " false\n     but: was visible labeled placeholder containing text: \"Empty!\"");
    }

}
