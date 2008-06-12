package org.uispec4j.extension;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JCountingButton extends JButton {
  private int count;

  public JCountingButton(String text) {
    super(text);
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        count++;
      }
    });
  }

  public void reset() {
    count = 0;
  }

  public int getCount() {
    return count;
  }
}
