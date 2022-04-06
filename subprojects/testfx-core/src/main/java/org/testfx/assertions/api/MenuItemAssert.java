/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2022 The TestFX Contributors
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

import javafx.scene.control.MenuItem;

/**
 * Assertion methods for {@link javafx.scene.control.MenuItem}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(MenuItem)}</code>.
 */
public class MenuItemAssert extends AbstractMenuItemAssert<MenuItemAssert> {

    protected MenuItemAssert(MenuItem actual) {
        super(actual, MenuItemAssert.class);
    }
}
