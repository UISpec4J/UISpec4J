package org.uispec4j;

import javax.swing.JButton;

import org.junit.Test;
import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.xml.XmlAssert;

public class ButtonTest extends ButtonTestCase {

  private JButton jButton = new JButton();
  private Button button;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		button = (Button) UIComponentFactory.createUIComponent(jButton);
	}

  protected AbstractButton getButton() {
    return button;
  }

  protected javax.swing.AbstractButton getSwingButton() {
    return jButton;
  }

  @Test
  public void testGetComponentTypeName() throws Exception {
    assertEquals("button", button.getDescriptionTypeName());
  }

  @Test
  public void testGetDescription() throws Exception {
    XmlAssert.assertEquivalent("<button/>", button.getDescription());
    jButton.setText("toto");
    XmlAssert.assertEquivalent("<button label='toto'/>", button.getDescription());
  }

  @Test
  public void testFactory() throws Exception {
    checkFactory(new JButton(), Button.class);
  }
}
