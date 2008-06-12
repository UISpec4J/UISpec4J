package org.uispec4j.interception.ui;

import org.uispec4j.interception.toolkit.Empty;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import java.awt.*;

///CLOVER:OFF

public class UISpecTreeUI extends BasicTreeUI {
  public static ComponentUI createUI(JComponent component) {
    return new UISpecTreeUI();
  }

  public void paint(Graphics g, JComponent c) {
  }

  public Dimension getPreferredSize(JComponent c) {
    return Empty.NULL_DIMENSION;
  }

  public Dimension getMaximumSize(JComponent c) {
    return Empty.NULL_DIMENSION;
  }

  public Dimension getMinimumSize(JComponent c) {
    return Empty.NULL_DIMENSION;
  }
}
