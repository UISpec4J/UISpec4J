package org.uispec4j;

import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.xml.XmlAssert;

import javax.swing.*;

public class CheckBoxTest extends ButtonTestCase {
  private CheckBox checkBox;
  private JCheckBox jCheckBox;

  protected void setUp() throws Exception {
    super.setUp();
    jCheckBox = new JCheckBox();
    jCheckBox.setName("myCheckBox");
    this.checkBox = (CheckBox)UIComponentFactory.createUIComponent(jCheckBox);
  }

  protected AbstractButton getButton() {
    return checkBox;
  }

  protected javax.swing.AbstractButton getSwingButton() {
    return jCheckBox;
  }

  public void testGetComponentTypeName() throws Exception {
    assertEquals("checkBox", checkBox.getDescriptionTypeName());
  }

  public void testGetDescription() throws Exception {
    XmlAssert.assertEquivalent("<checkBox name='myCheckBox'/>", checkBox.getDescription());
  }

  public void testFactory() throws Exception {
    checkFactory(new JCheckBox(), CheckBox.class);
  }

  public void testSelected() throws Exception {
    assertFalse(checkBox.isSelected());
    checkBox.select();
    assertTrue(checkBox.isSelected());
    checkBox.select();
    assertTrue(checkBox.isSelected());
    checkBox.unselect();
    assertFalse(checkBox.isSelected());
    checkBox.unselect();
    assertFalse(checkBox.isSelected());
  }
}