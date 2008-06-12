package samples.addressbook.gui.actions;

import samples.addressbook.gui.selection.CategorySelectionListener;
import samples.addressbook.gui.selection.SelectionHandler;
import samples.addressbook.model.AddressBook;
import samples.addressbook.model.Category;
import samples.addressbook.model.exceptions.NameAlreadyInUseException;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CreateCategoryAction
  extends AbstractAction implements CategorySelectionListener {
  public static final String INPUT_MESSAGE = "Category name:";

  private AddressBook book;
  private Category selectedCategory;

  public CreateCategoryAction(AddressBook book, SelectionHandler handler) {
    super("New category");
    this.book = book;
    handler.addCategorySelectionListener(this);
    setEnabled(false);
  }

  public void categorySelected(Category category) {
    this.selectedCategory = category;
    setEnabled(selectedCategory != null);
  }

  public void actionPerformed(ActionEvent e) {
    String name = JOptionPane.showInputDialog(null, INPUT_MESSAGE, "Create a category",
                                              JOptionPane.PLAIN_MESSAGE);
    if (name == null) {
      return;
    }
    try {
      book.createCategory(selectedCategory, name);
    }
    catch (NameAlreadyInUseException e1) {
      JOptionPane.showMessageDialog(null,
                                    "Category '" + name + "' already exists",
                                    "Category creation failed",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }
}
