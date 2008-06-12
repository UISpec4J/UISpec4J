package samples.addressbook.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Contact {
  public static class Field {

    public static final Field FIRST_NAME = new Field("First name");
    public static final Field LAST_NAME = new Field("Last name");
    public static final Field EMAIL = new Field("E-mail");
    public static final Field PHONE = new Field("Phone");
    public static final Field MOBILE = new Field("Mobile");
    public static final Field SECOND_EMAIL = new Field("Second e-mail");
    public static final Field URL = new Field("URL");
    public static final Field NOTES = new Field("Notes");

    private String name;

    private Field(String name) {
      this.name = name;
    }

    public String toString() {
      return name;
    }
  }

  private List categories = new ArrayList();

  public static final Field[] ALL_FIELDS = {
    Field.FIRST_NAME,
    Field.LAST_NAME,
    Field.EMAIL,
    Field.PHONE,
    Field.MOBILE,
    Field.SECOND_EMAIL,
    Field.URL,
    Field.NOTES
  };

  private Map fields = new HashMap();

  public String getValue(Field field) {
    return (String)fields.get(field);
  }

  void setValue(Field field, String value) {
    fields.put(field, value);
  }

  void addCategory(Category category) {
    categories.add(category);
  }

  void removeCategory(Category category) {
    categories.remove(category);
  }

  public Category[] getCategories() {
    return (Category[])categories.toArray(new Category[categories.size()]);
  }
}
