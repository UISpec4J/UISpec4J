package org.uispec4j.utils;

public class ExceptionContainer {
  private RuntimeException exception;
  private Error error;
  private StackTraceElement[] stackTraceElements;

  public ExceptionContainer() {
  }

  public ExceptionContainer(RuntimeException callerStack) {
    stackTraceElements = callerStack.getStackTrace();
  }

  public void set(Throwable e) {
    if (e instanceof RuntimeException) {
      exception = (RuntimeException)e;
    }
    else if (e instanceof Error) {
      error = (Error)e;
    }
    else {
      exception = new RuntimeException(e);
    }
  }

  public boolean isSet() {
    return (exception != null) || (error != null);
  }

  public void rethrowIfNeeded() {
    try {
      if (error != null) {
        if (stackTraceElements != null) {
          completeStackTrace(error);
        }
        throw error;
      }
      if (exception != null) {
        if (stackTraceElements != null) {
          completeStackTrace(exception);
        }
        throw exception;
      }
    }
    finally {
      reset();
    }
  }

  private void completeStackTrace(Throwable exception) {
    StackTraceElement[] stackTraceElements1 = exception.getStackTrace();
    StackTraceElement[] newStack = new StackTraceElement[stackTraceElements.length + stackTraceElements1.length];
    int i =0;
    for (StackTraceElement element : stackTraceElements) {
      newStack[i] = element;
      i++;
    }
    for (StackTraceElement element : stackTraceElements1) {
      newStack[i] = element;
      i++;
    }
    exception.setStackTrace(newStack);
  }

  public void reset() {
    exception = null;
    error = null;
  }
}
