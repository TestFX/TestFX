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
package org.testfx.toolkit;

import javafx.stage.Stage;

import com.google.common.util.concurrent.AbstractFuture;
import org.testfx.api.annotation.Unstable;

@Unstable(reason = "needs more tests")
public class PrimaryStageFuture extends AbstractFuture<Stage> {

    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    public static PrimaryStageFuture create() {
        return new PrimaryStageFuture();
    }

    //---------------------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //---------------------------------------------------------------------------------------------

    private PrimaryStageFuture() {}

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public boolean set(Stage stage) {
        return super.set(stage);
    }

    @Override
    public boolean setException(Throwable throwable) {
        return super.setException(throwable);
    }

}
