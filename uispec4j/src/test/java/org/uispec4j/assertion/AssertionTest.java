package org.uispec4j.assertion;

import org.uispec4j.utils.UnitTestCase;

public class AssertionTest extends UnitTestCase {
  public void testIsTrue() throws Exception {
    assertTrue(DummyAssertion.TRUE.isTrue());
    assertFalse(DummyAssertion.FALSE.isTrue());
  }
}