package org.loadui.testfx.utils.keymaps;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;

public class KeyChar
{
	private final char character;

	private final int modifiers;

	private final KeyCode keyCode;

	private int extraModifiers;

	private  KeyCode extraKeyCode;

	private final KeyCode[] modifiersKeyCodes;

	private KeyCode[] extraModifiersKeyCodes;

	public static final int SHIFT = 1;

	public static final int CTRL = 1 << 1;

	public static final int ALT = 1 << 2;

	public static final int ALT_GRAPH = 1 << 3;

	public static final int META = 1 << 4;

	public static final int DEAD = 1 << 5;

	/**
	 * Constructor for the key or serie of keys to get the character typed
	 * @param character
	 * @param modifiers
	 * @param name
	 */
	public KeyChar(final char character,
	    final int modifiers,
	    final KeyCode keyCode)
	{
		super();
		this.character = character;
		this.modifiers = modifiers;
		this.keyCode = keyCode;
		this.modifiersKeyCodes = modifiersToKeyCodes(this.modifiers);
	}

	public void setExtraKey(int extraModifiers, KeyCode extraKeyCode) {
		this.extraModifiers=extraModifiers;
		this.extraKeyCode=extraKeyCode;
		this.extraModifiersKeyCodes = modifiersToKeyCodes(this.extraModifiers);
	}

	/**
	 * Gets character
	 * @return the character
	 */
	public char getCharacter()
	{
		return character;
	}

	/**
	 * Gets the key code to press to type this character
	 * @return the code
	 */
	public int getCode()
	{
		return keyCode.impl_getCode();
	}

	/**
	 * Gets modifiers (shift,alt,control) to press to get the right version of the character
	 * @return the modifiers
	 */
	public int getModifiers()
	{
		return modifiers;
	}

	/**
	 * Whether or not this char need another key after to be printed
	 * @return true if the character is printed by a dead key
	 */
	public boolean isDeadKey()
	{
		return (modifiers & DEAD) != 0;
	}

	/**
	 * @return the KeyCode
	 */
	public KeyCode getKeyCode()
	{

		return keyCode;
	}

	/**
	 * @return the list of modifiers key codes to maintain pressed to get the character
	 */
	public KeyCode[] getModifiersKeyCodes()
	{
		return modifiersKeyCodes;
	}

	/**
	 * @return the modifiers to maintain pressed to type the character after the dead key
	 */
	public int getExtraModifiers() {
		return extraModifiers;
	}

	/**
	 * @return the key code to type after the dead key (if any)
	 */
	public KeyCode getExtraKeyCode() {
		return extraKeyCode;
	}

	/**
	 * @return the list of modifiers key codes to maintain pressed to get the character, after the dead key (if any)
	 */
	public KeyCode[] getExtraModifiersKeyCodes() {
		return extraModifiersKeyCodes;
	}

	private static KeyCode[] modifiersToKeyCodes(final int modifiers)
	{
		List<KeyCode> list = new ArrayList<>();
		if ( (modifiers & SHIFT) != 0 ) {
			list.add(KeyCode.SHIFT);
		}
		if ( (modifiers & CTRL) != 0 ) {
			list.add(KeyCode.CONTROL);
		}
		if ( (modifiers & ALT) != 0 ) {
			list.add(KeyCode.ALT);
		}
		if ( (modifiers & ALT_GRAPH) != 0 ) {
			// Due to invalid key code exception when using the ALT_GRAPH key code ...
			// list.add(KeyCode.ALT_GRAPH);
			// I use the ALT + CTRL key codes
			list.add(KeyCode.CONTROL);
			list.add(KeyCode.ALT);
		}
		if ( (modifiers & META) != 0 ) {
			list.add(KeyCode.META);
		}
	
		KeyCode[] param = new KeyCode[list.size()];
		return list.toArray(param);
	}

}
