package samples.addressbook.model.events;

import org.uispec4j.xml.EventLogger;
import samples.addressbook.model.AddressBook;
import samples.addressbook.model.Category;

public class DummyCategoryListener
  extends EventLogger
  implements CategoryCreationListener {

  public static DummyCategoryListener register(AddressBook book) {
    DummyCategoryListener listener = new DummyCategoryListener();
    EventHandler handler = book.getEventHandler();
    handler.addCategoryCreationListener(listener);
    return listener;
  }

  public void categoryCreated(Category category) {
    log("create").add("category", category.getPath());
  }

}
