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

import javafx.scene.control.MenuItem;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;

public class MenuItemMatchersTest extends FxRobot {

    MenuItem fooMenuItem;
    MenuItem barMenuItem;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupFixture(() -> {
            fooMenuItem = new MenuItem("foo");
            barMenuItem = new MenuItem("bar");
        });
    }

    @Test
    public void hasText() {
        assertThat(fooMenuItem, MenuItemMatchers.hasText("foo"));
    }

    @Test
    public void hasText_fails() {
        assertThatThrownBy(() -> assertThat(barMenuItem, MenuItemMatchers.hasText("foo")))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: MenuItem has text \"foo\"\n     " +
                        "but: was \"bar\"");
    }

    @Test
    public void hasText_matcher() {
        assertThat(fooMenuItem, MenuItemMatchers.hasText(endsWith("oo")));
    }

    @Test
    public void hasText_matcher_fails() {
        assertThatThrownBy(() -> assertThat(barMenuItem, MenuItemMatchers.hasText(endsWith("oo"))))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessage("\nExpected: MenuItem has a string ending with \"oo\"\n     " +
                        "but: was \"bar\"");
    }
}
