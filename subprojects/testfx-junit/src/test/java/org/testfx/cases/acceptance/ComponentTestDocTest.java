package org.testfx.cases.acceptance;

import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import org.junit.Test;
import org.testfx.framework.junit.ComponentTest;

import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

/**
 * Tests the code provided in Java Doc of ComponentTest
 *
 */
public class ComponentTestDocTest extends ComponentTest<ColorPicker> {

    @Override
    public ColorPicker createComponent() {
        return new ColorPicker(Color.BLUE);
    }

    @Test
    public void shouldAllowUserToChangeColor() {
        //given
        final Color c1 = getComponent().getValue();
        
        // when:
        clickOn(getComponent());
        moveBy(30, 70);
        clickOn(MouseButton.PRIMARY);

        // then:
        //actually differs from doc as selected color seems to be platform dependent...
        assertThat("Update JavaDoc of ComponentTest", getComponent().getValue(), not(c1));
    }

}
