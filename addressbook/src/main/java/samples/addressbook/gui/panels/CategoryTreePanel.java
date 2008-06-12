package samples.addressbook.gui.panels;

import samples.addressbook.gui.actions.CreateCategoryAction;
import samples.addressbook.gui.selection.SelectionHandler;
import samples.addressbook.model.AddressBook;
import samples.addressbook.model.Category;
import samples.addressbook.model.events.CategoryCreationListener;
import samples.utils.GridBag;
import samples.utils.SampleUtils;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.Enumeration;

public class CategoryTreePanel implements CategoryCreationListener {
  private AddressBook book;
  private SelectionHandler selectionHandler;
  private DefaultMutableTreeNode root;
  private JTree tree;
  private DefaultTreeModel treeModel;
  private JButton createNewCategoryButton;

  public CategoryTreePanel(AddressBook book, SelectionHandler handler) {
    this.book = book;
    this.selectionHandler = handler;
    this.createNewCategoryButton = new JButton(new CreateCategoryAction(book, handler));
    createTree();
    book.getEventHandler().addCategoryCreationListener(this);
  }

  private void createTree() {
    root = new DefaultMutableTreeNode(book.getRootCategory());
    tree = new JTree(root) {
      public String convertValueToText(Object value, boolean selected, boolean expanded,
                                       boolean leaf, int row, boolean hasFocus) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        Category category = (Category)node.getUserObject();
        return category.getName();
      }
    };
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    treeModel = (DefaultTreeModel)tree.getModel();
    tree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        TreePath path = tree.getSelectionPath();
        Category category = null;
        if (path != null) {
          DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
          category = (Category)node.getUserObject();
        }
        selectionHandler.selectCategory(category);
      }
    });
    tree.setSelectionPath(new TreePath(root.getPath()));
  }

  public JPanel getPanel() {
    JPanel panel = new JPanel();
    GridBag gridBag = new GridBag(panel);
    gridBag.add(createNewCategoryButton,
                0, 0,
                1, 1, 0, 0,
                GridBagConstraints.NONE,
                GridBagConstraints.NORTHWEST,
                new Insets(0, 0, 0, 0));
    gridBag.add(SampleUtils.createPanelWithScroller(tree),
                0, 1,
                1, 1, 1, 1,
                GridBagConstraints.BOTH,
                GridBagConstraints.CENTER,
                new Insets(0, 0, 0, 0));
    return panel;
  }

  public void categoryCreated(Category category) {
    Category parent = category.getParent();
    DefaultMutableTreeNode parentNode = findNode(parent, root);
    int index = findIndexForInsertion(parentNode, category.getName());
    DefaultMutableTreeNode newCategory = new DefaultMutableTreeNode(category);
    treeModel.insertNodeInto(newCategory, parentNode, index);
    tree.scrollPathToVisible(new TreePath(newCategory.getPath()));
  }

  private static DefaultMutableTreeNode findNode(Category category, DefaultMutableTreeNode node) {
    if (category == null) {
      return null;
    }
    if (category.equals(node.getUserObject())) {
      return node;
    }
    for (Enumeration children = node.children(); children.hasMoreElements();) {
      DefaultMutableTreeNode child = findNode(category, (DefaultMutableTreeNode)children.nextElement());
      if (child != null) {
        return child;
      }
    }
    return null;
  }

  private int findIndexForInsertion(DefaultMutableTreeNode parent, String categoryName) {
    for (int i = 0, max = parent.getChildCount(); i < max; i++) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)parent.getChildAt(i);
      Category category = (Category)node.getUserObject();
      if (categoryName.compareTo(category.getName()) < 0) {
        return i;
      }
    }
    return parent.getChildCount();
  }
}
