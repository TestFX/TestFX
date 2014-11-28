package org.testfx.lifecycle;

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
