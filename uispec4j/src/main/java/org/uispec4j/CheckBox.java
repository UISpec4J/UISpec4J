package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;

import javax.swing.*;

/**
 * Wrapper for JCheckBox components.
 */
public class CheckBox extends AbstractButton {
  public static final String TYPE_NAME = "checkBox";
  public static final Class[] SWING_CLASSES = {JCheckBox.class};

  private JCheckBox jCheckBox;

  public CheckBox(JCheckBox checkBox) {
    super(checkBox);
    this.jCheckBox = checkBox;
  }

  public JCheckBox getAwtComponent() {
    return jCheckBox;
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  public void select() {
    if (!jCheckBox.isSelected()) {
      click();
    }
  }

  public void unselect() {
    if (jCheckBox.isSelected()) {
      click();
    }
  }

  public Assertion isSelected() {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertTrue(jCheckBox.isSelected());
      }
    };
  }
}
