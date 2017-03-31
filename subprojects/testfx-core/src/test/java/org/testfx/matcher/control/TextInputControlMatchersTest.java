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
package org.testfx.matcher.control;

import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testfx.TestFXRule;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;

public class TextInputControlMatchersTest extends FxRobot {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public TextField foobarTextField;
    public TextField quuxTextField;
    public Region region;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupFixture(() -> {
            foobarTextField = new TextField("foobar");
            quuxTextField = new TextField("quux");
            region = new Region();
        });
    }

    @Test
    public void hasText() {
        // expect:
        assertThat(foobarTextField, TextInputControlMatchers.hasText("foobar"));
    }

    @Test
    public void hasText_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TextInputControl has text \"foobar\"\n");

        assertThat(quuxTextField, TextInputControlMatchers.hasText("foobar"));
    }

    @Test
    public void hasText_with_region_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TextInputControl has text \"foobar\"\n");

        assertThat(region, TextInputControlMatchers.hasText("foobar"));
    }

    @Test
    public void hasText_matcher() {
        // expect:
        assertThat(foobarTextField, TextInputControlMatchers.hasText(endsWith("bar")));
    }

    @Test
    public void hasText_matcher_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TextInputControl has a string ending with \"bar\"\n");

        assertThat(quuxTextField, TextInputControlMatchers.hasText(endsWith("bar")));
    }

    @Test
    public void hasText_matcher_with_region_fails() {
        // expect:
        exception.expect(AssertionError.class);
        exception.expectMessage("Expected: TextInputControl has a string ending with \"bar\"\n");

        assertThat(region, TextInputControlMatchers.hasText(endsWith("bar")));
    }

}
