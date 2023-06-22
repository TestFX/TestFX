/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2023 The TestFX Contributors
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
package org.testfx.cases.issue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

public class MemoryLeakBug extends FxRobot {

    private Application application;

    @Test
    public void testMemoryDoesNotGrowOverMultipleApplicationStartsAndStops() throws Exception {
        // This test intentionally opens multiple instances of a "large" application
        // to ensure that the application is cleaned up after each test run, and an
        // OutOfMemoryError is not thrown.

        // Start the application multiple times. This usually fails withing
        // five iterations, so if we can make it to ten iterations we consider
        // it successful.
        for (int index = 0; index < 10; index++) {
            startApplication();
            printMemoryUse(index);
            stopApplication();
        }
    }

    private Application startApplication() throws Exception {
        // This emulates what the junit ApplicationTest class does
        FxToolkit.registerPrimaryStage();
        application = FxToolkit.setupApplication(BigMemoryApp::new);
        return application;
    }

    private void stopApplication() throws Exception {
        // This emulates what the junit ApplicationTest class does
        // release all keys
        release(new KeyCode[0]);
        // release all mouse buttons
        release(new MouseButton[0]);
        FxToolkit.cleanupStages();
        FxToolkit.cleanupApplication(application);
    }

    public static class BigMemoryApp extends Application {

        private final List<String> memoryHog;

        public BigMemoryApp() {
            this.memoryHog = initMemoryHog();
        }

        @Override
        public void start(Stage stage) {
            Scene scene = new Scene(new TextField("Number of strings = " + memoryHog.size()), 300, 200);
            stage.setScene(scene);
            stage.show();
        }

        private List<String> initMemoryHog() {
            // It is important that this use a lot of memory.
            // This should generate about 50 million characters of data.
            List<String> list = new ArrayList<>();
            for (int x = 0; x <= 50000; x++) {
                list.add(String.format("%1024d", System.nanoTime()));
            }
            return list;
        }
    }

    public void printMemoryUse(int index) {
        Runtime runtime = Runtime.getRuntime();
        Function<Long, String> toMB = value -> String.valueOf(value / 1024 / 1024);

        long total = runtime.totalMemory();
        long free = runtime.freeMemory();
        Date timestamp = Calendar.getInstance().getTime();

        String template = "Index=%s  Memory total=%sMB  used=%sMB  free=%sM  time=%s%n";
        System.err.printf(template, index, toMB.apply(total), toMB.apply(total - free), toMB.apply(free), timestamp);
    }

}
