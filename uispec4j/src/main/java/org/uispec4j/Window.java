package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.UISpecAssert;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.xml.XmlWriter;

import javax.swing.*;
import java.awt.*;

/**
 * Wrapper for window components such as JFrame, JDialog, JInternalFrame.
 */
public class Window extends Panel {

  public static final String TYPE_NAME = "Window";
  public static final Class[] SWING_CLASSES = {JFrame.class,
                                               JDialog.class,
                                               JInternalFrame.class,
                                               Frame.class,
                                               java.awt.Window.class};
  private final Adapter adapter;

  public Window(JFrame frame) {
    super(frame);
    this.adapter = new JFrameAdapter(frame);
  }

  public Window(JDialog dialog) {
    super(dialog);
    this.adapter = new JDialogAdapter(dialog);
  }

  public Window(JInternalFrame frame) {
    super(frame);
    this.adapter = new JInternalFrameAdapter(frame);
  }

  public Window(Frame frame) {
    super(frame);
    this.adapter = new FrameAdapter(frame);
  }

  public Window(java.awt.Window window) {
    super(window);
    this.adapter = new WindowAdapter(window);
  }

  public String getDescriptionTypeName() {
    return "window";
  }

  protected void addAttributes(Component component, XmlWriter.Tag tag) {
    tag.addAttribute("title", adapter.getTitle());
  }

  public Assertion containsMenuBar() {
    return new Assertion() {
      public void check() {
        if (adapter.getJMenuBar() == null) {
          AssertAdapter.fail("No menuBar available");
        }
      }
    };
  }

  public MenuBar getMenuBar() {
    return new MenuBar(adapter.getJMenuBar());
  }

  public String getTitle() {
    return adapter.getTitle();
  }

  public Assertion titleEquals(final String expected) {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertEquals("Unexpected title -", expected, getTitle());
      }
    };
  }

  public Assertion titleContains(final String expected) {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertTrue("expected to contain:<" + expected + "> but was:<" + getTitle() + ">",
                                  getTitle().contains(expected));
      }
    };
  }

  /** @deprecated
   * @see #titleEquals(String) */
  public void assertTitleEquals(String expected) {
    UISpecAssert.assertTrue(titleEquals(expected));
  }

  /** @deprecated
   * @see #titleContains(String) */
  public void assertTitleContains(String expected) {
    UISpecAssert.assertTrue(titleContains(expected));
  }

  protected void getSubDescription(Container container, XmlWriter.Tag tag) {
    Container internalAwtContainer = adapter.getInternalAwtContainer();
    Panel contentPane = new Panel(internalAwtContainer);
    contentPane.getSubDescription(internalAwtContainer, tag);
  }

  public Container getInternalAwtContainer() {
    return adapter.getInternalAwtContainer();
  }

  public Assertion isModal() {
    return new Assertion() {
      public void check() {
        AssertAdapter.assertTrue(adapter.isModal());
      }
    };
  }

  /** Closes the window by calling its internal <code>dispose()</code> method. This is mainly used for unit
   * tests, when there is no "functional" way to close the window (for instance a "Close" button) */
  public void dispose() {
    adapter.dispose();
  }

  static interface Adapter {
    JMenuBar getJMenuBar();

    String getTitle();

    boolean isModal();

    Container getInternalAwtContainer();

    void dispose();
  }

  private static class JInternalFrameAdapter implements Adapter {
    private final JInternalFrame frame;

    JInternalFrameAdapter(JInternalFrame frame) {
      this.frame = frame;
    }

    public JMenuBar getJMenuBar() {
      return frame.getJMenuBar();
    }

    public String getTitle() {
      return frame.getTitle();
    }

    public boolean isModal() {
      return false;
    }

    public Container getInternalAwtContainer() {
      return frame.getContentPane();
    }

    public void dispose() {
      frame.dispose();
    }
  }

  private static class JFrameAdapter implements Adapter {
    private final JFrame frame;

    JFrameAdapter(JFrame frame) {
      this.frame = frame;
    }

    public JMenuBar getJMenuBar() {
      return frame.getJMenuBar();
    }

    public String getTitle() {
      return frame.getTitle();
    }

    public boolean isModal() {
      return false;
    }

    public Container getInternalAwtContainer() {
      return frame.getContentPane();
    }

    public void dispose() {
      frame.dispose();
    }
  }

  private static class JDialogAdapter implements Adapter {
    private final JDialog dialog;

    JDialogAdapter(JDialog dialog) {
      this.dialog = dialog;
    }

    public JMenuBar getJMenuBar() {
      return dialog.getJMenuBar();
    }

    public String getTitle() {
      return dialog.getTitle();
    }

    public boolean isModal() {
      return dialog.isModal();
    }

    public Container getInternalAwtContainer() {
      return dialog.getContentPane();
    }

    public void dispose() {
      dialog.dispose();
    }
  }

  private static class FrameAdapter implements Adapter {
    Frame frame;

    public FrameAdapter(Frame frame) {
      this.frame = frame;
    }

    public JMenuBar getJMenuBar() {
      AssertAdapter.fail("This component has no menu bar");
      return null;
    }

    public String getTitle() {
      return frame.getTitle();
    }

    public boolean isModal() {
      return false;
    }

    public Container getInternalAwtContainer() {
      return frame;
    }

    public void dispose() {
      frame.dispose();
    }
  }

  private static class WindowAdapter implements Adapter {

    private java.awt.Window window;

    public WindowAdapter(java.awt.Window window) {
      this.window = window;
    }

    public JMenuBar getJMenuBar() {
      AssertAdapter.fail("This component has no menu bar");
      return null;
    }

    public String getTitle() {
      return "";
    }

    public boolean isModal() {
      return false;
    }

    public Container getInternalAwtContainer() {
      return window;
    }

    public void dispose() {
      window.dispose();
    }
  }
}
