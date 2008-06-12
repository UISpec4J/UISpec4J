package org.uispec4j.interception.handlers;

import org.uispec4j.Window;
import org.uispec4j.utils.ComponentUtils;

public class ClosedInterceptionDetectionHandler extends AbstractInterceptionHandlerDecorator {
  public static final String MODAL_DIALOG_NOT_CLOSED_ERROR_MESSAGE =
    "Modal window was not closed - make sure that setVisible(false) gets called " +
    "by the production code";

  private long timeout;
  private Object lock = new Object();
  private boolean exceptionThrown = false;
  private String windowTitle;
  private Window window;
  private boolean windowClosed = false;
  private Thread thread;

  public ClosedInterceptionDetectionHandler(InterceptionHandler innerHandler, long timeout) {
    super(innerHandler);
    this.timeout = timeout;
  }

  public void process(final Window window) {
    if (!window.isModal().isTrue()) {
      super.process(window);
      return;
    }
    this.window = window;
    windowTitle = window.getTitle();
    thread = new Thread() {
      public void run() {
        try {
          long delay = timeout / 100;
          for (int i = 0; i < 100; i++) {
            try {
              sleep(delay);
            }
            catch (InterruptedException e) {
              return;
            }
            if (!window.getAwtComponent().isVisible()) {
              notifyWindowClosed();
              return;
            }
          }
        }
        finally {
          ComponentUtils.close(window);
        }
      }
    };
    thread.setName(thread.getName() + " [WindowInterceptor.CloseHandler]");
    thread.start();
    try {
      super.process(window);
    }
    catch (RuntimeException e) {
      notifyExceptionThrown();
      throw e;
    }
    catch (Error e) {
      notifyExceptionThrown();
      throw e;
    }
    catch (Throwable e) {
      throw new Error(e);
    }
  }

  private void notifyExceptionThrown() {
    synchronized (lock) {
      exceptionThrown = true;
      lock.notify();
    }
  }

  private void notifyWindowClosed() {
    synchronized (lock) {
      windowClosed = true;
      lock.notify();
    }
  }

  public void checkWindowWasClosed() {
    if (window == null) {
      return;
    }
    synchronized (lock) {
      if (!windowClosed && !exceptionThrown) {
        try {
          lock.wait(timeout);
        }
        catch (InterruptedException e) {
        }
        if (!windowClosed) {
          throw new WindowNotClosedError("Modal window '" + windowTitle +
                                         "' was not closed - make sure that setVisible(false) gets called " +
                                         "by the production code");
        }
      }
    }
  }

  public void stop() {
    if (thread != null) {
      thread.interrupt();
    }
  }
}
