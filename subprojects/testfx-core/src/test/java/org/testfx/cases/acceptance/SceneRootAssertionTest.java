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
package org.testfx.cases.acceptance;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import com.google.common.base.Predicate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.loadui.testfx.Assertions;
import org.testfx.api.FxToolkit;

@Ignore
public class SceneRootAssertionTest {

    // FIXTURES.
    StackPane stackPane;
    Label label;

    // FIXTURE METHODS.
    @Before
    public void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupSceneRoot(() -> {
            label = new Label("foobar");
            stackPane = new StackPane(label);
            return stackPane;
        });
    }

    // FEATURE METHODS.
    @Test
    public void should_have_stage_root_with_label() {
        Assertions.verifyThat(stackPane, hasChild(label));
    }

    // HELPER METHODS.
    private Predicate<Parent> hasChild(Node node) {
        return (Parent parent) -> {
            return parent.getChildrenUnmodifiable().contains(node);
        };
    }

}
