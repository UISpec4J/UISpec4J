package org.uispec4j.assertion;

import org.uispec4j.UISpec4J;
import org.uispec4j.assertion.dependency.InternalAssert;
import org.uispec4j.utils.Utils;

/**
 * Checks the validity of Assertion objects.
 */
public class UISpecAssert {

  /**
   * Checks that the given assertion succeeds (with a retry strategy).
   * The {@link Assertion#check()} method is called until the timeout
   * specified by {@link UISpec4J#setAssertionTimeLimit(long)} is reached.
   * This method is equivalent to {@link #assertTrue(Assertion)}.
   */
  public static void assertThat(Assertion assertion) {
    assertTrue(null, assertion);
  }

  /**
   * Checks that the given assertion succeeds (with a retry strategy).
   * The {@link Assertion#check()} method is called until the timeout
   * specified by {@link UISpec4J#setAssertionTimeLimit(long)} is reached.
   * If it fails an AssertionFailedError is thrown with the given message.
   * This method is equivalent to {@link #assertTrue(String,Assertion)}.
   */
  public static void assertThat(String message, Assertion assertion) {
    checkAssertion(message, assertion, UISpec4J.getAssertionTimeLimit());
  }

  /**
   * Checks that the given assertion succeeds (with a retry strategy).
   * The {@link Assertion#check()} method is called until the timeout
   * specified by {@link UISpec4J#setAssertionTimeLimit(long)} is reached.
   * This method is equivalent to {@link #assertThat(Assertion)}.
   */
  public static void assertTrue(Assertion assertion) {
    assertTrue(null, assertion);
  }

  /**
   * Checks that the given assertion succeeds (with a retry strategy).
   * The {@link Assertion#check()} method is called until the timeout
   * specified by {@link UISpec4J#setAssertionTimeLimit(long)} is reached.
   * If it fails an AssertionFailedError is thrown with the given message.
   * This method is equivalent to {@link #assertThat(Assertion)}.
   */
  public static void assertTrue(String message, Assertion assertion) {
    checkAssertion(message, assertion, UISpec4J.getAssertionTimeLimit());
  }

  /**
   * Checks that the given assertion fails (with a retry strategy).
   * The {@link Assertion#check()} method is called until the timeout
   * specified by {@link UISpec4J#setAssertionTimeLimit(long)} is reached.
   */
  public static void assertFalse(final Assertion assertion) {
    assertTrue(not(assertion));
  }

  /**
   * Checks the given assertion fails (with a retry strategy).
   * The {@link Assertion#check()} method is called until the timeout
   * specified by {@link UISpec4J#setAssertionTimeLimit(long)} is reached.
   * If it succeeds an AssertionFailedError is thrown with the given message.
   */
  public static void assertFalse(String message, final Assertion assertion) {
    assertTrue(message, not(assertion));
  }

  /**
   * Waits until the given assertion becomes true.
   * The {@link Assertion#check()} method is called until the timeout
   * specified as parameter (in milliseconds) is reached.
   */
  public static void waitUntil(Assertion assertion, long waitTimeLimit) {
    checkAssertion(null, assertion, waitTimeLimit);
  }

  /**
   * Waits until the given assertion becomes true.
   * The {@link Assertion#check()} method is called until the timeout
   * specified as parameter (in milliseconds) is reached.
   * If it fails an AssertionFailedError is thrown with the given message.
   */
  public static void waitUntil(String message, Assertion assertion, long waitTimeLimit) {
    checkAssertion(message, assertion, waitTimeLimit);
  }

  /**
   * Checks the given assertion equals the expected parameter (with a retry strategy).
   * The {@link Assertion#check()} method is called until the timeout
   * specified by {@link UISpec4J#setAssertionTimeLimit(long)} is reached.
   */
  public static void assertEquals(boolean expected, Assertion assertion) {
    assertEquals(null, expected, assertion);
  }

  /**
   * Checks the given assertion equals the expected parameter (with a retry strategy).
   * The {@link Assertion#check()} method is called until the timeout
   * specified by {@link UISpec4J#setAssertionTimeLimit(long)} is reached.
   * If it fails an AssertionFailedError is thrown with the given message.
   */
  public static void assertEquals(String message, boolean expected, Assertion assertion) {
    if (expected) {
      assertTrue(message, assertion);
    }
    else {
      assertFalse(message, assertion);
    }
  }

  /**
   * Returns a negation of the given assertion.
   */
  public static Assertion not(final Assertion assertion) {
    return new Assertion() {
      public void check() {
        try {
          assertion.check();
          throw new FailureNotDetectedError();
        }
        catch (FailureNotDetectedError e) {
          InternalAssert.fail(null);
        }
        catch (Throwable e) {
        }
      }
    };
  }

  /**
   * Returns the intersection of the {@link Assertion} parameters.
   */
  public static Assertion and(final Assertion... assertions) {
    return new Assertion() {
      public void check() throws Exception {
        for (int i = 0; i < assertions.length; i++) {
          assertions[i].check();
        }
      }
    };
  }

  /**
   * Returns the union of the {@link Assertion} parameters.
   */
  public static Assertion or(final Assertion... assertions) {
    return new Assertion() {
      public void check() {
        for (int i = 0; i < assertions.length; i++) {
          try {
            assertions[i].check();
            break;
          }
          catch (Throwable e) {
            if (i == assertions.length - 1) {
              InternalAssert.fail(e.getMessage());
            }
          }
        }
      }
    };
  }

  private static void checkAssertion(String message, Assertion assertion, long waitTimeLimit) {
    try {
      Utils.waitForPendingAwtEventsToBeProcessed();
      assertion.check();
    }
    catch (Throwable e) {
      retry(message, assertion, waitTimeLimit);
    }
  }

  /**
   * @noinspection ForLoopThatDoesntUseLoopVariable
   */
  private static void retry(String message, Assertion assertion, long waitTimeLimit) {
    long elapsedTime = 0;
    for (int i = 0; elapsedTime < waitTimeLimit; i++) {
      int waitTime = i < 10 ? 20 : 200;
      Utils.sleep(waitTime);
      elapsedTime += waitTime;
      try {
        assertion.check();
        return;
      }
      catch (Throwable e) {
      }
    }
    try {
      assertion.check();
    }
    catch (Error e) {
      if (message == null) {
        throw e;
      }
      InternalAssert.fail(message);
    }
    catch (Exception e) {
      if (message != null) {
        throw new RuntimeException(message, e);
      }

      else {
        throw new RuntimeException(e.getMessage(), e);
      }
    }
  }

  private static class FailureNotDetectedError extends Error {
  }
}
