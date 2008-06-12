package org.uispec4j.interception;

import org.uispec4j.Trigger;

import javax.swing.*;

class JComponentDisplayTrigger implements Trigger {
  JComponent component;

  public JComponentDisplayTrigger(JComponent component) {
    this.component = component;
  }

  public void run() {
    JDialog dialog = InterceptionTestCase.createModalDialog("aDialog");
    dialog.getContentPane().add(component);
    dialog.setVisible(true);
  }
}
