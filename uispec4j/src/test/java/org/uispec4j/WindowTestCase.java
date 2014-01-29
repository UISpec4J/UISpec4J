package org.uispec4j;

import org.junit.Test;
import org.uispec4j.assertion.UISpecAssert;
import org.uispec4j.interception.WindowInterceptor;
import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.utils.UIComponentFactory;
import org.uispec4j.xml.XmlAssert;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.Component;
import junit.framework.AssertionFailedError;

public abstract class WindowTestCase extends UIComponentTestCase {

  @Test
  public void testAssertTitleEquals() throws Exception {
    Window window = createWindowWithTitle("me");
    assertTrue(window.titleEquals("me"));

    try {
      assertTrue(window.titleEquals("you"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError failure) {
      assertEquals("Unexpected title - expected:<[you]> but was:<[me]>", failure.getMessage());
    }
  }

  @Test
  public void testAssertTitleContains() throws Exception {
    Window window = createWindowWithTitle("me and you");
    assertTrue(window.titleContains("me"));
    assertTrue(window.titleContains("you"));
    assertFalse(window.titleContains("You"));

    try {
      assertTrue(window.titleContains("us"));
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError failure) {
      assertEquals("expected to contain:<us> but was:<me and you>", failure.getMessage());
    }
  }

  @Test
  public void testGetTitle() throws Exception {
    assertEquals("me", createWindowWithTitle("me").getTitle());
  }

  @Override
  @Test
  public void testGetDescription() throws Exception {
    Window window = createWindowWithTitle("my title");
    window.getAwtComponent().setName("myFrame");

    JTextField textField = new JTextField();
    textField.setName("myText");
    addComponent(window, textField);

    XmlAssert.assertEquivalent("<window title='my title'>" +
                               "  <textBox name='myText'/>" +
                               "</window>",
                               window.getDescription());
  }

  protected UIComponent createComponent() {
    return createWindowWithTitle("title");
  }

  @Test
  public void testFinder() throws Exception {
    Window window = createWindow();

    JTextField textField = new JTextField();
    textField.setName("myText");
    addComponent(window, textField);

    TextBox uiComp = window.getTextBox("myText");

    assertSame(textField, uiComp.getAwtComponent());
  }

  @Test
  public void testWindowManagesMenuBars() throws Exception {
    JMenuBar jMenuBar = new JMenuBar();
    jMenuBar.add(new JMenu("Menu 1"));
    jMenuBar.add(new JMenu("Menu 2"));
    jMenuBar.add(new JMenu("Menu 3"));

    Window window = createWindowWithMenu(jMenuBar);

    window.getMenuBar().contentEquals(
      "Menu 1",
      "Menu 2",
      "Menu 3"
    );
  }

  @Test
  public void testContainsMenuBar() throws Exception {
    Window windowWithoutMenuBar = createWindow();
    UISpecAssert.assertFalse(windowWithoutMenuBar.containsMenuBar());

    if (supportsMenuBars()) {
      Window windowWithMenuBar = createWindowWithMenu(new JMenuBar());
      UISpecAssert.assertTrue(windowWithMenuBar.containsMenuBar());
    }
  }

  @Override
  @Test
  public void testGetComponentTypeName() {
    assertEquals("window", createWindow().getDescriptionTypeName());
  }

  @Override
  @Test
  public void testFactory() {
    Component component = createWindow().getAwtComponent();
    UIComponent uiComponent = UIComponentFactory.createUIComponent(component);
    assertNotNull(uiComponent);
    assertTrue(Window.class.isInstance(uiComponent));
  }

  @Test
  public void testWindowClosed() throws Exception {
    final Window window = createWindow();
    assertFalse(window.isVisible());
    WindowInterceptor.run(new Trigger() {
      public void run() throws Exception {
        show(window);
      }
    });

    assertTrue(window.isVisible());

    close(window);

    assertFalse(window.isVisible());
  }

  protected abstract boolean supportsMenuBars();

  protected abstract Window createWindowWithMenu(JMenuBar jMenuBar);

  protected Window createWindow() {
    return createWindowWithTitle("title");
  }

  protected abstract Window createWindowWithTitle(String title);

  protected void addComponent(Window window, JComponent component) {
    JScrollPane scroller = new JScrollPane();
    scroller.getViewport().add(component);
    JPanel panel = new JPanel();
    panel.add(scroller);
    window.getInternalAwtContainer().add(panel);
  }

  protected void checkIsModal(Window window, boolean modal) {
    assertEquals(modal, window.isModal());
    try {
      assertEquals(!modal, window.isModal());
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
    }
  }

  protected void show(final Window window) {
    window.getAwtContainer().setVisible(true);
  }

  protected abstract void close(Window window);
}

