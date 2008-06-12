package samples.addressbook.model.events;

import samples.addressbook.model.Contact;

public interface ContactCreationListener {
  void contactCreated(Contact contact);
}
