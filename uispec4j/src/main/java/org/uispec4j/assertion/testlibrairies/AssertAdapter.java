package org.uispec4j.assertion.testlibrairies;

public class AssertAdapter {
  private static final TestLibrary ASSERT;

  static {
    ASSERT = TestLibraries.loadDependency();
  }

  public static void fail(String message) {
    ASSERT.fail(message);
  }

  public static void assertTrue(boolean b) {
    ASSERT.assertTrue(b);
  }

  public static void assertTrue(String message, boolean b) {
    ASSERT.assertTrue(message, b);
  }

  public static void assertFalse(String description, boolean b) {
    ASSERT.assertFalse(description, b);
  }

  public static void assertEquals(String expected, String actual) {
    ASSERT.assertEquals(expected, actual);
  }

  public static void assertEquals(Object expected, Object actual) {
    ASSERT.assertEquals(expected, actual);
  }

  public static void assertEquals(String message, String expected, String actual) {
    ASSERT.assertEquals(message, expected, actual);
  }

  public static void assertEquals(String message, Object expected, Object actual) {
    ASSERT.assertEquals(message, expected, actual);
  }

  public static void assertSame(String message, Object expected, Object actual) {
    ASSERT.assertSame(message, expected, actual);
  }

  public static void assertNotNull(String message, Object o) {
    ASSERT.assertNotNull(message, o);
  }

  public static void assertNull(String message, Object o) {
    ASSERT.assertNull(message, o);
  }
}
