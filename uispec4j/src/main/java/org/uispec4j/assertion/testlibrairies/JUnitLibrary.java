package org.uispec4j.assertion.testlibrairies;

import org.junit.Assert;

class JUnitLibrary implements TestLibrary {
  public void fail(String message) {
    Assert.fail(message);
  }

  public void assertTrue(boolean b) {
    Assert.assertTrue(b);
  }

  public void assertTrue(String message, boolean b) {
    Assert.assertTrue(message, b);
  }

  public void assertFalse(String description, boolean b) {
    Assert.assertFalse(description, b);
  }

  public void assertEquals(String expected, String actual) {
    Assert.assertEquals(expected, actual);
  }

  public void assertEquals(Object expected, Object actual) {
    Assert.assertEquals(expected, actual);
  }

  public void assertEquals(String message, String expected, String actual) {
    Assert.assertEquals(message, expected, actual);
  }

  public void assertEquals(String message, Object expected, Object actual) {
    Assert.assertEquals(message, expected, actual);
  }

  public void assertSame(String message, Object expected, Object actual) {
    Assert.assertSame(message, expected, actual);
  }

  public void assertNotNull(String message, Object o) {
    Assert.assertNotNull(message, o);
  }

  public void assertNull(String message, Object o) {
    Assert.assertNull(message, o);
  }
}
