package org.uispec4j.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DummyActionListener implements ActionListener {
  private int callCount;

  public void actionPerformed(ActionEvent e) {
    callCount++;
  }

  public int getCallCount() {
    return callCount;
  }
}
