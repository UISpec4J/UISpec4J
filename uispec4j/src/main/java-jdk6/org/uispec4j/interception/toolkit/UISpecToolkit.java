package org.uispec4j.interception.toolkit;

import org.uispec4j.UISpec4J;

import javax.swing.*;
import java.awt.*;
import java.awt.im.spi.InputMethodDescriptor;
import java.awt.peer.*;

/**
 * Mock toolkit used for intercepting displayed frames and dialogs.<p>
 * You can set it up by calling the {@link #setUp()} method.
 *
 * @see <a href="http://www.uispec4j.org/intercepting-windows">Intercepting windows</a>
 */
public class UISpecToolkit extends ToolkitDelegate {
  static final String SYSTEM_PROPERTY = "awt.toolkit";
  static final String UNIX_SYSTEM_DEFAULT_VALUE = System.getProperty("awt.toolkit", "sun.awt.X11.XToolkit");
  static final String WINDOWS_SYSTEM_DEFAULT_VALUE = "sun.awt.windows.WToolkit";

  private static String awtToolkit;

  public UISpecToolkit() {
    setUp();
  }

  /**
   * @see UISpec4J#init
   * @deprecated Do not call this one directly anymore - use {@link UISpec4J#init} instead
   */
  public static void setUp() {
    if (underlyingToolkit != null) {
      return;
    }
    awtToolkit = System.getProperty(SYSTEM_PROPERTY);
    if (awtToolkit == null) {
      setAwtToolkitProperty();
    }
    buildUnderlyingToolkit(awtToolkit);
    System.setProperty(SYSTEM_PROPERTY, UISpecToolkit.class.getName());
  }

  /**
   * Sets the <code>awt.toolkit</code> to its initial value.
   * <p>This method will only work properly if the toolkit has not yet been instanciated by Swing.
   */
  public static void restoreAwtToolkit() {
    System.setProperty(SYSTEM_PROPERTY, awtToolkit);
  }

  public static UISpecToolkit instance() {
    Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
    if (!(defaultToolkit instanceof UISpecToolkit)) {
      fail("You must call UISpec4J.init() before using it");
    }
    return (UISpecToolkit)defaultToolkit;
  }

  protected LightweightPeer createComponent(Component target) {
    if (target instanceof JPopupMenu) {
      UISpecDisplay.instance().setCurrentPopup((JPopupMenu)target);
    }
    return super.createComponent(target);
  }

  public FramePeer createFrame(Frame target) {
    return new UISpecFramePeer(target);
  }

  public DialogPeer createDialog(Dialog target) throws HeadlessException {
    if (!(target instanceof JDialog)) {
      throw new InterceptionInternalError("Dialogs of type '"
                                          + target.getClass().getName() + "' are not supported.");
    }
    return new UISpecDialogPeer((JDialog)target);
  }

  public WindowPeer createWindow(Window target) throws HeadlessException {
    return new UISpecWindowPeer(target);
  }

  public CanvasPeer createCanvas(Canvas target) {
    return Empty.NULL_CANVAS_PEER;
  }

  public PanelPeer createPanel(Panel target) {
    return Empty.NULL_PANEL_PEER;
  }

  public RobotPeer createRobot(Robot robot, GraphicsDevice device) throws AWTException, HeadlessException {
    return Empty.NULL_ROBOT;
  }

  public KeyboardFocusManagerPeer createKeyboardFocusManagerPeer(KeyboardFocusManager keyboardFocusManager) throws HeadlessException {
    return null;
  }

  protected boolean syncNativeQueue(long l) {
    return false;
  }

  protected int getScreenWidth() {
    return 0;
  }

  protected int getScreenHeight() {
    return 0;
  }

  protected MouseInfoPeer getMouseInfoPeer() {
    return Empty.NULL_MOUSE_INFO;
  }

  private static void fail(String msg) {
    throw new InterceptionInternalError(msg);
  }

  private static void setAwtToolkitProperty() {
    try {
      Class.forName(WINDOWS_SYSTEM_DEFAULT_VALUE);
      awtToolkit = WINDOWS_SYSTEM_DEFAULT_VALUE;
    }
    catch (ClassNotFoundException e) {
      try {
        Class.forName(UNIX_SYSTEM_DEFAULT_VALUE);
        awtToolkit = UNIX_SYSTEM_DEFAULT_VALUE;
      }
      catch (ClassNotFoundException e1) {
        throw new AWTError("Unable to locate AWT Toolkit");
      }
    }
  }

  private static void buildUnderlyingToolkit(String awtToolkit) {
    try {
      underlyingToolkit = (Toolkit)Class.forName(awtToolkit).newInstance();
    }
    catch (Exception e) {
      throw new AWTError("Unable to load AWT Toolkit: " + awtToolkit + " - "
                         + e.getLocalizedMessage());
    }
  }

  public InputMethodDescriptor getInputMethodAdapterDescriptor() throws AWTException {
    return null;
  }
}
