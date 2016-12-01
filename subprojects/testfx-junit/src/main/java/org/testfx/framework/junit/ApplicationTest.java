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
package org.testfx.framework.junit;

import javafx.application.Application;
import javafx.application.Application.Parameters;
import javafx.application.HostServices;
import javafx.application.Preloader.PreloaderNotification;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.api.annotation.Unstable;

@Unstable(reason = "might be renamed to ApplicationTestBase")
public abstract class ApplicationTest extends FxRobot implements ApplicationFixture {

	/**
	 * This flag enables/disables the UI test. All tests will be skipped if set to false. 
	 */
	public static boolean doUITest=true;
	
    //---------------------------------------------------------------------------------------------
    // STATIC METHODS.
    //---------------------------------------------------------------------------------------------

    @Unstable(reason = "is missing apidocs")
    public static void launch(Class<? extends Application> appClass,
                              String... appArgs) throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(appClass, appArgs);
    }

    /**
     * Evaluates the environment variable {@code UI_TEST} and sets {@code doUITest} to false, 
     * if the variable exists and is set to anything else than true.
     */
	@BeforeClass public final static void internalBeforeClass(){
		String doUI=System.getenv("UI_TEST");
		if(doUI!=null){
			doUITest=doUI.equals("true");
			if(!doUITest)
				System.out.println("Executing UI-Tests="+doUITest);
		}
	}
    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    @Before
    @Unstable(reason = "is missing apidocs")
    public final void internalBefore()
                              throws Exception {
		//skipping tests if not doing ui tests
		org.junit.Assume.assumeTrue(doUITest);
		if(doUITest){
			FxToolkit.registerPrimaryStage();
			FxToolkit.setupApplication(() -> new ApplicationAdapter(this));
		}
    }

    @After
    @Unstable(reason = "is missing apidocs")
    public final void internalAfter()
                             throws Exception {
		if(doUITest){
			FxToolkit.cleanupApplication(new ApplicationAdapter(this));
		}
    }

    @Override
    @Unstable(reason = "is missing apidocs")
    public void init()
            throws Exception {}

    @Override
    @Unstable(reason = "is missing apidocs")
    public abstract void start(Stage stage)
                        throws Exception;

    @Override
    @Unstable(reason = "is missing apidocs")
    public void stop()
              throws Exception {}

    @Deprecated
    public final HostServices getHostServices() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final Parameters getParameters() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final void notifyPreloader(PreloaderNotification notification) {
        throw new UnsupportedOperationException();
    }

}
