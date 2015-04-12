package org.uispec4j.interception;

import org.uispec4j.*;
import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.UISpecAssert;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.interception.handlers.InterceptionHandler;
import org.uispec4j.interception.toolkit.UISpecDisplay;
import org.uispec4j.utils.TriggerRunner;

import javax.swing.*;

/**
 * Interceptor for (usually right-click triggered) pop-up menus.<p>
 * For instance, in the following code snippedt we right-click in the (2,4) cell of a table,
 * then click on the "Copy" item found in the popped-up menu:
 * <pre><code>
 * PopupMenuInterceptor
 *   .run(table.triggerRightClick(2, 4))
 *   .getSubMenu("Copy")
 *   .click();
 * </code></pre>
 *
 * @see <a href="http://www.uispec4j.org/intercepting-windows">Intercepting windows</a>
 */
public class PopupMenuInterceptor {

  /**
   * <p>Runs the given trigger and returns the intercepted popup menu.</p>
   * This method will wait for the popup to be shown, up to a time specified with
   * {@link UISpec4J#setWindowInterceptionTimeLimit(long)}.
   *
   * @throws Error if no popup is shown before the timeout expires.
   */
  public static MenuItem run(final Trigger trigger) {
    PopupHandler interceptor = new PopupHandler();
    final UISpecDisplay display = UISpecDisplay.instance();
    try {
      display.add(interceptor);
      display.setCurrentPopup(null);
      TriggerRunner.runInSwingThread(trigger);
      UISpecAssert.waitUntil("No popup was shown",
                             new Assertion() {
                               public void check() {
                                 if (display.getCurrentPopup() == null) {
                                   AssertAdapter.fail("No popup shown");
                                 }
                                 ;
                               }
                             },
                             UISpec4J.getWindowInterceptionTimeLimit());
      return new MenuItem(display.getCurrentPopup());
    }
    finally {
      display.remove(interceptor);
    }
  }

  private static class PopupHandler implements InterceptionHandler {
    MenuItem menu;

    public void process(Window window) {
      try {
        this.menu = new MenuItem((JPopupMenu)window.findSwingComponent(JPopupMenu.class));
      }
      catch (ItemNotFoundException e) {
      }
      catch (ComponentAmbiguityException e) {
      }
    }
  }
}
