package org.uispec4j.assertion;

/**
 * Interface used for defining conditions. These assertions are meant to be used
 * essentially with the {@link UISpecAssert} "assertXxx" and "waitUntil" methods.
 */
public abstract class Assertion {

  /**
   * Returns true if the {@link #check()} method does not fail.
   */
  public final boolean isTrue() {
    try {
      check();
      return true;
    }
    catch (Throwable e) {
      return false;
    }
  }

  /**
   * Throws an exception if the condition is not true.
   */
  public abstract void check();
}