package org.uispec4j;

import junit.framework.AssertionFailedError;
import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.xml.EventLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MenuItemForJPopupMenuTest extends MenuItemTestCase {
  public void testFactory() throws Exception {
    checkFactory(new JPopupMenu(), MenuItem.class);
  }

  public void testClickFailsIfTheMenuItemIsNotEnabled() throws Exception {
    JPopupMenu menu = new JPopupMenu();
    menu.add("a");
    menu.add("b");
    menu.add("c");
    MenuItem item = createMenuItem(menu);
    try {
      item.click();
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("This operation is not supported. You must first select a sub menu among: [a,b,c]",
                   e.getMessage());
    }
  }

  protected MenuItem createLoggingMenuItem(final EventLogger eventLogger) {
    final Action action = new AbstractAction("item") {
      public void actionPerformed(ActionEvent e) {
        eventLogger.log("action");
      }
    };
    JPopupMenu menu = new JPopupMenu();
    menu.add(action);
    return createMenuItem(menu);
  }

  static private MenuItem createMenuItem(JPopupMenu menu) {
    return (MenuItem)UIComponentFactory.createUIComponent(menu);
  }

  public static class JPopupMenuBuilder implements MenuBuilder {
    private JPopupMenu jPopupMenu;

    public JPopupMenuBuilder(String text) {
      this(new JPopupMenu(text));
    }

    public JPopupMenuBuilder(JPopupMenu menu) {
      this.jPopupMenu = menu;
    }

    public MenuBuilder setName(String name) {
      jPopupMenu.setName(name);
      return this;
    }

    public MenuBuilder add(String item) {
      jPopupMenu.add(new JMenuItem(item));
      return this;
    }

    public MenuBuilder add(Action action) {
      jPopupMenu.add(new JMenuItem(action));
      return this;
    }

    public MenuBuilder addSeparator() {
      jPopupMenu.addSeparator();
      return this;
    }

    public MenuBuilder setEnabled(boolean enabled) {
      jPopupMenu.setEnabled(enabled);
      return this;
    }

    public MenuBuilder startSubMenu(String item) {
      JMenu sub = new JMenu(item);
      jPopupMenu.add(sub);
      return new MenuItemForJMenuTest.JMenuBuilder(sub);
    }

    public MenuItem getMenuItem() {
      return new MenuItem(jPopupMenu);
    }
  }

  public MenuItemTestCase.MenuBuilder getBuilder(String text) {
    return new JPopupMenuBuilder(text);
  }
}