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

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.robots.BaseRobot;
import org.loadui.testfx.robots.SleepRobot;
import org.loadui.testfx.robots.WriteRobot;
import org.loadui.testfx.service.finder.WindowFinder;
import org.testfx.api.FxLifecycle;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public final class WriteRobotImplTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public WriteRobot writeRobot;

    public Stage stage;
    public Scene scene;
    public BaseRobot baseRobot;
    public SleepRobot sleepRobot;
    public WindowFinder windowFinder;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxLifecycle.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        FxLifecycle.setup(() -> {
            scene = new Scene(new Region());
            stage = new Stage();
            stage.setScene(scene);
        });

        baseRobot = mock(BaseRobot.class);
        sleepRobot = mock(SleepRobot.class);
        windowFinder = mock(WindowFinder.class);
        writeRobot = new WriteRobotImpl(baseRobot, sleepRobot, windowFinder);
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    @Test
    public void write_char() {
        // given:
        given(windowFinder.target()).willReturn(stage);

        // when:
        writeRobot.write('a');

        // then:
        verify(baseRobot, times(1)).typeKeyboard(eq(scene), eq(KeyCode.UNDEFINED), eq("a"));
    }

    @Test
    public void write_string() {
        // given:
        given(windowFinder.target()).willReturn(stage);

        // when:
        writeRobot.write("ae");

        // then:
        verify(baseRobot, times(1)).typeKeyboard(eq(scene), eq(KeyCode.UNDEFINED), eq("a"));
        verify(baseRobot, times(1)).typeKeyboard(eq(scene), eq(KeyCode.UNDEFINED), eq("e"));
    }

}
