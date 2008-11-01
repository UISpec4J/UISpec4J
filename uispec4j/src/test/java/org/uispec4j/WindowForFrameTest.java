package org.uispec4j;

import junit.framework.AssertionFailedError;
import org.uispec4j.utils.AssertionFailureNotDetectedError;

import javax.swing.*;
import java.awt.*;

public class WindowForFrameTest extends WindowTestCase {

  public void testIsModal() throws Exception {
    Window window = new Window(new Frame());
    assertFalse(window.isModal());
    try {
      assertTrue(window.isModal());
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
    }
  }

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

  protected boolean supportsMenuBars() {
    return false;
  }

  protected Window createWindowWithMenu(JMenuBar jMenuBar) {
    return null;
  }

  protected Window createWindowWithTitle(String title) {
    return new Window(new Frame(title));
  }

  protected void close(Window window) {
    Frame frame = (Frame)window.getAwtContainer();
    frame.dispose();
  }
}
