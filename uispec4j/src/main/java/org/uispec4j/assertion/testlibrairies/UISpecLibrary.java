package org.uispec4j.assertion.testlibrairies;

class UISpecLibrary implements TestLibrary {
  public void fail(String message) {
    throw new RuntimeException(message);
  }

  public void assertTrue(boolean b) {
    assertTrue("Expected true, but was false.", b);
  }

  public void assertTrue(String message, boolean b) {
    if (!b) {
      fail(message);
    }
  }

  public void assertFalse(String description, boolean b) {
    assertTrue(description, !b);
  }

  public void assertEquals(String expected, String actual) {
    assertEquals("", expected, actual);
  }

  public void assertEquals(Object expected, Object actual) {
    assertEquals("", expected, actual);
  }

  public void assertEquals(String message, String expected, String actual) {
    assertTrue(message, (expected == null)? actual == null : expected.equals(actual));
  }

  public void assertEquals(String message, Object expected, Object actual) {
    assertTrue(message, (expected == null)? actual == null : expected.equals(actual));
  }

  public void assertSame(String message, Object expected, Object actual) {
    assertTrue(message, expected == actual);
  }

  public void assertNotNull(String message, Object o) {
    assertTrue(message, o != null);
  }

  public void assertNull(String message, Object o) {
    assertTrue(message, o == null);
  }
}
