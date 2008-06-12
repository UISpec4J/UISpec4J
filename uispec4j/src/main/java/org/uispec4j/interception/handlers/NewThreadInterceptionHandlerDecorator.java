package org.uispec4j.interception.handlers;

import org.uispec4j.Window;
import org.uispec4j.utils.ExceptionContainer;

public class NewThreadInterceptionHandlerDecorator extends AbstractInterceptionHandlerDecorator {

  private ExceptionContainer exceptionContainer = new ExceptionContainer();
  private Thread handlerThread;

  public NewThreadInterceptionHandlerDecorator(InterceptionHandler innerHandler) {
    super(innerHandler);
  }

  public void process(final Window window) {
    handlerThread = new Thread() {
      public void run() {
        try {
          NewThreadInterceptionHandlerDecorator.super.process(window);
        }
        catch (Throwable e) {
          exceptionContainer.set(e);
        }
      }
    };
    handlerThread.setName(handlerThread.getName() + " [NewThreadHandler]");
    handlerThread.start();
  }

  public void complete() {
    join();
    exceptionContainer.rethrowIfNeeded();
  }

  public void join() {
    if (handlerThread != null) {
      try {
        handlerThread.join();
        handlerThread = null;
      }
      catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
