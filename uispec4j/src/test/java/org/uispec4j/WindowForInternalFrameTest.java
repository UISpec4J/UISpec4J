package org.uispec4j;

import javax.swing.*;

/**
 * Test class for {@link Window}.
 */
public class WindowForInternalFrameTest extends WindowTestCase {
  
  public void testIsModal() throws Exception {
    Window window = new Window(new JInternalFrame());
    checkIsModal(window, false);
  }

  protected Window createWindowWithMenu(JMenuBar jMenuBar) {
    JInternalFrame frame = new JInternalFrame();
    frame.setJMenuBar(jMenuBar);
    return new Window(frame);
  }

  protected Window createWindowWithTitle(String title) {
    return new Window(new JInternalFrame(title));
  }

  protected void show(Window window) {
    JInternalFrame internalFrame = (JInternalFrame)window.getAwtContainer();
    JDialog dialog = new JDialog();
    dialog.getContentPane().add(internalFrame);
    internalFrame.setVisible(true);
    dialog.setVisible(true);
  }

  protected void close(Window window) {
    JInternalFrame frame = (JInternalFrame)window.getAwtContainer();
    frame.dispose();
  }
}
