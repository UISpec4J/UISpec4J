package org.uispec4j.interception.toolkit;

/**
 * Thrown when an internal error occured when UISpec mock the display.
 */
public class InterceptionInternalError extends Error {
  public InterceptionInternalError(String msg) {
    super(msg);
  }
}
