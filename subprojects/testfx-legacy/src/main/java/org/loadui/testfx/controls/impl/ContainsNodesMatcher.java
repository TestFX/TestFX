/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2017 The TestFX Contributors
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
package org.loadui.testfx.controls.impl;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.loadui.testfx.GuiTest;

public class ContainsNodesMatcher extends TypeSafeMatcher<String> {

    final int numberOf;
    final String domQuery;

    public ContainsNodesMatcher(String domQuery) {
        this(-1, domQuery);
    }

    public ContainsNodesMatcher(int numberOf, String domQuery) {
        this.numberOf = numberOf;
        this.domQuery = domQuery;
    }

    @Factory
    public static Matcher<String> contains(String domQuery) {
        return new ContainsNodesMatcher(domQuery);
    }

    @Factory
    public static Matcher<String> contains(int numberOf, String domQuery) {
        return new ContainsNodesMatcher(numberOf, domQuery);
    }

    public void describeTo(Description desc) {
        desc.appendText("contains");
    }

    @Override
    public boolean matchesSafely(String domRoot) {
        if (numberOf >= 0) {
            return GuiTest.findAll(domRoot + " " + domQuery).size() == numberOf;
        }
        return !GuiTest.findAll(domRoot + " " + domQuery).isEmpty();
    }
}
