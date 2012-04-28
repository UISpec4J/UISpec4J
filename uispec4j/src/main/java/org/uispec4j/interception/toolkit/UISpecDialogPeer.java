package org.uispec4j.interception.toolkit;

import org.uispec4j.Window;
import org.uispec4j.interception.toolkit.empty.DialogPeer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class UISpecDialogPeer extends DialogPeer {
  private Toolkit toolkit;
  private JDialog dialog;
  private boolean listenerRegistered;

  public UISpecDialogPeer(Toolkit toolkit, JDialog dialog) {
    this.toolkit = toolkit;
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
    return toolkit;
  }
}

