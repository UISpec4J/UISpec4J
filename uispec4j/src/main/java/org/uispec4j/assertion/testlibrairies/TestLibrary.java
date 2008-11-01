package org.uispec4j.assertion.testlibrairies;

interface TestLibrary {
  public void fail(String message);

  public void assertTrue(boolean b);

  public void assertTrue(String message, boolean b);

  public void assertFalse(String description, boolean b);

  public void assertEquals(String expected, String actual);

  public void assertEquals(Object expected, Object actual);

  public void assertEquals(String message, String expected, String actual);

  public void assertEquals(String message, Object expected, Object actual);

  public void assertSame(String message, Object expected, Object actual);

  public void assertNotNull(String message, Object o);

  public void assertNull(String message, Object o);
}
