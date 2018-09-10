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
package org.testfx.matcher.base;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import org.junit.Test;
import org.testfx.cases.InternalContainerTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;

public class ParentMatchersTest extends InternalContainerTest {

    
    @Test
    public void hasChild() throws Exception {
        // given:
        addAll(new Label("foo"), new Button("bar"), new Button("baz"));

        // then:
        assertThat(getComponent(), ParentMatchers.hasChild());
    }

    @Test
    public void hasChild_fails() throws Exception {
        // given:

        // then:
        assertThatThrownBy(() -> assertThat(getComponent(), ParentMatchers.hasChild()))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: Parent has at least one child\n     " +
                        "but: was empty (contained no children)");
    }

    @Test
    public void hasChildren() throws Exception {
        // given:
        addAll(new Label("foo"), new Button("bar"), new Button("baz"));

        // then:
        assertThat(getComponent(), ParentMatchers.hasChildren(3));
    }

    @Test
    public void hasChildren_fails() throws Exception {
        // given:
        addAll(new Label("foo"), new Button("bar"));

        // then:
        assertThatThrownBy(() -> assertThat(getComponent(), ParentMatchers.hasChildren(3)))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: Parent has exactly 3 children\n     " +
                        "but: was [Label, Button] (which has 2 children)");
    }

}
