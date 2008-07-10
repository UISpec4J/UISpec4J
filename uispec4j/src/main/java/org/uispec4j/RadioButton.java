package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;

import javax.swing.*;

/**
 * Wrapper for JRadioButton components.
 */
public class RadioButton extends AbstractButton {
  public static final String TYPE_NAME = "radioButton";
  public static final Class[] SWING_CLASSES = {JRadioButton.class};

  private JRadioButton jRadioButton;

  public RadioButton(JRadioButton radioButton) {
    super(radioButton);
    this.jRadioButton = radioButton;
  }

  public JRadioButton getAwtComponent() {
    return jRadioButton;
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  public Assertion isSelected() {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertTrue(jRadioButton.isSelected());
      }
    };
  }
}
