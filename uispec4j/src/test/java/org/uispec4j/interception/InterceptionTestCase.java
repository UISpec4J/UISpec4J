package org.uispec4j.interception;

import junit.framework.AssertionFailedError;
import org.uispec4j.UISpec4J;
import org.uispec4j.interception.toolkit.UISpecDisplay;
import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.utils.UnitTestCase;
import org.uispec4j.xml.EventLogger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TestCase which automatically enables UISpec4J's interception mechanism.
 * <p/>
 * This test case is useful when the default toolkit has already been initialized
 * before an UISpec4J test was run.
 * </p>
 */
public abstract class InterceptionTestCase extends UnitTestCase {
  protected EventLogger logger;

  protected void setUp() throws Exception {
    super.setUp();
    UISpecDisplay.instance().reset();
    logger = new EventLogger();
    UISpec4J.setWindowInterceptionTimeLimit(300);
  }

  protected void tearDown() throws Exception {
    super.tearDown();
    assertEquals(0, UISpecDisplay.instance().getHandlerCount());
    UISpecDisplay.instance().rethrowIfNeeded();
  }

  public static JDialog createModalDialog(String title) {
    return new JDialog(((JFrame)null), title, true);
  }

  protected static void checkAssertionFailedError(WindowInterceptor interceptor,
                                                  String errorMessage) {
    try {
      interceptor.run();
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals(errorMessage, e.getMessage());
    }
    catch (InterceptionError e) {
      if (!e.getMessage().equals(errorMessage)) {
        throw new InterceptionError("Unexpected error (expected '" + errorMessage + "'", e);
      }
    }
  }

  public static void createAndShowModalDialog(String title) {
    createModalDialog(title).setVisible(true);
  }

  protected void addLoggerButton(final RootPaneContainer container, final String buttonLabel) {
    JButton button = new JButton(buttonLabel);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        logger.log("click").add("button", buttonLabel);
      }
    });
    container.getContentPane().add(button);
  }

  protected void addHideButton(final JDialog dialog, final String buttonLabel) {
    JButton button = new JButton(new AbstractAction(buttonLabel) {
      public void actionPerformed(ActionEvent e) {
        logger.log("click").add("button", buttonLabel);
        dialog.setVisible(false);
      }
    });
    dialog.getContentPane().add(button);
  }

  protected void addShowDialogButton(final RootPaneContainer container,
                                     final String buttonLabel,
                                     final JDialog dialogToShow) {
    JButton button = new JButton(new AbstractAction(buttonLabel) {
      public void actionPerformed(ActionEvent e) {
        logger.log("click").add("button", buttonLabel);
        dialogToShow.setVisible(true);
      }
    });
    container.getContentPane().add(button);
  }

  protected void addShowDialogAndCloseButton(final JDialog container,
                                             final String buttonLabel,
                                             final JDialog dialogToShow) {
    JButton button = new JButton(new AbstractAction(buttonLabel) {
      public void actionPerformed(ActionEvent e) {
        logger.log("click").add("button", buttonLabel);
        container.setVisible(false);
        dialogToShow.setVisible(true);
      }
    });
    container.getContentPane().add(button);
  }
}
