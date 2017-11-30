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
package org.testfx.service.adapter.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.TestFXRule;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.util.WaitForAsyncUtils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;
import static org.testfx.api.FxAssert.verifyThat;

public class JavafxRobotAdapterTest {

    @Rule
    public TestFXRule testFXRule = new TestFXRule();

    JavafxRobotAdapter robotAdapter;
    Stage targetStage;
    Parent sceneRoot;
    Region region;
    TextField textField;
    TextArea textArea;
    Point2D regionPoint;
    Point2D textFieldPoint;
    Point2D textAreaPoint;

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

        // Points are set to bounds in scene
        regionPoint = pointInCenterFor(region.localToScene(region.getBoundsInLocal()));
        textFieldPoint = pointInCenterFor(textField.localToScene(textField.getBoundsInLocal()));
        textAreaPoint = pointInCenterFor(textArea.localToScene(textArea.getBoundsInLocal()));
    }

    @After
    public void cleanup() {
        robotAdapter.keyRelease(KeyCode.A);
        robotAdapter.mouseRelease(MouseButton.PRIMARY);
    }

    @Test
    public void keyType_with_punctuation_and_numbers() {
        // given:
        robotAdapter.mouseMove(textFieldPoint);
        mousePressReleaseClick(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        char[] glyphs = new char[] {'.', ':', '1', '2'};

        // when:
        for (char character : glyphs) {
            robotAdapter.keyType(KeyCode.UNDEFINED, Character.toString(character));
        }
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        verifyThat(textField, TextInputControlMatchers.hasText(String.valueOf(glyphs)));
    }

    @Test
    public void keyType_with_lowercase_and_uppercase() {
        // given:
        robotAdapter.mouseMove(textFieldPoint);
        mousePressReleaseClick(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        char[] glyphs = new char[] {'e', 'E', 'u', 'U'};

        // when:
        for (char character : glyphs) {
            robotAdapter.keyType(KeyCode.UNDEFINED, Character.toString(character));
        }
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        verifyThat(textField, TextInputControlMatchers.hasText(String.valueOf(glyphs)));
    }

    @Test
    public void keyType_with_latin_and_ext_latin_accents() {
        // given:
        robotAdapter.mouseMove(textFieldPoint);
        mousePressReleaseClick(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        char[] glyphs = new char[] {'é', 'ü', 'ā', 'č'};

        // when:
        for (char character : glyphs) {
            robotAdapter.keyType(KeyCode.UNDEFINED, Character.toString(character));
        }
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        verifyThat(textField, TextInputControlMatchers.hasText(String.valueOf(glyphs)));
    }

    @Test
    public void keyType_with_cjk_symbols() {
        // given:
        robotAdapter.mouseMove(textFieldPoint);
        mousePressReleaseClick(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        char[] glyphs = new char[] {'树', '木', '한'};

        // when:
        for (char character : glyphs) {
            robotAdapter.keyType(KeyCode.UNDEFINED, Character.toString(character));
        }
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        verifyThat(textField, TextInputControlMatchers.hasText(String.valueOf(glyphs)));
    }

    @Test
    public void keyPressTypeRelease_english_text() {
        // given:
        robotAdapter.mouseMove(textAreaPoint);
        mousePressReleaseClick(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();

        // Source: http://en.wikisource.org/wiki/All_in_the_Golden_Afternoon
        String text = "All in the golden afternoon\n" +
                "\tFull leisurely we glide;\n" +
                "For both our oars, with little skill,\n" +
                "\tBy little arms are plied;\n" +
                "While little hands make vain pretence\n" +
                "\tOur wanderings to guide.";

        // when:
        typeText(text);

        // then:
        verifyThat(textArea, TextInputControlMatchers.hasText(text));
    }

    @Test
    public void keyPressTypeRelease_korean_text() {
        // given:
        robotAdapter.mouseMove(textAreaPoint);
        mousePressReleaseClick(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();
        // Source: http://ko.wikisource.org/wiki/이상한_나라의_앨리스
        String text = "화창한 오후마다\n" +
                "\t우린 느긋이 배를 타지;\n" +
                "노는 둘 다 젓는 듯 마는 듯,\n" +
                "\t노를 젓는다고 해도,\n" +
                "작은 손으로 시늉만 내는 동안\n" +
                "\t우리는 정처없이 흘러간다네.";

        // when:
        typeText(text);

        // then:
        verifyThat(textArea, TextInputControlMatchers.hasText(text));
    }

    @Test
    public void keyType_unicode_sequences() {
        // given:
        robotAdapter.mouseMove(textFieldPoint);
        mousePressReleaseClick(MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();

        // when:
        for (char character : LATIN_EXTENDED_A_GLYPHS) {
            keyPressTypeRelease(KeyCode.UNDEFINED, Character.toString(character));
        }
        for (int character : LATIN_EXTENDED_A_CODES) {
            keyPressTypeRelease(KeyCode.UNDEFINED, Character.toString((char) character));
        }
        WaitForAsyncUtils.waitForFxEvents();

        // then:
        verifyThat(textField, TextInputControlMatchers.hasText(String.valueOf(LATIN_EXTENDED_A_GLYPHS) +
                String.valueOf(LATIN_EXTENDED_A_GLYPHS)));
    }

    @Test
    public void getCapturePixelColor() throws InterruptedException {
        assumeThat("skipping: screen capture on macOS uses configured display's color profile",
                System.getenv("TRAVIS_OS_NAME"), is(not(equalTo("osx"))));

        // given:
        CountDownLatch captureColorLatch = new CountDownLatch(1);

        // when:
        CompletableFuture<Color> captureColorFutureResult = new CompletableFuture<>();
        Platform.runLater(() -> captureColorFutureResult.complete(robotAdapter.getCapturePixelColor(regionPoint)));

        // then:
        captureColorFutureResult.whenComplete((pixelColor, throwable) -> {
            if (throwable != null) {
                fail("JavafxRobotAdapter.getCapturePixelColor(..) should not have completed exceptionally");
            }
            else {
                assertThat(pixelColor, is(Color.MAGENTA));
                captureColorLatch.countDown();
            }
        });

        assertThat(captureColorLatch.await(3, TimeUnit.SECONDS), is(true));
    }

    @Test
    public void getCaptureRegion() throws InterruptedException {
        assumeThat("skipping: screen capture on macOS uses configured display's color profile",
                System.getenv("TRAVIS_OS_NAME"), is(not(equalTo("osx"))));

        // given:
        CountDownLatch captureRegionLatch = new CountDownLatch(1);

        // when:
        Rectangle2D region = new Rectangle2D(regionPoint.getX(), regionPoint.getY(), 10, 20);
        CompletableFuture<Image> captureRegionFutureResult = new CompletableFuture<>();
        Platform.runLater(() -> captureRegionFutureResult.complete(robotAdapter.getCaptureRegion(region)));

        // then:
        captureRegionFutureResult.whenComplete((regionImage, throwable) -> {
            if (throwable != null) {
                fail("JavafxRobotAdapter.getCaptureRegion(..) should not have completed exceptionally");
            }
            else {
                assertThat(regionImage.getWidth(), is(10.0));
                assertThat(regionImage.getHeight(), is(20.0));
                assertThat(regionImage.getPixelReader().getColor(5, 10), is(Color.MAGENTA));
                captureRegionLatch.countDown();
            }
        });

        assertThat(captureRegionLatch.await(5, TimeUnit.SECONDS), is(true));
    }

    // BASIC LATIN (U+0000 TO U+007F).

    public String        BASIC_LATIN_UPPERCASE_GLYPHS = "@ABCDEFGHIJKLMNO";
    public List<Integer> BASIC_LATIN_UPPERCASE_CODES  = closedRangeToInts(0x0040, 0x004f);

    public String        BASIC_LATIN_LOWERCASE_GLYPHS = "`abcdefghijklmno";
    public List<Integer> BASIC_LATIN_LOWERCASE_CODES  = closedRangeToInts(0x0060, 0x006f);

    // LATIN-1 SUPPLEMENT (U+0080 TO U+00FF).

    public String        LATIN_1_SUPPLEMENT_UPPERCASE_GLYPHS = "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏ";
    public List<Integer> LATIN_1_SUPPLEMENT_UPPERCASE_CODES  = closedRangeToInts(0x00c0, 0x00cf);

    public String        LATIN_1_SUPPLEMENT_LOWERCASE_GLYPHS = "àáâãäåæçèéêëìíîï";
    public List<Integer> LATIN_1_SUPPLEMENT_LOWERCASE_CODES  = closedRangeToInts(0x00e0, 0x00ef);

    // LATIN EXTENDED-A (U+0100 TO U+017F).

    public char[]        LATIN_EXTENDED_A_GLYPHS = new char[] {'Ā', 'ā', 'Ă', 'ă', 'Ą', 'ą', 'Ć', 'ć', 'Ĉ', 'ĉ',
        'Ċ', 'ċ', 'Č', 'č', 'Ď', 'ď'};
    public List<Integer> LATIN_EXTENDED_A_CODES  = closedRangeToInts(0x0100, 0x010f);

    private List<Integer> closedRangeToInts(int lower, int upper) {
        return IntStream.range(lower, upper + 1).boxed().collect(Collectors.toList());
    }

    private void typeText(String text) {
        // TODO(mike): This extra tab should not be needed but currently {@code textField} has focus
        // instead of {@code textArea} when this method is called.
        keyPressTypeRelease(KeyCode.TAB, String.valueOf('\t'));

        for (char character : text.chars().mapToObj(i -> (char) i).collect(Collectors.toList())) {
            KeyCode key = KeyCode.UNDEFINED;
            key = (character == '\n') ? KeyCode.ENTER : key;
            key = (character == '\t') ? KeyCode.TAB : key;
            keyPressTypeRelease(key, String.valueOf(character));
        }
        WaitForAsyncUtils.waitForFxEvents();
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

    private Point2D pointInCenterFor(Bounds bounds) {
        return new Point2D(
                bounds.getMinX() + (bounds.getWidth() * 0.5),
                bounds.getMinY() + (bounds.getHeight() * 0.5)
        );
    }

}
