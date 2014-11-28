package org.testfx.lifecycle.impl;

import java.util.concurrent.Future;
import javafx.application.Application;
import javafx.stage.Stage;

import com.google.common.util.concurrent.SettableFuture;
import com.sun.javafx.application.ParametersImpl;
import org.testfx.lifecycle.ApplicationService;

import static org.loadui.testfx.utils.WaitForAsyncUtils.asyncFx;

public class ApplicationServiceImpl implements ApplicationService {

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Override
    public Future<Application> create(Class<? extends Application> appClass,
                                      String... appArgs) {
        // Should run in JavaFX application thread.
        return asyncFx(() -> {
            Application application = createApplication(appClass);
            //registerApplicationParameters(application, appArgs);
            return application;
        });
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

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    public Application createApplication(Class<? extends Application> applicationClass)
                                       throws Exception {
        return applicationClass.newInstance();
    }

    public void registerApplicationParameters(Application application,
                                              String... applicationArgs) {
        ParametersImpl parameters = new ParametersImpl(applicationArgs);
        ParametersImpl.registerParameters(application, parameters);
    }

}
