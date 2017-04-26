/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2017 The TestFX Contributors
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
package org.loadui.testfx.controls.impl;

import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Text;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import static com.google.common.base.Preconditions.checkArgument;
import static org.loadui.testfx.GuiTest.find;

public class HasLabelStringMatcher extends TypeSafeMatcher<Object> {

    private final String label;
    private String actualText;

    public HasLabelStringMatcher(String label) {
        this.label = label;
    }

    public void describeTo(Description desc) {
        desc.appendText("Node should have label ");
        desc.appendValue(label);
    }

    @Override
    public void describeMismatchSafely(Object query, Description desc) {
        desc.appendText("Label was ");
        desc.appendValue(actualText);
    }


    @Override
    public boolean matchesSafely(Object target) {
        if (target instanceof String) {
            return nodeHasLabel(find((String) target));
        }
        else if (target instanceof Labeled || target instanceof TextInputControl) {
            return nodeHasLabel((Node) target);
        }
        return false;
    }

    private boolean nodeHasLabel(Node node) {
        checkArgument(node instanceof Labeled || node instanceof TextInputControl || node instanceof Text,
                "Target node must be Labeled or TextInputControl or Text, was (" + node.getClass().getName() + ").");

        if (node instanceof Labeled) {
            Labeled labeled = (Labeled) node;
            actualText = labeled.getText();
        }
        else if (node instanceof TextInputControl) {
            TextInputControl textInput = (TextInputControl) node;
            actualText = textInput.getText();
        } else {
            Text textInput = (Text) node;
            actualText = textInput.getText();
        }
        return label.equals(actualText);
    }

}
