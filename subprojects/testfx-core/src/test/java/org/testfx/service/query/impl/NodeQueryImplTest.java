/*
 * Copyright 2013-2014 SmartBear Software
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
package org.testfx.service.query.impl;

import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import com.google.common.base.Optional;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.service.query.NodeQuery;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.testfx.service.query.impl.NodeQueryUtils.bySelector;
import static org.testfx.service.query.impl.NodeQueryUtils.combine;
import static org.testfx.service.query.impl.NodeQueryUtils.hasId;
import static org.testfx.service.query.impl.NodeQueryUtils.rootOfScene;

public class NodeQueryImplTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    NodeQuery nodeQuery;

    Scene scene;

    @FXML Pane labels;
    @FXML Pane buttons;
    @FXML Pane textfields;

    @FXML Label label0;
    @FXML Label label1;
    @FXML Label label2;

    @FXML Button button0;
    @FXML Button button1;
    @FXML Button button2;

    @FXML TextField textfield0;
    @FXML TextField textfield1;
    @FXML TextField textfield2;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        nodeQuery = new NodeQueryImpl();

        FxToolkit.setupStage((stage) -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("res/nodeQueryImpl.fxml"));
            loader.setController(this);
            try {
                scene = new Scene(loader.load());
            }
            catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void queryAll() {
        // when:
        Set<Node> result = nodeQuery
            .from(label0, label1, label2)
            .queryAll();

        // then:
        assertThat(result, contains(label0, label1, label2));
    }

    @Test
    public void queryFirst() {
        // when:
        Optional<Node> result = nodeQuery
            .from(label0, label1, label2)
            .queryFirst();

        // then:
        assertThat(result, is(Optional.of(label0)));
    }

    @Test
    public void queryFirst_absent() {
        // when:
        Optional<Node> result = nodeQuery
            .from()
            .queryFirst();

        // then:
        assertThat(result, is(Optional.absent()));
    }

    @Test
    public void from_label0_label1() {
        // when:
        Set<Node> result = nodeQuery
            .from(label0, label1)
            .queryAll();

        // then:
        assertThat(result, contains(label0, label1));
    }

    @Test
    public void from_label1_label0() {
        // when:
        Set<Node> result = nodeQuery
            .from(label1, label0)
            .queryAll();

        // then:
        assertThat(result, contains(label1, label0));
    }

    @Test
    public void from_label0_from_label1() {
        // when:
        Set<Node> result = nodeQuery
            .from(label0).from(label1)
            .queryAll();

        // then:
        assertThat(result, contains(label0, label1));
    }

    @Test
    public void from_label0_label0() {
        // when:
        Set<Node> result = nodeQuery
            .from(label0, label0)
            .queryAll();

        // then:
        assertThat(result, contains(label0));
    }

    @Test
    public void from_label0_from_label0() {
        // when:
        Set<Node> result = nodeQuery
            .from(label0).from(label0)
            .queryAll();

        // then:
        assertThat(result, contains(label0));
    }

    @Test
    public void lookup() {
        // when:
        Set<Node> result = nodeQuery
            .from(rootOfScene(scene))
            .lookup(bySelector(".label"))
            .queryAll();

        // then:
        assertThat(result, contains(label0, label1, label2));
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
        assertThat(result, contains(label0, label1, label2));
    }

    @Test
    public void lookup_combine() {
        // when:
        Set<Node> result = nodeQuery
            .from(rootOfScene(scene))
            .lookup(combine(bySelector(".label"), bySelector(".button")))
            .queryAll();

        // then:
        assertThat(result, contains(label0, label1, label2, button0, button1, button2));
    }

    @Test
    public void lookupAt() {
        // when:
        Set<Node> result = nodeQuery
            .from(labels)
            .lookupAt(1, bySelector(".label"))
            .queryAll();

        // then:
        assertThat(result, contains(label1));
    }

    @Test
    public void lookupAt_lookupAt() {
        // when:
        Set<Node> result = nodeQuery
            .from(rootOfScene(scene))
            .lookupAt(0, bySelector("#labels"))
            .lookupAt(2, bySelector(".label"))
            .queryAll();

        // then:
        assertThat(result, contains(label2));
    }

    @Test
    public void lookupAt_with_index_out_of_bounds() {
        // when:
        Set<Node> result = nodeQuery
            .from(labels)
            .lookupAt(99, bySelector(".label"))
            .queryAll();

        // then:
        assertThat(result, empty());
    }

    @Test
    public void lookupAt_lookupAt_with_indizes_out_of_bounds() {
        // when:
        Set<Node> result = nodeQuery
            .from(rootOfScene(scene))
            .lookupAt(0, bySelector("#labels"))
            .lookupAt(99, bySelector(".label"))
            .queryAll();

        // then:
        assertThat(result, empty());
    }

    @Test
    public void childAt() {
        // when:
        Set<Node> result = nodeQuery
            .from(labels)
            .lookup(bySelector(".label"))
            .childAt(1)
            .queryAll();

        // then:
        assertThat(result, contains(label1));
    }

    @Test
    public void match() {
        // when:
        Set<Node> result = nodeQuery
            .from(rootOfScene(scene))
            .lookup(bySelector(".button"))
            .match(hasId("button1"))
            .queryAll();

        // then:
        assertThat(result, contains(button1));
    }

}
