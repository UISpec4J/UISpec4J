package samples.addressbook.gui;

import samples.addressbook.gui.panels.CategoryTreePanel;
import samples.addressbook.gui.panels.ContactEditionPanel;
import samples.addressbook.gui.panels.ContactTablePanel;
import samples.addressbook.gui.selection.SelectionHandler;
import samples.addressbook.model.AddressBook;

import javax.swing.*;
import java.awt.*;

public class MainWindow {
  private JFrame frame;

  public MainWindow(AddressBook book) {
    SelectionHandler selectionHandler = new SelectionHandler();
    ContactTablePanel contactTablePanel = new ContactTablePanel(book, selectionHandler);
    ContactEditionPanel contactEditionPanel = new ContactEditionPanel(book, selectionHandler);
    CategoryTreePanel categoryTreePanel = new CategoryTreePanel(book, selectionHandler);

    JSplitPane horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                                contactTablePanel.getPanel(),
                                                categoryTreePanel.getPanel());

    JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                              horizontalSplit,
                                              contactEditionPanel.getPanel());
    frame = new JFrame();
    frame.setTitle("Address Book");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getRootPane().setDefaultButton(contactTablePanel.getCreateNewContactButton());
    Container contentPane = frame.getContentPane();
    contentPane.add(verticalSplit, BorderLayout.CENTER);
  }

  public JFrame getFrame() {
    return frame;
  }
}
