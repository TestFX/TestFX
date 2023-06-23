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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryLeakBug extends FxRobot {

    private static final long MB = 1048576L;

    private static long dataSize;

    @BeforeClass
    public static void setupSpec() {
        dataSize = Runtime.getRuntime().maxMemory() / 10;
    }

    /**
     * This test intentionally opens multiple instances of a "large"
     * application to ensure that the application is cleaned up after each
     * test run, and an OutOfMemoryError is not thrown.
     * @throws TimeoutException if application operations do not complete in time
     */
    @Test
    public void testMemoryDoesNotGrowOverMultipleApplicationStartsAndStops() throws TimeoutException {
        // Start and stop the application multiple times.
        // This usually fails within five iterations, so if the test can make
        // it to ten iterations, and the memory use is still within limits, we
        // consider the test successful.
        for (int index = 0; index < 10; index++) {
            // Using a weak reference here allows the application to be garbage collected
            WeakReference<Application> applicationReference = new WeakReference<>(startApplication());
            stopApplication(applicationReference.get());
            printMemoryUse(index);
        }

        // Ensure the memory use is still withing reasonable limits
        // 2023-06-23 Limit set to 15MB since current memory use is around 12MB
        assertThat(printMemoryUse(-1)).isLessThan(15 * MB);
    }

    private Application startApplication() throws TimeoutException {
        // This emulates what the junit ApplicationTest class does
        FxToolkit.registerPrimaryStage();
        return FxToolkit.setupApplication(BigMemoryApp::new);
    }

    private void stopApplication(Application application) throws TimeoutException {
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
            int count = (int)(dataSize / 1024);
            List<String> list = new ArrayList<>(count);
            for (int x = 0; x <= count; x++) {
                list.add(String.format("%1024d", System.nanoTime()));
            }
            return list;
        }
    }

    public long printMemoryUse(int index) {
        Runtime runtime = Runtime.getRuntime();
        Function<Long, String> toMB = value -> String.valueOf(value / MB);

        // Request the JVM to reclaim memory before checking memory use
        System.gc();

        long total = runtime.totalMemory();
        long free = runtime.freeMemory();
        long used = total - free;

        Date timestamp = Calendar.getInstance().getTime();

        String template = "Index=%s  Memory total=%sMB  used=%sMB  free=%sMB  time=%s%n";
        System.err.printf(template, index, toMB.apply(total), toMB.apply(used), toMB.apply(free), timestamp);

        return used;
    }

}
