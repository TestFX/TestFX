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

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import org.junit.Test;
import org.testfx.cases.InternalTestCaseBase;

public class SimpleLabelTest extends InternalTestCaseBase {


    @Override
    public Node createComponent() {
        StackPane p = new StackPane(new Label("SimpleLabelTest"));
        p.setPrefSize(300, 100);
        return p;
    }

    @Test
    public void should_move_to_label() {
        moveTo(".label").sleep(1000);
    }

}
