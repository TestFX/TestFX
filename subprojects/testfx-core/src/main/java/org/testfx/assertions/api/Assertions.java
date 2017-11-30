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
package org.testfx.assertions.api;

import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputControl;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Window;

/**
 * The entry point for all TestFX assertions for different JavaFX types.
 */
public class Assertions extends org.assertj.core.api.Assertions {

    protected Assertions() {}

    /**
     * Create assertion for {@link Button}.
     *
     * @param actual the actual value
     * @return the created assertion object
     */
    public static ButtonAssert assertThat(Button actual) {
        return new ButtonAssert(actual);
    }

    /**
     * Create assertion for {@link Color}.
     *
     * @param actual the actual value
     * @return the created assertion object
     */
    public static ColorAssert assertThat(Color actual) {
        return new ColorAssert(actual);
    }

    /**
     * Create assertion for {@link ComboBox}.
     *
     * @param actual the actual value
     * @param <T> the type of the value contained in the {@link ComboBox}
     * @return the created assertion object
     */
    public static <T> ComboBoxAssert<T> assertThat(ComboBox<T> actual) {
        return new ComboBoxAssert<>(actual);
    }

    /**
     * Create assertion for {@link Dimension2D}.
     *
     * @param actual the actual value
     * @return the created assertion object
     */
    public static Dimension2DAssert assertThat(Dimension2D actual) {
        return new Dimension2DAssert(actual);
    }

    /**
     * Create assertion for {@link Button}.
     *
     * @param actual the actual value
     * @return the created assertion object
     */
    public static LabeledAssert assertThat(Labeled actual) {
        return new LabeledAssert(actual);
    }

    /**
     * Create assertion for {@link ListView}.
     *
     * @param actual the actual value
     * @param <T> the type of the value contained in the {@link ListView}
     * @return the created assertion object
     */
    public static <T> ListViewAssert<T> assertThat(ListView<T> actual) {
        return new ListViewAssert<>(actual);
    }

    /**
     * Create assertion for {@link Button}.
     *
     * @param actual the actual value
     * @return the created assertion object
     */
    public static NodeAssert assertThat(Node actual) {
        return new NodeAssert(actual);
    }

    /**
     * Create assertion for {@link Parent}.
     *
     * @param actual the actual value
     * @return the created assertion object
     */
    public static ParentAssert assertThat(Parent actual) {
        return new ParentAssert(actual);
    }

    /**
     * Create assertion for {@link TableView}.
     *
     * @param actual the actual value
     * @param <T> the type of the value contained in the {@link TableView}
     * @return the created assertion object
     */
    public static <T> TableViewAssert<T> assertThat(TableView<T> actual) {
        return new TableViewAssert<>(actual);
    }

    /**
     * Create assertion for {@link Text}.
     *
     * @param actual the actual value
     * @return the created assertion object
     */
    public static TextAssert assertThat(Text actual) {
        return new TextAssert(actual);
    }

    /**
     * Create assertion for {@link TextFlow}.
     *
     * @param actual the actual value
     * @return the created assertion object
     */
    public static TextFlowAssert assertThat(TextFlow actual) {
        return new TextFlowAssert(actual);
    }

    /**
     * Create assertion for {@link TextInputControl}.
     *
     * @param actual the actual value
     * @return the created assertion object
     */
    public static TextInputControlAssert assertThat(TextInputControl actual) {
        return new TextInputControlAssert(actual);
    }

    /**
     * Create assertion for {@link Window}.
     *
     * @param actual the actual value
     * @return the created assertion object
     */
    public static WindowAssert assertThat(Window actual) {
        return new WindowAssert(actual);
    }
}
