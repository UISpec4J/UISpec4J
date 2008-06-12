package samples.addressbook.gui.selection;

import org.uispec4j.xml.EventLogger;
import samples.addressbook.model.Category;
import samples.addressbook.model.Contact;

public class DummySelectionListener
  extends EventLogger
  implements ContactSelectionListener, CategorySelectionListener {

  public static DummySelectionListener register(SelectionHandler handler) {
    DummySelectionListener listener = new DummySelectionListener();
    handler.addContactSelectionListener(listener);
    handler.addCategorySelectionListener(listener);
    return listener;
  }

  public void contactSelected(Contact contact) {
    if (contact != null) {
      log("contact")
        .add("firstName", contact.getValue(Contact.Field.FIRST_NAME))
        .add("lastName", contact.getValue(Contact.Field.LAST_NAME));
    }
    else {
      log("no_contact");
    }
  }

  public void categorySelected(Category category) {
    if (category != null) {
      log("category").add("path", category.getPath());
    }
    else {
      log("no_category");
    }
  }
}
