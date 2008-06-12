package samples.addressbook.gui.panels;

import samples.addressbook.gui.actions.CreateContactAction;
import samples.addressbook.gui.selection.CategorySelectionListener;
import samples.addressbook.gui.selection.ContactSelectionListener;
import samples.addressbook.gui.selection.SelectionHandler;
import samples.addressbook.model.AddressBook;
import samples.addressbook.model.Category;
import samples.addressbook.model.Contact;
import samples.addressbook.model.events.ContactChangeListener;
import samples.addressbook.model.events.ContactCreationListener;
import samples.addressbook.model.events.ContactDeletionListener;
import samples.utils.GridBag;
import samples.utils.SampleUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ContactTablePanel {
  private JTable table;
  private ContactTableModel model;
  private JButton createNewContactButton;
  private JPopupMenu tablePopupMenu;

  private static final Contact.Field[] ALL_FIELDS =
    {Contact.Field.FIRST_NAME,
     Contact.Field.LAST_NAME,
     Contact.Field.EMAIL,
     Contact.Field.PHONE,
     Contact.Field.MOBILE};

  public ContactTablePanel(final AddressBook book, final SelectionHandler selectionHandler) {
    this.model = new ContactTableModel(book, selectionHandler);
    this.table = new JTable(model);
    this.createNewContactButton = new JButton(new CreateContactAction(book, selectionHandler));
    this.tablePopupMenu = createTablePopupMenu(book, selectionHandler);
    installSelection(selectionHandler);
    table.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        maybePopupMenu(e);
      }

      public void mouseReleased(MouseEvent e) {
        maybePopupMenu(e);
      }
    });
    table.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
      public Component getTableCellRendererComponent(JTable table, Object modelValue, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, modelValue, isSelected, hasFocus, row, column);
        boolean isNameDefined = ((column != 0) && column != 1) || !"".equals(modelValue);
        component.setBackground(isNameDefined ? Color.WHITE : Color.PINK);
        return component;
      }
    });
  }

  private void maybePopupMenu(MouseEvent e) {
    if (e.isPopupTrigger()) {
      tablePopupMenu.show(e.getComponent(), e.getX(), e.getY());
    }
  }

  private JPopupMenu createTablePopupMenu(AddressBook book, SelectionHandler selectionHandler) {
    JPopupMenu menu = new JPopupMenu();
    menu.add(new RemoveContactAction(book, selectionHandler));
    return menu;
  }

  private boolean confirm(String title, String message) {
    return JOptionPane.showConfirmDialog(table, message, title,
                                         JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
  }

  private void installSelection(final SelectionHandler selectionHandler) {
    this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        int row = table.getSelectedRow();
        if (row < 0) {
          selectionHandler.selectContact(null);
        }
        else {
          selectionHandler.selectContact(model.getContact(row));
        }
      }
    });
  }

  public JPanel getPanel() {
    JPanel panel = new JPanel();
    GridBag gridBag = new GridBag(panel);
    gridBag.add(createNewContactButton,
                0, 0,
                1, 1, 0, 0,
                GridBagConstraints.NONE,
                GridBagConstraints.NORTHWEST,
                new Insets(0, 0, 0, 0));
    gridBag.add(SampleUtils.createPanelWithScroller(table),
                0, 1,
                1, 1, 1, 1,
                GridBagConstraints.BOTH,
                GridBagConstraints.CENTER,
                new Insets(0, 0, 0, 0));
    return panel;
  }

  private class ContactTableModel
    extends DefaultTableModel
    implements ContactCreationListener, ContactChangeListener, CategorySelectionListener, ContactDeletionListener {
    private List contacts = new ArrayList();
    private AddressBook book;

    public ContactTableModel(AddressBook book, SelectionHandler selectionHandler) {
      super(ALL_FIELDS, 0);
      this.book = book;
      selectionHandler.addCategorySelectionListener(this);
      book.getEventHandler().addContactChangeListener(this);
      book.getEventHandler().addContactCreationListener(this);
      book.getEventHandler().addContactDeletionListener(this);
      setContacts(book.getContacts());
    }

    public void setContacts(List contacts) {
      this.contacts.clear();
      this.contacts.addAll(contacts);
      fireTableDataChanged();
    }

    public int getRowCount() {
      if (contacts == null) {
        return 0;
      }
      return contacts.size();
    }

    public Class getColumnClass(int columnIndex) {
      return String.class;
    }

    public Object getValueAt(int row, int column) {
      Contact contact = getContact(row);
      String value = contact.getValue(ALL_FIELDS[column]);
      return (value == null) ? "" : value;
    }

    public void setValueAt(Object aValue, int row, int column) {
      Contact contact = getContact(row);
      book.changeContact(contact, ALL_FIELDS[column], (String)aValue);
    }

    private Contact getContact(int row) {
      return (Contact)contacts.get(row);
    }

    public void categorySelected(Category category) {
      setContacts(book.getContacts(category == null ? book.getRootCategory() : category));
    }

    public void contactCreated(Contact contact) {
      contacts.add(contact);
      int row = getRowIndex(contact);
      fireTableRowsInserted(row, row);
      table.setRowSelectionInterval(row, row);
    }

    public void contactChanged(Contact contact, Contact.Field field, String newValue) {
      int row = getRowIndex(contact);
      if (row < 0) {
        return;
      }
      int column = getColumnIndex(field);
      if (column < 0) {
        return;
      }
      fireTableCellUpdated(row, column);
    }

    public void contactDeleted(Contact contact) {
      contacts.remove(contact);
      int rowIndex = getRowIndex(contact);
      fireTableRowsDeleted(rowIndex, rowIndex);
    }

    private int getRowIndex(Contact contact) {
      return contacts.indexOf(contact);
    }

    private int getColumnIndex(Contact.Field field) {
      for (int i = 0; i < ALL_FIELDS.length; i++) {
        if (ALL_FIELDS[i].equals(field)) {
          return i;
        }
      }
      return -1;
    }
  }

  private class RemoveContactAction extends AbstractAction implements ContactSelectionListener {
    private final AddressBook book;
    private Contact selectedContact;

    public RemoveContactAction(AddressBook book, SelectionHandler selectionHandler) {
      super("Remove");
      this.book = book;
      selectionHandler.addContactSelectionListener(this);
      setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
      if (confirm("Confirmation", "Are you sure you want to delete this contact?")) {
        book.removeContact(selectedContact);
      }
    }

    public void contactSelected(Contact contact) {
      this.selectedContact = contact;
      setEnabled(contact != null);
    }
  }
}
