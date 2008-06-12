package org.uispec4j;

import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.xml.EventLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MenuItemForJMenuTest extends MenuItemTestCase {
  public void testFactory() throws Exception {
    checkFactory(new JMenu(), MenuItem.class);
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

  public static class JMenuBuilder implements MenuBuilder {
    private JMenu jMenu;

    public JMenuBuilder(String text) {
      this(new JMenu(text));
    }

    public JMenuBuilder(JMenu menu) {
      this.jMenu = menu;
    }

    public MenuBuilder setName(String name) {
      jMenu.setName(name);
      return this;
    }

    public MenuBuilder add(String item) {
      jMenu.add(new JMenuItem(item));
      return this;
    }

    public MenuBuilder add(Action action) {
      jMenu.add(new JMenuItem(action));
      return this;
    }

    public MenuBuilder addSeparator() {
      jMenu.addSeparator();
      return this;
    }

    public MenuBuilder setEnabled(boolean enabled) {
      jMenu.setEnabled(enabled);
      return this;
    }

    public MenuBuilder startSubMenu(String item) {
      JMenu sub = new JMenu(item);
      jMenu.add(sub);
      return new JMenuBuilder(sub);
    }

    public MenuItem getMenuItem() {
      return new MenuItem(jMenu);
    }
  }

  public MenuItemTestCase.MenuBuilder getBuilder(String text) {
    return new JMenuBuilder(text);
  }
}