package org.loadui.testfx.utils.keymaps;

import java.awt.im.InputContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.input.KeyCode;

public final class KeyCharMap
    implements Cloneable, Serializable
{

	private volatile static KeyCharMap defaultKeyCharMap = initDefault();

	private static Map<String, KeyCharMap> cachedCharMap;

	private final String keyCharMapName;

	private final Map<Character, KeyChar> keyCharMap;

	/**
	 * Gets the current value of the keyboard layout reading default locale
	 * of the Java Virtual Machine.
	 *
	 * @return the default characted map for the current keyboard layout
	 */
	public static KeyCharMap getDefault()
	{
		return defaultKeyCharMap;
	}

	public static KeyCharMap getInstance(
	    final String keyboardLayoutName)
	{
		KeyCharMap ret = cachedCharMap.get(keyboardLayoutName);
		if ( ret == null ) {
			ret = initKeyCharMap(keyboardLayoutName);
		}
		return ret;
	}

	private static KeyCharMap initDefault()
	{
		String name = InputContext.getInstance().getLocale().toString();
		return initKeyCharMap(name);
	}

	private static KeyCharMap initKeyCharMap(
	    final String name)
	{
		URL url = KeyCharMap.class.getResource("/keycharmaps/keycharmap_" + name + ".tsv");
		Map<Character, KeyChar> map = null;
		try {
			map = readKeyCharMapFile(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new KeyCharMap(name, map);
	}

	/**
	 * Private constructor to prevent instanciation
	 * @param map
	 */
	private KeyCharMap(final String keyCharMapName,
	    final Map<Character, KeyChar> map)
	{
		this.keyCharMapName = keyCharMapName;
		this.keyCharMap = map;
		if ( cachedCharMap == null ) {
			cachedCharMap = new HashMap<>();
		}
		cachedCharMap.put(this.keyCharMapName, this);
	}

	static private Map<Character, KeyChar> readKeyCharMapFile(
	    final URL keyCharMapFile)
	    throws IOException
	{
		Map<Character, KeyChar> keyCharMap = null;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(keyCharMapFile.openStream(), Charset.forName("UTF-8")))) {
			keyCharMap = new HashMap<>();
			String line = null;
			reader.readLine(); // Header (column names)
			while ( (line = reader.readLine()) != null ) {
				line = line.trim();
				if ( !line.isEmpty() ) {
					String[] parts = line.split("\t");
					if ( parts.length == 4 ) {
						KeyCode keyCode = KeyCode.getKeyCode(parts[3]);
						// Workaround bug JavaFX RT-37431
						if ( parts[3].equals("Dead Circumflex") ) {
							keyCode = KeyCode.DEAD_CIRCUMFLEX;
						}
						KeyChar c = new KeyChar(parts[0].charAt(0), Integer.parseInt(parts[1].substring(2), 16), Integer.parseInt(parts[2]), keyCode);
						keyCharMap.put(c.getCharacter(), c);
					}
				}
			}
		}
		return keyCharMap;
	}

	public Map<Character, KeyChar> getKeyCharMap()
	{
		return Collections.unmodifiableMap(keyCharMap);
	}

	public KeyChar getKeyChar(
	    final char character)
	{
		return keyCharMap.get(character);
	}
}
