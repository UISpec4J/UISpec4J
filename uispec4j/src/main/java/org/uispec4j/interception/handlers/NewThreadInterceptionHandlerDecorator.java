package org.uispec4j.interception.handlers;

import org.uispec4j.Window;
import org.uispec4j.utils.ExceptionContainer;
import org.uispec4j.utils.ThreadManager;

public class NewThreadInterceptionHandlerDecorator extends AbstractInterceptionHandlerDecorator {
  private ExceptionContainer exceptionContainer = new ExceptionContainer();
  private ThreadManager.ThreadDelegate interuptible;

  public NewThreadInterceptionHandlerDecorator(InterceptionHandler innerHandler) {
    super(innerHandler);
  }

  public void process(final Window window) {
    interuptible = ThreadManager.getInstance().addRunnable(" [NewThreadHandler]", new Runnable() {
      public void run() {
        try {
          NewThreadInterceptionHandlerDecorator.super.process(window);
        }
        catch (Throwable e) {
          exceptionContainer.set(e);
        }
      }
    });
  }

  public void complete() {
    join();
    exceptionContainer.rethrowIfNeeded();
  }

  public void join() {
    if (interuptible != null) {
      try {
        interuptible.join();
        interuptible = null;
      }
      catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
