package org.uispec4j.interception.handlers;

import org.uispec4j.Window;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;

public class ShownInterceptionDetectionHandler extends AbstractInterceptionHandlerDecorator {
  public static final String NO_WINDOW_WAS_SHOWN_ERROR_MESSAGE = "No window was shown (timeout expired)";

  private boolean windowWasShown = false;
  private long waitTimeLimit;

  public ShownInterceptionDetectionHandler(InterceptionHandler handler, long waitTimeLimit) {
    super(handler);
    this.waitTimeLimit = waitTimeLimit;
  }

  public void process(Window window) {
    synchronized (this) {
      windowWasShown = true;
      notify();
      super.process(window);
    }
  }

  public void waitWindow() {
    long end = System.currentTimeMillis() + waitTimeLimit;
    long still = waitTimeLimit;
    synchronized (this) {
      while (!windowWasShown && still > 0) {
        try {
          wait(still);
          still = end - System.currentTimeMillis();
        }
        catch (InterruptedException e) {
        }
      }
    }
    if (!windowWasShown) {
      AssertAdapter.fail(NO_WINDOW_WAS_SHOWN_ERROR_MESSAGE);
    }
  }
}
