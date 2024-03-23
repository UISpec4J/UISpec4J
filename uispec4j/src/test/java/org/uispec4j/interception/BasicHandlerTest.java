package org.uispec4j.interception;

import org.uispec4j.Trigger;
import org.uispec4j.Window;

import javax.swing.*;

public class BasicHandlerTest extends InterceptionTestCase {

  public void testStandardUsage() throws Exception {
    WindowInterceptor
      .init(triggerShowDialog())
      .process(BasicHandler.init()
        .assertTitleEquals("Dialog title")
        .assertTitleContains("title")
        .assertContainsText("some text")
        .clickButton("OK")
        .triggerButtonClick("Hide"))
      .run();
    logger.assertEquals("<log>" +
                        "  <click button='OK'/>" +
                        "  <click button='Hide'/>" +
                        "</log>");
  }

  public void testTitleEqualsError() throws Exception {
    checkAssertionError(
      WindowInterceptor
        .init(triggerShowDialog())
        .process(BasicHandler
        .init()
        .assertTitleEquals("Error")
        .triggerButtonClick("Hide")),
      "Unexpected title - expected:<[Error]> but was:<[Dialog title]>");
  }

  public void testTitleContainsError() throws Exception {
    checkAssertionError(
      WindowInterceptor
        .init(triggerShowDialog())
        .process(BasicHandler
        .init()
        .assertTitleContains("Error")
        .triggerButtonClick("Hide")),
      "expected to contain:<Error> but was:<Dialog title>");
  }

  public void testAssertContainsTextError() throws Exception {
    checkAssertionError(WindowInterceptor
      .init(triggerShowDialog())
      .process(BasicHandler
      .init()
      .assertContainsText("Error")
      .triggerButtonClick("Hide")),
                              "Text not found: Error");
  }

  public void testClickButtonError() throws Exception {
    checkAssertionError(WindowInterceptor
      .init(triggerShowDialog())
      .process(BasicHandler
      .init()
      .clickButton("Unknown")
      .triggerButtonClick("Hide")),
                              "Component 'Unknown' of type 'button' not found - available names: [Hide,OK]");
  }

  public void testJOptionPaneConfirmationReplies() throws Exception {
    checkSelectedValue(JOptionPane.YES_OPTION, JOptionPane.YES_NO_OPTION, getLocalLabel("OptionPane.yesButtonText"));
    checkSelectedValue(JOptionPane.NO_OPTION, JOptionPane.YES_NO_OPTION, getLocalLabel("OptionPane.noButtonText"));
    checkSelectedValue(JOptionPane.OK_OPTION, JOptionPane.OK_CANCEL_OPTION, getLocalLabel("OptionPane.okButtonText"));
    checkSelectedValue(JOptionPane.CANCEL_OPTION, JOptionPane.OK_CANCEL_OPTION, getLocalLabel("OptionPane.cancelButtonText"));
  }

  public void testSetInputInJOptionPane() throws Exception {
    WindowInterceptor
      .init(new Trigger() {
        public void run() throws Exception {
          assertEquals("result", JOptionPane.showInputDialog("Message"));
        }
      })
      .process(BasicHandler.init()
        .setText("result")
        .triggerButtonClick("OK"))
      .run();
  }

  public void testSetInputWithNullValueInJOptionPane() throws Exception {
    WindowInterceptor
      .init(new Trigger() {
        public void run() throws Exception {
          assertEquals("", JOptionPane.showInputDialog("Message"));
        }
      })
      .process(BasicHandler.init()
        .setText(null)
        .triggerButtonClick("OK"))
      .run();
  }

  /* This is not a feature, but a known limitation */
  public void testSetInputFollowedByACancelInJOptionPaneReturnsTheInputValue() throws Exception {
    WindowInterceptor
      .init(new Trigger() {
        public void run() throws Exception {
          assertEquals("Result", JOptionPane.showInputDialog("Message"));
        }
      })
      .process(BasicHandler.init()
        .setText("Result")
        .triggerButtonClick(getLocalLabel("OptionPane.cancelButtonText")))
      .run();
  }

  public void testInterceptingAJOptionPaneFromInsideATrigger() throws Exception {
    final JFrame frame = new JFrame();
    WindowInterceptor
      .init(new Trigger() {
        public void run() throws Exception {
          WindowInterceptor
            .init(new Trigger() {
              public void run() throws Exception {
                int result = JOptionPane.showConfirmDialog(frame, "OK?",
                                                           "Title",
                                                           JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                  logger.log("showDialog");
                  JDialog dialog = new JDialog(frame, "Dialog", true);
                  dialog.setVisible(true);
                }
                else {
                  throw new Error("unexpected result " + result);
                }
              }
            })
            .process(BasicHandler.init().triggerButtonClick("OK"))
            .run();
        }
      })
      .process(new WindowHandler() {
        public Trigger process(final Window window) {
          logger.log("dialogShown").add("title", window.getTitle());
          return new Trigger() {
            public void run() throws Exception {
              window.getAwtContainer().setVisible(false);
            }
          };
        }
      })
      .run();
    logger.assertEquals("<log>" +
                        "  <showDialog/>" +
                        "  <dialogShown title='Dialog'/>" +
                        "</log>");
  }

  public void testJOptionPaneInterceptionInAWindowSequence() throws Exception {
    final JFrame frame = new JFrame();
    WindowInterceptor
      .init(new Trigger() {
        public void run() throws Exception {
          int result = JOptionPane.showConfirmDialog(frame, "Confirm?", "Title", JOptionPane.YES_NO_OPTION);
          if (result == JOptionPane.YES_OPTION) {
            logger.log("start");
            JDialog dialog = new JDialog(frame, "dialog", true);
            addHideButton(dialog, "Close");
            dialog.setVisible(true);
            logger.log("end");
          }
          else {
            throw new Error("Unexpected result " + result);
          }
        }
      })
      .process(BasicHandler.init()
        .assertContainsText("Confirm?")
        .triggerButtonClick(getLocalLabel("OptionPane.yesButtonText")))
      .processWithButtonClick("Close")
      .run();
    logger.assertEquals("<log>" +
                        "  <start/>" +
                        "  <click button='Close'/>" +
                        "  <end/>" +
                        "</log>");
  }

  private void checkSelectedValue(final int value, final int optionType, String button) {
    WindowInterceptor
      .init(new Trigger() {
        public void run() throws Exception {
          assertEquals(value,
                       JOptionPane.showConfirmDialog(new JFrame(), "msg", "title",
                                                     optionType,
                                                     JOptionPane.WARNING_MESSAGE));
        }
      })
      .process(BasicHandler.init().triggerButtonClick(button))
      .run();
  }

  private Trigger triggerShowDialog() {
    return new Trigger() {
      public void run() throws Exception {
        JDialog dialog = createModalDialog("aDialog");
        dialog.setTitle("Dialog title");
        dialog.getContentPane().add(new JTextArea("some text"));
        addHideButton(dialog, "Hide");
        addLoggerButton(dialog, "OK");
        dialog.setVisible(true);
      }
    };
  }
}
