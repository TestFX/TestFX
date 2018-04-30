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
package org.testfx.toolkit.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import javafx.application.Application;

import org.testfx.toolkit.ApplicationLauncher;

public class ApplicationLauncherImpl implements ApplicationLauncher {

    @Override
    public void launch(Class<? extends Application> appClass, String... appArgs) {
        initMonocleHeadless();
        Application.launch(appClass, appArgs);
    }

    private void initMonocleHeadless() {
        if (Boolean.getBoolean("testfx.headless")) {
            try {
                assignMonoclePlatform();
                assignHeadlessPlatform();
            }
            catch (ClassNotFoundException exception) {
                throw new IllegalStateException("monocle headless platform not found - did you forget to add " +
                        "a dependency on monocle (https://github.com/TestFX/Monocle)?", exception);
            }
            catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    private void assignMonoclePlatform() throws Exception {
        Class<?> platformFactoryClass = Class.forName("com.sun.glass.ui.PlatformFactory");
        Object platformFactoryImpl = Class.forName("com.sun.glass.ui.monocle.MonoclePlatformFactory")
                .getDeclaredConstructor().newInstance();
        assignPrivateStaticField(platformFactoryClass, "instance", platformFactoryImpl);
    }

    private void assignHeadlessPlatform() throws Exception {
        Class<?> nativePlatformFactoryClass = Class.forName("com.sun.glass.ui.monocle.NativePlatformFactory");
        try {
            Constructor<?> nativePlatformCtor = Class.forName(
                    "com.sun.glass.ui.monocle.HeadlessPlatform").getDeclaredConstructor();
            nativePlatformCtor.setAccessible(true);
            assignPrivateStaticField(nativePlatformFactoryClass, "platform", nativePlatformCtor.newInstance());
        }
        catch (ClassNotFoundException exception) {
            // Before Java 8u40 HeadlessPlatform was located inside of a "headless" package.
            Constructor<?> nativePlatformCtor = Class.forName(
                    "com.sun.glass.ui.monocle.headless.HeadlessPlatform").getDeclaredConstructor();
            nativePlatformCtor.setAccessible(true);
            assignPrivateStaticField(nativePlatformFactoryClass, "platform", nativePlatformCtor.newInstance());
        }
    }

    private void assignPrivateStaticField(Class<?> clazz, String name, Object value) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        field.set(clazz, value);
        field.setAccessible(false);
    }

}
