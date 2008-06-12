package samples.addressbook.gui.selection;

import samples.addressbook.model.Category;
import samples.addressbook.model.Contact;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SelectionHandler {
  private List contactSelectionListeners = new ArrayList();
  private List categorySelectionListeners = new ArrayList();

  public void addContactSelectionListener(ContactSelectionListener listener) {
    contactSelectionListeners.add(listener);
  }

  public void selectContact(Contact contact) {
    for (Iterator iterator = contactSelectionListeners.iterator(); iterator.hasNext();) {
      ContactSelectionListener listener = (ContactSelectionListener)iterator.next();
      listener.contactSelected(contact);
    }
  }

  public void addCategorySelectionListener(CategorySelectionListener listener) {
    categorySelectionListeners.add(listener);
  }

  public void selectCategory(Category category) {
    for (Iterator iterator = categorySelectionListeners.iterator(); iterator.hasNext();) {
      CategorySelectionListener listener = (CategorySelectionListener)iterator.next();
      listener.categorySelected(category);
    }
  }
}
