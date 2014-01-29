package org.uispec4j;

import org.junit.Test;
import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.xml.XmlAssert;
import javax.swing.JButton;
import javax.swing.JTree;

public class TreeComponentTest extends UIComponentTestCase {
  @Override
  @Test
  public void testGetComponentTypeName() throws Exception {
    assertEquals("tree", UIComponentFactory.createUIComponent(new JTree()).getDescriptionTypeName());
  }

  @Override
  @Test
  public void testGetDescription() throws Exception {
    JTree jTree = new JTree();
    jTree.setName("myTree");
    Tree tree = new Tree(jTree);
    XmlAssert.assertEquivalent("<tree name='myTree'/>", tree.getDescription());
  }

  @Override
  @Test
  public void testFactory() throws Exception {
    checkFactory(new JButton(), Button.class);
  }

  protected UIComponent createComponent() {
    return new Tree(new JTree());
  }

}
