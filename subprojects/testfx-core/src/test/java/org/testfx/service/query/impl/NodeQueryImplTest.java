/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2024 The TestFX Contributors
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
package org.testfx.service.query.impl;

import java.util.Optional;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.TestFXRule;
import org.testfx.api.FxToolkit;
import org.testfx.service.query.EmptyNodeQueryException;
import org.testfx.service.query.NodeQuery;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testfx.util.NodeQueryUtils.bySelector;
import static org.testfx.util.NodeQueryUtils.combine;
import static org.testfx.util.NodeQueryUtils.hasId;
import static org.testfx.util.NodeQueryUtils.rootOfScene;

public class NodeQueryImplTest {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    NodeQuery nodeQuery;
    Scene scene;

    Pane labels;
    Label label0;
    Label label1;
    Label label2;
    Button button0;
    Button button1;
    Button button2;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        nodeQuery = new NodeQueryImpl();

        FxToolkit.setupStage(stage -> {
            label0 = new Label("0");
            label1 = new Label("1");
            label2 = new Label("2");
            labels = new HBox(label0, label1, label2);
            labels.setId("labels");
            labels.setPadding(new Insets(10));

            button0 = new Button("0");
            button0.setMnemonicParsing(false);
            button1 = new Button("1");
            button1.setMnemonicParsing(false);
            button1.setId("button1");
            button2 = new Button("2");
            button2.setMnemonicParsing(false);
            HBox buttons = new HBox(button0, button1, button2);
            buttons.setPadding(new Insets(10));

            VBox vBox = new VBox(labels, buttons);
            vBox.setPrefSize(100, 200);
            StackPane stackPane = new StackPane();
            stackPane.setPrefSize(200, 400);
            scene = new Scene(new StackPane(vBox));
        });
    }

    @Test
    public void query() {
        // when:
        Node result = nodeQuery
            .from(label0, label1, label2)
            .query();

        // then:
        assertThat(result, is(label0));
    }

    @Test
    public void empty_query_throws_exception() {
        assertThatThrownBy(() -> nodeQuery.query())
                .isExactlyInstanceOf(EmptyNodeQueryException.class)
                .hasMessageContaining("the empty NodeQuery");
    }

    @Test
    public void tryQuery() {
        // when:
        Optional<Node> result = nodeQuery
            .from(label0, label1, label2)
            .tryQuery();

        // then:
        assertThat(result, is(Optional.of(label0)));
    }

    @Test
    public void tryQueryFirst_absent() {
        // when:
        Optional<Node> result = nodeQuery
            .tryQuery();

        // then:
        assertThat(result, is(Optional.empty()));
    }

    @Test
    public void queryAll() {
        // when:
        Set<Node> result = nodeQuery
            .from(label0, label1, label2)
            .queryAll();

        // then:
        assertThat(result, hasItems(label0, label1, label2));
    }

    @Test
    public void queryAll_is_empty() {
        // when:
        Set<Node> result = nodeQuery
            .queryAll();

        // then:
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void queryAllAs() {
        // when:
        Set<Label> result = nodeQuery
                .from(label0, label1, label2)
                .queryAllAs(Label.class);

        // then:
        assertThat(result, hasItems(label0, label1, label2));
    }

    @Test
    public void from_label0_label1() {
        // when:
        Set<Node> result = nodeQuery
            .from(label0, label1)
            .queryAll();

        // then:
        assertThat(result, hasItems(label0, label1));
    }

    @Test
    public void from_label1_label0() {
        // when:
        Set<Node> result = nodeQuery
            .from(label1, label0)
            .queryAll();

        // then:
        assertThat(result, hasItems(label1, label0));
    }

    @Test
    public void from_label0_from_label1() {
        // when:
        Set<Node> result = nodeQuery
            .from(label0).from(label1)
            .queryAll();

        // then:
        assertThat(result, hasItems(label0, label1));
    }

    @Test
    public void from_label0_label0() {
        // when:
        Set<Node> result = nodeQuery
            .from(label0, label0)
            .queryAll();

        // then:
        assertThat(result, hasItems(label0));
    }

    @Test
    public void from_label0_from_label0() {
        // when:
        Set<Node> result = nodeQuery
            .from(label0).from(label0)
            .queryAll();

        // then:
        assertThat(result, hasItems(label0));
    }

    @Test
    public void lookup() {
        // when:
        Set<Node> result = nodeQuery
            .from(rootOfScene(scene))
            .lookup(bySelector(".label"))
            .queryAll();

        // then:
        assertThat(result, hasItems(label0, label1, label2));
    }

    @Test
    public void lookup_lookup() {
        // when:
        Set<Node> result = nodeQuery
            .from(rootOfScene(scene))
            .lookup(bySelector("#labels"))
            .lookup(bySelector(".label"))
            .queryAll();

        // then:
        assertThat(result, hasItems(label0, label1, label2));
    }

    @Test
    public void lookup_combine() {
        // when:
        Set<Node> result = nodeQuery
            .from(rootOfScene(scene))
            .lookup(combine(bySelector(".label"), bySelector(".button")))
            .queryAll();

        // then:
        assertThat(result, hasItems(label0, label1, label2, button0, button1, button2));
    }

    @Test
    public void lookup_selectAt() {
        // when:
        Set<Node> result = nodeQuery
            .from(labels)
            .lookup(bySelector(".label"))
            .nth(1)
            .queryAll();

        // then:
        assertThat(result, hasItems(label1));
    }

    @Test
    public void lookup_selectAt_lookup_selectAt() {
        // when:
        Set<Node> result = nodeQuery
            .from(rootOfScene(scene))
            .lookup(bySelector("#labels"))
            .nth(0)
            .lookup(bySelector(".label"))
            .nth(2)
            .queryAll();

        // then:
        assertThat(result, hasItems(label2));
    }

    @Test
    public void lookup_selectAt_with_index_out_of_bounds() {
        // when:
        Set<Node> result = nodeQuery
            .from(labels)
            .lookup(bySelector(".label"))
            .nth(99)
            .queryAll();

        // then:
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void lookup_selectAt_lookup_selectAt_with_indices_out_of_bounds() {
        // when:
        Set<Node> result = nodeQuery
            .from(rootOfScene(scene))
            .lookup(bySelector("#labels"))
            .nth(0)
            .lookup(bySelector(".label"))
            .nth(99)
            .queryAll();

        // then:
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void lookup_select() {
        // when:
        Set<Node> result = nodeQuery
            .from(rootOfScene(scene))
            .lookup(bySelector(".button"))
            .match(hasId("button1"))
            .queryAll();

        // then:
        assertThat(result, hasItems(button1));
    }

}
