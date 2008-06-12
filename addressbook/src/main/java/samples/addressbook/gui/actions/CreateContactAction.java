package samples.addressbook.gui.actions;

import samples.addressbook.gui.selection.CategorySelectionListener;
import samples.addressbook.gui.selection.SelectionHandler;
import samples.addressbook.model.AddressBook;
import samples.addressbook.model.Category;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CreateContactAction extends AbstractAction implements CategorySelectionListener {
  private AddressBook addressBook;
  private Category selectedCategory;

  public CreateContactAction(AddressBook addressBook, SelectionHandler handler) {
    super("New contact");
    this.addressBook = addressBook;
    handler.addCategorySelectionListener(this);
  }

  public void categorySelected(Category category) {
    this.selectedCategory = category;
  }

  public void actionPerformed(ActionEvent e) {
    addressBook.createContact(selectedCategory == null ? addressBook.getRootCategory() : selectedCategory);
  }
}
