package org.uispec4j.utils;

import java.util.Arrays;
import java.util.List;

import org.uispec4j.UISpec4J;
import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.UISpecAssert;
import org.uispec4j.interception.InterceptionError;

import junit.framework.TestCase;

public abstract class UnitTestCase extends TestCase {
  static {
    UISpec4J.init();
  }

  protected void setUp() throws Exception {
    UISpec4J.setWindowInterceptionTimeLimit(100);
    UISpec4J.setAssertionTimeLimit(30);
  }

  public void assertTrue(Assertion assertion) {
    UISpecAssert.assertTrue(assertion);
  }

  public void waitUntil(Assertion assertion, int timeLimit) {
    UISpecAssert.waitUntil(assertion, timeLimit);
  }

  public void assertFalse(Assertion assertion) {
    UISpecAssert.assertFalse(assertion);
  }

  public Assertion not(Assertion assertion) {
    return UISpecAssert.not(assertion);
  }

  public void assertEquals(boolean expected, Assertion assertion) {
    UISpecAssert.assertEquals(expected, assertion);
  }

  protected void checkAssertionFails(Assertion assertion, String expectedMessage) throws Exception {
    try {
      assertTrue(assertion);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals(expectedMessage, e.getMessage());
    }
  }

  protected void checkException(Functor functor, String expectedMessage) throws Exception {
    try {
      functor.run();
      throw new AssertionFailureNotDetectedError();
    }
    catch (Exception e) {
      assertEquals(expectedMessage, e.getMessage());
    }
  }

  protected void checkInterceptionError(Functor functor, String expectedMessage) throws Exception {
    try {
      functor.run();
      throw new AssertionFailureNotDetectedError();
    }
    catch (InterceptionError e) {
      assertEquals(expectedMessage, e.getMessage());
    }
  }

  protected void checkAssertionError(Functor functor, String expectedMessage) throws Exception {
    try {
      functor.run();
      throw new AssertionFailureNotDetectedError();
    }
    catch (Throwable e) {
      assertEquals(expectedMessage, e.getMessage());
    }
  }

  protected void checkAssertionError(Functor functor) throws Exception {
    try {
      functor.run();
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
    }
    catch (InterceptionError e) {
    }
  }
  
  protected boolean isJavaVersionAtLeast( String javaVersion ) {
    String currentJavaVersion = System.getProperty("java.specification.version");
    List<String> javaVersions = Arrays.asList("1.6", "1.7", "1.8", "9", "10");
    return javaVersions.indexOf(javaVersion) <= javaVersions.indexOf(currentJavaVersion);
  }
}
