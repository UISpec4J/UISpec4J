package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;

import javax.swing.*;

/**
 * Wrapper for JToggleButton components.
 */
public class ToggleButton extends AbstractButton {
  public static final String TYPE_NAME = "toggleButton";
  public static final Class[] SWING_CLASSES = {JToggleButton.class};

  private JToggleButton jToggleButton;

  public ToggleButton(JToggleButton jToggleButton) {
    super(jToggleButton);
    this.jToggleButton = jToggleButton;
  }

  public JToggleButton getAwtComponent() {
    return jToggleButton;
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  public Assertion isSelected() {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertTrue(jToggleButton.isSelected());
      }
    };
  }

  public void select() {
    jToggleButton.setSelected(true);
  }

  public void unselect() {
    jToggleButton.setSelected(false);
  }
}
