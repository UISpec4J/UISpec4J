package org.uispec4j.interception;

import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.utils.Functor;
import org.uispec4j.utils.Utils;

import javax.swing.*;

public class WindowInterceptorCustomMethodsTest extends WindowInterceptorTestCase {

  public void testProcessTransientWindow() throws Exception {
    WindowInterceptor
      .init(new TransientWindowTrigger())
      .processTransientWindow("Actual")
      .run();
  }

  public void testProcessTransientWindowWithNoTitle() throws Exception {
    WindowInterceptor
      .init(new TransientWindowTrigger())
      .processTransientWindow()
      .run();
  }

  public void testProcessTransientWindowError() throws Exception {
    checkInterceptionError(new Functor() {
      public void run() throws Exception {
        WindowInterceptor
          .init(new TransientWindowTrigger())
          .processTransientWindow("Expected")
          .run();
      }
    }, "Invalid window title - expected:<Expected> but was:<Actual>");
  }

  public void testWindowTitleChecking() throws Exception {
    WindowInterceptor
      .init(new Trigger() {
        public void run() throws Exception {
          JDialog dialog = createModalDialog("dialog title");
          addHideButton(dialog, "Hide");
          dialog.setVisible(true);
        }
      })
      .process("dialog title", new ButtonTriggerHandler("Hide"))
      .run();
    logger.assertEquals("<log>" +
                        "  <click button='Hide'/>" +
                        "</log>");
  }

  public void testWindowTitleError() throws Exception {
    checkAssertionFailedError(
      WindowInterceptor
        .init(new Trigger() {
          public void run() throws Exception {
            JDialog dialog = createModalDialog("dialog title");
            addHideButton(dialog, "Hide");
            dialog.setVisible(true);
          }
        })
        .process("error", new ButtonTriggerHandler("Hide")),
      "Unexpected title - expected:<error> but was:<dialog title>");
  }

  public void testWindowTitleErrorInASequence() throws Exception {
    checkAssertionFailedError(
      WindowInterceptor
        .init(getShowFirstDialogTrigger())
        .processWithButtonClick("OK")
        .process("error", new ButtonTriggerHandler("OK")),
      "Error in handler 'error': Unexpected title - expected:<error> but was:<second dialog>");
  }

  public void testProcessWithButtonClick() {
    WindowInterceptor
      .init(getShowFirstDialogTrigger())
      .processWithButtonClick("OK")
      .processWithButtonClick("second dialog", "OK")
      .processWithButtonClick("Dispose")
      .run();
    logger.assertEquals("<log>" +
                        "  <trigger/>" +
                        "  <click button='OK'/>" +
                        "  <click button='OK'/>" +
                        "  <click button='Dispose'/>" +
                        "</log>");
  }

  public void testProcessSeveralHandlers() throws Exception {
    WindowInterceptor
      .init(getShowFirstDialogTrigger())
      .process(new WindowHandler[]{
        new ButtonTriggerHandler("OK"),
        new ButtonTriggerHandler("OK"),
        new ButtonTriggerHandler("Dispose"),
      })
      .run();
    logger.assertEquals("<log>" +
                        "  <trigger/>" +
                        "  <click button='OK'/>" +
                        "  <click button='OK'/>" +
                        "  <click button='Dispose'/>" +
                        "</log>");
  }

  private static class ButtonTriggerHandler extends WindowHandler {
    private String buttonName;

    public ButtonTriggerHandler(String buttonName) {
      this.buttonName = buttonName;
    }

    public Trigger process(Window window) throws Exception {
      return window.getButton(buttonName).triggerClick();
    }
  }

  public void testProcessWithButtonClickWithAnUnknownButtonName() throws Exception {
    checkAssertionFailedError(WindowInterceptor
      .init(getShowFirstDialogTrigger())
      .processWithButtonClick("unknown"),
                              "Component 'unknown' of type 'button' not found - available names: [Dispose,OK]");
  }

  public void testProcessWithButtonClickHandlesJOptionPaneDialogs() throws Exception {
    final JFrame frame = new JFrame();
    WindowInterceptor.run(new Trigger() {
      public void run() throws Exception {
        frame.setVisible(true);
      }
    });
    WindowInterceptor
      .init(new Trigger() {
        public void run() throws Exception {
          int result = JOptionPane.showConfirmDialog(frame, "msg");
          logger.log("confirm").add("result", result);
        }
      })
      .processWithButtonClick("Yes")
      .run();
    logger.assertEquals("<log>" +
                        "  <confirm result='0'/>" +
                        "</log>");
  }

  public void testProcessWithButtonClickWithAnInvalidTitle() throws Exception {
    checkInterceptionError(new Functor() {
      public void run() throws Exception {
        WindowInterceptor
          .init(new Trigger() {
            public void run() throws Exception {
              JDialog dialog = new JDialog(new JFrame(), "Actual");
              addHideButton(dialog, "ok");
              dialog.setVisible(true);
            }
          })
          .processWithButtonClick("Expected", "OK")
          .run();
      }
    }, "Invalid window title - expected:<Expected> but was:<Actual>");
  }

  private static class TransientWindowTrigger implements Trigger {
    public void run() throws Exception {
      JDialog dialog = new JDialog(new JFrame(), "Actual");
      Thread thread = new Thread() {
        public void run() {
          Utils.sleep(20);
        }
      };
      thread.run();
      dialog.setVisible(true);
      thread.join();
    }
  }
}
