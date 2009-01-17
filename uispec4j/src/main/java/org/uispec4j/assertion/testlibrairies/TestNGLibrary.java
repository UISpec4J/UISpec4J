package org.uispec4j.assertion.testlibrairies;

import org.testng.Assert;

class TestNGLibrary implements TestLibrary {
  public void fail(String message) {
    Assert.fail(message);
  }

  public void assertTrue(boolean b) {
    Assert.assertTrue(b);
  }

  public void assertTrue(String message, boolean b) {
    Assert.assertTrue(b, message);
  }

  public void assertFalse(String description, boolean b) {
    Assert.assertFalse(b, description);
  }

  public void assertEquals(String expected, String actual) {
    Assert.assertEquals(actual, expected);
  }

  public void assertEquals(Object expected, Object actual) {
    Assert.assertEquals(actual, expected);
  }

  public void assertEquals(String message, String expected, String actual) {
    Assert.assertEquals(actual, expected, message);
  }

  public void assertEquals(String message, Object expected, Object actual) {
    Assert.assertEquals(actual, expected, message);
  }

  public void assertSame(String message, Object expected, Object actual) {
    Assert.assertSame(actual, expected, message);
  }

  public void assertNotNull(String message, Object o) {
    Assert.assertNotNull(o, message);
  }

  public void assertNull(String message, Object o) {
    Assert.assertNull(o, message);
  }
}
