/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.testfx.lifecycle.impl;

import java.util.Objects;
import javafx.application.Application;

import org.testfx.lifecycle.LifecycleLauncher;

public class LifecycleLauncherToolkitImpl implements LifecycleLauncher {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final String PROPERTY_JAVAFX_MACOSX_EMBEDDED = "javafx.macosx.embedded";

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void launch(Class<? extends Application> appClass,
                       String... appArgs) {
        initMacosxEmbedded();
        Application.launch(appClass, appArgs);
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private void initMacosxEmbedded() {
        if (checkSystemPropertyEquals(PROPERTY_JAVAFX_MACOSX_EMBEDDED, null)) {
            System.setProperty(PROPERTY_JAVAFX_MACOSX_EMBEDDED, "true");
        }
    }

    private boolean checkSystemPropertyEquals(String propertyName,
                                              String valueOrNull) {
        return Objects.equals(System.getProperty(propertyName, null), valueOrNull);
    }

}
