package samples.addressbook.functests;

import org.uispec4j.DefaultTableCellValueConverter;
import org.uispec4j.utils.ColorUtils;

import java.awt.*;

public class ContactCreationTest extends AddressBookTestCase {

  public void testTableStateAtStartup() throws Exception {
    assertThat(contactTable.isEmpty());
    assertThat(contactTable.getHeader().contentEquals("First name",
                                                      "Last name",
                                                      "E-mail",
                                                      "Phone",
                                                      "Mobile"));
  }

  public void testCreatingAContact() throws Exception {
    newContactButton.click();
    assertThat(contactTable.contentEquals(new String[][]{
      {"", "", "", "", ""}
    }));
    assertThat(contactTable.rowIsSelected(0));
    changeFields("John", "Smith",
                 "john.smith@uispec4j.org",
                 "01.02.03.04.05", "06.07.08.09.10");
    assertThat(contactTable.contentEquals(new String[][]{
      {"John", "Smith", "john.smith@uispec4j.org", "01.02.03.04.05", "06.07.08.09.10"}
    }));
  }

  public void testTextFieldsAreDisabledWhenNoContactIsSelected() throws Exception {
    assertTextFieldsEditable(false);
    newContactButton.click();
    assertTextFieldsEditable(true);
    contactTable.clearSelection();
    assertTextFieldsEditable(false);
  }

  public void testTheTableIsEditable() throws Exception {
    createContact("Homer", "Simpson",
                  "homer@simpsons.org",
                  "01.02.03.04.05", "06.07.08.09.10");
    assertThat(contactTable.isEditable(new boolean[][]{
      {true, true, true, true, true}
    }));
  }

  public void testSwitchingBetweenContacts() throws Exception {
    createContact("Homer", "Simpson",
                  "homer@simpsons.org",
                  "01.02.03.04.05", "06.07.08.09.10");
    createContact("Bart", "Simpson",
                  "bart@simpsons.org",
                  "01.02.03.04.05", "06.07.08.09.10");

    contactTable.selectRow(0);
    checkFields("Homer", "Simpson",
                "homer@simpsons.org",
                "01.02.03.04.05", "06.07.08.09.10");

    contactTable.clearSelection();
    checkFields("", "", "", "", "");
  }

  public void testApplyUpdatesTheFieldsWhenEnterWasNotPressed() throws Exception {
    createContact("Homer", "Simpson",
                  "homer@simpsons.org",
                  "01.02.03.04.05", "06.07.08.09.10");

    firstNameField.insertText("2", 5);
    assertThat(contactTable.contentEquals(new String[][]{
      {"Homer", "Simpson", "homer@simpsons.org", "01.02.03.04.05", "06.07.08.09.10"}
    }));

    applyButton.click();
    assertThat(contactTable.contentEquals(new String[][]{
      {"Homer2", "Simpson", "homer@simpsons.org", "01.02.03.04.05", "06.07.08.09.10"}
    }));
  }

  public void testPhoneCellsAreRedWhenNotDefined() throws Exception {
    contactTable.setDefaultCellValueConverter(new DefaultTableCellValueConverter() {
      public Object getValue(int row, int column, Component renderedComponent, Object modelObject) {
        Object value = super.getValue(row, column, renderedComponent, modelObject);
        return (ColorUtils.equals("red", renderedComponent.getBackground()) ? "#RED" : value);
      }
    });

    newContactButton.click();
    assertThat(contactTable.contentEquals(new String[][]{
      {"#RED", "#RED", "", "", ""}
    }));

    contactTable.selectRow(0);
    firstNameField.setText("Paula");
    assertThat(contactTable.contentEquals(new String[][]{
      {"Paula", "#RED", "", "", ""}
    }));

    createContact("Homer", "Simpson", "homer@s.com", "0001122", "1234");
    createContact("", "Simpson", "bart@s.com", "", "1234");

    assertThat(contactTable.contentEquals(new String[][]{
      {"Paula", "#RED", "", "", ""},
      {"Homer", "Simpson", "homer@s.com", "0001122", "1234"},
      {"#RED", "Simpson", "bart@s.com", "", "1234"}
    }));
  }

  private void checkFields(String firstName, String lastName, String email, String phone, String mobile) {
    assertThat(firstNameField.textEquals(firstName));
    assertThat(lastNameField.textEquals(lastName));
    assertThat(emailField.textEquals(email));
    assertThat(phoneField.textEquals(phone));
    assertThat(mobileField.textEquals(mobile));
  }

  private void assertTextFieldsEditable(boolean editable) {
    assertEquals(editable, firstNameField.isEditable());
    assertEquals(editable, lastNameField.isEditable());
    assertEquals(editable, emailField.isEditable());
    assertEquals(editable, phoneField.isEditable());
    assertEquals(editable, mobileField.isEditable());
  }
}
