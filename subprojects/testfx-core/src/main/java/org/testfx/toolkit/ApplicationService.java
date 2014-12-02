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
package org.testfx.toolkit;

import java.util.concurrent.Future;
import javafx.application.Application;
import javafx.stage.Stage;

public interface ApplicationService {

    public Future<Application> create(Class<? extends Application> appClass,
                                      String... appArgs);

    public Future<Void> init(Application application);

    public Future<Void> start(Application application,
                              Stage targetStage);

    public Future<Void> stop(Application application);

}
