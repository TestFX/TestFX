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
package org.testfx.cases.acceptance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.beans.InvalidationListener;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import org.junit.Rule;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.framework.junit.TestFXRule;

import static org.junit.Assert.fail;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.util.DebugUtils.informedErrorMessage;

public class ApplicationStartTest extends ApplicationTest {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();
    CountDownLatch setButtonTextLatch;

    @Override
    public void init() throws Exception {
        FxToolkit.registerStage(Stage::new);
    }

    @Override
    public void start(Stage stage) {
        CountDownLatch setSceneLatch = new CountDownLatch(1);
        setButtonTextLatch = new CountDownLatch(1);
        InvalidationListener invalidationListener = observable -> setSceneLatch.countDown();
        stage.sceneProperty().addListener(observable -> {
            setSceneLatch.countDown();
            stage.sceneProperty().removeListener(invalidationListener);
        });
        Button button = new Button("click me!");
        button.setOnAction(actionEvent -> {
            button.setText("clicked!");
            setButtonTextLatch.countDown();
        });
        stage.setScene(new Scene(button, 100, 100));
        try {
            if (!setSceneLatch.await(10, TimeUnit.SECONDS)) {
                fail("Timeout while waiting for scene to be set on stage.");
            }
        }
        catch (Exception ex) {
            fail("Unexpected exception: " + ex);
        }
        stage.show();
    }

    @Override
    public void stop() throws TimeoutException {
        FxToolkit.hideStage();
    }

    @Test(timeout = 3000)
    public void should_contain_button() {
        // expect:
        verifyThat(".button", hasText("click me!"), informedErrorMessage(this));
    }

    @Test(timeout = 3000)
    public void should_click_on_button() {
        // when:
        moveTo(".button");
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);

        // then:
        verifyThat(".button", hasText("clicked!"), informedErrorMessage(this));
    }

}
