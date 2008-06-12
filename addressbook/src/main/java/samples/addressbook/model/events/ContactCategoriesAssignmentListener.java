package samples.addressbook.model.events;

import samples.addressbook.model.Category;
import samples.addressbook.model.Contact;

public interface ContactCategoriesAssignmentListener {
  public void categoriesAssigned(Contact contact, Category[] categories);
}
