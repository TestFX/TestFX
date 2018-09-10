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

import javafx.scene.Node;
import javafx.scene.control.Button;

import org.junit.Test;
import org.testfx.cases.InternalTestCaseBase;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;

public class ButtonMatchersTest extends InternalTestCaseBase {


    Button button;

    
    @Override
    public Node createComponent() {
        button = new Button();
        return button;
    }

    @Test
    public void isCancelButton() {
        // given:
        button.setCancelButton(true);

        // then:
        assertThat(button, ButtonMatchers.isCancelButton());
    }

    @Test
    public void isCancelButton_fails() {
        assertThatThrownBy(() -> assertThat(button, ButtonMatchers.isCancelButton()))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("\nExpected: Button is cancel button\n");
    }

    @Test
    public void isDefaultButton() {
        // given:
        button.setDefaultButton(true);

        // then:
        assertThat(button, ButtonMatchers.isDefaultButton());
    }

    @Test
    public void isDefaultButton_fails() {
        assertThatThrownBy(() -> assertThat(button, ButtonMatchers.isDefaultButton()))
                .isExactlyInstanceOf(AssertionError.class)
                .hasMessageStartingWith("\nExpected: Button is default button\n");
    }
}
