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
package org.testfx.robot.impl;

import javafx.scene.input.KeyCode;

import org.junit.Before;
import org.junit.Test;
import org.testfx.cases.InternalTestCaseBase;
import org.testfx.robot.BaseRobot;
import org.testfx.robot.SleepRobot;
import org.testfx.robot.WriteRobot;
import org.testfx.service.finder.WindowFinder;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WriteRobotImplTest extends InternalTestCaseBase {

    WriteRobot writeRobot;
    BaseRobot baseRobot;
    SleepRobot sleepRobot;
    WindowFinder windowFinder;


    @Before
    public void setup() throws Exception {
        baseRobot = mock(BaseRobot.class);
        sleepRobot = mock(SleepRobot.class);
        windowFinder = mock(WindowFinder.class);
        writeRobot = new WriteRobotImpl(baseRobot, sleepRobot, windowFinder);
    }

    @Test
    public void write_char() {
        // given:
        given(windowFinder.targetWindow()).willReturn(getTestStage());

        // when:
        writeRobot.write('a');

        // then:
        verify(baseRobot, times(1)).typeKeyboard(eq(getTestScene()), eq(KeyCode.UNDEFINED), eq("a"));
    }

    @Test
    public void write_char_with_whitespace() {
        // given:
        given(windowFinder.targetWindow()).willReturn(getTestStage());

        // when:
        writeRobot.write('\t');
        writeRobot.write('\n');

        // then:
        verify(baseRobot, times(1)).typeKeyboard(eq(getTestScene()), eq(KeyCode.TAB), eq("\t"));
        verify(baseRobot, times(1)).typeKeyboard(eq(getTestScene()), eq(KeyCode.ENTER), eq("\n"));
    }

    @Test
    public void write_string() {
        // given:
        given(windowFinder.targetWindow()).willReturn(getTestStage());

        // when:
        writeRobot.write("ae");

        // then:
        verify(baseRobot, times(1)).typeKeyboard(eq(getTestScene()), eq(KeyCode.UNDEFINED), eq("a"));
        verify(baseRobot, times(1)).typeKeyboard(eq(getTestScene()), eq(KeyCode.UNDEFINED), eq("e"));
    }

}
