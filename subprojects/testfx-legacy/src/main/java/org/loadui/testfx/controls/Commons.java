/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.loadui.testfx.controls;

import javafx.scene.Node;
import javafx.scene.control.Label;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.controls.impl.HasLabelMatcher;
import org.loadui.testfx.controls.impl.HasLabelStringMatcher;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Commons {
    /**
     * Matches any Labeled Node that has the given label.
     *
     * @deprecated Use {@link #hasText(String)} instead.
     */
    @Deprecated
    @Factory
    public static Matcher<Object> hasLabel(String label) {
        return new HasLabelStringMatcher(label);
    }

    /**
     * Matches any Labeled Node that has a label that matches the given stringMatcher.
     *
     * @deprecated Use {@link #hasText(Matcher)} instead.
     */
    @Deprecated
    @Factory
    public static Matcher<Object> hasLabel(Matcher<String> stringMatcher) {
        return new HasLabelMatcher(stringMatcher);
    }

    /**
     * Matches any Labeled Node that has the given label.
     *
     * @param text
     */
    @Factory
    public static Matcher<Object> hasText(String text) {
        return new HasLabelStringMatcher(text);
    }

    /**
     * Matches any Labeled Node that has a label that matches the given stringMatcher.
     *
     * @param stringMatcher
     */
    @Factory
    public static Matcher<Object> hasText(Matcher<String> stringMatcher) {
        return new HasLabelMatcher(stringMatcher);
    }

    public static Node nodeLabeledBy(String labelQuery) {
        Node foundNode = GuiTest.find(labelQuery);

        checkArgument(foundNode instanceof Label);
        Label label = (Label) foundNode;
        Node labelFor = label.getLabelFor();
        checkNotNull(labelFor);
        return labelFor;
    }

    public static Node  nodeLabeledBy(Label label) {
        Node labelFor = label.getLabelFor();
        checkNotNull(labelFor);
        return labelFor;
    }
}
