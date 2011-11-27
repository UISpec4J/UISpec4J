package org.uispec4j.interception.toolkit;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.peer.DialogPeer;
import java.awt.peer.LightweightPeer;
import java.awt.peer.WindowPeer;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;

import javax.swing.JDialog;
import javax.swing.JPopupMenu;

import org.uispec4j.interception.toolkit.Empty.FramePeer;

public class ToolkitHandler implements MethodFilter, MethodHandler {
  private static final Map<String, Object> HANDLED_METHODS;

  static {
    Map<String, Object> handledMethods = new HashMap<String, Object>();
    handledMethods.put("createCanvas", Empty.NULL_CANVAS_PEER);
    handledMethods.put("createDesktopPeer", Empty.NULL_DESKTOP_PEER);
    handledMethods.put("getMouseInfoPeer", Empty.NULL_MOUSE_INFO);
    handledMethods.put("createPanel", Empty.NULL_PANEL_PEER);
    handledMethods.put("createRobot", Empty.NULL_ROBOT);
    handledMethods.put("createSystemTray", Empty.NULL_SYSTEM_TRAY_PEER);
    handledMethods.put("createTrayIcon", Empty.NULL_TRAY_ICON_PEER);
    handledMethods.put("getInputMethodAdapterDescriptor", Empty.NULL_INPUT_METHOD_DESCRIPTOR); // TODO original return null
    handledMethods.put("isModalExclusionTypeSupported", false);
    handledMethods.put("isModalityTypeSupported", false);
    handledMethods.put("isDesktopSupported", false);
    handledMethods.put("isTraySupported", false);
    handledMethods.put("syncNativeQueue", false);
    handledMethods.put("isWindowOpacityControlSupported", false);
    handledMethods.put("isWindowShapingSupported", false);
    handledMethods.put("isWindowTranslucencySupported", false);
    handledMethods.put("getScreenHeight", 0);
    handledMethods.put("getScreenWidth", 0);
    handledMethods.put("grab", null);
    handledMethods.put("ungrab", null);
    handledMethods.put("createKeyboardFocusManagerPeer", null);
    handledMethods.put("createComponent", new CreateComponentTask());
    handledMethods.put("createFrame", new CreateFrameTask());
    handledMethods.put("createDialog", new CreateDialogTask());
    handledMethods.put("createWindow", new CreateWindowTask());
    HANDLED_METHODS = Collections.unmodifiableMap(handledMethods);
  }

  public boolean isHandled(Method method) {
    return HANDLED_METHODS.containsKey(method.getName());
  }

  public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
    Toolkit toolkit = (Toolkit) self;
    if (isHandled(thisMethod)) {
      Object value = HANDLED_METHODS.get(thisMethod.getName());
      if (value instanceof Task) {
        return ((Task<?>) value).execute(toolkit, proceed, args);
      }
      return value;
    }
    return proceed.invoke(toolkit, args);
  }

  private static interface Task<T> {
    public T execute(Toolkit toolkit, Method method, Object... args) throws Exception;
  }

  private static final class CreateComponentTask implements Task<LightweightPeer> {
    public LightweightPeer execute(Toolkit toolkit, Method method, Object... args) throws Exception {
      Component target = (Component) args[0];
      if (target instanceof JPopupMenu) {
        UISpecDisplay.instance().setCurrentPopup((JPopupMenu) target);
      }
      return (LightweightPeer) method.invoke(toolkit, args);
    }
  }

  private static final class CreateFrameTask implements Task<FramePeer> {
    public FramePeer execute(Toolkit toolkit, Method method, Object... args) throws Exception {
      return new UISpecFramePeer(toolkit, (Frame) args[0]);
    }
  }

  private static final class CreateDialogTask implements Task<DialogPeer> {
    public DialogPeer execute(Toolkit toolkit, Method method, Object... args) throws Exception {
      Dialog target = (Dialog) args[0];
      if (!(target instanceof JDialog)) {
        throw new InterceptionInternalError("Dialogs of type '" + target.getClass().getName() + "' are not supported.");
      }
      return new UISpecDialogPeer(toolkit, (JDialog) target);
    }
  }

  private static final class CreateWindowTask implements Task<WindowPeer> {
    public WindowPeer execute(Toolkit toolkit, Method method, Object... args) throws Exception {
      return new UISpecWindowPeer(toolkit, (Window) args[0]);
    }
  }
}
