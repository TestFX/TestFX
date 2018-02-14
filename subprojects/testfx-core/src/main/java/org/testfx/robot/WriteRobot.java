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
package org.testfx.robot;

public interface WriteRobot {

    /**
     * Writes a given text character.
     *
     * @param character the text character to write
     */
    void write(char character);

    /**
     * Writes the given text characters one after the other.
     *
     * @param text the text characters to write
     */
    void write(String text);

    /**
     * Writes the given text characters one after the other, sleeping for
     * {@code sleepMillis} milliseconds after each typed character.
     *
     * @param text the text characters to write
     * @param sleepMillis the milliseconds to sleep for after each
     * character
     */
    void write(String text, int sleepMillis);
}
