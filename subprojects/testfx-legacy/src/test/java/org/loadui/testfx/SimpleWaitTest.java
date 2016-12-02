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
package org.loadui.testfx;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.categories.TestFX;

import static org.loadui.testfx.controls.Commons.hasText;

/**
 * A Simple example to test the wait methods.
 */
@Category(TestFX.class)
public class SimpleWaitTest extends GuiTest {

  public static final int THREE_SECONDS = 3;
  private final ScheduledThreadPoolExecutor sch
      = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(5);


  private final Runnable oneShotTask = () -> {
    System.out.println("\t oneShotTask Execution Time: " + LocalDateTime.now());
    final Button button = find("#btn");
    Platform.runLater(() -> button.setText("was clicked"));
  };

  @Override
  protected Parent getRootNode() {
    final Button btn = new Button();
    btn.setId("btn");
    btn.setText("Hello World");
    btn.setOnAction((actionEvent) -> {
      System.out.println("Submission Time: " + LocalDateTime.now());
      ScheduledFuture<?> oneShotFuture = sch.schedule(oneShotTask, THREE_SECONDS, TimeUnit.SECONDS);
    });
    return btn;
  }


  @Test(expected = java.lang.RuntimeException.class)
  public void shouldClickButton01() {
    final Button button = find("#btn");
    clickOn(button);
    waitUntil(button, hasText("was clicked"), 1);
  }

  @Test
  public void shouldClickButton02() {
    final Button button = find("#btn");
    clickOn(button);
    waitUntil(button, hasText("was clicked"), 10);
  }


}
