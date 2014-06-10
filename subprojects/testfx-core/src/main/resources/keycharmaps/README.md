Create keycharmap files
=======================

Those files are maps from wanted characters to combination of key codes.

Beacuse JavaFX does not handle correctly most of non Qwerty/US keyboards, we need a map to convert a character to the right combination of keycode.

NB: Only printable characters can be mapped.

### Naming

The correct file will be automatically loaded if it's present, and named as follow:
 * into the folder \[...\]/src/main/java/resources/keycharmaps
 * starts with keycharmap
 * suffixed with the string code returned by InputContext.getInstance().getLocale().toString(); to identify the keyboard.
 * and finishes with .tsv (meaning tab separated values).

### Content

Fisrt line of file is not taken in account, Empty lines neither.

Colums:
 - The character to print.
 - The keycode (in fact, this information is not taken in account)
 - The modifiers :
  - 1 <=> SHIFT
  - 2 <=> CONTROL
  - 4 <=> ALT
  - 8 <=> ALT GRAPH (Alt Gr)
  - 16 <=> META
  - 32 <=> DEAD Key (a whitespace will be pressed after the key to get it printed)
 - The name of the KeyCode instance in the class javafx.scene.input.KeyCode

### Example

For French Azerty Keyboards, digits are shited keys. So without the keycharmap file, asking testFX to type "12" will issue "&é". To correct this 4 lines are needed :
 * 1 0x31 1 1
 * 2 0x32 1 2
 * & 0x31 0 1
 * é 0x32 0 2

Explanation for first line :
 * Characted wanted : 1
 * Key Code : 0x31 (Hexadecimal mandatory, prefix 0x mandatory)
 * Modifier: 1 <=> SHIFT
 * KeyCode name associated with the 0x31 code: 1

A more complicated example can be the dead trema "¨". On French Azerty Keyboards, this character is a dead key (meaning that it will not issue a character but modify the next one), and is obtained with a combination of the SHIFT + ^.
 * ¨	0x82	33	Dead Circumflex

Explanation :
 * Characted wanted : ¨
 * Key Code : 0x82
 * Modifiers : 1 (Shift) + 32 (Dead) = 33
 * KeyCode name "Dead Circumflex"


### Furtherwork

This file cannot print complex combination of key, for example, the character ë need to press SHIFT + ^, and then "e" (dead key combination).
