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
package org.testfx.framework.junit;

import java.util.concurrent.TimeoutException;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.testfx.api.FxToolkit;
import org.testfx.util.WaitForAsyncUtils;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.fail;

/**
 * This class tests exception handling of the GUI within the JUnit framework.
 */
public class JUnitExceptionTest extends ApplicationTest {

    @Rule
    public TestRule rule = RuleChain.outerRule(Timeout.millis(10000)).around(exception = ExpectedException.none());
    public ExpectedException exception;

    @BeforeClass
    public static void setUpClass() {
        try {
            FxToolkit.registerPrimaryStage();
        }
        catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Button button = new Button("Throws Exception");
        button.setOnAction(e ->  {
            throw new UnsupportedOperationException("Something Went Wrong: Notice me");
        });
        StackPane root = new StackPane(button);

        Scene scene = new Scene(root, 200, 150);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hello World");
        primaryStage.show();
    }

    /**
     * This test checks if the exceptions triggered by a mouse click are correctly thrown.
     *
     * @see <a href="https://github.com/TestFX/TestFX/issues/155">Issue #155</a>
     */
    @Test
    public void exceptionOnClickTest() throws Throwable {
        // Hints for failing tests: Timing might be an issue increase also the
        // time in checkException()
        WaitForAsyncUtils.printException = false; // do not print expected exception to log
        exception.expectCause(instanceOf(UnsupportedOperationException.class));
        WaitForAsyncUtils.clearExceptions(); // just ensure no other test put an exception into the buffer
        try {
            clickOn("Throws Exception"); // does already handle all the async stuff...
            WaitForAsyncUtils.checkException(); // need to check Exception, as event is executed after click
            fail("checkException didn't detect Exception");
        }
        catch (Exception e) { // clean up...
            release(MouseButton.PRIMARY);
            moveBy(100, 100); // otherwise the press release test fails?!
            WaitForAsyncUtils.printException = true; //  enable printing for other tests
            WaitForAsyncUtils.clearExceptions(); // just ensure no pending exceptions in buffer
            throw e;
        }
    }


}
