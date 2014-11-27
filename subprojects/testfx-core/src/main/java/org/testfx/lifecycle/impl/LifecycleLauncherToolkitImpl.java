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

import java.lang.reflect.Field;
import java.util.Objects;
import javafx.application.Application;

import org.testfx.lifecycle.LifecycleLauncher;

public class LifecycleLauncherToolkitImpl implements LifecycleLauncher {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final String PROPERTY_JAVAFX_MACOSX_EMBEDDED = "javafx.macosx.embedded";
    private static final String PROPERTY_TESTFX_HEADLESS = "testfx.headless";

    private static final String PLATFORM_FACTORY_CLASS =
        "com.sun.glass.ui.PlatformFactory";
    private static final String PLATFORM_FACTORY_MONOCLE_IMPL =
        "com.sun.glass.ui.monocle.MonoclePlatformFactory";

    private static final String NATIVE_PLATFORM_FACTORY_CLASS =
        "com.sun.glass.ui.monocle.NativePlatformFactory";
    private static final String NATIVE_PLATFORM_HEADLESS_IMPL =
        "com.sun.glass.ui.monocle.headless.HeadlessPlatform";

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void launch(Class<? extends Application> appClass,
                       String... appArgs) {
        initMacosxEmbedded();
        initMonocleHeadless();
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

    private void initMonocleHeadless() {
        if (checkSystemPropertyEquals(PROPERTY_TESTFX_HEADLESS, "true")) {
            try {
                assignMonoclePlatform();
                assignHeadlessPlatform();
            }
            catch (ClassNotFoundException exception) {
                throw new IllegalStateException("Monocle headless platform not found", exception);
            }
            catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    private boolean checkSystemPropertyEquals(String propertyName,
                                              String valueOrNull) {
        return Objects.equals(System.getProperty(propertyName, null), valueOrNull);
    }

    private void assignMonoclePlatform()
                                throws Exception {
        Class<?> platformFactoryClass = Class.forName(PLATFORM_FACTORY_CLASS);
        Object platformFactoryImpl = Class.forName(PLATFORM_FACTORY_MONOCLE_IMPL).newInstance();
        assignPrivateStaticField(platformFactoryClass, "instance", platformFactoryImpl);
    }

    private void assignHeadlessPlatform()
                                 throws Exception {
        Class<?> nativePlatformFactoryClass = Class.forName(NATIVE_PLATFORM_FACTORY_CLASS);
        Object nativePlatformImpl = Class.forName(NATIVE_PLATFORM_HEADLESS_IMPL).newInstance();
        assignPrivateStaticField(nativePlatformFactoryClass, "platform", nativePlatformImpl);
    }

    private void assignPrivateStaticField(Class<?> cls,
                                          String name,
                                          Object value)
                                   throws Exception {
        Field field = cls.getDeclaredField(name);
        field.setAccessible(true);
        field.set(cls, value);
        field.setAccessible(false);
    }

}
