package org.uispec4j.assertion;

import org.junit.Test;
import org.uispec4j.utils.UnitTestCase;

public class AssertionTest extends UnitTestCase {
  @Test
  public void testIsTrue() throws Exception {
    assertTrue(DummyAssertion.TRUE.isTrue());
    assertFalse(DummyAssertion.FALSE.isTrue());
  }
}