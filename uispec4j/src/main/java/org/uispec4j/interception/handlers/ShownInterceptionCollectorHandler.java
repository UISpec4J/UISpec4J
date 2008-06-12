package org.uispec4j.interception.handlers;

import org.uispec4j.Window;

public class ShownInterceptionCollectorHandler implements InterceptionHandler {
  private Window window;

  public void process(Window window) {
    this.window = window;
  }

  public Window getWindow() {
    return window;
  }
}
