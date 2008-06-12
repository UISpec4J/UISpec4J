package samples.addressbook.model;

import samples.addressbook.model.exceptions.NameAlreadyInUseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Category {
  public static final String ROOT_NAME = "All";

  private String name;
  private List children = new ArrayList();
  private Category parent;

  static Category createRoot() {
    return new Category(null, ROOT_NAME);
  }

  static Category createChild(Category parent, String name) throws NameAlreadyInUseException {
    Category child = new Category(parent, name);
    parent.addChild(child);
    return child;
  }

  private Category(Category parent, String name) {
    this.parent = parent;
    this.name = name;
  }

  public void deleteAllChildren() {
    children.clear();
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    StringBuffer path = new StringBuffer();
    computePath(this, parent, path);
    return path.toString();
  }

  private static void computePath(Category category, Category parent, StringBuffer path) {
    if (parent != null) {
      computePath(parent, parent.getParent(), path);
      path.append('/');
    }

    path.append(category.getName());
  }

  public Category getParent() {
    return parent;
  }

  public List getChildren() {
    return Collections.unmodifiableList(children);
  }

  public boolean isSameOrAncestorOf(Category category) {
    for (Category ancestor = category; ancestor != null; ancestor = ancestor.getParent()) {
      if (ancestor == this) {
        return true;
      }
    }
    return false;
  }

  public String toString() {
    return getName();
  }

  private void addChild(Category category) throws NameAlreadyInUseException {
    checkNameIsNotUsed(category.getName());
    children.add(category);
  }

  private void checkNameIsNotUsed(String name) throws NameAlreadyInUseException {
    for (Iterator iterator = children.iterator(); iterator.hasNext();) {
      Category child = (Category)iterator.next();
      if (name.equalsIgnoreCase(child.getName())) {
        throw new NameAlreadyInUseException();
      }
    }
  }
}
