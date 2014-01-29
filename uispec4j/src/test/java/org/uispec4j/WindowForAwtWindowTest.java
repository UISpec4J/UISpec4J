package org.uispec4j;

import org.junit.Test;
import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.xml.XmlAssert;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import java.awt.Frame;
import junit.framework.AssertionFailedError;

public class WindowForAwtWindowTest extends WindowTestCase {
  public void test() throws Exception {
    Window window = createWindow();
    assertEquals("", window.getTitle());
  }

  @Override
  @Test
  public void testWindowManagesMenuBars() throws Exception {
    Window window = new Window(new Frame());
    try {
      window.getMenuBar();
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("This component has no menu bar", e.getMessage());
    }
  }

  @Override
  @Test
  public void testGetTitle() throws Exception {
    assertEquals("", createWindow().getTitle());
  }

  @Override
  @Test
  public void testAssertTitleEquals() throws Exception {
    final Window window = createWindow();
    assertTrue(window.titleEquals(""));
    checkAssertionFails(window.titleEquals("title"), "Unexpected title - expected:<[title]> but was:<[]>");
  }

  @Override
  @Test
  public void testAssertTitleContains() throws Exception {
    final Window window = createWindow();
    assertTrue(window.titleContains(""));
    checkAssertionFails(window.titleContains("title"), "expected to contain:<title> but was:<>");
  }

  @Override
  @Test
  public void testGetDescription() throws Exception {
    Window window = createWindow();
    window.getAwtComponent().setName("myFrame");

    JTextField textField = new JTextField();
    textField.setName("myText");
    addComponent(window, textField);

    XmlAssert.assertEquivalent("<window title=''>" + "  <textBox name='myText'/>" + "</window>",
                               window.getDescription());
  }

  protected boolean supportsMenuBars() {
    return false;
  }

  protected Window createWindowWithMenu(JMenuBar jMenuBar) {
    throw new AssertionFailedError("not supported");
  }

  protected Window createWindowWithTitle(String title) {
    throw new AssertionFailedError("not supported");
  }

  protected void close(Window window) {
    java.awt.Window awtWindow = (java.awt.Window)window.getAwtComponent();
    awtWindow.setVisible(false);
  }

  protected UIComponent createComponent() {
    return createWindow();
  }

  protected Window createWindow() {
    java.awt.Window awtWindow = new java.awt.Window(new Frame());
    return new Window(awtWindow);
  }
}
