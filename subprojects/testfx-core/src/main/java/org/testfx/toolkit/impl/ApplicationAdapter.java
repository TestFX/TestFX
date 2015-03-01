/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2015 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.toolkit.impl;

import javafx.application.Application;
import javafx.stage.Stage;

import org.testfx.api.annotation.Unstable;
import org.testfx.toolkit.ApplicationFixture;

@Unstable(reason = "needs more tests")
public final class ApplicationAdapter implements ApplicationFixture {

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private Application application;

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    public ApplicationAdapter(Application application) {
        this.application = application;
    }

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public void init() throws Exception {
        application.init();
    }

    @Override
    public void start(Stage stage) throws Exception {
        application.start(stage);
    }

    @Override
    public void stop() throws Exception {
        application.stop();
    }

}
