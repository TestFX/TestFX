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

import javafx.scene.Parent;

import org.testfx.matcher.base.ParentMatchers;

import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.assertions.impl.Adapter.fromInverseMatcher;
import static org.testfx.assertions.impl.Adapter.fromMatcher;

/**
 * Base class for all {@link javafx.scene.Parent} assertions.
 */
public class AbstractParentAssert<SELF extends AbstractParentAssert<SELF>> extends AbstractNodeAssert<SELF> {

    protected AbstractParentAssert(Parent actual, Class<?> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link javafx.scene.Parent} has at least one child.
     *
     * @return this assertion object
     */
    public SELF hasAnyChild() {
        assertThat(actual).is(fromMatcher(ParentMatchers.hasChild()));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.Parent} has no children.
     *
     * @return this assertion object
     */
    public SELF hasNoChildren() {
        assertThat(actual).is(fromInverseMatcher(ParentMatchers.hasChild()));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.Parent} has exactly the given
     * {@code amount} of children.
     *
     * @param amount the given amount of children that the actual {@code Parent} should
     * exactly have
     * @return this assertion object
     */
    public SELF hasExactlyNumChildren(int amount) {
        assertThat(actual).is(fromMatcher(ParentMatchers.hasChildren(amount)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.Parent} does not have exactly the given
     * {@code amount} of children.
     *
     * @param amount the given amount of children that the actual {@code Parent} should not
     * exactly have
     * @return this assertion object
     */
    public SELF doesNotHaveExactlyNumChildren(int amount) {
        assertThat(actual).is(fromInverseMatcher(ParentMatchers.hasChildren(amount)));
        return myself;
    }
}
