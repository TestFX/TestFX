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
package org.testfx.matcher.control;

import javafx.scene.control.TextField;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.cases.InternalContainerTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;

public class TextInputControlMatchersTest extends InternalContainerTest {


    TextField foobarTextField;
    TextField quuxTextField;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        foobarTextField = new TextField("foobar");
        quuxTextField = new TextField("quux");
        addAll(foobarTextField, quuxTextField);
    }

    @Test
    public void hasText() {
        assertThat(foobarTextField, TextInputControlMatchers.hasText("foobar"));
    }

    @Test
    public void hasText_fails() {
        assertThatThrownBy(() -> assertThat(quuxTextField, TextInputControlMatchers.hasText("foobar")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: TextInputControl has text \"foobar\"\n     " +
                        "but: was TextField with text: \"quux\"");
    }

    @Test
    public void hasText_matcher() {
        assertThat(foobarTextField, TextInputControlMatchers.hasText(endsWith("bar")));
    }

    @Test
    public void hasText_matcher_fails() {
        assertThatThrownBy(() -> assertThat(quuxTextField, TextInputControlMatchers.hasText(endsWith("bar"))))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: TextInputControl has a string ending with \"bar\"\n     " +
                        "but: was TextField with text: \"quux\"");
    }

}
