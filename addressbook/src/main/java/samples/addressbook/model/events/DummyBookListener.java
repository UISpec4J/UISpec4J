package samples.addressbook.model.events;

import org.uispec4j.xml.EventLogger;
import samples.addressbook.model.AddressBook;

public class DummyBookListener
  extends EventLogger
  implements BookResetListener {
  public static DummyBookListener register(AddressBook book) {
    DummyBookListener listener = new DummyBookListener();
    book.getEventHandler().addBookResetListener(listener);
    return listener;
  }

  public void bookReset() {
    log("bookReset");
  }
}
