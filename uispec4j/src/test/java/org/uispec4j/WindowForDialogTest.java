package org.uispec4j;

import javax.swing.*;

public class WindowForDialogTest extends WindowTestCase {

  public void testIsModalWithNonModalDialog() throws Exception {
    Window window = new Window(new JDialog());
    checkIsModal(window, false);
  }

  public void testIsModalWithModalDialog() throws Exception {
    JFrame frame = new JFrame();
    Window window = new Window(new JDialog(frame, true));
    checkIsModal(window, true);
  }

  protected Window createWindowWithMenu(JMenuBar jMenuBar) {
    JDialog dialog = new JDialog();
    dialog.setJMenuBar(jMenuBar);
    return new Window(dialog);
  }

  protected Window createWindowWithTitle(String title) {
    JDialog dialog = new JDialog();
    dialog.setTitle(title);
    return new Window(dialog);
  }

  protected void close(Window window) {
    JDialog dialog = (JDialog)window.getAwtContainer();
    dialog.dispose();
  }
}
