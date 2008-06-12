package org.uispec4j.utils;

public class ExceptionContainer {
  private RuntimeException exception;
  private Error error;

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
        throw error;
      }
      if (exception != null) {
        throw exception;
      }
    }
    finally {
      reset();
    }
  }

  public void reset() {
    exception = null;
    error = null;
  }
}
