package org.uispec4j.extension;

import org.uispec4j.Button;

public class DerivedCountingButton extends Button {
  public static final String TYPE_NAME = "countingButton";
  public static final Class[] SWING_CLASSES = {JCountingButton.class};

  JCountingButton jButton;

  public DerivedCountingButton(JCountingButton jButton) {
    super(jButton);
    this.jButton = jButton;
  }

  public JCountingButton getAwtComponent() {
    return jButton;
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  public void click() {
    jButton.doClick();
  }
}
