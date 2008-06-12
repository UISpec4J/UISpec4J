package org.uispec4j;

import junit.framework.AssertionFailedError;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.xml.EventLogger;
import org.uispec4j.xml.XmlAssert;

import javax.swing.*;
import java.awt.event.ActionEvent;

public abstract class MenuItemTestCase extends UIComponentTestCase {

  public void testGetComponentTypeName() throws Exception {
    MenuItem item = getBuilder("menuTest").getMenuItem();
    assertEquals("menu", item.getDescriptionTypeName());
  }

  public void testGetDescription() throws Exception {
    MenuItem item = getBuilder("menuTest").setName("myMenu").getMenuItem();
    XmlAssert.assertEquivalent("<menu name='myMenu'/>", item.getDescription());
  }

  protected UIComponent createComponent() {
    return getBuilder("item").getMenuItem();
  }

  public void testMenuContent() throws Exception {
    MenuBuilder rootBuilder = getBuilder("menuTest");
    rootBuilder.add("item1");
    MenuBuilder item2Builder = rootBuilder.startSubMenu("item2");
    item2Builder.add("subItem1");
    item2Builder.add("subItem2");
    rootBuilder.add("item3");

    MenuItem item = rootBuilder.getMenuItem();
    assertTrue(item.contentEquals("<menu name='menuTest'>" +
                                  "  <menu name='item1'/>" +
                                  "  <menu name='item2'>" +
                                  "    <menu name='subItem1'/>" +
                                  "    <menu name='subItem2'/>" +
                                  "  </menu>" +
                                  "  <menu name='item3'/>" +
                                  "</menu>"));
  }

  public void testCheckMenu() throws Exception {
    MenuBuilder builder = getBuilder("menuTest");
    builder.add("item1");
    builder.addSeparator();
    builder.add("item2");
    MenuItem item = builder.getMenuItem();
    assertTrue(item.contentEquals("<menu name='menuTest'>" +
                                  "  <menu name='item1'/>" +
                                  "  <separator/>" +
                                  "  <menu name='item2'/>" +
                                  "</menu>"));
    assertTrue(item.contentEquals(new String[]{"item1", "item2"}));
  }

  public void testCheckMenuOnMenuItemWithNoError() throws Exception {
    MenuBuilder builder = getBuilder("root");
    builder.add("item1");
    MenuItem item = builder.getMenuItem();
    MenuItem subMenu = item.getSubMenu("item1");
    assertTrue(subMenu.contentEquals("<menu name='item1'/>"));
    assertTrue(item.contentEquals(new String[]{"item1"}));
  }

  public void testGetSubMenuWorksWithPartOfItsName() throws Exception {
    MenuBuilder builder = getBuilder("root");
    builder.add("one two three...");
    MenuItem item = builder.getMenuItem();
    assertNotNull(item.getSubMenu("one two three..."));
    assertNotNull(item.getSubMenu("one"));
    assertNotNull(item.getSubMenu("two"));
  }

  public void testClickFailsIfTheMenuItemIsNotEnabled() throws Exception {
    MenuItem item = getBuilder("").setEnabled(false).getMenuItem();
    try {
      item.click();
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("The menu item is not enabled, it cannot be activated", e.getMessage());
    }
  }

  public void testCheckMenuWithOneLevel() throws Exception {
    MenuItem item =
      getBuilder("")
        .add("item1")
        .add("item2")
        .add("item3")
        .getMenuItem();
    assertTrue(item.contentEquals(new String[]{"item1", "item2", "item3"}));
  }

  public void testActivateSimulatesAClickOnTheMenuItem() throws Exception {
    EventLogger eventLogger = new EventLogger();
    MenuItem menuItem = createLoggingMenuItem(eventLogger);
    assertTrue(menuItem.contentEquals(new String[]{
      "item"
    }));
    menuItem.getSubMenu("item").click();

    eventLogger.assertEquals("<log>" +
                             "  <action/>" +
                             "</log>");
  }

  public void testTriggerClickWorksAsClick() throws Exception {
    EventLogger eventLogger = new EventLogger();
    MenuItem menuItem = createLoggingMenuItem(eventLogger);
    menuItem.getSubMenu("item").triggerClick().run();
    eventLogger.assertEquals("<log>" +
                             "  <action/>" +
                             "</log>");
  }

  public void testGetSubmenuError() throws Exception {
    checkGetSubmenuError(new String[]{"One", "Two", "Three"}, "Four", "There is no menu item matching 'Four' - actual elements: [One,Two,Three]");
  }

  public void testGetAmbiguitySubMenu() throws Exception {
    checkGetSubmenuError(new String[]{"One two three...", "One two three four...", "two three"},
                         "three",
                         "Could not retrieve subMenu item : There are more than one component matching 'three'");
  }

  private void checkGetSubmenuError(String[] menus, String searchedItem, String expectedMessage) {
    MenuBuilder builder = getBuilder("");
    for (int i = 0; i < menus.length; i++) {
      builder.add(menus[i]);
    }
    MenuItem item = builder.getMenuItem();
    try {
      item.getSubMenu(searchedItem);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals(expectedMessage,
                   e.getMessage());
    }
  }

  public void testGetSubMenuWhenSubMenuIsItselfAMenu() throws Exception {
    MenuBuilder rootBuilder = getBuilder("");
    rootBuilder.add("item1");
    rootBuilder.startSubMenu("item2").add("subItem1").add("subItem2").setName("subMenuItem2");
    rootBuilder.add("item3");
    MenuItem subMenuItem = rootBuilder.getMenuItem().getSubMenu("item2");
    assertTrue(subMenuItem.contentEquals("<menu name='item2'>" +
                                         "  <menu name='subItem1'/>" +
                                         "  <menu name='subItem2'/>" +
                                         "</menu>"));
  }

  public void testHandlingADialogShownByAPopupMenu() throws Exception {
    final EventLogger logger = new EventLogger();

    final JPanel panel = new JPanel();
    WindowInterceptor.run(new Trigger() {
      public void run() throws Exception {
        showDialog(panel);
      }
    });

    final JButton button = new JButton(new AbstractAction("ok") {
      public void actionPerformed(ActionEvent e) {
        logger.log("ok");
      }
    });

    MenuItem menuItem =
      getBuilder("")
        .add(new AbstractAction("menu") {
          public void actionPerformed(ActionEvent e) {
            logger.log("dialog");
            showDialog(button);
          }
        })
        .getMenuItem();
    WindowInterceptor
      .init(menuItem.getSubMenu("menu").triggerClick())
      .process(new WindowHandler() {
        public Trigger process(Window window) throws Exception {
          logger.log("handleWindow");
          return window.getButton("ok").triggerClick();
        }
      })
      .run();

    logger.assertEquals("<log>" +
                        "  <dialog/>" +
                        "  <handleWindow/>" +
                        "  <ok/>" +
                        "</log>");
  }

  private void showDialog(final JComponent component) {
    JDialog dialog = new JDialog();
    dialog.getContentPane().add(component);
    dialog.setVisible(true);
  }

  protected abstract MenuItem createLoggingMenuItem(EventLogger eventLogger);

  protected abstract MenuBuilder getBuilder(String text);

  public interface MenuBuilder {
    MenuBuilder add(String item);

    MenuBuilder add(Action action);

    MenuBuilder addSeparator();

    MenuBuilder setEnabled(boolean enabled);

    MenuBuilder setName(String s);

    MenuBuilder startSubMenu(String item);

    MenuItem getMenuItem();
  }
}
