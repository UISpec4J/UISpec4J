package org.uispec4j;

import org.uispec4j.interception.WindowInterceptor;

/**
 * Interface for window interception triggers - UI interactions which display a window.<p>
 * Ready-to-use triggers are made available by several components such as Buttons or Menus.
 *
 * @see Button#triggerClick
 * @see MenuItem#triggerClick
 * @see WindowInterceptor
 */
public interface Trigger {
  void run() throws Exception;

  public static final Trigger DO_NOTHING = new Trigger() {
    public void run() throws Exception {
    }
  };
}
