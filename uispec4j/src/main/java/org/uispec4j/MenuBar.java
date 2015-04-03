package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.utils.ArrayUtils;

import javax.swing.*;

/**
 * <p>Wrapper for JMenuBar components.</p>
 * A MenuBar is a container for top-level menu items represented by {@link MenuItem} components.
 */
public class MenuBar extends AbstractUIComponent {
  public static final String TYPE_NAME = "menuBar";
  public static final Class[] SWING_CLASSES = {JMenuBar.class};

  private JMenuBar jMenuBar;

  public MenuBar(JMenuBar menuBar) {
    AssertAdapter.assertNotNull("The menu bar should not be null", menuBar);
    this.jMenuBar = menuBar;
  }

  public JMenuBar getAwtComponent() {
    return jMenuBar;
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  /**
   * Returns a {@link MenuItem} component representing a top-level menu (for instance File/Edit/etc.).
   * That MenuItem can be used then to access the individual menu commands, or other submenus.
   */
  public MenuItem getMenu(String menuName) {
    int menuIndex = getMenuIndex(menuName);
    AssertAdapter.assertFalse("Menu '" + menuName + "' does not exist", menuIndex == -1);
    JMenu menu = jMenuBar.getMenu(menuIndex);
    return new MenuItem(menu);
  }

  /**
   * Checks the names displayed in the menu, ommiting separators.
   */
  public Assertion contentEquals(final String... menuNames) {
    return new Assertion() {
      public void check() {
        String[] actual = new String[jMenuBar.getMenuCount()];
        for (int i = 0; i < actual.length; i++) {
          actual[i] = jMenuBar.getMenu(i).getText();
        }
        ArrayUtils.assertEquals(menuNames, actual);
      }
    };
  }

  /**
   * Returns the index of a menu item given its name, or -1 if it was not found.
   */
  private int getMenuIndex(String menuName) {
    for (int i = 0; i < jMenuBar.getMenuCount(); i++) {
      JMenu menu = jMenuBar.getMenu(i);
      if (menu == null) {
        return -1;
      }
      String text = menu.getText();
      if (text == null) {
        continue;
      }
      if (text.equals(menuName)) {
        return i;
      }
    }
    return -1;
  }
}
