/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2024 The TestFX Contributors
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
import java.util.List;
import java.util.concurrent.TimeoutException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.Repeat;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryLeakBugLaunchTest extends FxRobot {

    private static final int ITERATION_COUNT = 10;

    private static final long KB = 1024L;

    private static long initialMemoryUse;

    private static long dataSize;

    @Rule
    public Repeat retry = new Repeat(ITERATION_COUNT);

    private Application application;

    @BeforeClass
    public static void setupSpec() {
        dataSize = Runtime.getRuntime().maxMemory() / ITERATION_COUNT;
    }

    @Before
    public void setup() throws Exception {
        this.application = ApplicationTest.launch(BigMemoryApp.class);
    }

    @After
    public void cleanup() throws TimeoutException {
        FxToolkit.cleanupAfterTest(this, application);
    }

    @Test
    public void multipleApplicationStartsDoNotEatMemory() {
        if (initialMemoryUse == 0) {
            initialMemoryUse = gcAndGetMemoryUse();
        }
        // Ensure the memory use is still withing reasonable limits
        assertThat(gcAndGetMemoryUse()).isLessThan(initialMemoryUse + dataSize);
    }

    public static class BigMemoryApp extends Application {

        private final List<String> memoryHog;

        public BigMemoryApp() {
            this.memoryHog = createMemoryHog();
        }

        @Override
        public void start(Stage stage) {
            Scene scene = new Scene(new TextField("Data size=" + dataSize + " count=" + memoryHog.size()), 320, 180);
            stage.setScene(scene);
            stage.show();
        }

    }

    private static List<String> createMemoryHog() {
        // It is important that this use a lot of memory.
        int count = (int) (dataSize / KB);
        List<String> list = new ArrayList<>(count);
        for (int x = 0; x <= count; x++) {
            list.add(String.format("%" + KB + "d", System.nanoTime()));
        }
        return list;
    }

    private static long gcAndGetMemoryUse() {
        // Request the JVM to reclaim memory before checking memory use
        System.gc();

        Runtime runtime = Runtime.getRuntime();
        long total = runtime.totalMemory();
        long free = runtime.freeMemory();

        return total - free;
    }

}
