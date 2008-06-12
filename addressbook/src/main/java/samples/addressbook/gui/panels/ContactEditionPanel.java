package samples.addressbook.gui.panels;

import samples.addressbook.gui.selection.ContactSelectionListener;
import samples.addressbook.gui.selection.SelectionHandler;
import samples.addressbook.model.AddressBook;
import samples.addressbook.model.Contact;
import samples.addressbook.model.events.ContactChangeListener;
import samples.utils.GridBag;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ContactEditionPanel
  implements ContactSelectionListener,
             ContactChangeListener {

  private Map fieldToComponentMap;
  private JButton applyButton;
  private Contact currentContact;
  private AddressBook book;

  public ContactEditionPanel(AddressBook book, SelectionHandler handler) {
    this.book = book;
    handler.addContactSelectionListener(this);
    book.getEventHandler().addContactChangeListener(this);
    initFieldMap();
    initTextFields();
    initApplyButton();
    setTextFieldsEditable(false);
  }

  private void initApplyButton() {
    applyButton = new JButton(new AbstractAction("Apply") {
      public void actionPerformed(ActionEvent e) {
        applyChanges();
      }
    });
  }

  private void setTextFieldsEditable(boolean editable) {
    for (Iterator iterator = fieldToComponentMap.values().iterator(); iterator.hasNext();) {
      JTextComponent component = (JTextComponent)iterator.next();
      component.setEditable(editable);
    }
  }

  public void contactSelected(Contact contact) {
    this.currentContact = contact;
    setTextFieldsEditable(contact != null);
    for (Iterator iterator = fieldToComponentMap.entrySet().iterator(); iterator.hasNext();) {
      Map.Entry entry = (Map.Entry)iterator.next();
      JTextComponent component = (JTextComponent)entry.getValue();
      Contact.Field field = (Contact.Field)entry.getKey();
      component.setText(contact != null ? contact.getValue(field) : "");
    }
  }

  public void contactChanged(Contact contact, Contact.Field field, String newValue) {
    if (contact.equals(currentContact)) {
      getTextComponent(field).setText(newValue);
    }
  }

  public JPanel getPanel() {
    JPanel panel = new JPanel();
    GridBag gridBag = new GridBag(panel);
    addField(gridBag, "First name", Contact.Field.FIRST_NAME, 0, 0);
    addField(gridBag, "Last name", Contact.Field.LAST_NAME, 1, 0);
    addField(gridBag, "Phone", Contact.Field.PHONE, 2, 0);
    addField(gridBag, "Mobile", Contact.Field.MOBILE, 3, 0);
    addField(gridBag, "E-Mail", Contact.Field.EMAIL, 0, 1);
    addField(gridBag, "Second e-mail", Contact.Field.SECOND_EMAIL, 1, 1);
    addField(gridBag, "URL", Contact.Field.URL, 2, 1);
    gridBag.add(applyButton, 4, 0,
                1, 1, 0, 0,
                GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));

    gridBag.add(new JLabel("Notes"),
                0, 4,
                1, 1, 0, 0,
                GridBagConstraints.NONE,
                GridBagConstraints.NORTHEAST,
                new Insets(5, 10, 5, 5));
    JScrollPane notesScroller = new JScrollPane();
    notesScroller.getViewport().add(getTextComponent(Contact.Field.NOTES));
    gridBag.add(notesScroller, 1, 4,
                5, 1, 1, 1,
                GridBagConstraints.BOTH,
                GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 5));
    return panel;
  }

  private void addField(GridBag gridBag, String label, Contact.Field field,
                        int row, int column) {
    gridBag.add(new JLabel(label),
                2 * column, row,
                1, 1, 0, 0,
                GridBagConstraints.NONE,
                GridBagConstraints.EAST,
                new Insets(5, 10, 5, 5));
    gridBag.add(getTextComponent(field),
                (2 * column) + 1, row,
                1, 1, 1, 0,
                GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER,
                new Insets(5, 5, 5, 10));
  }

  private void applyChanges() {
    for (Iterator iterator = fieldToComponentMap.entrySet().iterator(); iterator.hasNext();) {
      Map.Entry entry = (Map.Entry)iterator.next();
      JTextComponent component = (JTextComponent)entry.getValue();
      Contact.Field field = (Contact.Field)entry.getKey();
      book.changeContact(currentContact, field, component.getText());
    }
  }

  private JTextComponent getTextComponent(Contact.Field field) {
    return (JTextComponent)fieldToComponentMap.get(field);
  }

  private void initTextFields() {
    for (Iterator iterator = fieldToComponentMap.entrySet().iterator(); iterator.hasNext();) {
      Map.Entry entry = (Map.Entry)iterator.next();
      JTextComponent component = (JTextComponent)entry.getValue();
      Contact.Field field = (Contact.Field)entry.getKey();
      component.setName("field:" + field.toString());
      installListener(component, field);
    }
  }

  private void initFieldMap() {
    fieldToComponentMap = new HashMap();
    fieldToComponentMap.put(Contact.Field.FIRST_NAME, new JTextField());
    fieldToComponentMap.put(Contact.Field.LAST_NAME, new JTextField());
    fieldToComponentMap.put(Contact.Field.EMAIL, new JTextField());
    fieldToComponentMap.put(Contact.Field.PHONE, new JTextField());
    fieldToComponentMap.put(Contact.Field.MOBILE, new JTextField());
    fieldToComponentMap.put(Contact.Field.URL, new JTextField());
    fieldToComponentMap.put(Contact.Field.SECOND_EMAIL, new JTextField());
    fieldToComponentMap.put(Contact.Field.NOTES, new JTextArea());
  }

  private void installListener(final JTextComponent component, final Contact.Field field) {
    if (component instanceof JTextField) {
      JTextField textField = (JTextField)component;
      textField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          book.changeContact(currentContact, field, component.getText());
        }
      });
    }
  }
}
