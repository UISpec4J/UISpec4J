package org.uispec4j.utils;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import junit.framework.AssertionFailedError;

public class ArrayUtilsTest extends UnitTestCase {
  @Test
  public void testToStringWithObjects() throws Exception {
    assertEquals("[3,true,Hello]", ArrayUtils.toString(new Object[]{new Integer(3), Boolean.TRUE, "Hello"}));
  }

  public void testToStringForArrays() {
    assertEquals("[]", ArrayUtils.toString(new String[0]));
    assertEquals("[a]", ArrayUtils.toString(new String[]{"a"}));
    assertEquals("[a,b]", ArrayUtils.toString(new String[]{"a", "b"}));
    assertEquals("[a,b,c]", ArrayUtils.toString(new String[]{"a", "b", "c"}));

    assertEquals("[a,b,[null,d],[e,[f,g]],h]", ArrayUtils.toString(new Object[]{
      "a",
      "b",
      new String[]{null, "d"},
      new Object[]{"e", new String[]{"f", "g"}},
      "h"
    }));
  }

  @Test
  public void testToStringForLists() throws Exception {
    List list = new ArrayList();
    assertEquals("[]", ArrayUtils.toString(list));
    list.add("a");
    assertEquals("[a]", ArrayUtils.toString(new String[]{"a"}));
    list.add("b");
    assertEquals("[a,b]", ArrayUtils.toString(new String[]{"a", "b"}));
    list.add("c");
    assertEquals("[a,b,c]", ArrayUtils.toString(new String[]{"a", "b", "c"}));
  }

  @Test
  public void testToStringWithIntegers() throws Exception {
    assertEquals("[4,6,9]",
                 ArrayUtils.toString(new int[]{4, 6, 9}));
  }

  @Test
  public void testToStringForTwoDimensionalArrays() throws Exception {
    assertEquals("[]", ArrayUtils.toString(new String[][]{}));
    assertEquals("[[a]]", ArrayUtils.toString(new String[][]{{"a"}}));
    assertEquals("[[a,\tb]\n [c,\td]]", ArrayUtils.toString(new String[][]{{"a", "b"}, {"c", "d"}}));
  }

  @Test
  public void testAssertEmptyForAnArray() throws Exception {
    ArrayUtils.assertEmpty((String[])null);
    ArrayUtils.assertEmpty(new Object[0]);
    try {
      ArrayUtils.assertEmpty(new String[]{"a"});
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("Array should be empty but is [a]", e.getMessage());
    }
  }

  @Test
  public void testAssertEmpty() throws Exception {
    ArrayUtils.assertEmpty((List[])null);
    ArrayUtils.assertEmpty(Collections.EMPTY_LIST);
    try {
      List list = new ArrayList();
      list.add("a");
      ArrayUtils.assertEmpty(list);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionFailedError e) {
      assertEquals("List should be empty but is [a]", e.getMessage());
    }
  }
}
