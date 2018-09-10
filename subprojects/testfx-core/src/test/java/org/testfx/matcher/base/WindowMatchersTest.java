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
package org.testfx.matcher.base;

import java.util.concurrent.Callable;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.cases.InternalTestCaseBase;

import static org.hamcrest.MatcherAssert.assertThat;

public class WindowMatchersTest extends InternalTestCaseBase {


    @Test
    public void isNotShowing() throws Exception {
        Window window = FxToolkit.setupFixture((Callable<Stage>) Stage::new);
        assertThat(window, WindowMatchers.isNotShowing());
    }

    @Test
    public void isNotFocused() throws Exception {
        Window window = FxToolkit.setupFixture((Callable<Stage>) Stage::new);
        assertThat(window, WindowMatchers.isNotFocused());
    }

    @Test
    public void isShowing() throws Exception {
        assertThat(getTestStage(), WindowMatchers.isShowing());
    }


    @Test
    //@Ignore("See https://github.com/TestFX/TestFX/pull/284 for details")
    public void isFocused() throws Exception {
        interact(() -> getTestStage().requestFocus());
        sleep(200); // acquiring focus takes some time, may be flaky

        assertThat(getTestStage(), WindowMatchers.isFocused());
    }
}
