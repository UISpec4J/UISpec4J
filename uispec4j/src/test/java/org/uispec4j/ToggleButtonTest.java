package org.uispec4j;

import org.uispec4j.xml.XmlAssert;

import javax.swing.AbstractButton;
import javax.swing.*;

public class ToggleButtonTest extends ButtonTestCase {

  private JToggleButton jToggleButton = new JToggleButton();
  private ToggleButton toggle;

  protected void setUp() throws Exception {
    super.setUp();
    toggle = new ToggleButton(jToggleButton);
  }

  protected org.uispec4j.AbstractButton getButton() {
    return toggle;
  }

  protected AbstractButton getSwingButton() {
    return jToggleButton;
  }

  public void testGetComponentTypeName() throws Exception {
    assertEquals("toggleButton", toggle.getDescriptionTypeName());
  }

  public void testGetDescription() throws Exception {
    XmlAssert.assertEquivalent("<toggleButton/>", toggle.getDescription());
    jToggleButton.setText("toto");
    XmlAssert.assertEquivalent("<toggleButton label='toto'/>", toggle.getDescription());
  }

  public void testFactory() throws Exception {
    checkFactory(new JToggleButton(), ToggleButton.class);
  }

  public void testSelectionThroughClick() throws Exception {
    jToggleButton.setSelected(false);
    toggle.click();
    assertTrue(toggle.isSelected());
    toggle.click();
    assertFalse(toggle.isSelected());
  }
}
