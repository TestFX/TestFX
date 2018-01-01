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

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.CoreMatchers.is

import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.stage.Stage

/**
 * Verifies that keyboard keys and mouse buttons are released after each
 * test (even if the developer forgets to manually release them).
 */
class KeyAndButtonReleaseSpec extends ApplicationSpec {

    @Override
    void start(Stage stage) throws Exception {
        stage.show()
    }

    def "Keys are released whether test remembers to release them or not"() {
        given: 'a test where keys are pressed at some point and not released'
        press(KeyCode.CONTROL, KeyCode.SHIFT, KeyCode.ALT)

        when: 'the test ends'
        internalAfter()

        then: 'all pressed keys have been released'
        assertThat(robotContext().keyboardRobot.pressedKeys.isEmpty(), is(true))
    }

    def "Buttons are released whether test remembers to release them or not"() {
        given: 'a test where keys are pressed at some point and not released'
        press(MouseButton.PRIMARY, MouseButton.SECONDARY, MouseButton.MIDDLE)

        when: 'the test ends'
        internalAfter()

        then: 'all pressed mouse buttons have been released'
        assertThat(robotContext().mouseRobot.pressedButtons.isEmpty(), is(true))
    }
}
