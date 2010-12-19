package samples.addressbook.main;

import samples.addressbook.gui.MainWindow;
import samples.addressbook.model.AddressBook;
import samples.utils.SampleUtils;

import java.util.Locale;

public class Main {
  public static void main(String... args) throws Exception {
    Locale.setDefault(Locale.US);
    AddressBook book = new AddressBook();
    MainWindow window = new MainWindow(book);
    SampleUtils.show(window.getFrame());
  }
}
