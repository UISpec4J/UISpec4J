package samples.addressbook.model.events;

import samples.addressbook.model.Category;
import samples.addressbook.model.Contact;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EventHandler {
  private List bookResetListeners = new ArrayList();
  private List contactCreationListeners = new ArrayList();
  private List contactDeletionListeners = new ArrayList();
  private List contactChangeListeners = new ArrayList();
  private List categoryCreationListeners = new ArrayList();
  private List contactCategoriesAssignmentListeners = new ArrayList();

  public void addBookResetListener(BookResetListener listener) {
    bookResetListeners.add(listener);
  }

  public void notifyBookReset() {
    for (Iterator iterator = bookResetListeners.iterator(); iterator.hasNext();) {
      BookResetListener listener = (BookResetListener)iterator.next();
      listener.bookReset();
    }
  }

  public void addContactCreationListener(ContactCreationListener listener) {
    contactCreationListeners.add(listener);
  }

  public void notifyContactCreation(Contact contact) {
    for (Iterator iterator = contactCreationListeners.iterator(); iterator.hasNext();) {
      ContactCreationListener listener = (ContactCreationListener)iterator.next();
      listener.contactCreated(contact);
    }
  }

  public void addContactDeletionListener(ContactDeletionListener listener) {
    contactDeletionListeners.add(listener);
  }

  public void notifyContactDeletion(Contact contact) {
    for (Iterator iterator = contactDeletionListeners.iterator(); iterator.hasNext();) {
      ContactDeletionListener listener = (ContactDeletionListener)iterator.next();
      listener.contactDeleted(contact);
    }
  }

  public void addContactChangeListener(ContactChangeListener listener) {
    contactChangeListeners.add(listener);
  }

  public void notifyContactChange(Contact contact, Contact.Field field, String value) {
    for (Iterator iterator = contactChangeListeners.iterator(); iterator.hasNext();) {
      ContactChangeListener listener = (ContactChangeListener)iterator.next();
      listener.contactChanged(contact, field, value);
    }
  }

  public void addCategoryCreationListener(CategoryCreationListener listener) {
    categoryCreationListeners.add(listener);
  }

  public void notifyCategoryCreation(Category category) {
    for (Iterator iterator = categoryCreationListeners.iterator(); iterator.hasNext();) {
      CategoryCreationListener listener = (CategoryCreationListener)iterator.next();
      listener.categoryCreated(category);
    }
  }

  public void addContactCategoriesAssignmentListener(ContactCategoriesAssignmentListener listener) {
    contactCategoriesAssignmentListeners.add(listener);
  }

  public void notifyCategoriesChange(Contact contact, Category[] categories) {
    for (Iterator iterator = contactCategoriesAssignmentListeners.iterator(); iterator.hasNext();) {
      ContactCategoriesAssignmentListener listener = (ContactCategoriesAssignmentListener)iterator.next();
      listener.categoriesAssigned(contact, categories);
    }
  }

  public void notifyBookCleared() {
    for (Iterator iterator = bookResetListeners.iterator(); iterator.hasNext();) {
      BookResetListener listener = (BookResetListener)iterator.next();
      listener.bookReset();
    }

  }
}
