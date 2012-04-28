package org.uispec4j.interception.toolkit.empty;

import java.awt.Font;
import java.awt.FontMetrics;

class DummyFontMetrics extends FontMetrics {
  public static final int[] WIDTHS = new int[256];

  public DummyFontMetrics(Font font) {
    super(font);
  }

  public int[] getWidths() {
    return WIDTHS;
  }

  public int stringWidth(String str) {
    return 0;
  }
}