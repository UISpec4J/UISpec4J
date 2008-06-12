package org.uispec4j;

import junit.framework.AssertionFailedError;
import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.xml.XmlAssert;

import javax.swing.*;

public class MenuBarTest extends UIComponentTestCase {
  private MenuBar menuBar;
  private JMenuBar jMenuBar;
  private JMenu jFileMenu;
  private JMenu jEditMenu;

  protected void setUp() throws Exception {
    super.setUp();
    jMenuBar = new JMenuBar();
    jFileMenu = new JMenu("File");
    jFileMenu.setName("fileMenu");
    jMenuBar.add(jFileMenu);
    jEditMenu = new JMenu("Edit");
    jEditMenu.setName("editMenu");
    jMenuBar.add(jEditMenu);
    jMenuBar.setName("myMenuBar");
    menuBar = (MenuBar)UIComponentFactory.createUIComponent(jMenuBar);
  }

  public void testGetComponentTypeName() throws Exception {
    assertEquals("menuBar", menuBar.getDescriptionTypeName());
  }

  public void testGetDescription() throws Exception {
    XmlAssert.assertEquivalent("<menuBar name='myMenuBar'>" +
                               "  <menu name='fileMenu'/>" +
                               "  <menu name='editMenu'/>" +
                               "</menuBar>", menuBar.getDescription());
  }

  public void testFactory() throws Exception {
    checkFactory(new JMenuBar(), MenuBar.class);
  }

  protected UIComponent createComponent() {
    return menuBar;
  }

  public void testGetContents() throws Exception {
    assertTrue(menuBar.contentEquals("File", "Edit"));
  }

  public void testGetContentsError() throws Exception {
    try {
      assertTrue(menuBar.contentEquals("File", "Other"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
    }
  }

  public void testGetMenu() throws Exception {
    assertSame(jFileMenu, menuBar.getMenu("File").getAwtComponent());
    assertSame(jEditMenu, menuBar.getMenu("Edit").getAwtComponent());
  }
}