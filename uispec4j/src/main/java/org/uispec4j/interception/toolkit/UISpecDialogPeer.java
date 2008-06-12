package org.uispec4j.interception.toolkit;

import org.uispec4j.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class UISpecDialogPeer extends Empty.DialogPeer {
  private JDialog dialog;
  private boolean listenerRegistered;

  public UISpecDialogPeer(JDialog dialog) {
    this.dialog = dialog;
  }

  public void show() {
    try {
      UISpecDisplay.instance().assertAcceptsWindow(new Window(dialog));
    }
    catch (Error t) {
      if (SwingUtilities.isEventDispatchThread()) {
        dialog.setVisible(false);
        return;
      }
      else {
        throw t;
      }
    }
    if (!listenerRegistered) {
      dialog.addComponentListener(new ComponentAdapter() {
        public void componentShown(ComponentEvent e) {
          try {
            UISpecDisplay.instance().showDialog(dialog);
          }
          catch (Throwable t) {
            throw new RuntimeException(t);
          }
        }
      });
      listenerRegistered = true;
    }
  }

  public Toolkit getToolkit() {
    return UISpecToolkit.instance();
  }
}

