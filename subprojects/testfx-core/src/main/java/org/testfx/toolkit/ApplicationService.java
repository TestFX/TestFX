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
package org.testfx.toolkit;

import java.util.concurrent.Future;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Helper interface whose methods' returned {@link Future} objects' {@link Future#get()} method either indicates
 * when an {@link Application}'s init, start, or stop methods are finished or returns the created application.
 */
public interface ApplicationService {

    /**
     * @return a {@link Future} whose {@link Future#get()} will return when the
     * given application has finished its {@link Application#init()} method
     */
    Future<Void> init(Application application);

    /**
     * @return a {@link Future} whose {@link Future#get()} will return when the
     * given application has finished its {@link Application#start(Stage)} method
     */
    Future<Void> start(Application application,
                       Stage targetStage);

    /**
     * @return a {@link Future} whose {@link Future#get()} will return when the
     * given application has finished its {@link Application#stop()} method
     */
    Future<Void> stop(Application application);

}
