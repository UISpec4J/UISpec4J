package org.uispec4j.utils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class DummyTreeCellRenderer extends DefaultTreeCellRenderer {
  private final Font normalFont;
  private final Font boldFont;

  public DummyTreeCellRenderer(JTree tree) {
    this.normalFont = tree.getFont();
    this.boldFont = normalFont.deriveFont(Font.BOLD);
  }

  public final Component getTreeCellRendererComponent(JTree tree,
                                                      Object modelObject,
                                                      boolean selected,
                                                      boolean expanded,
                                                      boolean leaf, int row,
                                                      boolean hasFocus) {
    super.getTreeCellRendererComponent(tree, "", selected, expanded, leaf, row, hasFocus);
    setText((modelObject == null ? "null" : getUserObject(modelObject).text));
    setFont(getUserObject(modelObject).isBold ? boldFont : normalFont);
    if (selected) {
      setForeground(textSelectionColor);
    }
    else {
      setForeground(getUserObject(modelObject).color);
    }
    return this;
  }

  private UserObject getUserObject(Object modelObject) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)modelObject;
    return (UserObject)node.getUserObject();
  }

  public static class UserObject {
    String text;
    boolean isBold;
    Color color = Color.BLACK;

    public UserObject(String text) {
      this.text = text;
    }

    public String toString() {
      return "obj:" + text;
    }

    public void setBold(boolean isBold) {
      this.isBold = isBold;
    }

    public void setColor(Color color) {
      this.color = color;
    }
  }
}
