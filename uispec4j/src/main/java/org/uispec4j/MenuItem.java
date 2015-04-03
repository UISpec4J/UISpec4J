package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.finder.StringMatcher;
import org.uispec4j.utils.ArrayUtils;
import org.uispec4j.xml.XmlAssert;
import org.uispec4j.xml.XmlWriter;

import javax.swing.*;
import java.awt.*;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Wrapper for menu items (commands or sub-menus) such as JMenu, JMenuItem or JPopupMenu.</p>
 * A given MenuItem can be either a command, or a sub-menu containing other MenuItem
 * components.
 */
public class MenuItem extends AbstractUIComponent {
  public static final String TYPE_NAME = "menu";
  public static final Class[] SWING_CLASSES = {JMenuItem.class, JPopupMenu.class};

  private final MenuWrapper wrapper;

  public MenuItem(JMenuItem menu) {
    this.wrapper = new JMenuItemWrapper(menu);
  }

  public MenuItem(final JPopupMenu menu) {
    this.wrapper = new JPopupMenuWrapper(menu);
  }

  public String getDescriptionTypeName() {
    return TYPE_NAME;
  }

  public Component getAwtComponent() {
    return wrapper.getAwtComponent();
  }

  public void click() {
    wrapper.click();
  }

  public Trigger triggerClick() {
    return new Trigger() {
      public void run() {
        MenuItem.this.click();
      }
    };
  }

  /**
   * Returns a submenu given its name, or raises an exception if it was not found.
   */
  public MenuItem getSubMenu(String subMenuItem) {
    MenuItem menuItem = retrieveMatchingSubMenu(subMenuItem, StringMatcher.identity(subMenuItem));
    if (menuItem == null) {
      menuItem = retrieveMatchingSubMenu(subMenuItem, StringMatcher.substring(subMenuItem));
    }
    if (menuItem == null) {
      AssertAdapter.fail("There is no menu item matching '" + subMenuItem + "' - actual elements: " +
                          ArrayUtils.toString(getSubElementNames()));
    }
    return menuItem;
  }

  public Assertion contentEquals(final String... expectedNames) {
    return new Assertion() {
      public void check() {
        ArrayUtils.assertEquals(expectedNames, getSubElementNames());
      }
    };
  }

  /**
   * Returns the names of the items found in this MenuItem in case where it is a submenu.
   * Separators are not represented in this list.
   */
  private String[] getSubElementNames() {
    List<String> actualWrappersList = new ArrayList<String>();
    MenuWrapper[] subMenus = wrapper.getSubElements(true);
    for (MenuWrapper menuItem : subMenus) {
      actualWrappersList.add(menuItem.getText());
    }
    return actualWrappersList.toArray(new String[actualWrappersList.size()]);
  }

  public Assertion contentEquals(final String xmlContent) {
    return new Assertion() {
      public void check() {
        StringWriter writer = new StringWriter();
        XmlWriter.Tag tag = XmlWriter.startTag(writer, getDescriptionTypeName());
        if (wrapper.getText() != null) {
          tag.addAttribute("name", wrapper.getText());
        }
        computeContent(tag, wrapper.getSubElements(false));
        tag.end();
        XmlAssert.assertEquals(xmlContent, writer.toString());
      }
    };
  }

  private void computeContent(XmlWriter.Tag tag, MenuWrapper[] elements) {
    for (MenuWrapper element : elements) {
      if (element.isSeparator()) {
        tag.start("separator").end();
      }
      else {
        XmlWriter.Tag menuTag = tag.start("menu").addAttribute("name", element.getText());
        computeContent(menuTag, element.getSubElements(false));
        menuTag.end();
      }
    }
  }

  private MenuItem retrieveMatchingSubMenu(String toFind, StringMatcher menuMatcher) {
    MenuItem subMenuItem = null;
    MenuWrapper[] subMenus = wrapper.getSubElements(true);
    for (MenuWrapper subMenu : subMenus) {
      if (isSubMenuMatching(subMenu.getText(), menuMatcher, toFind, subMenuItem != null)) {
        subMenuItem = subMenu.toItem();
      }
    }
    return subMenuItem;
  }

  private boolean isSubMenuMatching(String name,
                                    StringMatcher menuMatcher,
                                    String toFind,
                                    boolean firstMatchFound) {
    if (menuMatcher.matches(name)) {
      if (firstMatchFound) {
        AssertAdapter.fail("Could not retrieve subMenu item : There are more than one component matching '" + toFind + "'");
      }
      return true;
    }
    return false;
  }

  private interface MenuWrapper {
    String getText();

    MenuWrapper[] getSubElements(boolean skipSeparators);

    void click();

    Component getAwtComponent();

    boolean isSeparator();

    MenuItem toItem();
  }

  private class JMenuItemWrapper implements MenuWrapper {
    private JMenuItem menuItem;

    public JMenuItemWrapper(JMenuItem menuItem) {
      this.menuItem = menuItem;
    }

    public void click() {
      AssertAdapter.assertTrue("The menu item is not enabled, it cannot be activated",
                                menuItem.isEnabled());
      AbstractButton.doClick(menuItem);
    }

    public MenuWrapper[] getSubElements(boolean skipSeparators) {
      if (!(menuItem instanceof JMenu)) {
        return new MenuWrapper[0];
      }
      JMenu menu = (JMenu)menuItem;
      List<MenuWrapper> result = new ArrayList<MenuWrapper>();
      Component[] menuComponents = menu.getMenuComponents();
      for (Component menuComponent : menuComponents) {
        if (menuComponent instanceof JMenuItem) {
          result.add(new JMenuItemWrapper((JMenuItem)menuComponent));
        }
        else if ((menuComponent instanceof JPopupMenu.Separator) && !skipSeparators) {
          result.add(new SeparatorWrapper());
        }
      }
      return result.toArray(new MenuWrapper[result.size()]);
    }

    public String getText() {
      return menuItem.getText();
    }

    public Component getAwtComponent() {
      return menuItem;
    }

    public boolean isSeparator() {
      return false;
    }

    public MenuItem toItem() {
      return new MenuItem(menuItem);
    }
  }

  private class JPopupMenuWrapper implements MenuWrapper {
    private JPopupMenu popupMenu;

    public JPopupMenuWrapper(JPopupMenu popupMenu) {
      this.popupMenu = popupMenu;
    }

    public void click() {
      AssertAdapter.fail("This operation is not supported. You must first select a sub menu among: "
                          + ArrayUtils.toString(getSubElementNames()));
    }

    public MenuWrapper[] getSubElements(boolean skipSeparators) {
      Component[] components = popupMenu.getComponents();
      List<MenuWrapper> elements = new ArrayList<MenuWrapper>();
      for (Component component : components) {
        if (component instanceof JMenuItem) {
          elements.add(new JMenuItemWrapper((JMenuItem)component));
        }
        else if ((component instanceof JPopupMenu.Separator)) {
          if (!skipSeparators) {
            elements.add(new SeparatorWrapper());
          }
        }
        else {
          AssertAdapter.fail("Unexpected menu item of class: " + component.getClass());
        }
      }
      return elements.toArray(new MenuWrapper[elements.size()]);
    }

    public String getText() {
      return popupMenu.getLabel();
    }

    public Component getAwtComponent() {
      return popupMenu;
    }

    public boolean isSeparator() {
      return false;
    }

    public MenuItem toItem() {
      return new MenuItem(popupMenu);
    }
  }

  private static class SeparatorWrapper implements MenuWrapper {
    public void click() {
    }

    public Component getAwtComponent() {
      return null;
    }

    public MenuWrapper[] getSubElements(boolean skipSeparators) {
      return new MenuWrapper[0];
    }

    public String getText() {
      return null;
    }

    public boolean isSeparator() {
      return true;
    }

    public MenuItem toItem() {
      throw new UnsupportedOperationException();
    }
  }
}
