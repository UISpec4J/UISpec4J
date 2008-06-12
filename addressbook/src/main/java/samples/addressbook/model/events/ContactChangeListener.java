package samples.addressbook.model.events;

import samples.addressbook.model.Contact;

public interface ContactChangeListener {
  void contactChanged(Contact contact, Contact.Field field, String newValue);
}
