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

Columns:
 - The character to print.
 - The modifiers :
  - 1 <=> SHIFT
  - 2 <=> CONTROL
  - 4 <=> ALT
  - 8 <=> ALT GRAPH (Alt Gr)
  - 16 <=> META
  - 32 <=> DEAD Key
 - The name of the KeyCode instance in the class javafx.scene.input.KeyCode
 - (Optional) if the character need a dead key, then a second modifiers can be specified
 - (Optional) if the character need a dead key, then a second name for the KeyCode instance can be specified

### Example

For French Azerty Keyboards, digits are shited keys. So without the keycharmap file, asking testFX to type "12" will issue "&é". To correct this 4 lines are needed :
 * 1 1 1
 * 2  2
 * & 0 1
 * é 0 2

Explanation for first line :
 * Characted wanted : 1
 * Modifier: 1 <=> SHIFT
 * KeyCode name associated with the 0x31 code: 1

A more complicated example can be the dead diaeresis "¨". On French Azerty Keyboards, this character is a dead key (meaning that it will not issue a character but modify the next one), and is obtained with a combination of the SHIFT + ^.
 * ¨	33	Dead Circumflex

Explanation :
 * Characted wanted : ¨
 * Modifiers : 1 (Shift) + 32 (Dead) = 33
 * KeyCode name : "Dead Circumflex"

NB: This is a dead key, and not extra keys are specified, so a whitespace will be issued automatically.

Last example : Ë
  * Ë 33 Dead Circumflex 1 E

Explanation :
 * Characted wanted : Ë
 * Modifiers : 1 (Shift) + 32 (Dead) = 33
 * KeyCode name "Dead Circumflex"
 * Extra modifiers (for the capital E) : 1 (Shift)
 * Extra KeyCode name : "E"

