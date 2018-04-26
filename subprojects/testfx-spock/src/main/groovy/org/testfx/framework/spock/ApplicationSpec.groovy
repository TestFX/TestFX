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
package org.testfx.framework.spock

import javafx.application.Application
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.stage.Stage
import org.testfx.api.FxRobot
import org.testfx.api.FxToolkit
import spock.lang.Specification

/**
 * Base class for all TestFX Spock specifications.
 * <p>
 * <h3>Example:</h3>
 * <pre><code>
 * class MySpec extends ApplicationSpec {
 *
 * {@literal @}Override
 * void start(Stage stage) {
 *     Button button = new Button("click me!")
 *     button.setOnAction { button.setText("clicked!") }
 *     stage.setScene(new Scene(new StackPane(button), 100, 100))
 *     stage.show()
 * }
 *
 * def "button should change text when clicked"() {
 *    when:
 *    clickOn(".button")
 *
 *    then:
 *    verifyThat(".button", hasText("clicked!"))
 * }
 * </code></pre>
 */
abstract class ApplicationSpec extends Specification implements ApplicationFixture {

    @Delegate
    private final FxRobot robot = new FxRobot()

    static void launch(Class<? extends Application> appClass,
                              String... appArgs) throws Exception {
        FxToolkit.registerPrimaryStage()
        FxToolkit.setupApplication(appClass, appArgs)
    }

    void setup() {
        internalBefore()
    }

    void cleanup() {
        internalAfter()
    }

    final void internalBefore() throws Exception {
        FxToolkit.registerPrimaryStage()
        FxToolkit.setupApplication { new ApplicationAdapter(this) }
    }

    final void internalAfter() throws Exception {
        // release all keys
        release(new KeyCode[0])
        // release all mouse buttons
        release(new MouseButton[0])
        FxToolkit.cleanupApplication(new ApplicationAdapter(this))
    }

    @Override
    void init() throws Exception {
    }

    @Override
    abstract void start(Stage stage) throws Exception

    @Override
    void stop() throws Exception {
    }
}
