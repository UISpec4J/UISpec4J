package samples.addressbook.model.events;

import org.uispec4j.utils.ArrayUtils;
import org.uispec4j.xml.EventLogger;
import samples.addressbook.model.AddressBook;
import samples.addressbook.model.Category;
import samples.addressbook.model.Contact;

public class DummyContactListener
  extends EventLogger
  implements ContactChangeListener,
             ContactCreationListener,
             ContactDeletionListener,
             ContactCategoriesAssignmentListener {

  public static DummyContactListener register(AddressBook book) {
    DummyContactListener listener = new DummyContactListener();
    EventHandler handler = book.getEventHandler();
    handler.addContactChangeListener(listener);
    handler.addContactCreationListener(listener);
    handler.addContactDeletionListener(listener);
    handler.addContactCategoriesAssignmentListener(listener);
    return listener;
  }

  private DummyContactListener() {
  }

  public void contactChanged(Contact contact, Contact.Field field, String newValue) {
    log("change").add("field", field.toString()).add("value", newValue);
  }

  public void contactCreated(Contact contact) {
    log("create");
  }

  public void contactDeleted(Contact contact) {
    log("delete").add("name", contact.getValue(Contact.Field.LAST_NAME));
  }

  public void categoriesAssigned(Contact contact, Category[] categories) {
    log("categoriesChange")
      .add("firstName", contact.getValue(Contact.Field.FIRST_NAME))
      .add("categories", ArrayUtils.toString(categories));
  }
}
