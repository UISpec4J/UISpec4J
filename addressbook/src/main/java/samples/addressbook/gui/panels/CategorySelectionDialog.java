package samples.addressbook.gui.panels;

import samples.addressbook.model.AddressBook;
import samples.addressbook.model.Category;
import samples.utils.GridBag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class CategorySelectionDialog {
  private JList jList;
  private JDialog dialog;
  private List result;

  public CategorySelectionDialog(AddressBook book, Frame parentFrame) {
    Category[] categoriesArray = getCategories(book.getRootCategory());
    createJList(categoriesArray);
    createDialog(parentFrame);
  }

  private void createJList(Category[] categoriesArray) {
    jList = new JList(categoriesArray);
    jList.setCellRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
        Category category = (Category)value;
        return super.getListCellRendererComponent(list, category.getPath(), index,
                                                  isSelected, cellHasFocus);
      }
    });
  }

  private Category[] getCategories(Category category) {
    List result = new ArrayList();
    addCategories(category, result);
    Collections.sort(result, new Comparator() {
      public int compare(Object o1, Object o2) {
        Category cat1 = (Category)o1;
        Category cat2 = (Category)o2;
        return cat1.getPath().compareTo(cat2.getPath());
      }
    });
    return (Category[])result.toArray(new Category[result.size()]);
  }

  private void addCategories(Category category, List result) {
    for (Iterator iterator = category.getChildren().iterator(); iterator.hasNext();) {
      Category subCategory = (Category)iterator.next();
      result.add(subCategory);
      addCategories(subCategory, result);
    }
  }

  private class OkAction extends AbstractAction {
    private OkAction() {
      super("OK");
    }

    public void actionPerformed(ActionEvent e) {
      result = Arrays.asList(jList.getSelectedValues());
      dialog.dispose();
    }
  }

  private class CancelAction extends AbstractAction {
    private CancelAction() {
      super("Cancel");
    }

    public void actionPerformed(ActionEvent e) {
      dialog.dispose();
    }
  }

  private void createDialog(Frame parentFrame) {
    dialog = new JDialog(parentFrame);
    GridBag gridBag = new GridBag(dialog.getContentPane());
    gridBag.add(jList,
                0, 0, 3, 1, 1, 1,
                GridBagConstraints.BOTH,
                GridBagConstraints.CENTER,
                new Insets(10, 10, 10, 10));
    gridBag.add(new JButton(new OkAction()),
                1, 1, 1, 1, 0, 0,
                GridBagConstraints.NONE,
                GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
    gridBag.add(new JButton(new CancelAction()),
                2, 1, 1, 1, 0, 0,
                GridBagConstraints.NONE,
                GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
  }

  public JDialog getDialog() {
    return dialog;
  }

  public Category[] getResult() {
    if ((result == null) || result.isEmpty()) {
      return null;
    }
    return (Category[])result.toArray(new Category[result.size()]);
  }
}
