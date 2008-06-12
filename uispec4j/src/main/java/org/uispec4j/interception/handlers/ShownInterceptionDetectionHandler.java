package org.uispec4j.interception.handlers;

import org.uispec4j.Window;
import org.uispec4j.assertion.dependency.InternalAssert;
import org.uispec4j.utils.Utils;

public class ShownInterceptionDetectionHandler extends AbstractInterceptionHandlerDecorator {
  public static final String NO_WINDOW_WAS_SHOWN_ERROR_MESSAGE = "No window was shown (timeout expired)";

  private boolean windowWasShown = false;
  private long waitTimeLimit;

  public ShownInterceptionDetectionHandler(InterceptionHandler handler, long waitTimeLimit) {
    super(handler);
    this.waitTimeLimit = waitTimeLimit;
  }

  public void process(Window window) {
    windowWasShown = true;
    super.process(window);
  }

  public void waitWindow() {
    long step = waitTimeLimit / 100;
    for (int i = 0; i < 100; i++) {
      if (windowWasShown) {
        return;
      }
      Utils.sleep(step);
    }
    InternalAssert.fail(NO_WINDOW_WAS_SHOWN_ERROR_MESSAGE);
  }
}
