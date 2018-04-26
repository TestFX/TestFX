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
package org.testfx.toolkit.impl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import javafx.application.Application;
import javafx.stage.Stage;

import org.testfx.toolkit.ApplicationService;

import static org.testfx.util.WaitForAsyncUtils.asyncFx;

public class ApplicationServiceImpl implements ApplicationService {

    @Override
    public Future<Void> init(Application application) {
        // Should be called in TestFX launcher thread.
        CompletableFuture<Void> future = new CompletableFuture<>();
        try {
            application.init();
            future.complete(null);
        }
        catch (Exception exception) {
            future.completeExceptionally(exception);
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
