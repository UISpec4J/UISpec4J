package org.uispec4j;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Contants class defining keyboard keys.
 */
public class Key {
  public static final Key A = new Key(KeyEvent.VK_A, true);
  public static final Key B = new Key(KeyEvent.VK_B, true);
  public static final Key C = new Key(KeyEvent.VK_C, true);
  public static final Key D = new Key(KeyEvent.VK_D, true);
  public static final Key E = new Key(KeyEvent.VK_E, true);
  public static final Key F = new Key(KeyEvent.VK_F, true);
  public static final Key G = new Key(KeyEvent.VK_G, true);
  public static final Key H = new Key(KeyEvent.VK_H, true);
  public static final Key I = new Key(KeyEvent.VK_I, true);
  public static final Key J = new Key(KeyEvent.VK_J, true);
  public static final Key K = new Key(KeyEvent.VK_K, true);
  public static final Key L = new Key(KeyEvent.VK_L, true);
  public static final Key M = new Key(KeyEvent.VK_M, true);
  public static final Key N = new Key(KeyEvent.VK_N, true);
  public static final Key O = new Key(KeyEvent.VK_O, true);
  public static final Key P = new Key(KeyEvent.VK_P, true);
  public static final Key Q = new Key(KeyEvent.VK_Q, true);
  public static final Key R = new Key(KeyEvent.VK_R, true);
  public static final Key S = new Key(KeyEvent.VK_S, true);
  public static final Key T = new Key(KeyEvent.VK_T, true);
  public static final Key U = new Key(KeyEvent.VK_U, true);
  public static final Key V = new Key(KeyEvent.VK_V, true);
  public static final Key W = new Key(KeyEvent.VK_W, true);
  public static final Key X = new Key(KeyEvent.VK_X, true);
  public static final Key Y = new Key(KeyEvent.VK_Y, true);
  public static final Key Z = new Key(KeyEvent.VK_Z, true);
  public static final Key d0 = new Key(KeyEvent.VK_0, true);
  public static final Key d1 = new Key(KeyEvent.VK_1, true);
  public static final Key d2 = new Key(KeyEvent.VK_2, true);
  public static final Key d3 = new Key(KeyEvent.VK_3, true);
  public static final Key d4 = new Key(KeyEvent.VK_4, true);
  public static final Key d5 = new Key(KeyEvent.VK_5, true);
  public static final Key d6 = new Key(KeyEvent.VK_6, true);
  public static final Key d7 = new Key(KeyEvent.VK_7, true);
  public static final Key d8 = new Key(KeyEvent.VK_8, true);
  public static final Key d9 = new Key(KeyEvent.VK_9, true);
  public static final Key SLASH = new Key(KeyEvent.VK_SLASH, true);
  public static final Key BACKSLASH = new Key(KeyEvent.VK_BACK_SLASH, true);
  public static final Key EQUALS = new Key(KeyEvent.VK_EQUALS, true);
  public static final Key ASTERISK = new Key(KeyEvent.VK_ASTERISK, true);
  public static final Key DOLLAR = new Key(KeyEvent.VK_DOLLAR, true);

  public static final Key ENTER = new Key(KeyEvent.VK_ENTER, false);
  public static final Key DELETE = new Key(KeyEvent.VK_DELETE, false);
  public static final Key UP = new Key(KeyEvent.VK_UP, false);
  public static final Key DOWN = new Key(KeyEvent.VK_DOWN, false);
  public static final Key LEFT = new Key(KeyEvent.VK_LEFT, false);
  public static final Key RIGHT = new Key(KeyEvent.VK_RIGHT, false);
  public static final Key PAGE_DOWN = new Key(KeyEvent.VK_PAGE_DOWN, false);
  public static final Key PAGE_UP = new Key(KeyEvent.VK_PAGE_UP, false);
  public static final Key CONTROL = new Key(KeyEvent.VK_CONTROL, false);
  public static final Key SHIFT = new Key(KeyEvent.VK_SHIFT, false);
  public static final Key ALT = new Key(KeyEvent.VK_ALT, false);

  private int code;
  private Modifier modifier;
  private boolean printable;

  private Key(int code, boolean printable) {
    this(code, Modifier.NONE, printable);
  }

  private Key(int code, Modifier modifier, boolean printable) {
    this.code = code;
    this.modifier = modifier;
    this.printable = printable;
  }

  public static Key control(Key key) {
    return new Key(key.getCode(), Modifier.CONTROL, false);
  }

  public static Key alt(Key key) {
    return new Key(key.getCode(), Modifier.ALT, false);
  }

  public static Key shift(Key key) {
    return new Key(key.getCode(), Modifier.SHIFT, false);
  }

  public int getCode() {
    return code;
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
