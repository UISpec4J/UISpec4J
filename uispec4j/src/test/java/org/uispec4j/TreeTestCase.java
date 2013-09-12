package org.uispec4j;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.uispec4j.utils.DummyTreeCellRenderer;
import org.uispec4j.utils.UnitTestCase;
import org.uispec4j.xml.EventLogger;

public abstract class TreeTestCase extends UnitTestCase {
  protected JTree jTree;
  protected Tree tree;
  protected DefaultMutableTreeNode rootNode;
  protected DefaultMutableTreeNode child1Node;
  protected DefaultMutableTreeNode child1_1Node;
  protected DefaultMutableTreeNode child2Node;
  protected DummyTreeCellRenderer.UserObject root =
    new DummyTreeCellRenderer.UserObject("root");
  protected DummyTreeCellRenderer.UserObject child1 =
    new DummyTreeCellRenderer.UserObject("child1");
  protected DummyTreeCellRenderer.UserObject child1_1 =
    new DummyTreeCellRenderer.UserObject("child1_1");
  protected DummyTreeCellRenderer.UserObject child2 =
    new DummyTreeCellRenderer.UserObject("child2");

  @Override
protected void setUp() throws Exception {
    super.setUp();
    rootNode = new DefaultMutableTreeNode(root);
    child1Node = new DefaultMutableTreeNode(child1);
    child1_1Node = new DefaultMutableTreeNode(child1_1);
    child2Node = new DefaultMutableTreeNode(child2);

    rootNode.add(child1Node);
    child1Node.add(child1_1Node);
    rootNode.add(child2Node);
    initTree();
  }

  protected void initTree() {
    jTree = new JTree(rootNode);
    jTree.setCellRenderer(new DummyTreeCellRenderer(jTree));
    jTree.setName("myTree");
    tree = new Tree(jTree);
  }

  protected void assertNoSelection(final JTree jTree) {
    final String property = System.getProperty("java.specification.version");
    if (Arrays.asList("1.7", "1.8").contains(property)) {
      assertEquals(0, jTree.getSelectionRows().length);
      return;
    }
    assertNull(jTree.getSelectionRows());
  }

  protected static class DummyTreeCellValueConverter extends EventLogger implements TreeCellValueConverter {
    private String boldPattern;
    private String redFontPattern;

    public String getValue(final Component renderedComponent, final Object modelObject) {
      log("getValue")
        .add("component", renderedComponent.getClass().getName())
        .add("object", modelObject.toString());
      return '_' + modelObject.toString() + '_';
    }

    public void setRedFontPattern(final String redFontPattern) {
      this.redFontPattern = redFontPattern;
    }

    public Color getForeground(final Component renderedComponent, final Object modelObject) {
      if ((redFontPattern != null) &&
          (modelObject.toString().indexOf(redFontPattern) >= 0)) {
        return Color.RED;
      }

      return Color.BLACK;
    }

    public void setBoldPattern(final String boldPattern) {
      this.boldPattern = boldPattern;
    }

    public boolean isBold(final Component renderedComponent, final Object modelObject) {
      if (boldPattern == null) {
        return false;
      }
      return modelObject.toString().indexOf(boldPattern) >= 0;
    }
  }
}
