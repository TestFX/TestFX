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
package org.testfx.framework.junit;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.Before;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

/**
 * The base class that your JUnit test classes should extend from that interact with and/or verify the
 * state of a JavaFX UI. Such test classes, containing one or more {@code @Test}-annotated methods
 * (individual test cases), can interact with a JavaFX UI using the {@link FxRobot} methods that test
 * class will inherit (as it extends {@code ApplicationTest} (this class) which extends {@code FxRobot}).
 * Verifying the state of the UI can be accomplished by using either the <a href="http://hamcrest.org/">Hamcrest</a>
 * based {@link org.testfx.api.FxAssert#verifyThat} or the <a href="http://joel-costigliola.github.io/assertj/">AssertJ</a>
 * based {@link org.testfx.assertions.api.Assertions#assertThat(Node)}.
 * <p>
 * Example:
 * <pre>{@code
 * public class ColorSelectorTest extends ApplicationTest {
 *
 *     Stage stage;
 *     ColorPicker colorPicker;
 *
 *     {@literal @}Override
 *     public void start(Stage stage) throws Exception {
 *         this.stage = stage;
 *     }
 *
 *    {@literal @}Before
 *     public void beforeEachTest() throws Exception {
 *         Platform.runLater(() -> {
 *             colorPicker = new ColorPicker(Color.BLUE);
 *             StackPane stackPane = new StackPane(colorPicker);
 *             Scene scene = new Scene(root, 800, 800);
 *             stage.setScene(scene);
 *             stage.show();
 *         });
 *        WaitForAsyncUtils.waitForFxEvents();
 *     }
 *
 *     {@literal @}Test
 *     public void shouldAllowUserToChangeColor() {
 *         // when:
 *         clickOn(colorPicker);
 *         type(KeyCode.DOWN);
 *         type(KeyCode.DOWN);
 *
 *         // then:
 *         assertThat(colorPicker.getValue()).isEqualTo(Color.TEAL);
 *     }
 * }
 * }</pre>
 */
public abstract class ApplicationTest extends FxRobot implements ApplicationFixture {

    public static void launch(Class<? extends Application> appClass, String... appArgs) throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(appClass, appArgs);
    }

    @Before
    public final void internalBefore() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(() -> new ApplicationAdapter(this));
    }

    @After
    public final void internalAfter() throws Exception {
        // release all keys
        release(new KeyCode[0]);
        // release all mouse buttons
        release(new MouseButton[0]);
        FxToolkit.cleanupStages();
        FxToolkit.cleanupApplication(new ApplicationAdapter(this));
    }

    @Override
    public void init() throws Exception {}

    @Override
    public void start(Stage stage) throws Exception {}

    @Override
    public void stop() throws Exception {}
}
