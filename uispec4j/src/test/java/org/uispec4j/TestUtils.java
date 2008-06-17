package org.uispec4j;

import junit.framework.Assert;
import org.uispec4j.utils.Utils;

import java.awt.*;
import java.util.Arrays;
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

  public static <T extends Component> void assertSwingComponentsEquals (T[] expected, T[] actual) {
    Utils.assertSetEquals(expected, actual, SwingComponentStringifier.instance());
  }


    public static boolean isMacOsX() {
        return "Mac OS X".equals(System.getProperty("os.name"));
    }
}
