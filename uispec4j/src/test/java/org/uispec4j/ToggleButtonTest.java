package org.uispec4j;

import org.junit.Test;
import org.uispec4j.xml.XmlAssert;
import javax.swing.AbstractButton;
import javax.swing.JToggleButton;

public class ToggleButtonTest extends ButtonTestCase {

  private JToggleButton jToggleButton = new JToggleButton();
  private ToggleButton toggle;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    toggle = new ToggleButton(jToggleButton);
  }

  protected org.uispec4j.AbstractButton getButton() {
    return toggle;
  }

  protected AbstractButton getSwingButton() {
    return jToggleButton;
  }

  @Override
  @Test
  public void testGetComponentTypeName() throws Exception {
    assertEquals("toggleButton", toggle.getDescriptionTypeName());
  }

  @Override
  @Test
  public void testGetDescription() throws Exception {
    XmlAssert.assertEquivalent("<toggleButton/>", toggle.getDescription());
    jToggleButton.setText("toto");
    XmlAssert.assertEquivalent("<toggleButton label='toto'/>", toggle.getDescription());
  }

  @Override
  @Test
  public void testFactory() throws Exception {
    checkFactory(new JToggleButton(), ToggleButton.class);
  }

  @Test
  public void testSelectionThroughClick() throws Exception {
    jToggleButton.setSelected(false);
    toggle.click();
    assertTrue(toggle.isSelected());
    toggle.click();
    assertFalse(toggle.isSelected());
  }

  @Test
  public void testSelectAndUnselect() throws Exception {

    toggle.select();
    assertTrue(toggle.isSelected());

    toggle.select();
    assertTrue(toggle.isSelected());

    toggle.unselect();
    assertFalse(toggle.isSelected());

    toggle.unselect();
    assertFalse(toggle.isSelected());

    toggle.select();
    assertTrue(toggle.isSelected());
  }
}
