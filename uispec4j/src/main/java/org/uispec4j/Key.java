package org.uispec4j;

import sun.security.action.GetPropertyAction;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.security.AccessController;

/**
 * Contants class defining keyboard keys.
 */

public final class Key {
  private static Map<Character, Key> KEYS = new HashMap<Character, Key>();

  public static final Key A = create(KeyEvent.VK_A, 'a', true);
  public static final Key B = create(KeyEvent.VK_B, 'b', true);
  public static final Key C = create(KeyEvent.VK_C, 'c', true);
  public static final Key D = create(KeyEvent.VK_D, 'd', true);
  public static final Key E = create(KeyEvent.VK_E, 'e', true);
  public static final Key F = create(KeyEvent.VK_F, 'f', true);
  public static final Key G = create(KeyEvent.VK_G, 'g', true);
  public static final Key H = create(KeyEvent.VK_H, 'h', true);
  public static final Key I = create(KeyEvent.VK_I, 'i', true);
  public static final Key J = create(KeyEvent.VK_J, 'j', true);
  public static final Key K = create(KeyEvent.VK_K, 'k', true);
  public static final Key L = create(KeyEvent.VK_L, 'l', true);
  public static final Key M = create(KeyEvent.VK_M, 'm', true);
  public static final Key N = create(KeyEvent.VK_N, 'n', true);
  public static final Key O = create(KeyEvent.VK_O, 'o', true);
  public static final Key P = create(KeyEvent.VK_P, 'p', true);
  public static final Key Q = create(KeyEvent.VK_Q, 'q', true);
  public static final Key R = create(KeyEvent.VK_R, 'r', true);
  public static final Key S = create(KeyEvent.VK_S, 's', true);
  public static final Key T = create(KeyEvent.VK_T, 't', true);
  public static final Key U = create(KeyEvent.VK_U, 'u', true);
  public static final Key V = create(KeyEvent.VK_V, 'v', true);
  public static final Key W = create(KeyEvent.VK_W, 'w', true);
  public static final Key X = create(KeyEvent.VK_X, 'x', true);
  public static final Key Y = create(KeyEvent.VK_Y, 'y', true);
  public static final Key Z = create(KeyEvent.VK_Z, 'z', true);
  public static final Key d0 = create(KeyEvent.VK_0, '0', true);
  public static final Key d1 = create(KeyEvent.VK_1, '1', true);
  public static final Key d2 = create(KeyEvent.VK_2, '2', true);
  public static final Key d3 = create(KeyEvent.VK_3, '3', true);
  public static final Key d4 = create(KeyEvent.VK_4, '4', true);
  public static final Key d5 = create(KeyEvent.VK_5, '5', true);
  public static final Key d6 = create(KeyEvent.VK_6, '6', true);
  public static final Key d7 = create(KeyEvent.VK_7, '7', true);
  public static final Key d8 = create(KeyEvent.VK_8, '8', true);
  public static final Key d9 = create(KeyEvent.VK_9, '9', true);
  public static final Key NUM0 = create(KeyEvent.VK_NUMPAD0, '0', true);
  public static final Key NUM1 = create(KeyEvent.VK_NUMPAD1, '1', true);
  public static final Key NUM2 = create(KeyEvent.VK_NUMPAD2, '2', true);
  public static final Key NUM3 = create(KeyEvent.VK_NUMPAD3, '3', true);
  public static final Key NUM4 = create(KeyEvent.VK_NUMPAD4, '4', true);
  public static final Key NUM5 = create(KeyEvent.VK_NUMPAD5, '5', true);
  public static final Key NUM6 = create(KeyEvent.VK_NUMPAD6, '6', true);
  public static final Key NUM7 = create(KeyEvent.VK_NUMPAD7, '7', true);
  public static final Key NUM8 = create(KeyEvent.VK_NUMPAD8, '8', true);
  public static final Key NUM9 = create(KeyEvent.VK_NUMPAD9, '9', true);
  public static final Key MINUS = create(KeyEvent.VK_MINUS, '-', true);
  public static final Key ADD = create(KeyEvent.VK_ADD, '+', true);
  public static final Key NUM_DIVIDE = create(KeyEvent.VK_DIVIDE, '/', true);
  public static final Key NUM_MULTIPLY = create(KeyEvent.VK_MULTIPLY, '*', true);
  public static final Key NUM_SEPARATOR = create(KeyEvent.VK_SEPARATOR, '-', true);
  public static final Key SLASH = create(KeyEvent.VK_SLASH, '/', true);
  public static final Key BACKSLASH = create(KeyEvent.VK_BACK_SLASH, '\\', true);
  public static final Key EQUALS = create(KeyEvent.VK_EQUALS, '=', true);
  public static final Key ASTERISK = create(KeyEvent.VK_ASTERISK, '*', true);
  public static final Key DOLLAR = create(KeyEvent.VK_DOLLAR, '$', true);
  public static final Key LEFT_PARENTHESIS = create(KeyEvent.VK_LEFT_PARENTHESIS, '(', true);
  public static final Key RIGHT_PARENTHESIS = create(KeyEvent.VK_RIGHT_PARENTHESIS, ')', true);
  public static final Key AT = create(KeyEvent.VK_AT, '@', true);
  public static final Key AMPERSAND = create(KeyEvent.VK_AMPERSAND, '&', true);
  public static final Key QUOTE = create(KeyEvent.VK_QUOTE, '`', true);
  public static final Key BACKQUOTE = create(KeyEvent.VK_BACK_QUOTE, '`', true);
  public static final Key DOUBLE_QUOTE = create(KeyEvent.VK_QUOTEDBL, '"', true);
  public static final Key LEFT_BRACE = create(KeyEvent.VK_BRACELEFT, '{', true);
  public static final Key RIGHT_BRACE = create(KeyEvent.VK_BRACERIGHT, '}', true);
  public static final Key CARAT = create(KeyEvent.VK_CIRCUMFLEX, '^', true);
  public static final Key OPEN_BRACKET = create(KeyEvent.VK_OPEN_BRACKET, '[', true);
  public static final Key LEFT_BRACKET = create(KeyEvent.VK_OPEN_BRACKET, '[', true);
  public static final Key CLOSE_BRACKET = create(KeyEvent.VK_CLOSE_BRACKET, ']', true);
  public static final Key RIGHT_BRACKET = create(KeyEvent.VK_CLOSE_BRACKET, ']', true);
  public static final Key COLON = create(KeyEvent.VK_COLON, ':', true);
  public static final Key COMMA = create(KeyEvent.VK_COMMA, ',', true);
  public static final Key DECIMAL = create(KeyEvent.VK_DECIMAL, '.', true);
  public static final Key EXCLAMATION = create(KeyEvent.VK_EXCLAMATION_MARK, '!', true);
  public static final Key GREATER_THAN = create(KeyEvent.VK_GREATER, '>', true);
  public static final Key LESS_THAN = create(KeyEvent.VK_LESS, '<', true);
  public static final Key HASH = create(KeyEvent.VK_NUMBER_SIGN, '#', true);
  public static final Key SEMICOLON = create(KeyEvent.VK_SEMICOLON, ';', true);
  public static final Key SPACE = create(KeyEvent.VK_SPACE, ' ', true);
  public static final Key UNDERSCORE = create(KeyEvent.VK_UNDERSCORE, '_', true);
  public static final Key QUESTION = create(0, '?', true);
  public static final Key VERTICAL_LINE = create(0, '|', true);
  public static final Key PERCENT = create(0, '%', true);
  public static final Key TAB = create(KeyEvent.VK_TAB, '\u0009', true);

  public static final Key ENTER = create(KeyEvent.VK_ENTER, '\r', false);
  public static final Key DELETE = create(KeyEvent.VK_DELETE, '\u007f', false);
  public static final Key BACKSPACE = create(KeyEvent.VK_BACK_SPACE, '\u0008', false);
  public static final Key UP = create(KeyEvent.VK_UP, '\u2191', false);
  public static final Key DOWN = create(KeyEvent.VK_DOWN, '\u2193', false);
  public static final Key LEFT = create(KeyEvent.VK_LEFT, '\u2190', false);
  public static final Key RIGHT = create(KeyEvent.VK_RIGHT, '\u2192', false);
  public static final Key PAGE_DOWN = create(KeyEvent.VK_PAGE_DOWN, '\u21DF', false);
  public static final Key PAGE_UP = create(KeyEvent.VK_PAGE_UP, '\u21DE', false);
  public static final Key END = create(KeyEvent.VK_END, '\u0003', true);
  public static final Key ESCAPE = create(KeyEvent.VK_ESCAPE, '\u001B', false);
  public static final Key CONTROL = create(KeyEvent.VK_CONTROL, null, false);
  public static final Key SHIFT = create(KeyEvent.VK_SHIFT, null, false);
  public static final Key ALT = create(KeyEvent.VK_ALT, null, false);
  public static final Key INSERT = create(KeyEvent.VK_INSERT, null, false);
  public static final Key F1 = create(KeyEvent.VK_F1, null, false);
  public static final Key F2 = create(KeyEvent.VK_F2, null, false);
  public static final Key F3 = create(KeyEvent.VK_F3, null, false);
  public static final Key F4 = create(KeyEvent.VK_F4, null, false);
  public static final Key F5 = create(KeyEvent.VK_F5, null, false);
  public static final Key F6 = create(KeyEvent.VK_F6, null, false);
  public static final Key F7 = create(KeyEvent.VK_F7, null, false);
  public static final Key F8 = create(KeyEvent.VK_F8, null, false);
  public static final Key F9 = create(KeyEvent.VK_F9, null, false);
  public static final Key F10 = create(KeyEvent.VK_F10, null, false);
  public static final Key F11 = create(KeyEvent.VK_F11, null, false);
  public static final Key F12 = create(KeyEvent.VK_F12, null, false);
  public static final Key F13 = create(KeyEvent.VK_F13, null, false);
  public static final Key F14 = create(KeyEvent.VK_F14, null, false);
  public static final Key F15 = create(KeyEvent.VK_F15, null, false);
  public static final Key F16 = create(KeyEvent.VK_F16, null, false);
  public static final Key F17 = create(KeyEvent.VK_F17, null, false);
  public static final Key F18 = create(KeyEvent.VK_F18, null, false);
  public static final Key F19 = create(KeyEvent.VK_F19, null, false);
  public static final Key F20 = create(KeyEvent.VK_F20, null, false);
  public static final Key F21 = create(KeyEvent.VK_F21, null, false);
  public static final Key F22 = create(KeyEvent.VK_F22, null, false);
  public static final Key F23 = create(KeyEvent.VK_F23, null, false);
  public static final Key F24 = create(KeyEvent.VK_F24, null, false);
  public static final Key NUM_LOCK = create(KeyEvent.VK_NUM_LOCK, null, false);
  public static final Key SCROLL_LOCK = create(KeyEvent.VK_SCROLL_LOCK, null, false);
  public static final Key CAPS_LOCK = create(KeyEvent.VK_CAPS_LOCK, null, false);

  private int code;
  private Modifier modifier;
  private boolean printable;
  private Character chr;

  private static Key create(int code, Character chr, boolean printable) {
    Key key = new Key(code, chr, Modifier.NONE, printable);
    if (chr != null && printable) {
      KEYS.put(chr, key);
    }
    return key;
  }

  private Key(int code, Character chr, Modifier modifier, boolean printable) {
    this.code = code;
    this.chr = chr;
    this.modifier = modifier;
    this.printable = printable;
  }

  public static Key get(Character chr) {
    return KEYS.get(chr);
  }

  public static Key control(Key key) {
    return new Key(key.getCode(), key.getChar(), Modifier.CONTROL, false);
  }

  public static Key meta(Key key) {
    return new Key(key.getCode(), key.getChar(), Modifier.META, false);
  }

  public static Key alt(Key key) {
    return new Key(key.getCode(), key.getChar(), Modifier.ALT, false);
  }

  public static Key shift(Key key) {
    return new Key(key.getCode(), Character.toUpperCase(key.getChar()), Modifier.SHIFT, key.isPrintable());
  }

  /**
   * Returns META-Key on MacOS X, and CTRL-Key on other platforms.
   */
  public static Key plaformSpecificCtrl(Key key) {
    String os = (String)AccessController.doPrivileged(new GetPropertyAction("os.name"));
    boolean isMacOSX = os.contains("Mac OS X");
    return isMacOSX ? meta(key) : control(key);
  }

  public int getCode() {
    return code;
  }

  public Character getChar() {
    return (chr == null)? KeyEvent.CHAR_UNDEFINED : chr;
  }

  public Modifier getModifier() {
    return modifier;
  }

  public boolean isPrintable() {
    return printable;
  }

  /**
   * Constants class for keyboard modifiers such as Control or Shift.
   */
  public static final class Modifier {
    public static final Modifier CONTROL = new Modifier(InputEvent.CTRL_MASK);
    public static final Modifier SHIFT = new Modifier(InputEvent.SHIFT_MASK);
    public static final Modifier ALT = new Modifier(InputEvent.ALT_MASK);
    public static final Modifier META = new Modifier(InputEvent.META_MASK);
    public static final Modifier NONE = new Modifier(0);

    private int code;

    private Modifier(int code) {
      this.code = code;
    }

    public int getCode() {
      return code;
    }
  }
}
