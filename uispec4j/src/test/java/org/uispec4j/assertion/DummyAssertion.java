package org.uispec4j.assertion;

public class DummyAssertion extends Assertion {
  public static final Assertion TRUE = new DummyAssertion(true);
  public static final Assertion FALSE = new DummyAssertion(false);
  public static final String DEFAULT_ERROR_MSG = "custom error";

  private Error error = null;

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
    this.error = new AssertionError(errorMessage);
  }

  public void check() {
    if (error != null) {
      throw error;
    }
  }
}
