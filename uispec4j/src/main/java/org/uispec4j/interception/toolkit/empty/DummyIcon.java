package org.uispec4j.interception.toolkit.empty;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class DummyIcon implements Icon {

  public void paintIcon(Component c, Graphics g, int x, int y) {
  }

  public int getIconWidth() {
    return 0;
  }

  public int getIconHeight() {
    return 0;
  }
}