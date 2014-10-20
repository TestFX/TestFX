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

import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.robots.SleepRobot;
import org.loadui.testfx.robots.TypeRobot;
import org.loadui.testfx.robots.WriteRobot;

import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.COLON;
import static javafx.scene.input.KeyCode.E;
import static javafx.scene.input.KeyCode.PERIOD;
import static javafx.scene.input.KeyCode.SHIFT;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public final class WriteRobotImplTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public WriteRobot writeRobot;

    public SleepRobot sleepRobot;
    public TypeRobot typeRobot;

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
        verify(typeRobot, times(1)).push(eq(A));
        verify(typeRobot, times(1)).push(eq(E));
    }

    @Test
    public void write_uppercase_A() {
        // when:
        writeRobot.write("AE");

        // then:
        verify(typeRobot, times(1)).push(eq(SHIFT), eq(A));
        verify(typeRobot, times(1)).push(eq(SHIFT), eq(E));
    }

    @Test
    public void write_period_and_colon() {
        // when:
        writeRobot.write(".:");

        // then:
        verify(typeRobot, times(1)).push(eq(PERIOD));
        verify(typeRobot, times(1)).push(eq(COLON));
    }

}
