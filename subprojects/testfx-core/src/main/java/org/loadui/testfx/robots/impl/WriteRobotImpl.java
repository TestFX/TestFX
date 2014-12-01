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

import com.google.common.collect.Lists;
import org.loadui.testfx.robots.BaseRobot;
import org.loadui.testfx.robots.SleepRobot;
import org.loadui.testfx.robots.WriteRobot;
import org.loadui.testfx.service.finder.WindowFinder;

public class WriteRobotImpl implements WriteRobot {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final long SLEEP_AFTER_CHARACTER_IN_MILLIS = 25;

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public BaseRobot baseRobot;
    public SleepRobot sleepRobot;
    public WindowFinder windowFinder;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public WriteRobotImpl(BaseRobot baseRobot,
                          SleepRobot sleepRobot,
                          WindowFinder windowFinder) {
        this.baseRobot = baseRobot;
        this.sleepRobot = sleepRobot;
        this.windowFinder = windowFinder;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void write(char character) {
        pushCharacter(character);
    }

    @Override
    public void write(String text) {
        for (char character : Lists.charactersOf(text)) {
            pushCharacter(character);
            sleepRobot.sleep(SLEEP_AFTER_CHARACTER_IN_MILLIS);
        }
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private void pushCharacter(char character) {
        Scene scene = windowFinder.target().getScene();
        baseRobot.typeKeyboard(scene, KeyCode.UNDEFINED, Character.toString(character));
        baseRobot.awaitEvents();
    }

}
