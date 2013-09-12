package org.uispec4j.interception.toolkit;

import java.awt.AWTError;
import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.Panel;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.im.spi.InputMethodDescriptor;
import java.awt.peer.CanvasPeer;
import java.awt.peer.DialogPeer;
import java.awt.peer.FramePeer;
import java.awt.peer.KeyboardFocusManagerPeer;
import java.awt.peer.LightweightPeer;
import java.awt.peer.MouseInfoPeer;
import java.awt.peer.PanelPeer;
import java.awt.peer.RobotPeer;
import java.awt.peer.WindowPeer;

import javax.swing.JDialog;
import javax.swing.JPopupMenu;

import org.uispec4j.UISpec4J;

import sun.awt.LightweightFrame;

/**
 * Mock toolkit used for intercepting displayed frames and dialogs.<p>
 * You can set it up by calling the {@link #setUp()} method.
 *
 * @see <a href="http://www.uispec4j.org/intercepting-windows">Intercepting windows</a>
 */
public class UISpecToolkit extends ToolkitDelegate {
  static final String SYSTEM_PROPERTY = "awt.toolkit";
  static final String UNIX_SYSTEM_DEFAULT_VALUE = "sun.awt.motif.MToolkit";
  static final String WINDOWS_SYSTEM_DEFAULT_VALUE = "sun.awt.windows.WToolkit";

  private static String awtToolkit;

  public UISpecToolkit() {
    setUp();
  }

  /**
   * @see UISpec4J#init
   * @deprecated Do not call this one directly anymore - use {@link UISpec4J#init} instead
   */
  @Deprecated
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
    final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
    if (!(defaultToolkit instanceof UISpecToolkit)) {
      fail("You must call UISpec4J.init() before using it");
    }
    return (UISpecToolkit)defaultToolkit;
  }

  @Override
protected LightweightPeer createComponent(final Component target) {
    if (target instanceof JPopupMenu) {
      UISpecDisplay.instance().setCurrentPopup((JPopupMenu)target);
    }
    return super.createComponent(target);
  }

  @Override
public FramePeer createFrame(final Frame target) {
    return new UISpecFramePeer(target);
  }

  @Override
public DialogPeer createDialog(final Dialog target) throws HeadlessException {
    if (!(target instanceof JDialog)) {
      throw new InterceptionInternalError("Dialogs of type '"
                                          + target.getClass().getName() + "' are not supported.");
    }
    return new UISpecDialogPeer((JDialog)target);
  }

  @Override
public WindowPeer createWindow(final Window target) throws HeadlessException {
    return new UISpecWindowPeer(target);
  }

  @Override
public CanvasPeer createCanvas(final Canvas target) {
    return Empty.NULL_CANVAS_PEER;
  }

  @Override
public PanelPeer createPanel(final Panel target) {
    return Empty.NULL_PANEL_PEER;
  }

  @Override
public RobotPeer createRobot(final Robot robot, final GraphicsDevice device) throws AWTException, HeadlessException {
    return Empty.NULL_ROBOT;
  }

  public KeyboardFocusManagerPeer createKeyboardFocusManagerPeer(final KeyboardFocusManager keyboardFocusManager) throws HeadlessException {
    return null;
  }

  @Override
protected boolean syncNativeQueue(final long l) {
    return false;
  }

  @Override
protected int getScreenWidth() {
    return 0;
  }

  @Override
protected int getScreenHeight() {
    return 0;
  }

  @Override
protected MouseInfoPeer getMouseInfoPeer() {
    return Empty.NULL_MOUSE_INFO;
  }

  private static void fail(final String msg) {
    throw new InterceptionInternalError(msg);
  }

  private static void setAwtToolkitProperty() {
    try {
      Class.forName(WINDOWS_SYSTEM_DEFAULT_VALUE);
      awtToolkit = WINDOWS_SYSTEM_DEFAULT_VALUE;
    }
    catch (final ClassNotFoundException e) {
      try {
        Class.forName(UNIX_SYSTEM_DEFAULT_VALUE);
        awtToolkit = UNIX_SYSTEM_DEFAULT_VALUE;
      }
      catch (final ClassNotFoundException e1) {
        throw new AWTError("Unable to locate AWT Toolkit");
      }
    }
  }

  private static void buildUnderlyingToolkit(final String awtToolkit) {
    try {
      underlyingToolkit = (Toolkit)Class.forName(awtToolkit).newInstance();
    }
    catch (final Exception e) {
      throw new AWTError("Unable to load AWT Toolkit: " + awtToolkit + " - "
                         + e.getLocalizedMessage());
    }
  }

  @Override
public InputMethodDescriptor getInputMethodAdapterDescriptor() throws AWTException {
    return null;
  }
  
  @Override
	public FramePeer createLightweightFrame(final LightweightFrame target)
			throws HeadlessException {
		return new UISpecFramePeer(target);
	}
  
  @Override
	public KeyboardFocusManagerPeer getKeyboardFocusManagerPeer() throws HeadlessException {
		return null;
	}
  
}