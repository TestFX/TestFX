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
package org.testfx.service.adapter.impl;

import java.util.List;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxToolkit;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

public class JavafxRobotAdapterTest {

    //---------------------------------------------------------------------------------------------
    // FIELDS.
    //---------------------------------------------------------------------------------------------

    public JavafxRobotAdapter robotAdapter;

    public Stage targetStage;
    public Parent sceneRoot;

    public Region region;
    public TextField textField;
    public TextArea textArea;
    public Point2D regionPoint;
    public Point2D textFieldPoint;
    public Point2D textAreaPoint;

    //---------------------------------------------------------------------------------------------
    // FIXTURE METHODS.
    //---------------------------------------------------------------------------------------------

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        targetStage = FxToolkit.setupStage(stage -> {
            region = new Region();
            region.setStyle("-fx-background-color: magenta;");
            textField = new TextField();
            textArea = new TextArea();
            textArea.setPrefRowCount(6);

            VBox box = new VBox(region, textField, textArea);
            box.setPadding(new Insets(10));
            box.setSpacing(10);
            VBox.setVgrow(region, Priority.ALWAYS);

            sceneRoot = new StackPane(box);
            Scene scene = new Scene(sceneRoot, 300, 300);
            stage.setScene(scene);
            stage.show();
        });

        robotAdapter = new JavafxRobotAdapter();
        robotAdapter.robotCreate(targetStage.getScene());

        regionPoint = pointInCenterFor(boundsInSceneFor(region));
        textFieldPoint = pointInCenterFor(boundsInSceneFor(textField));
        textAreaPoint = pointInCenterFor(boundsInSceneFor(textArea));
    }

    @After
    public void cleanup() {
        robotAdapter.keyRelease(KeyCode.A);
        robotAdapter.mouseRelease(MouseButton.PRIMARY);
    }

    //---------------------------------------------------------------------------------------------
    // FEATURE METHODS.
    //---------------------------------------------------------------------------------------------

    // KEY.

    @Test
    public void keyType_with_punctuation_and_numbers() {
        String glyphs = ".:12";

        // given:
        robotAdapter.mouseMove(textFieldPoint);
        mousePressReleaseClick(MouseButton.PRIMARY);
        robotAdapter.timerWaitForIdle();

        // when:
        for (char character : Lists.charactersOf(glyphs)) {
            robotAdapter.keyType(KeyCode.UNDEFINED, Character.toString(character));
        }
        robotAdapter.timerWaitForIdle();

        // then:
        verifyThat(textField, hasText(glyphs));
    }

    @Test
    public void keyType_with_lowercase_and_uppercase() {
        String glyphs = "eEuU";

        // given:
        robotAdapter.mouseMove(textFieldPoint);
        mousePressReleaseClick(MouseButton.PRIMARY);
        robotAdapter.timerWaitForIdle();

        // when:
        for (char character : Lists.charactersOf(glyphs)) {
            robotAdapter.keyType(KeyCode.UNDEFINED, Character.toString(character));
        }
        robotAdapter.timerWaitForIdle();

        // then:
        verifyThat(textField, hasText(glyphs));
    }

    @Test
    public void keyType_with_latin_and_ext_latin_accents() {
        String glyphs = "éüāč";

        // given:
        robotAdapter.mouseMove(textFieldPoint);
        mousePressReleaseClick(MouseButton.PRIMARY);
        robotAdapter.timerWaitForIdle();

        // when:
        for (char character : Lists.charactersOf(glyphs)) {
            robotAdapter.keyType(KeyCode.UNDEFINED, Character.toString(character));
        }
        robotAdapter.timerWaitForIdle();

        // then:
        verifyThat(textField, hasText(glyphs));
    }

    @Test
    public void keyType_with_cjk_symbols() {
        String glyphs = "树木한";

        // given:
        robotAdapter.mouseMove(textFieldPoint);
        mousePressReleaseClick(MouseButton.PRIMARY);
        robotAdapter.timerWaitForIdle();

        // when:
        for (char character : Lists.charactersOf(glyphs)) {
            robotAdapter.keyType(KeyCode.UNDEFINED, Character.toString(character));
        }
        robotAdapter.timerWaitForIdle();

        // then:
        verifyThat(textField, hasText(glyphs));
    }

    @Test
    public void keyType_with_whitespace() {

    }

    @Test
    public void keyPressTypeRelease_english_text() {
        // Source: http://en.wikisource.org/wiki/All_in_the_Golden_Afternoon
        String text = "All in the golden afternoon\n" +
                "\tFull leisurely we glide;\n" +
                "For both our oars, with little skill,\n" +
                "\tBy little arms are plied;\n" +
                "While little hands make vain pretence\n" +
                "\tOur wanderings to guide.";

        // given:
        robotAdapter.mouseMove(textAreaPoint);
        mousePressReleaseClick(MouseButton.PRIMARY);
        robotAdapter.timerWaitForIdle();

        // when:
        for (char character : Lists.charactersOf(text)) {
            KeyCode key = KeyCode.UNDEFINED;
            key = (character == '\n') ? KeyCode.ENTER : key;
            key = (character == '\t') ? KeyCode.TAB : key;
            keyPressTypeRelease(key, String.valueOf(character));
        }
        robotAdapter.timerWaitForIdle();

        // then:
        verifyThat(textArea, hasText(text));
    }

    @Test
    public void keyPressTypeRelease_korean_text() {
        // Source: http://ko.wikisource.org/wiki/이상한_나라의_앨리스
        String text = "화창한 오후마다\n" +
                "\t우린 느긋이 배를 타지;\n" +
                "노는 둘 다 젓는 듯 마는 듯,\n" +
                "\t노를 젓는다고 해도,\n" +
                "작은 손으로 시늉만 내는 동안\n" +
                "\t우리는 정처없이 흘러간다네.";

        // given:
        robotAdapter.mouseMove(textAreaPoint);
        mousePressReleaseClick(MouseButton.PRIMARY);
        robotAdapter.timerWaitForIdle();

        // when:
        for (char character : Lists.charactersOf(text)) {
            KeyCode key = KeyCode.UNDEFINED;
            key = (character == '\n') ? KeyCode.ENTER : key;
            key = (character == '\t') ? KeyCode.TAB : key;
            keyPressTypeRelease(key, String.valueOf(character));
        }
        robotAdapter.timerWaitForIdle();

        // then:
        verifyThat(textArea, hasText(text));
    }

    @Test
    public void keyType_unicode_sequences() {
        // given:
        robotAdapter.mouseMove(textFieldPoint);
        mousePressReleaseClick(MouseButton.PRIMARY);
        robotAdapter.timerWaitForIdle();

        // when:
        for (char character : Lists.charactersOf(LATIN_EXTENDED_A_GLYPHS)) {
            keyPressTypeRelease(KeyCode.UNDEFINED, Character.toString(character));
        }
        for (int character : LATIN_EXTENDED_A_CODES) {
            keyPressTypeRelease(KeyCode.UNDEFINED, Character.toString((char) character));
        }
        robotAdapter.timerWaitForIdle();

        // then:
        verifyThat(textField, hasText(LATIN_EXTENDED_A_GLYPHS + LATIN_EXTENDED_A_GLYPHS));
    }

    //---------------------------------------------------------------------------------------------
    // HELPER FIELDS.
    //---------------------------------------------------------------------------------------------

    // BASIC LATIN (U+0000 TO U+007F).

    public String        BASIC_LATIN_UPPERCASE_GLYPHS = "@ABCDEFGHIJKLMNO";
    public List<Integer> BASIC_LATIN_UPPERCASE_CODES  = rangeToInts(0x0040, 0x004f);

    public String        BASIC_LATIN_LOWERCASE_GLYPHS = "`abcdefghijklmno";
    public List<Integer> BASIC_LATIN_LOWERCASE_CODES  = rangeToInts(0x0060, 0x006f);

    // LATIN-1 SUPPLEMENT (U+0080 TO U+00FF).

    public String        LATIN_1_SUPPLEMENT_UPPERCASE_GLYPHS = "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏ";
    public List<Integer> LATIN_1_SUPPLEMENT_UPPERCASE_CODES  = rangeToInts(0x00c0, 0x00cf);

    public String        LATIN_1_SUPPLEMENT_LOWERCASE_GLYPHS = "àáâãäåæçèéêëìíîï";
    public List<Integer> LATIN_1_SUPPLEMENT_LOWERCASE_CODES  = rangeToInts(0x00e0, 0x00ef);

    // LATIN EXTENDED-A (U+0100 TO U+017F).

    public String        LATIN_EXTENDED_A_GLYPHS = "ĀāĂăĄąĆćĈĉĊċČčĎď";
    public List<Integer> LATIN_EXTENDED_A_CODES  = rangeToInts(0x0100, 0x010f);

    //---------------------------------------------------------------------------------------------
    // HELPER METHODS.
    //---------------------------------------------------------------------------------------------

    private List<Integer> rangeToInts(int lower, int upper) {
        Range<Integer> range = Range.closed(lower, upper);
        return ContiguousSet.create(range, DiscreteDomain.integers()).asList();
    }

    private void keyPressTypeRelease(KeyCode key, String string) {
        // KeyEvent: "For key typed events, {@code code} is always {@code KeyCode.UNDEFINED}."
        robotAdapter.keyPress(key);
        robotAdapter.keyType(KeyCode.UNDEFINED, string);
        robotAdapter.keyRelease(key);
    }

    private void mousePressReleaseClick(MouseButton button) {
        robotAdapter.mousePress(button);
        robotAdapter.mouseRelease(button);
        robotAdapter.mouseClick(button);
    }

    private Bounds boundsInSceneFor(Node node) {
        return node.localToScene(node.getBoundsInLocal());
    }

    private Point2D pointInCenterFor(Bounds bounds) {
        return new Point2D(
            bounds.getMinX() + (bounds.getWidth() * 0.5),
            bounds.getMinY() + (bounds.getHeight() * 0.5)
        );
    }

}
