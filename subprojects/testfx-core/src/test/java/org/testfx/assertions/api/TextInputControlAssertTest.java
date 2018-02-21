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
package org.testfx.assertions.api;

import javafx.scene.control.TextField;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.testfx.assertions.api.Assertions.assertThat;

public class TextInputControlAssertTest extends FxRobot {

    TextField foobarTextField;
    TextField quuxTextField;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupFixture(() -> {
            foobarTextField = new TextField("foobar");
            quuxTextField = new TextField("quux");
        });
    }

    @Test
    public void hasText() {
        assertThat(foobarTextField).hasText("foobar");
    }

    @Test
    public void hasText_fails() {
        assertThatThrownBy(() -> assertThat(quuxTextField).hasText("foobar"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TextInputControl has text \"foobar\"\n     " +
                        "but: was TextField with text: \"quux\"");
    }

    @Test
    public void doesNotHaveText() {
        assertThat(foobarTextField).doesNotHaveText("minnesota");
    }

    @Test
    public void doesNotHaveText_fails() {
        assertThatThrownBy(() -> assertThat(foobarTextField).doesNotHaveText("foobar"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TextInputControl has text \"foobar\" to be false\n     " +
                        "but: was TextField with text: \"foobar\"");
    }

    @Test
    public void hasText_matcher() {
        assertThat(foobarTextField).hasText(endsWith("bar"));
    }

    @Test
    public void hasText_matcher_fails() {
        assertThatThrownBy(() -> assertThat(quuxTextField).hasText(endsWith("bar")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TextInputControl has a string ending with \"bar\"\n     " +
                        "but: was TextField with text: \"quux\"");
    }

    @Test
    public void doesNotHaveText_matcher() {
        assertThat(foobarTextField).doesNotHaveText(startsWith("fuu"));
    }

    @Test
    public void doesNotHaveText_matcher_fails() {
        assertThatThrownBy(() -> assertThat(foobarTextField).doesNotHaveText(endsWith("bar")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: TextInputControl has a string ending with \"bar\" to be false\n     " +
                        "but: was TextField with text: \"foobar\"");
    }
}
