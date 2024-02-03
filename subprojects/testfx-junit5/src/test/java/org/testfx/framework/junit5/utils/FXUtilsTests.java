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
package org.testfx.framework.junit5.utils;

import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.platform.commons.logging.Logger;
//import org.junit.platform.commons.logging.LoggerFactory;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.JavaFXInterceptorUtils.SelectiveJavaFxInterceptor;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.TestFx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link  org.testfx.framework.junit5.utils.FXUtils }
 */
@ExtendWith(ApplicationExtension.class)
@ExtendWith(SelectiveJavaFxInterceptor.class)
public class FXUtilsTests {
    private static final Class<?> CLAZZ = FXUtilsTests.class;
    //private static final Logger LOGGER = LoggerFactory.getLogger(CLAZZ);
    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;
    private Label testLabel;

    @Start
    public void start(@SuppressWarnings("unused") Stage stage) {
        // needed only to initialize FX UI Thread infrastructure
        testLabel = new Label("test label");
        stage.setScene(new Scene(testLabel, WIDTH, HEIGHT));
        stage.show();
    }

    @TestFx
    public void testWithinFxThread() throws Exception {
        FXUtils.assertJavaFxThread();
        FXUtils.keepJavaFxAlive();

        FXUtils.runAndWait(() -> {
            // LOGGER.trace(() -> "execute Runnable in JavaFX thread");
            FXUtils.assertJavaFxThread();
        });

        assertTrue(FXUtils.runAndWait(() -> {
            // LOGGER.trace(() -> "execute Supplier in JavaFX thread");
            FXUtils.assertJavaFxThread();
            return true;
        }));

        assertEquals(42.0, FXUtils.runAndWait(42.0, a -> {
            // LOGGER.trace(() -> "execute Function<R,T> in JavaFX thread");
            FXUtils.assertJavaFxThread();
            return a;
        }));

        FXUtils.runFX(() -> {
            // LOGGER.trace(() -> "execute Runnable in runLater in JavaFX thread");
            FXUtils.assertJavaFxThread();
        });

        Awaitility.setDefaultPollDelay(100, TimeUnit.MILLISECONDS);
        Awaitility.pollInSameThread();
        // FXUtils.waitForFxTicks(testLabel.getScene(), 3);

        // TODO For TestFX 5

        //        Awaitility.waitAtMost(200, TimeUnit.MILLISECONDS).until(() -> {
        //            return FXUtils.waitForFxTicks(testLabel.getScene(), 3);
        //        });
        //
        //        Awaitility.await().atMost(200, TimeUnit.MILLISECONDS).until(() -> {
        //            return FXUtils.waitForFxTicks(testLabel.getScene(), 3, 100);
        //        });

        // test assertions
        assertThrows(IllegalStateException.class, () -> FXUtils.runFX(() -> {
            // LOGGER.trace(() -> "execute Runnable with exception in runFX in JavaFX thread");
            throw new IllegalStateException("should be caught and swallowed by unit-test");
        }));

        assertThrows(IllegalStateException.class, () -> FXUtils.runAndWait(() -> {
            // LOGGER.trace(() -> "execute Runnable with exception in runAndWait in JavaFX thread");
            throw new IllegalStateException("should be caught and swallowed by unit-test");
        }));
    }

    @Test
    public void testOutsideFxThread() throws Exception {
        assertFalse(Platform.isFxApplicationThread());

        assertThrows(IllegalStateException.class, () -> FXUtils.assertJavaFxThread());

        FXUtils.runAndWait(() -> {
            // LOGGER.trace(() -> "execute Runnable in JavaFX thread");
            FXUtils.assertJavaFxThread();
        });

        assertTrue(FXUtils.runAndWait(() -> {
            // LOGGER.trace(() -> "execute Supplier in JavaFX thread");
            FXUtils.assertJavaFxThread();
            return true;
        }));

        assertEquals(42.0, FXUtils.runAndWait(42.0, a -> {
            // LOGGER.trace(() -> "execute Function<R,T> in JavaFX thread");
            FXUtils.assertJavaFxThread();
            return a;
        }));

        FXUtils.runFX(() -> {
            // LOGGER.trace(() -> "execute Runnable in runLater in JavaFX thread");
            FXUtils.assertJavaFxThread();
        });

        Awaitility.setDefaultPollDelay(100, TimeUnit.MILLISECONDS);
        Awaitility.pollInSameThread();
        // FXUtils.waitForFxTicks(testLabel.getScene(), 3);

        // TODO For TestFX 5

        //        Awaitility.waitAtMost(200, TimeUnit.MILLISECONDS).until(() -> {
        //            return FXUtils.waitForFxTicks(testLabel.getScene(), 3);
        //        });
        //
        //        // LOGGER.info(() -> "following [FXUtils-thread] 'FXUtils::waitForTicks(..) " +
        //        //        "interrupted by timeout' warning is the normal library behaviour");
        //        Awaitility.await().atMost(200, TimeUnit.MILLISECONDS).until(() -> {
        //            return FXUtils.waitForFxTicks(testLabel.getScene(), 3, 100);
        //        });
        //
        //        // check for own time-out
        //        assertFalse(FXUtils.waitForFxTicks(testLabel.getScene(), 1000, 20));

        // test assertions
        // N.B. the exception thrown in runLater cannot be forwarded to the calling thread (asynchronicity)
        // assertThrows(IllegalStateException.class, () -> FXUtils.runFX(() -> {
        //      LOGGER.atInfo().log("execute Runnable with exception in runFX in JavaFX thread");
        //      throw new IllegalStateException("should be caught and swallowed by unit-test");
        // }));

        assertThrows(IllegalStateException.class, () -> FXUtils.runAndWait(() -> {
            // LOGGER.trace(() -> "execute Runnable with exception in runAndWait in JavaFX thread");
            throw new IllegalStateException("should be caught and swallowed by unit-test");
        }));
    }
}
