package org.uispec4j.assertion;

import junit.framework.AssertionFailedError;

public class DummyAssertion extends Assertion {
  public static final Assertion TRUE = new DummyAssertion(true);
  public static final Assertion FALSE = new DummyAssertion(false);
  public static final String DEFAULT_ERROR_MSG = "custom error";

  private Error error = null;
  private Exception exception = null;

  public DummyAssertion() {
  }

  public DummyAssertion(boolean success) {
    if (!success) {
      setError(DEFAULT_ERROR_MSG);
    }
  }

  public DummyAssertion(String errorMessage) {
    setError(errorMessage);
  }

  public void setError(String errorMessage) {
    this.error = new AssertionFailedError(errorMessage);
  }

  public void setException(Exception exception) {
    this.exception = exception;
  }

  public void check() throws Exception {
    if (error != null) {
      throw error;
    }
    if (exception != null) {
      throw exception;
    }
  }
}
