package org.uispec4j;

import org.junit.Test;
import javax.swing.JPasswordField;

public class PasswordFieldTest extends UIComponentTestCase {

  private PasswordField passwordField;
  private JPasswordField jPasswordField;

  @Override
  public void setUp() throws Exception {
    jPasswordField = new JPasswordField();
    passwordField = new PasswordField(jPasswordField);
  }

  @Override
  @Test
  public void testGetComponentTypeName() throws Exception {
    assertEquals("passwordField", passwordField.getDescriptionTypeName());
  }

  @Override
  @Test
  public void testGetDescription() throws Exception {
    assertEquals("<passwordField/>", passwordField.getDescription());
  }

  @Override
  @Test
  public void testFactory() throws Exception {
    checkFactory(new JPasswordField(), PasswordField.class);
  }

  @Override
  protected UIComponent createComponent() {
    return passwordField;
  }

  @Test
  public void testPasswordEquals() throws Exception {
    jPasswordField.setText("pwd");
    assertTrue(passwordField.passwordEquals("pwd"));
    assertFalse(passwordField.passwordEquals("unknown"));
  }

  @Test
  public void testEnterPassword() throws Exception {
    passwordField.setPassword("pwd");
    assertEquals("pwd", new String(jPasswordField.getPassword()));
  }
}
