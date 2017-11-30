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

import org.testfx.matcher.control.ButtonMatchers;

import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.assertions.impl.Adapter.fromInverseMatcher;
import static org.testfx.assertions.impl.Adapter.fromMatcher;

/**
 * Base class for all {@link javafx.scene.control.Button} assertions.
 */
public class AbstractButtonAssert<SELF extends AbstractButtonAssert<SELF>> extends AbstractLabeledAssert<SELF> {

    protected AbstractButtonAssert(Button actual, Class<?> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.Button} is a cancel button.
     *
     * @return this assertion object
     */
    public SELF isCancelButton() {
        assertThat(actual).is(fromMatcher(ButtonMatchers.isCancelButton()));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.Button} is not a cancel button.
     *
     * @return this assertion object
     */
    public SELF isNotCancelButton() {
        assertThat(actual).is(fromInverseMatcher(ButtonMatchers.isCancelButton()));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.Button} is a default button.
     *
     * @return this assertion object
     */
    public SELF isDefaultButton() {
        assertThat(actual).is(fromMatcher(ButtonMatchers.isDefaultButton()));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.Button} is not a default button.
     *
     * @return this assertion object
     */
    public SELF isNotDefaultButton() {
        assertThat(actual).is(fromInverseMatcher(ButtonMatchers.isDefaultButton()));
        return myself;
    }
}
