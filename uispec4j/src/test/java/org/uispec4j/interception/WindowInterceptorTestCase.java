package org.uispec4j.interception;

import org.uispec4j.Trigger;

import javax.swing.*;
import java.awt.event.ActionEvent;

public abstract class WindowInterceptorTestCase extends InterceptionTestCase {
  protected Trigger getShowFirstDialogTrigger() {
    return new Trigger() {
      public void run() throws Exception {
        JDialog firstDialog = createDialogs();
        logger.log("trigger");
        firstDialog.setVisible(true);
      }
    };
  }

  private JDialog createDialogs() {
    final JFrame frame = new JFrame();

    final JDialog firstDialog = new JDialog(frame, "first dialog", true);
    addHideButton(firstDialog, "Dispose");

    JDialog secondDialog = new JDialog(frame, "second dialog", true);
    addShowDialogAndCloseButton(firstDialog, "OK", secondDialog);
    addHideButton(secondDialog, "Dispose");

    JDialog thirdDialog = new JDialog(frame, "third dialog", true);
    addHideButton(thirdDialog, "Dispose");
    addShowDialogAndCloseButton(secondDialog, "OK", thirdDialog);
    return firstDialog;
  }

  protected static class ShowDialogAction extends AbstractAction {
    private boolean modal;

    public ShowDialogAction(boolean modal) {
      super("Run");
      this.modal = modal;
    }

    public void actionPerformed(ActionEvent e) {
      JDialog dialog = new JDialog();
      dialog.setModal(modal);
      dialog.setTitle("MyDialog");
      dialog.setVisible(true);
    }
  }
}
