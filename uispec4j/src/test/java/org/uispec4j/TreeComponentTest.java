package org.uispec4j;

import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.xml.XmlAssert;

import javax.swing.*;

public class TreeComponentTest extends UIComponentTestCase {
  public void testGetComponentTypeName() throws Exception {
    assertEquals("tree", UIComponentFactory.createUIComponent(new JTree()).getDescriptionTypeName());
  }

  public void testGetDescription() throws Exception {
    JTree jTree = new JTree();
    jTree.setName("myTree");
    Tree tree = new Tree(jTree);
    XmlAssert.assertEquivalent("<tree name='myTree'/>", tree.getDescription());
  }

  public void testFactory() throws Exception {
    checkFactory(new JButton(), Button.class);
  }

  protected UIComponent createComponent() {
    return new Tree(new JTree());
  }

}
