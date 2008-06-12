package samples.addressbook.model.events;

import samples.addressbook.model.Contact;

public interface ContactDeletionListener {
  void contactDeleted(Contact contact);
}
