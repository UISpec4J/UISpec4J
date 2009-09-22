package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.UISpecAssert;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.utils.Utils;

import javax.swing.*;

/**
 * Wrapper for Multiple Desktop Interface (MDI) widgets implemented as JDesktopPane components.
 */
public class Desktop extends AbstractUIComponent {
  public static final String TYPE_NAME = "desktop";
  public static final Class[] SWING_CLASSES = {JDesktopPane.class};

  private final JDesktopPane jDesktopPane;

  public Desktop(JDesktopPane jDesktopPane) {
    this.jDesktopPane = jDesktopPane;
  }

  public JDesktopPane getAwtComponent() {
    return jDesktopPane;
  }

  public String getDescriptionTypeName() {
    return "desktop";
  }

  /**
   * Returs all the internal windows contained in the desktop.
   */
  public Window[] getWindows() {
    JInternalFrame[] allFrames = jDesktopPane.getAllFrames();
    Window[] result = new Window[allFrames.length];
    for (int i = 0; i < allFrames.length; i++) {
      result[i] = new Window(allFrames[i]);
    }
    return result;
  }

  public Assertion containsWindow(final String title) {
    return new Assertion() {
      public void check() {
        JInternalFrame[] allFrames = jDesktopPane.getAllFrames();
        for (JInternalFrame allFrame : allFrames) {
          if (Utils.equals(title, allFrame.getTitle())) {
            return;
          }
        }
        AssertAdapter.fail("No window with title '" + title + "' found");
      }
    };
  }

  /**
   * Returns a window given its title, and waits if it is not available yet.
   * This method will fail after a given timeout if the window is not found.
   *
   * @see UISpec4J#setWindowInterceptionTimeLimit(long)
   */
  public Window getWindow(String title) throws ComponentAmbiguityException {
    InternalFrameIntercepted assertion = new InternalFrameIntercepted(title);
    UISpecAssert.waitUntil(assertion, UISpec4J.getWindowInterceptionTimeLimit());
    return assertion.getResult();
  }

  private class InternalFrameIntercepted extends Assertion {

    private String windowTitle;
    private Window result;

    public InternalFrameIntercepted(String windowTitle) {
      this.windowTitle = windowTitle;
    }

    public void check() {
      Window[] windows = getWindows();
      for (int i = 0; i < windows.length; i++) {
        Window window = windows[i];
        if (windowTitle.equals(window.getTitle())) {
          if (result != null) {
            throw new ComponentAmbiguityException("There are several windows with title '" +
                                                  windowTitle + "'");
          }
          result = window;
        }
      }
      if (result == null) {
        throw new ItemNotFoundException("Window '" + windowTitle + "' not found");
      }
    }

    public Window getResult() {
      return result;
    }
  }
}