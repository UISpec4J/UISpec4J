package org.uispec4j.interception;

import org.uispec4j.Trigger;

import javax.swing.*;

public class PopupMenuInterceptionTest extends InterceptionTestCase {
  public void test() throws Exception {
    String[] items = {"item 1", "item 2"};
    assertTrue(PopupMenuInterceptor
      .run(new PopupTrigger(items))
      .contentEquals(items));
  }

  private static class PopupTrigger implements Trigger {
    JPopupMenu popup = new JPopupMenu();

    public PopupTrigger(String[] items) {
      for (int i = 0; i < items.length; i++) {
        popup.add(items[i]);
      }
    }

    public void run() throws Exception {
      popup.show(null, 100, 100);
    }
  }
}
