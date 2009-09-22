package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;

import javax.swing.*;

/**
 * Wrapper for JPasswordField components.
 */
public class PasswordField extends AbstractUIComponent {
  public static final String TYPE_NAME = "passwordField";
  public static final Class[] SWING_CLASSES = {JPasswordField.class};

  private JPasswordField jPasswordField;

  public PasswordField(JPasswordField passwordField) {
    this.jPasswordField = passwordField;
  }

  public JPasswordField getAwtComponent() {
    return jPasswordField;
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  public Assertion passwordEquals(final String hiddenPassword) {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertEquals(hiddenPassword, new String(jPasswordField.getPassword()));
      }
    };
  }

  public void setPassword(String passsword) {
    jPasswordField.setText(passsword);
  }
}
