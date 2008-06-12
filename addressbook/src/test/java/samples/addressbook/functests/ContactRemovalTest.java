package samples.addressbook.functests;

import org.uispec4j.MenuItem;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.PopupMenuInterceptor;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

public class ContactRemovalTest extends AddressBookTestCase {

  protected void setUp() throws Exception {
    super.setUp();
    createContact("Smith", "John");
    createContact("Smith", "Maria");
  }

  public void testSimpleContactRemoval() throws Exception {
    deleteRow(0, true);
    assertThat(contactTable.contentEquals(new Object[][]{
      {"Smith", "Maria", "", "", ""}
    }));

    deleteRow(0, false);
    assertThat(contactTable.contentEquals(new Object[][]{
      {"Smith", "Maria", "", "", ""}
    }));
  }

  public void testActionIsDisabledWhenNoContactIsSelected() throws Exception {
    contactTable.clearSelection();
    assertFalse(getRemoveMenuItem().isEnabled());
  }

  private void deleteRow(int row, final boolean confirm) {
    contactTable.selectRow(row);
    MenuItem removeContactMenuItem = getRemoveMenuItem();
    WindowInterceptor.init(removeContactMenuItem.triggerClick())
      .process("Confirmation", new WindowHandler() {
        public Trigger process(Window window) throws Exception {
          assertThat(window.containsLabel("Are you sure you want to delete this contact?"));
          return window.getButton(confirm ? "Yes" : "No").triggerClick();
        }
      })
      .run();
  }

  private MenuItem getRemoveMenuItem() {
    return PopupMenuInterceptor.run(contactTable.triggerRightClick(0, 0)).getSubMenu("Remove");
  }
}
