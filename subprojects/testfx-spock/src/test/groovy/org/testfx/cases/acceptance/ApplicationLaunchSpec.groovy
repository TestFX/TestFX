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
package org.testfx.cases.acceptance

import static org.testfx.api.FxAssert.verifyThat
import static org.testfx.matcher.control.LabeledMatchers.hasText

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import org.testfx.api.FxRobot
import org.testfx.api.FxToolkit
import org.testfx.framework.spock.ApplicationSpec
import spock.lang.Specification

class ApplicationLaunchSpec extends Specification {

    @Delegate
    private final FxRobot robot = new FxRobot()

    static class DemoApplication extends Application {
        @Override
        void start(Stage stage) {
            Button button = new Button('click me!')
            button.setOnAction { button.setText('clicked!') }
            stage.setScene(new Scene(new StackPane(button), 100, 100))
            stage.show()
        }
    }

    void setup() throws Exception {
        ApplicationSpec.launch(DemoApplication.class)
    }

    void cleanup() throws Exception {
        FxToolkit.cleanupStages()
    }

    def "should contain button"() {
        expect:
        verifyThat('.button', hasText('click me!'))
    }

    def "should click on button"() {
        when:
        clickOn('.button')

        then:
        verifyThat('.button', hasText('clicked!'))
    }

}
