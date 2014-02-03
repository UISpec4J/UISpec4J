package org.uispec4j;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import junit.framework.AssertionFailedError;
import org.uispec4j.utils.AssertionFailureNotDetectedError;

public class WindowForJFrameTest extends WindowTestCase {

  public void testIsModal() throws Exception {
    Window window = new Window(new JFrame());
    assertFalse(window.isModal());
    try {
      assertTrue(window.isModal());
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
    }
  }

  protected boolean supportsMenuBars() {
    return true;
  }

  protected Window createWindowWithMenu(JMenuBar jMenuBar) {
    JFrame frame = new JFrame();
    frame.setJMenuBar(jMenuBar);
    return new Window(frame);
  }

  protected Window createWindowWithTitle(String title) {
    return new Window(new JFrame(title));
  }

  protected void close(Window window) {
    JFrame frame = (JFrame)window.getAwtContainer();
    frame.dispose();
  }
}
