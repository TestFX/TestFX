package org.loadui.testfx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBoxBuilder;
import org.junit.Test;

import static javafx.scene.input.KeyCode.TAB;
import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.Commons.hasText;
import static org.loadui.testfx.controls.TextInputControls.clearTextIn;

public class TimeoutTest extends GuiTest
{

	public static final String TEXT_FIELD = ".text-field";

	@Override
	protected Parent getRootNode()
	{
		final Button b = new Button("Exit");
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                b.getScene().getWindow().hide();
            }
        });
		return VBoxBuilder
				.create()
				.children(b).build();
	}

	@Test(timeout = 15_000)
	public void shouldNotFreezeOnFxTerminate()
	{
        click("Exit");
	}
}
