/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.loadui.testfx.robots.impl;

import javafx.scene.input.KeyCode;

import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.robots.SleepRobot;
import org.loadui.testfx.robots.TypeRobot;
import org.loadui.testfx.robots.WriteRobot;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public final class WriteRobotImplTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    SleepRobot sleepRobot;
    TypeRobot typeRobot;
    WriteRobot writeRobot;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    public void setup() {
        sleepRobot = mock(SleepRobot.class);
        typeRobot = mock(TypeRobot.class);
        writeRobot = new WriteRobotImpl(typeRobot, sleepRobot);
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void write_lowercase_AE() {
        // when:
        writeRobot.write("ae");

        // then:
        verify(typeRobot, times(1)).push(eq(KeyCode.A));
        verify(typeRobot, times(1)).push(eq(KeyCode.E));
    }

    @Test
    public void write_uppercase_A() {
        // when:
        writeRobot.write("AE");

        // then:
        verify(typeRobot, times(1)).push(eq(KeyCode.SHIFT), eq(KeyCode.A));
        verify(typeRobot, times(1)).push(eq(KeyCode.SHIFT), eq(KeyCode.E));
    }

    @Test
    public void write_period_and_colon() {
        // when:
        writeRobot.write(".:");

        // then:
        verify(typeRobot, times(1)).push(eq(KeyCode.PERIOD));
        verify(typeRobot, times(1)).push(eq(KeyCode.COLON));
    }

}
