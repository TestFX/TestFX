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

import javafx.stage.Window;

import org.assertj.core.api.AbstractAssert;
import org.testfx.matcher.base.WindowMatchers;

import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.assertions.impl.Adapter.fromMatcher;

/**
 * Base class for all {@link javafx.stage.Window} assertions.
 */
public class AbstractWindowAssert<SELF extends AbstractWindowAssert<SELF>> extends AbstractAssert<SELF, Window> {

    protected AbstractWindowAssert(Window window, Class<?> selfType) {
        super(window, selfType);
    }

    /**
     * Verifies that the actual {@link javafx.stage.Window} is showing.
     *
     * @return this assertion object
     */
    public SELF isShowing() {
        assertThat(actual).is(fromMatcher(WindowMatchers.isShowing()));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.stage.Window} is not showing.
     *
     * @return this assertion object
     */
    public SELF isNotShowing() {
        assertThat(actual).is(fromMatcher(WindowMatchers.isNotShowing()));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.stage.Window} is focused.
     *
     * @return this assertion object
     */
    public SELF isFocused() {
        assertThat(actual).is(fromMatcher(WindowMatchers.isFocused()));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.stage.Window} is not focused.
     *
     * @return this assertion object
     */
    public SELF isNotFocused() {
        assertThat(actual).is(fromMatcher(WindowMatchers.isNotFocused()));
        return myself;
    }
}
