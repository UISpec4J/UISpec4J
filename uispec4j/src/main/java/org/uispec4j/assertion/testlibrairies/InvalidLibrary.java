package org.uispec4j.assertion.testlibrairies;

class InvalidLibrary implements TestLibrary {
  private String message;

  public InvalidLibrary(String message) {
    this.message = message;
  }

  public void fail(String message) {
    error();
  }

  public void assertTrue(boolean b) {
    error();
  }

  public void assertTrue(String message, boolean b) {
    error();
  }

  public void assertFalse(String description, boolean b) {
    error();
  }

  public void assertEquals(String expected, String actual) {
    error();
  }

  public void assertEquals(Object expected, Object actual) {
    error();
  }

  public void assertEquals(String message, String expected, String actual) {
    error();
  }

  public void assertEquals(String message, Object expected, Object actual) {
    error();
  }

  public void assertSame(String message, Object expected, Object actual) {
    error();
  }

  public void assertNotNull(String message, Object o) {
    error();
  }

  public void assertNull(String message, Object o) {
    error();
  }

  private void error() {
    throw new Error(message);
  }
}
