package org.uispec4j.utils;

import org.uispec4j.assertion.testlibrairies.AssertAdapter;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility for checking colors given either hexa or "natural language" string descriptions.
 * <p>For all assertEquals/equals methods, the "expected" parameter can be either:
 * <ul>
 * <li>A <code>Color</code> object specifying the exact color</li>
 * <li>A <code>String</code> object containing the hexadecimal description of the exact color</li>
 * <li>A <code>String</code> object containing the name of a color for an approximate comparison.
 * The authorized names are those of the predefined Color objects defined in the Java
 * <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/awt/Color.html">Color</a> class.</li>
 * </ul>
 * </p>
 *
 * @see <a href="http://www.uispec4j.org/usingcolors.html">Using colors</a>
 */
public final class ColorUtils {
  private static final Map nameToColorMap = buildNameToColorMap();
  static final String UNEXPECTED_COLOR_CLASS = "Expected color should be an instance of Color or String";

  private ColorUtils() {
  }

  public static void assertEquals(Object expected, Color actual) {
    assertEquals(null, expected, actual);
  }

  public static void assertEquals(String message, Object expected, Color actual) {
    if (!equals(expected, actual)) {
      String errorMsg = (message != null ? message + " " : "")
                        + "expected:<" + getColorDescription(expected)
                        + "> but was:<" + getColorDescription(actual)
                        + ">";
      AssertAdapter.fail(errorMsg);
    }
  }

  public static boolean equals(Object expectedColor, Color actual) {
    if (expectedColor instanceof Color) {
      return expectedColor.equals(actual);
    }
    else if (expectedColor instanceof String) {
      return equals((String)expectedColor, actual);
    }
    throw new IllegalArgumentException(UNEXPECTED_COLOR_CLASS);
  }

  public static boolean equals(String expectedColor, Color actual) {
    Color foundColor = (Color)nameToColorMap.get(expectedColor.toUpperCase());
    if (foundColor != null) {
      return equals(foundColor, actual, true);
    }

    try {
      foundColor = getColor(expectedColor);
      return equals(foundColor, actual, false);
    }
    catch (NumberFormatException e) {
      throw new IllegalArgumentException("'" + expectedColor + "' does not seem to be a color");
    }
  }

  public static Color getColor(String hexaString) {
    return new Color(Integer.parseInt(hexaString, 16));
  }

  /**
   * Returns a normalized string description given a Color or another String description
   */
  public static String getColorDescription(Object color) {
    if (color instanceof Color) {
      return getColorDescription((Color)color);
    }
    else if (color instanceof String) {
      return getColorDescription((String)color);
    }
    throw new IllegalArgumentException(UNEXPECTED_COLOR_CLASS);
  }

  /**
   * Returns a description string for a given Color object
   */
  public static String getColorDescription(Color color) {
    return toHexString(color).toUpperCase();
  }

  /**
   * Normalizes a given color description string
   */
  public static String getColorDescription(String color) {
    return color.toUpperCase();
  }

  private static boolean equals(Color expected, Color actual, boolean matchBySimilarity) {
    if (matchBySimilarity) {
      return computeHSBDistance(expected, actual) < 0.9;
    }
    else {
      return expected.equals(actual);
    }
  }

  private static double computeHSBDistance(Color expected, Color actual) {
    float[] expectedHSB = toHSB(expected);
    float[] actualHSB = toHSB(actual);

    return Math.sqrt(Math.pow((expectedHSB[0] - actualHSB[0]) * 20, 2)
                     + Math.pow(expectedHSB[1] - actualHSB[1], 2)
                     + Math.pow((expectedHSB[2] - actualHSB[2]) * 2, 2));
  }

  private static float[] toHSB(Color aColor) {
    return Color.RGBtoHSB(aColor.getRed(), aColor.getGreen(), aColor.getBlue(), null);
  }

  private static Map buildNameToColorMap() {
    Map colorMap = new HashMap();
    Field[] fields = Color.class.getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      if (isConstantColorField(field)) {
        try {
          Color color = (Color)field.get(null);
          colorMap.put(field.getName().toUpperCase(), color);
        }
        catch (IllegalAccessException e) {
          // Should not occur because the field is public and static
        }
      }
    }
    return colorMap;
  }

  private static boolean isConstantColorField(Field field) {
    return Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())
           && Color.class == field.getType();
  }

  private static String toHexString(Color color) {
    return Integer.toHexString(color.getRGB()).substring(2);
  }
}
