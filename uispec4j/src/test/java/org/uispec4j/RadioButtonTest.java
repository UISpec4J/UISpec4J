package org.uispec4j;

import org.junit.Test;
import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.xml.XmlAssert;
import javax.swing.JRadioButton;

public class RadioButtonTest extends ButtonTestCase {
  private RadioButton radioButton;
  private JRadioButton jRadioButton;

  public void setUp() throws Exception {
    super.setUp();
    jRadioButton = new JRadioButton("myRadioButton");
    radioButton = (RadioButton)UIComponentFactory.createUIComponent(jRadioButton);
  }

  @Override
  protected AbstractButton getButton() {
    return radioButton;
  }

  protected javax.swing.AbstractButton getSwingButton() {
    return jRadioButton;
  }

  @Test
  public void testGetComponentTypeName() throws Exception {
    assertEquals("radioButton", radioButton.getDescriptionTypeName());
  }

  @Test
  public void testGetDescription() throws Exception {
    XmlAssert.assertEquivalent("<radioButton label='myRadioButton'/>", radioButton.getDescription());
  }

  @Override
  @Test
  public void testFactory() throws Exception {
    checkFactory(jRadioButton, RadioButton.class);
  }

  @Test
  public void testIsActivated() throws Exception {
    assertFalse(radioButton.isSelected());
    jRadioButton.doClick();
    assertTrue(radioButton.isSelected());
    jRadioButton.doClick();
    assertFalse(radioButton.isSelected());
  }

  @Test
  public void testActivate() throws Exception {
    assertFalse(radioButton.isSelected());
    radioButton.click();
    assertTrue(jRadioButton.isSelected());
    assertTrue(radioButton.isSelected());
    radioButton.click();
    assertFalse(radioButton.isSelected());
    assertFalse(jRadioButton.isSelected());
  }
}