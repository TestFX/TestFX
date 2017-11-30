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

import javafx.scene.text.Text;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.testfx.assertions.api.Assertions.assertThat;

public class TextAssertTest extends FxRobot {

    Text foobarText;
    Text quuxText;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupFixture(() -> {
            foobarText = new Text("foobar");
            quuxText = new Text("quux");
        });
    }

    @Test
    public void hasText() {
        // expect:
        assertThat(foobarText).hasText("foobar");
    }

    @Test
    public void hasText_fails() {
        // expect:
        assertThatThrownBy(() -> assertThat(quuxText).hasText("foobar"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Text has text \"foobar\"\n     " +
                        "but: was Text with text: \"quux\"");
    }

    @Test
    public void doesNotHaveText() {
        // expect:
        assertThat(foobarText).doesNotHaveText("veritas");
    }

    @Test
    public void doesNotHaveText_fails() {
        // expect:
        assertThatThrownBy(() -> assertThat(quuxText).doesNotHaveText("quux"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Text has text \"quux\" to be false\n     " +
                        "but: was Text with text: \"quux\"");
    }

    @Test
    public void hasText_matcher() {
        // expect:
        assertThat(foobarText).hasText(endsWith("bar"));
    }

    @Test
    public void hasText_matcher_fails() {
        // expect:
        assertThatThrownBy(() -> assertThat(quuxText).hasText(endsWith("bar")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Text has a string ending with \"bar\"\n     " +
                        "but: was Text with text: \"quux\"");
    }

    @Test
    public void doesNotHaveText_matcher() {
        // expect:
        assertThat(foobarText).doesNotHaveText(startsWith("bar"));
    }

    @Test
    public void doesNotHaveText_matcher_fails() {
        // expect:
        assertThatThrownBy(() -> assertThat(foobarText).doesNotHaveText(startsWith("foo")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Text has a string starting with \"foo\" to be false\n     " +
                        "but: was Text with text: \"foobar\"");
    }
}
