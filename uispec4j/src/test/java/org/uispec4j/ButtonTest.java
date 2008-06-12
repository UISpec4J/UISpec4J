package org.uispec4j;

import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.xml.XmlAssert;

import javax.swing.*;

public class ButtonTest extends ButtonTestCase {

  private JButton jButton = new JButton();
  private Button button;

  protected void setUp() throws Exception {
    super.setUp();
    button = (Button)UIComponentFactory.createUIComponent(jButton);
  }

  protected AbstractButton getButton() {
    return button;
  }

  protected javax.swing.AbstractButton getSwingButton() {
    return jButton;
  }

  public void testGetComponentTypeName() throws Exception {
    assertEquals("button", button.getDescriptionTypeName());
  }

  public void testGetDescription() throws Exception {
    XmlAssert.assertEquivalent("<button/>", button.getDescription());
    jButton.setText("toto");
    XmlAssert.assertEquivalent("<button label='toto'/>", button.getDescription());
  }

  public void testFactory() throws Exception {
    checkFactory(new JButton(), Button.class);
  }
}
