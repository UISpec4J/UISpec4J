package org.uispec4j.interception;

import junit.framework.AssertionFailedError;
import org.uispec4j.Button;
import org.uispec4j.Trigger;
import org.uispec4j.UISpec4J;
import org.uispec4j.Window;
import org.uispec4j.interception.handlers.ShownInterceptionDetectionHandler;
import org.uispec4j.interception.toolkit.UISpecDisplay;
import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.utils.Utils;

import javax.swing.*;

public class WindowInterceptorForModalDialogsTest extends WindowInterceptorTestCase {
  private Thread thread;

  protected void tearDown() throws Exception {
    super.tearDown();
    if (thread != null) {
      thread.join();
      thread = null;
    }
  }

  public void testInterceptingAModalDialog() throws Exception {
    Window window = WindowInterceptor.getModalDialog(new Trigger() {
      public void run() {
        logger.log("triggerRun");
        JDialog dialog = createModalDialog("aDialog");
        addHideButton(dialog, "OK");
        dialog.show();
      }
    });
    logger.assertEquals("<log>" +
                        "  <triggerRun/>" +
                        "</log>");
    assertTrue(window.isVisible());
    window.getButton("OK").click();
    logger.assertEquals("<log>" +
                        "  <click button='OK'/>" +
                        "</log>");
    assertFalse(window.isVisible());
  }

  public void testInterceptingAFrame() throws Exception {
    try {
      WindowInterceptor.getModalDialog(new Trigger() {
        public void run() {
          new JFrame("aFrame").show();
        }
      });
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Window 'aFrame' is non-modal, it must be intercepted with WindowInterceptor.run(Trigger)",
                   e.getMessage());
    }
  }

  public void testInterceptingANonModalJDialog() throws Exception {
    try {
      WindowInterceptor.getModalDialog(new Trigger() {
        public void run() {
          JDialog dialog = new JDialog();
          dialog.setTitle("aDialog");
          dialog.show();
        }
      });
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Window 'aDialog' is non-modal, it must be intercepted with WindowInterceptor.run(Trigger)",
                   e.getMessage());
    }
  }

  public void testInterceptionWithATriggerThatDisplaysNothing() throws Exception {
    try {
      WindowInterceptor.getModalDialog(Trigger.DO_NOTHING);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals(ShownInterceptionDetectionHandler.NO_WINDOW_WAS_SHOWN_ERROR_MESSAGE,
                   e.getMessage());
    }
  }

  public void testTriggerExceptionsAreConvertedIntoInterceptionErrors() throws Exception {
    final Exception exception = new IllegalAccessException("error");
    try {
      WindowInterceptor.getModalDialog(new Trigger() {
        public void run() throws Exception {
          throw exception;
        }
      });
      throw new AssertionFailureNotDetectedError();
    }
    catch (RuntimeException e) {
      assertSame(exception, e.getCause());
    }
  }

  public void testTriggerExceptionsAreStoredAndRethrownWhenNotCaughtImmediately() throws Exception {
    final Exception exception = new RuntimeException("unexpected production code exception");
    Window window1 = WindowInterceptor.getModalDialog(new Trigger() {
      public void run() throws Exception {
        JDialog dialog = createModalDialog("dialog");
        addHideButton(dialog, "OK");
        dialog.show();
        JDialog dialog2 = createModalDialog("dialog2");
        addHideButton(dialog2, "OK");
        dialog2.show();
        throw exception;
      }
    });

    Window window2 = WindowInterceptor.getModalDialog(window1.getButton("OK").triggerClick());
    window2.titleEquals("dialog2");
    window2.getButton("OK").click();
    Utils.sleep(1);

    try {
      WindowInterceptor.run(new Trigger() {
        public void run() throws Exception {
          JDialog dialog3 = new JDialog();
          addHideButton(dialog3, "OK");
          dialog3.show();
        }
      });
      fail();
    }
    catch (Exception e) {
      assertSame(exception, e);
    }
  }

  public void testTriggerExceptionsAreStoredWhenNotCaughtImmediately2() throws Exception {
    final RuntimeException exception = new RuntimeException("unexpected production code exception");
    Window window = WindowInterceptor.getModalDialog(new Trigger() {
      public void run() throws Exception {
        JDialog dialog = createModalDialog("dialog");
        addHideButton(dialog, "OK");
        dialog.show();
        throw exception;
      }
    });

    window.getButton("OK").click();
    Utils.sleep(1);
    try {
      UISpecDisplay.instance().rethrowIfNeeded();
      fail();
    }
    catch (Exception e) {
      assertSame(exception, e);
    }
  }

  public void testInterceptingUsingAButtonTrigger() throws Exception {
    Button button = new Button(new JButton(new ShowDialogAction(true)));
    Window window = WindowInterceptor.getModalDialog(button.triggerClick());
    window.titleEquals("MyDialog");
  }

  public void testInterceptingAJDialogShownFromAnotherThread() throws Exception {
    Window window = WindowInterceptor.getModalDialog(new Trigger() {
      public void run() throws Exception {
        thread = new Thread() {
          public void run() {
            JDialog dialog = createModalDialog("expected title");
            addHideButton(dialog, "OK");
            dialog.show();
          }
        };
        thread.start();
      }
    });
    window.titleEquals("expected title");
    window.getButton("OK").click();
    assertFalse(window.isVisible());
  }

  public void disabled_testInterceptingAModalDialogShownFromAnotherThread() throws Exception {
    showModalDialogInThread(200, 100);
    logger.assertEquals("<log>" +
                        "  <triggerRun/>" +
                        "</log>");
  }

  private void showModalDialogInThread(int waitWindowTimeLimit, final int waitTimeInThread) {
    final JDialog dialog = new JDialog(new JFrame(), "dialogShownInThread", true);
    UISpec4J.setWindowInterceptionTimeLimit(waitWindowTimeLimit);
    assertNotNull(WindowInterceptor.getModalDialog(new Trigger() {
      public void run() {
        logger.log("triggerRun");
        thread = new Thread(new Runnable() {
          public void run() {
            Utils.sleep(waitTimeInThread);
            dialog.show();
          }
        });
        thread.setName(thread.getName() + "(" + getName() + ")");
        thread.start();
      }
    }));
  }
}
