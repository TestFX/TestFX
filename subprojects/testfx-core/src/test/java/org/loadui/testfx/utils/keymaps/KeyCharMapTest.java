package org.loadui.testfx.utils.keymaps;

import static org.assertj.core.api.Assertions.assertThat;
import javafx.scene.input.KeyCode;

import org.junit.Test;
import org.loadui.testfx.robots.KeyboardRobot;
import org.loadui.testfx.robots.TypeRobot;
import org.loadui.testfx.robots.impl.TypeRobotImpl;

public class KeyCharMapTest {

	private KeyBoardRobotLogger logger;
	private TypeRobot robot;

	@Test
	public void fallbackTypeTest() {
		initKeyLogger("NON_EXISTENT_LAYOUT");

		final String text = "H3llo W0rld";
		final String keyCodes = "0x10 0x48 0x33 0x4C 0x4C 0x4F 0x20 0x10 0x57 0x30 0x52 0x4C 0x44";

		// Type a small sentence and check that all key are released and the key
		// code sequence produced
		compareWantedTextAndKeyCodeSequence(text, keyCodes);

		// Check for certain special char
		compareWantedTextAndKeyCodeSequence("#", "0x100F");
		compareWantedTextAndKeyCodeSequence("&", "0x96");
	}

	@Test
	public void azertyTypeTest() {
		initKeyLogger("fr_FR");

		final String text = "H3llo W0rld";
		final String keyCodes = "0x10 0x48 0x10 0x33 0x4C 0x4C 0x4F 0x20 0x10 0x57 0x10 0x30 0x52 0x4C 0x44";

		compareWantedTextAndKeyCodeSequence(text, keyCodes);

		// Check for certain special char
		// DIGIT3 without modifier
		compareWantedTextAndKeyCodeSequence("\"", "0x33");

		// DIGIT3 with Shift
		compareWantedTextAndKeyCodeSequence("3", "0x10 0x33");

		// DIGIT3 with Alt Graphic
		compareWantedTextAndKeyCodeSequence("#", "0x11 0x12 0x33");

		// Dead Circumflex alone
		compareWantedTextAndKeyCodeSequence("¨", "0x10 0x82 0x20");

		// Dead Circumflex with letter
		compareWantedTextAndKeyCodeSequence("ë", "0x10 0x82 0x45");

		// Dead Circumflex with capital letter
		compareWantedTextAndKeyCodeSequence("Ë", "0x10 0x82 0x10 0x45");
	}

	private void initKeyLogger(String keyboardLayout) {
		logger = new KeyBoardRobotLogger();
		robot = new TypeRobotImpl(logger, keyboardLayout);
	}

	private void compareWantedTextAndKeyCodeSequence(final String wantedText,
			final String keyCodeSequence) {
		robot.type(wantedText);
		assertThat(logger.getPressedKeys()).isEqualTo(0);
		assertThat(logger.getLog()).isEqualTo(keyCodeSequence);
		logger.clearLog();
	}

	private class KeyBoardRobotLogger implements KeyboardRobot {

		private StringBuilder keyCodeLogs = new StringBuilder();

		private int pressedKeys = 0;

		/**
		 * @return the number of actual pressed keys
		 */
		public int getPressedKeys() {
			return pressedKeys;
		}

		@Override
		public void press(KeyCode... keyCodes) {
			for (int i = 0; i < keyCodes.length; i++) {
				keyCodeLogs.append(String.format("0x%02X",
						keyCodes[i].impl_getCode()));
				keyCodeLogs.append(' ');
				pressedKeys++;
			}
		}

		@Override
		public void release(KeyCode... keyCodes) {
			for (int i = 0; i < keyCodes.length; i++) {
				pressedKeys--;
			}
		}

		public void clearLog() {
			keyCodeLogs = new StringBuilder();
		}

		public String getLog() {
			return keyCodeLogs.toString().trim();
		}
	}
}
