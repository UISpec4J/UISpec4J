package samples.addressbook.model;

import samples.addressbook.model.events.EventHandler;
import samples.addressbook.model.exceptions.NameAlreadyInUseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AddressBook {
  private List contacts = new ArrayList();
  private Category rootCategory = Category.createRoot();
  private EventHandler eventHandler = new EventHandler();

  public EventHandler getEventHandler() {
    return eventHandler;
  }

  public List getContacts() {
    return Collections.unmodifiableList(contacts);
  }

  public List getContacts(Category category) {
    if (rootCategory.equals(category)) {
      return getContacts();
    }
    List result = new ArrayList();
    for (Iterator iterator = contacts.iterator(); iterator.hasNext();) {
      Contact contact = (Contact)iterator.next();
      Category[] categories = contact.getCategories();
      for (int i = 0; i < categories.length; i++) {
        if (category.isSameOrAncestorOf(categories[i])) {
          result.add(contact);
        }
      }
    }
    return result;
  }

  public Contact createContact() {
    Contact newContact = new Contact();
    contacts.add(newContact);
    eventHandler.notifyContactCreation(newContact);
    return newContact;
  }

  public Contact createContact(Category category) {
    Contact newContact = createContact();
    addCategory(newContact, category);
    return newContact;
  }

  public void removeContact(Contact contact) {
    contacts.remove(contact);
    eventHandler.notifyContactDeletion(contact);
  }

  public void changeContact(Contact contact, Contact.Field field, String value) {
    contact.setValue(field, value);
    eventHandler.notifyContactChange(contact, field, value);
  }

  public void reset() {
    contacts.clear();
    rootCategory.deleteAllChildren();
    eventHandler.notifyBookCleared();
  }

  public Category getRootCategory() {
    return rootCategory;
  }

  public Category createCategory(Category parent, String name) throws NameAlreadyInUseException {
    Category newCategory = Category.createChild(parent, name);
    eventHandler.notifyCategoryCreation(newCategory);
    return newCategory;
  }

  public void addCategory(Contact contact, Category category) {
    if (category.equals(getRootCategory())) {
      return;
    }
    Category[] categories = contact.getCategories();
    for (int i = 0; i < categories.length; i++) {
      if (category.isSameOrAncestorOf(categories[i])) {
        contact.removeCategory(categories[i]);
      }
    }
    contact.addCategory(category);
    eventHandler.notifyCategoriesChange(contact, contact.getCategories());
  }
}
