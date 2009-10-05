package org.uispec4j;

import junit.framework.Assert;
import org.uispec4j.utils.Utils;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TestUtils {
  public static void assertUIComponentRefersTo(Component expectedComponent, UIComponent uiComponent) {
    Assert.assertSame(expectedComponent, uiComponent.getAwtComponent());
  }

  public static void assertUIComponentsReferTo(Component[] expectedComponents, UIComponent[] uiComponents) {
    int expectedLength = expectedComponents.length;
    int actualLength = uiComponents.length;
    Assert.assertEquals("Expected " + expectedLength + " components but was " + actualLength,
                        expectedLength, actualLength);

    List list = Arrays.asList(expectedComponents);
    for (int i = 0; i < uiComponents.length; i++) {
      Assert.assertTrue("unexpected component ", list.contains(uiComponents[i].getAwtComponent()));
    }
  }

  public static <T extends Component> void assertSwingComponentsEquals(T[] expected, T[] actual) {
    Utils.assertSetEquals(expected, actual, SwingComponentStringifier.instance());
  }

  public static boolean isMacOsX() {
    return "Mac OS X".equals(System.getProperty("os.name"));
  }

  public static <T> void assertEquals(T[] expected, T[] actual) {
    assertEquals(Arrays.asList(expected), Arrays.asList(actual));
  }

  public static <T> void assertEquals(T[] expected, List<T> actual) {
    assertEquals(Arrays.asList(expected), actual);
  }

  public static <T> void assertEquals(List<T> expectedList, List<T> actualList) {
    if (expectedList.size() != actualList.size()) {
      showDiff(expectedList, actualList);
    }
    if (!expectedList.equals(actualList)) {
      showDiff(expectedList, actualList);
    }
  }

  private static <T> void showDiff(Collection<T> expected, Collection<T> actual) {
    Assert.assertEquals(format("Expected", expected), format("Actual", actual));
  }

  private static <T> String format(String title, Collection<T> collection) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(title).append("\n");
    for (T t : collection) {
      buffer.append(t).append("\n");
    }
    return buffer.toString();
  }
}
