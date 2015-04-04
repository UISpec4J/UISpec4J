package org.uispec4j.assertion;

import org.uispec4j.UISpec4J;
import org.uispec4j.utils.Chrono;
import org.uispec4j.utils.Functor;
import org.uispec4j.utils.UnitTestCase;
import org.uispec4j.utils.Utils;

public class UISpecAssertTest extends UnitTestCase {

  protected void setUp() throws Exception {
    UISpec4J.setAssertionTimeLimit(UISpec4J.DEFAULT_ASSERTION_TIME_LIMIT);
  }

  public void testAssertTrue() throws Exception {
    UISpecAssert.assertTrue(DummyAssertion.TRUE);
    checkAssertionError(new Functor() {
      public void run() throws Exception {
        UISpecAssert.assertTrue(DummyAssertion.FALSE);
      }
    });
  }

  public void testAssertTrueRetriesUntilTheAssertionSucceeds() throws Exception {
    Chrono chrono = Chrono.start();
    runThreadAndCheckAssertion(40, true);
    chrono.assertElapsedTimeLessThan(150);
  }

  public void testWaitForAssertionDoesNotTakeIntoAccountGlobalWaitTimeLimit() throws Exception {
    UISpec4J.setAssertionTimeLimit(0);
    Chrono chrono = Chrono.start();
    runThreadAndWaitForAssertion(50, 100);
    chrono.assertElapsedTimeLessThan(200);

    UISpec4J.setAssertionTimeLimit(500);
    checkAssertionError(new Functor() {
      public void run() throws Exception {
        runThreadAndWaitForAssertion(300, 100);
      }
    }, "error!");

    final DummyAssertion assertion = new DummyAssertion("custom message");

    checkAssertionError(new Functor() {
      public void run() throws Exception {
        UISpecAssert.waitUntil(assertion, 0);
      }
    }, "custom message");

    checkAssertionError(new Functor() {
      public void run() throws Exception {
        UISpecAssert.waitUntil("other message", assertion, 0);
      }
    }, "other message");
  }

  public void testAssertTrueRetriesUpToATimeLimit() throws Exception {
    checkAssertionError(new Functor() {
      public void run() throws Exception {
        runThreadAndCheckAssertion(900, true);
      }
    }, "error!");
  }

  public void testAssertTrueAssertionErrorMessage() throws Exception {
    UISpec4J.setAssertionTimeLimit(0);
    final DummyAssertion assertion = new DummyAssertion("custom message");

    checkAssertionFails(assertion, "custom message");

    checkAssertionError(new Functor() {
      public void run() throws Exception {
        UISpecAssert.assertTrue("other message", assertion);
      }
    }, "other message");

    checkAssertionError(new Functor() {
      public void run() throws Exception {
        assertion.setError("exception message");
        UISpecAssert.assertTrue("assertTrue message", assertion);
      }
    }, "assertTrue message");
  }

  public void testAssertFalseAssertionErrorMessage() throws Exception {
    UISpec4J.setAssertionTimeLimit(0);
    final DummyAssertion assertion = new DummyAssertion("custom message");

    checkAssertionError(new Functor() {
      public void run() throws Exception {
        UISpecAssert.assertFalse(not(assertion));
      }
    }, null);

    checkAssertionError(new Functor() {
      public void run() throws Exception {
        UISpecAssert.assertFalse("other message", not(assertion));
      }
    }, "other message");
  }

  public void testAssertFalse() throws Exception {
    UISpecAssert.assertFalse(DummyAssertion.FALSE);
    checkAssertionError(new Functor() {
      public void run() throws Exception {
        UISpecAssert.assertFalse(DummyAssertion.TRUE);
      }
    });
  }

  public void testAssertFalseRetriesUntilTheAssertionFails() throws Exception {
    Chrono chrono = Chrono.start();
    runThreadAndCheckAssertion(80, false);
    chrono.assertElapsedTimeLessThan(200);
  }

  public void testAssertFalseRetriesUpToATimeLimit() throws Exception {
    checkAssertionError(new Functor() {
      public void run() throws Exception {
        runThreadAndCheckAssertion(900, false);
      }
    });
  }

  public void testAssertEquals() throws Exception {
    UISpecAssert.assertEquals(false, DummyAssertion.FALSE);
    UISpecAssert.assertEquals(true, DummyAssertion.TRUE);
    checkAssertionError(new Functor() {
      public void run() throws Exception {
        UISpecAssert.assertEquals(true, DummyAssertion.FALSE);
      }
    }, DummyAssertion.DEFAULT_ERROR_MSG);
    checkAssertionError(new Functor() {
      public void run() throws Exception {
        UISpecAssert.assertEquals(false, DummyAssertion.TRUE);
      }
    });
  }

  public void testAssertEqualsWithMessage() throws Exception {
    final String message = "my custom message";
    UISpecAssert.assertEquals(message, false, DummyAssertion.FALSE);
    UISpecAssert.assertEquals(message, true, DummyAssertion.TRUE);
    checkAssertionError(new Functor() {
      public void run() throws Exception {
        UISpecAssert.assertEquals(message, true, DummyAssertion.FALSE);
      }
    }, message);
    checkAssertionError(new Functor() {
      public void run() throws Exception {
        UISpecAssert.assertEquals(message, false, DummyAssertion.TRUE);
      }
    }, message);
  }

  public void testAssertionNegationOperator() throws Exception {
    UISpecAssert.assertTrue(DummyAssertion.TRUE);
    UISpecAssert.assertFalse(UISpecAssert.not(DummyAssertion.TRUE));
    UISpecAssert.assertTrue(UISpecAssert.not(UISpecAssert.not(DummyAssertion.TRUE)));
  }

  public void testAssertionIntersectionOperator() throws Exception {
    DummyAssertion assertion = new DummyAssertion(true);
    UISpecAssert.assertTrue(UISpecAssert.and(new Assertion[]{assertion, DummyAssertion.TRUE}));
    UISpecAssert.assertFalse(UISpecAssert.and(new Assertion[]{assertion, DummyAssertion.FALSE}));

    assertion.setError("");
    UISpecAssert.assertFalse(UISpecAssert.and(new Assertion[]{assertion, DummyAssertion.TRUE}));
    UISpecAssert.assertFalse(UISpecAssert.and(new Assertion[]{assertion, DummyAssertion.FALSE}));
  }

  public void testAssertionUnionOperator() throws Exception {
    DummyAssertion assertion = new DummyAssertion(true);
    UISpecAssert.assertTrue(UISpecAssert.or(new Assertion[]{assertion, DummyAssertion.TRUE}));
    UISpecAssert.assertTrue(UISpecAssert.or(new Assertion[]{assertion, DummyAssertion.FALSE}));

    assertion.setError("");
    UISpecAssert.assertTrue(UISpecAssert.or(new Assertion[]{assertion, DummyAssertion.TRUE}));
    UISpecAssert.assertTrue(UISpecAssert.or(new Assertion[]{assertion, DummyAssertion.TRUE, DummyAssertion.FALSE}));
    UISpecAssert.assertTrue(UISpecAssert.or(new Assertion[]{assertion, DummyAssertion.FALSE, DummyAssertion.TRUE}));
    UISpecAssert.assertFalse(UISpecAssert.or(new Assertion[]{assertion, DummyAssertion.FALSE}));
  }

  private void runThreadAndCheckAssertion(int threadSleepTime, final boolean useAssertTrue) throws Exception {
    final DummyThread thread = new DummyThread(threadSleepTime);
    thread.start();
    Assertion assertion = new Assertion() {
      public void check() {
        if ((useAssertTrue) ? !thread.timeoutExpired : thread.timeoutExpired) {
          throw new AssertionError("error!");
        }
      }
    };
    if (useAssertTrue) {
      UISpecAssert.assertTrue(assertion);
    }
    else {
      UISpecAssert.assertFalse(assertion);
    }
    thread.join();
  }

  private void runThreadAndWaitForAssertion(int threadSleepTime, long waitTimeLimit) throws Exception {
    final DummyThread thread = new DummyThread(threadSleepTime);
    thread.start();
    Assertion assertion = new Assertion() {
      public void check() {
        if (!thread.timeoutExpired) {
          throw new AssertionError("error!");
        }
      }
    };
    UISpecAssert.waitUntil(assertion, waitTimeLimit);
    thread.join();
  }

  private static class DummyThread extends Thread {
    boolean timeoutExpired;
    private int sleepTime;

    public DummyThread(int sleepTime) {
      this.sleepTime = sleepTime;
    }

    public void run() {
      Utils.sleep(sleepTime);
      timeoutExpired = true;
    }
  }
}
