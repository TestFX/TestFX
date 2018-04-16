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
package org.testfx.cases.integration;

import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.cases.TestCaseBase;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.util.WaitForAsyncUtils;

import static javafx.collections.FXCollections.observableArrayList;
import static org.assertj.core.api.Assertions.assertThat;

public class DragAndDropTest extends TestCaseBase {

    @Rule
    public TestFXRule testFXRule = new TestFXRule(3);

    ListView<String> leftListView;
    ListView<String> rightListView;

    @Before
    public void setup() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            leftListView = new ListView<>(observableArrayList("L1", "L2", "L3"));
            rightListView = new ListView<>(observableArrayList("R1", "R2", "R3"));
            setupListView(leftListView);
            setupListView(rightListView);
            return new HBox(leftListView, rightListView);
        });
        FxToolkit.setupStage(Stage::show);
    }

    void setupListView(ListView<String> listView) {
        listView.setOnDragDetected(event -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            ClipboardContent content = new ClipboardContent();
            content.putString(selectedItem);
            Dragboard dragboard = listView.startDragAndDrop(TransferMode.MOVE);
            dragboard.setContent(content);
            event.consume();
        });

        listView.setOnDragEntered(event -> event.acceptTransferModes(TransferMode.MOVE));
        listView.setOnDragOver(event -> event.acceptTransferModes(TransferMode.MOVE));

        listView.setOnDragDropped(event -> {
            event.acceptTransferModes(TransferMode.MOVE);
            Dragboard dragboard = event.getDragboard();
            String acceptedItem = dragboard.getString();
            if (acceptedItem != null) {
                listView.getItems().add(acceptedItem);
                event.setDropCompleted(true);
                event.consume();
            }
        });

        listView.setOnDragDone(event -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            listView.getItems().remove(selectedItem);
        });
    }

    @Test
    public void should_have_initialized_items() {
        assertThat(leftListView.getItems()).containsExactly("L1", "L2", "L3");
        assertThat(rightListView.getItems()).containsExactly("R1", "R2", "R3");
    }

    @Test
    public void should_drag_and_drop_from_left_to_right() {
        // when:
        drag("L1");
        moveTo("R1");
        drop();

        // then:
        assertThat(leftListView.getItems()).containsExactly("L2", "L3");
        // gets added to end of ListView
        assertThat(rightListView.getItems()).containsExactly("R1", "R2", "R3", "L1");
    }

    @Test
    public void should_drag_and_drop_from_right_to_left() {
        // when:
        drag("R3");
        dropTo("L2");
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(leftListView.getItems()).containsExactly("L1", "L2", "L3", "R3");
        assertThat(rightListView.getItems()).containsExactly("R1", "R2");
    }

    @Test
    @Ignore("see #383")
    public void should_drag_and_drop_from_left_to_left() {
        // when:
        drag("L3");
        dropTo("L2");
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        assertThat(leftListView.getItems()).containsExactly("L1", "L2", "L3");
        assertThat(rightListView.getItems()).containsExactly("R1", "R2", "R3");
    }

}
