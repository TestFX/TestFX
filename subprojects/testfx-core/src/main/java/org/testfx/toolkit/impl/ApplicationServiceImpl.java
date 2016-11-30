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

import com.google.common.util.concurrent.SettableFuture;
import javafx.application.Application;
import javafx.stage.Stage;
import org.testfx.api.annotation.Unstable;
import org.testfx.toolkit.ApplicationService;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static org.testfx.util.WaitForAsyncUtils.asyncFx;

@Unstable(reason = "needs more tests")
public class ApplicationServiceImpl implements ApplicationService {

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public Future<Application> create(Callable<Application> applicationCallable) {
        // Should run in JavaFX application thread.
        return asyncFx(applicationCallable);
    }

    @Override
    public Future<Void> init(Application application) {
        // Should be called in TestFX launcher thread.
        SettableFuture<Void> future = SettableFuture.create();
        try {
            application.init();
            future.set(null);
        }
        catch (Exception exception) {
            future.setException(exception);
        }
        return future;
    }

    @Override
    public Future<Void> start(Application application,
                              Stage targetStage) {
        // Should run in JavaFX application thread.
        return asyncFx(() -> {
            application.start(targetStage);
            return null;
        });
    }

    @Override
    public Future<Void> stop(Application application) {
        // Should run in JavaFX application thread.
        return asyncFx(() -> {
            application.stop();
            return null;
        });
    }

}
