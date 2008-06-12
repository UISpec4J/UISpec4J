package samples.addressbook.model.events;

import samples.addressbook.model.Category;

public interface CategoryCreationListener {
  void categoryCreated(Category category);
}
