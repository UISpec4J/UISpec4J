package samples.addressbook.functests;

import org.uispec4j.*;
import org.uispec4j.interception.BasicHandler;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowInterceptor;
import samples.addressbook.main.Main;

public abstract class AddressBookTestCase extends UISpecTestCase {
  protected Table contactTable;
  protected Tree categoryTree;
  protected Button newCategoryButton;
  protected Button newContactButton;
  protected TextBox firstNameField;
  protected TextBox lastNameField;
  protected TextBox phoneField;
  protected TextBox mobileField;
  protected TextBox emailField;
  protected Button applyButton;

  protected void setUp() throws Exception {
    super.setUp();
    setAdapter(new MainClassAdapter(Main.class));

    Window window = getMainWindow();
    this.contactTable = window.getTable();
    this.categoryTree = window.getTree();
    this.newCategoryButton = window.getButton("new category");
    this.newContactButton = window.getButton("new contact");
    this.firstNameField = window.getTextBox("field:first");
    this.lastNameField = window.getTextBox("field:last");
    this.phoneField = window.getTextBox("field:phone");
    this.mobileField = window.getTextBox("field:mobile");
    this.emailField = window.getTextBox("field:e-mail");
    this.applyButton = window.getButton("apply");

    UISpec4J.setWindowInterceptionTimeLimit(100);
  }

  protected void changeFields(String firstName, String lastName, String email, String phone, String mobile) {
    firstNameField.setText(firstName);
    lastNameField.setText(lastName);
    emailField.setText(email);
    phoneField.setText(phone);
    mobileField.setText(mobile);
  }

  protected void createContact(String firstName, String lastName) {
    createContact(firstName, lastName, "", "", "");
  }

  protected void createContact(String firstName, String lastName, String email, String phone, String mobile) {
    newContactButton.click();
    changeFields(firstName, lastName, email, phone, mobile);
  }

  protected void createCategory(String parentCategoryPath, String categoryName) {
    Trigger trigger = newCategoryButton.triggerClick();
    createCategory(parentCategoryPath, categoryName, trigger);
  }

  protected void createCategory(String parentCategoryPath, String categoryName, Trigger trigger) {
    categoryTree.select(parentCategoryPath);
    WindowInterceptor
      .init(trigger)
      .process(BasicHandler.init()
        .assertContainsText("Category name:")
        .setText(categoryName)
        .triggerButtonClick("OK"))
      .run();
  }
}
