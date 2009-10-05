package org.uispec4j.interception;

import org.uispec4j.*;
import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.utils.Functor;
import org.uispec4j.utils.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class PopupMenuInterceptorTest extends InterceptionTestCase {

  public void testStandardUsageWithHeavyweightPopup() throws Exception {
    JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    checkStandardUsage();
  }

  public void testStandardUsageWithLightweightPopup() throws Exception {
    JPopupMenu.setDefaultLightWeightPopupEnabled(true);
    checkStandardUsage();
  }

  private void checkStandardUsage() {
    PopupDisplayTrigger trigger = getPopupTrigger();
    launchInterception(trigger);
    logger.assertEquals("<log>" +
                        "<click popupMenuItem='item 1'/>" +
                        "</log>");
  }

  public void testRetryStrategy() throws Exception {
    checkInterceptionAfterSomeTime(false);
    logger.assertEquals("<log>" +
                        "<click popupMenuItem='item 1'/>" +
                        "</log>");
  }

  public void testRetryStrategyWithAnotherTimeout() throws Exception {
    checkInterceptionAfterSomeTime(true);
  }

  private void checkInterceptionAfterSomeTime(boolean exceedsTimeLimit) throws Exception {
    final int delay = 20;
    final PopupDisplayTrigger trigger = getPopupTrigger();
    final Thread thread = new Thread() {
      public void run() {
        Utils.sleep(delay);
        try {
          trigger.run();
        }
        catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    };

    UISpec4J.setWindowInterceptionTimeLimit((exceedsTimeLimit) ? delay / 2 : delay * 2);
    try {
      launchInterception(new Trigger() {
        public void run() throws Exception {
          thread.start();
        }
      });
    }
    catch (Throwable e) {
      if (exceedsTimeLimit) {
        UISpec4J.setWindowInterceptionTimeLimit(delay);
        launchInterception(Trigger.DO_NOTHING);
        assertEquals("No popup was shown", e.getMessage());
      }
      else {
        throw new Exception(e);
      }
    }
    finally {
      thread.join();
    }
  }

  private PopupDisplayTrigger getPopupTrigger() {
    final JButton button = new JButton();
    showLargeDialogWithComponent(button);
    return new PopupDisplayTrigger(button);
  }

  public void testAnErrorIsRaisedIfTheTriggerDoesNotPopupAMenu() throws Exception {
    checkAssertionFailedError(new Functor() {
      public void run() throws Exception {
        PopupMenuInterceptor.run(Trigger.DO_NOTHING);
      }
    }, "No popup was shown");
  }

  public void testExceptionRaisedWhenAPopupAppearsWithoutInterceptionOnMacOsX() throws Throwable {
    if (!TestUtils.isMacOsX()) {
      return;
    }
    final JButton button = new JButton();
    showLargeDialogWithComponent(button);
    try {
      new PopupDisplayTrigger(button).run();
      throw new AssertionFailureNotDetectedError();
    }
    catch (Error e) {
      assertTrue(e.getMessage().startsWith("Unexpected window shown - this window should be handled with WindowInterceptor."));
    }
  }
  
  public void testNoExceptionRaisedWhenAPopupAppearsWithoutInterception() throws Throwable {
    if (TestUtils.isMacOsX()) {
      return;
    }
    final JButton button = new JButton();
    showLargeDialogWithComponent(button);
    new PopupDisplayTrigger(button).run();
  }

  public void testExceptionRaisedByTheTriggerAreConvertedIntoRuntimeExceptions() throws Exception {
    try {
      PopupMenuInterceptor.run(new Trigger() {
        public void run() throws Exception {
          throw new IOException("msg");
        }
      });
      throw new AssertionFailureNotDetectedError();
    }
    catch (RuntimeException e) {
      assertEquals("msg", e.getCause().getMessage());
    }
  }

  public void testHandlingADialogShownByAPopupMenu() throws Exception {
    final JPanel panel = new JPanel();
    showLargeDialogWithComponent(panel);

    final JButton button = new JButton(new AbstractAction("ok") {
      public void actionPerformed(ActionEvent e) {
        logger.log("ok");
      }
    });

    WindowInterceptor
      .init(PopupMenuInterceptor
        .run(new Trigger() {
          public void run() throws Exception {
            JPopupMenu menu = new JPopupMenu();
            menu.add(new AbstractAction("menu") {
              public void actionPerformed(ActionEvent e) {
                logger.log("dialog");
                JDialog dialog = new JDialog();
                dialog.getContentPane().add(button);
                dialog.setVisible(true);
              }
            });
            menu.show(panel, 10, 10);
          }
        })
        .getSubMenu("menu")
        .triggerClick())
      .process(new WindowHandler() {
        public Trigger process(Window window) throws Exception {
          logger.log("handleWindow");
          return window.getButton("ok").triggerClick();
        }
      })
      .run();

    logger.assertEquals("<log>" +
                        "  <dialog/>" +
                        "  <handleWindow/>" +
                        "  <ok/>" +
                        "</log>");
  }

  public void testPopupMenuShownFromATree() throws Exception {
    final JTree jTree = new JTree();
    jTree.addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        JPopupMenu menu = new JPopupMenu();
        menu.add(new AbstractAction("Run") {
          public void actionPerformed(ActionEvent e) {
            logger.log("click");
          }
        });
        menu.show(jTree, 5, 5);
      }
    });
    final JDialog dialog = new JDialog(new JFrame(), "dialog");
    addHideButton(dialog, "Close");
    dialog.getContentPane().add(jTree);

    WindowInterceptor
      .init(new Trigger() {
        public void run() throws Exception {
          dialog.setVisible(true);
        }
      })
      .process(new WindowHandler() {
        public Trigger process(final Window window) throws Exception {
          PopupMenuInterceptor
            .run(window.getTree().triggerClick(""))
            .getSubMenu("Run")
            .click();
          return window.getButton("Close").triggerClick();
        }
      })
      .run();

    logger.assertEquals("<log>" +
                        "  <click/>" +
                        "  <click button='Close'/>" +
                        "</log>");
  }

  private void launchInterception(Trigger trigger) {
    MenuItem menu = PopupMenuInterceptor.run(trigger);
    assertNotNull(menu);
    assertTrue(menu.contentEquals(new String[]{"item 1", "item 2"}));
    menu.getSubMenu("item 1").click();
  }

  private void showLargeDialogWithComponent(final JComponent component) {
    WindowInterceptor.run(new Trigger() {
      public void run() throws Exception {
        JDialog dialog = new JDialog();
        dialog.getContentPane().add(component);
        // Setting a large size for the dialog forces the use of lightweight popups
        // if this feature was enabled
        dialog.setSize(2000, 2000);
        dialog.setVisible(true);
      }
    });
  }

  private class PopupDisplayTrigger implements Trigger {
    private JComponent component;

    public PopupDisplayTrigger(JComponent c) {
      component = c;
    }

    public void run() throws Exception {
      JPopupMenu menu = new JPopupMenu();
      menu.add(new AbstractAction("item 1") {
        public void actionPerformed(ActionEvent e) {
          logger.log("click").add("popupMenuItem", "item 1");
        }
      });
      menu.add("item 2");
      menu.show(component, 10, 10);
    }
  }
}
