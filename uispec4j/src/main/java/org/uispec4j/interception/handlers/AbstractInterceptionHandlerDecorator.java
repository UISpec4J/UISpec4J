package org.uispec4j.interception.handlers;

import org.uispec4j.Window;

public abstract class AbstractInterceptionHandlerDecorator implements InterceptionHandler {

  private InterceptionHandler innerHandler;

  protected AbstractInterceptionHandlerDecorator(InterceptionHandler innerHandler) {
    this.innerHandler = innerHandler;
  }

  public void process(final Window window) {
    innerHandler.process(window);
  }
}
