package org.uispec4j.interception.toolkit;

import org.uispec4j.Window;
import org.uispec4j.interception.handlers.InterceptionHandler;
import org.uispec4j.utils.ComponentUtils;
import org.uispec4j.utils.ExceptionContainer;
import org.uispec4j.utils.ThreadManager;
import org.uispec4j.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Virtual display used by the interception mechanism.
 *
 * @see UISpecToolkit
 */
public class UISpecDisplay {

  private static final UISpecDisplay singletonInstance = new UISpecDisplay();
  private Stack handlerStack = new Stack();
  private JPopupMenu currentPopup;
  private ExceptionContainer exceptionContainer = new ExceptionContainer();
  private List<ThreadManager.ThreadDelegate> threads = new ArrayList<ThreadManager.ThreadDelegate>();

  public static UISpecDisplay instance() {
    return singletonInstance;
  }

  public void showFrame(JFrame frame) {
    processWindow(new Window(frame));
  }

  public void showFrame(Frame frame) {
    if (frame instanceof JFrame) {
      showFrame((JFrame)frame);
    }
    else {
      processWindow(new Window(frame));
    }
  }

  public void showDialog(JDialog dialog) {
    processWindow(new Window(dialog));
  }

  public void showWindow(java.awt.Window window) {
    processWindow(new Window(window));
  }

  public void add(InterceptionHandler handler) {
    synchronized (handlerStack) {
      handlerStack.push(handler);
    }
  }

  public void reset() {
    synchronized (handlerStack) {
      handlerStack.clear();
    }
    exceptionContainer.reset();
    for (ThreadManager.ThreadDelegate thread : threads) {
      try {
        thread.join(10);
      }
      catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      thread.interrupt();
    }
    threads.clear();
  }

  public void remove(InterceptionHandler handler) {
    synchronized (handlerStack) {
      handlerStack.remove(handler);
    }
  }

  public void rethrowIfNeeded() {
    exceptionContainer.rethrowIfNeeded();
  }

  private UISpecDisplay() {
    // Constructor is private since this is a singleton class
  }

  private void processWindow(Window window) {
    try {
      InterceptionHandler handler;
      synchronized (handlerStack) {
        if (!assertAcceptsWindow(window)) {
          return;
        }
        handler = (InterceptionHandler)handlerStack.pop();
      }
      handler.process(window);
    }
    catch (Throwable e) {
      ComponentUtils.close(window);
      store(e);
    }
  }

  public boolean assertAcceptsWindow(Window window) {
    if (!handlerStack.isEmpty()) {
      return true;
    }

    Error error =
      new Error("Unexpected window shown - this window should be handled with " +
                "WindowInterceptor. " + Utils.LINE_SEPARATOR +
                "Window contents:" + Utils.LINE_SEPARATOR +
                window.getDescription());
    if (interceptionInProgress()) {
      store(error);
      ComponentUtils.close(window);
    }
    else {
      throw error;
    }
    return false;
  }

  private boolean interceptionInProgress() {
    return !handlerStack.isEmpty();
  }

  public void setCurrentPopup(JPopupMenu popupMenu) {
    this.currentPopup = popupMenu;
  }

  public JPopupMenu getCurrentPopup() {
    return currentPopup;
  }

  public void store(Throwable throwable) {
    exceptionContainer.set(throwable);
  }

  public int getHandlerCount() {
    return handlerStack.size();
  }

  public void runInNewThread(Runnable runnable) {
    ThreadManager.ThreadDelegate threadDelegate =
      ThreadManager.getInstance().addRunnable("", runnable);
    threads.add(threadDelegate);
  }
}
