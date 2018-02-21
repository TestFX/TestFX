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

import javafx.scene.control.Button;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.testfx.assertions.api.Assertions.assertThat;

public class LabeledAssertTest extends FxRobot {

    Button foobarButton;
    Button quuxButton;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupFixture(() -> {
            foobarButton = new Button("foobar");
            quuxButton = new Button("quux");
        });
    }

    @Test
    public void hasText() {
        assertThat(foobarButton).hasText("foobar");
    }

    @Test
    public void hasText_fails() {
        assertThatThrownBy(() -> assertThat(quuxButton).hasText("foobar"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Labeled has text \"foobar\"\n     " +
                        "but: was \"quux\"");
    }

    @Test
    public void doesNotHaveText() {
        assertThat(foobarButton).doesNotHaveText("lilly pad");
    }

    @Test
    public void doesNotHaveText_fails() {
        assertThatThrownBy(() -> assertThat(foobarButton).doesNotHaveText("foobar"))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Labeled has text \"foobar\" to be false\n     " +
                        "but: was \"foobar\"");
    }

    @Test
    public void hasText_matcher() {
        assertThat(foobarButton).hasText(endsWith("bar"));
    }

    @Test
    public void hasText_matcher_fails() {
        assertThatThrownBy(() -> assertThat(quuxButton).hasText(endsWith("bar")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Labeled has a string ending with \"bar\"\n     " +
                        "but: was \"quux\"");
    }

    @Test
    public void doesNotHaveText_matcher() {
        assertThat(foobarButton).doesNotHaveText(endsWith("baz"));
    }

    @Test
    public void doesNotHaveText_matcher_fails() {
        assertThatThrownBy(() -> assertThat(foobarButton).doesNotHaveText(startsWith("foo")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("Expected: Labeled has a string starting with \"foo\" to be false\n     " +
                        "but: was \"foobar\"");
    }
}
