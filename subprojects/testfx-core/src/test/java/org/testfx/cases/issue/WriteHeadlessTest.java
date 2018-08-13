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
package org.testfx.cases.issue;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import org.junit.Test;
import org.testfx.cases.InternalTestCaseBase;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;
import static org.testfx.util.DebugUtils.informedErrorMessage;

public class WriteHeadlessTest extends InternalTestCaseBase {


    @Override
    public Node createComponent() {
        TextField textField = new TextField();
        textField.setId("name");
        return new HBox(textField);
    }

    @Test
    public void write_should_work() {
        // when:
        clickOn("#name").write("John");

        // then:
        verifyThat("#name", hasText("John"), informedErrorMessage(this));
    }
}
