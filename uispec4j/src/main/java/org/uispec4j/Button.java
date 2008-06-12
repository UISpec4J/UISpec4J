package org.uispec4j;

import javax.swing.*;

/**
 * Wrapper for JButton components.
 */
public class Button extends AbstractButton {
  public static final String TYPE_NAME = "button";
  public static final Class[] SWING_CLASSES = {JButton.class};

  private JButton jButton;

  public Button(JButton jButton) {
    super(jButton);
    this.jButton = jButton;
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  public JButton getAwtComponent() {
    return jButton;
  }
}
