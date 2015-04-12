package org.uispec4j.utils;

import java.awt.*;

/**
 * Test class for {@link ColorUtils}
 */
public class ColorUtilsTest extends UnitTestCase {

  public void testAssertEqualsWithPrefix() throws Exception {
    ColorUtils.assertEquals("Message", "FF0000", Color.RED);
    ColorUtils.assertEquals("Message", "red", Color.RED);
    ColorUtils.assertEquals("Message", "red", new Color(0xDD1111));

    checkAssertEqualsError("Message", "112233", new Color(0x332211),
                           "Message - expected:<112233> but was:<332211>");
    checkAssertEqualsError("Message", ColorUtils.getColor("112233"), new Color(0x332211),
                           "Message - expected:<112233> but was:<332211>");
  }

  public void testAssertEquals() throws Exception {
    ColorUtils.assertEquals("FF0000", Color.RED);
    ColorUtils.assertEquals("red", Color.RED);
    ColorUtils.assertEquals("red", new Color(0xDD1111));

    checkAssertEqualsError("112233", new Color(0x332211),
                           "expected:<112233> but was:<332211>");
  }

  public void testInvalidArgumentsToAssertEquals() throws Exception {
    try {
      ColorUtils.assertEquals(2, Color.red);
      fail();
    }
    catch (IllegalArgumentException error) {
      assertEquals(ColorUtils.UNEXPECTED_COLOR_CLASS, error.getMessage());
    }

    try {
      ColorUtils.assertEquals("Msg", 2, Color.red);
      fail();
    }
    catch (IllegalArgumentException error) {
      assertEquals(ColorUtils.UNEXPECTED_COLOR_CLASS, error.getMessage());
    }
  }

  public void testEqualsByHexaAndRGB() throws Exception {
    assertTrue(ColorUtils.equals("FF0000", new Color(255, 0, 0)));
    assertTrue(ColorUtils.equals("FF0000", new Color(0xFF0000)));
  }

  public void testEqualsByName() throws Exception {
    assertTrue(ColorUtils.equals("red", Color.RED));
    assertTrue(ColorUtils.equals("RED", Color.RED));

    assertFalse(ColorUtils.equals("blue", Color.RED));
    assertTrue(ColorUtils.equals("darkGray", Color.darkGray));
  }

  public void testEqualsWithAdditionalNamedColor() throws Exception {
    assertTrue(ColorUtils.equals("darkGrey", ColorUtils.getColor("555555")));
    assertTrue(ColorUtils.equals("darkRed", ColorUtils.getColor("550000")));
    assertTrue(ColorUtils.equals("darkGreen", ColorUtils.getColor("005500")));
    assertTrue(ColorUtils.equals("darkBlue", ColorUtils.getColor("000055")));

    assertTrue(ColorUtils.equals("DARK_GREY", ColorUtils.getColor("555555")));
    assertTrue(ColorUtils.equals("DARK_RED", ColorUtils.getColor("550000")));
    assertTrue(ColorUtils.equals("DARK_GREEN", ColorUtils.getColor("005500")));
    assertTrue(ColorUtils.equals("DARK_BLUE", ColorUtils.getColor("000055")));
  }

  public void testEqualsByNameAndSimilarity() throws Exception {
    assertFalse(ColorUtils.equals("blue", Color.red));
    assertTrue(ColorUtils.equals("red", Color.red));
    assertTrue(ColorUtils.equals("red", new Color(170, 5, 5)));
    assertTrue(ColorUtils.equals("red", ColorUtils.getColor("ffc8c8")));
    assertTrue(ColorUtils.equals("yellow", ColorUtils.getColor("ffffd0")));

    assertTrue(ColorUtils.equals("lightGray", Color.gray));
    assertFalse(ColorUtils.equals("white", Color.gray));
    assertFalse(ColorUtils.equals("black", Color.gray));

    assertFalse(ColorUtils.equals("red", ColorUtils.getColor("ffaaff")));
  }

  public void testBadColorDescription() throws Exception {
    try {
      ColorUtils.equals("not a color", Color.red);
      fail("Should have failed because 'not a color' is not a color");
    }
    catch (IllegalArgumentException error) {
      assertEquals("'not a color' does not seem to be a color", error.getMessage());
    }

    try {
      ColorUtils.equals(2, Color.red);
      fail();
    }
    catch (IllegalArgumentException error) {
      assertEquals(ColorUtils.UNEXPECTED_COLOR_CLASS, error.getMessage());
    }
  }

  public void testGetColor() throws Exception {
    assertEquals(Color.red, ColorUtils.getColor("ff0000"));
    assertEquals(Color.green, ColorUtils.getColor("00ff00"));
    assertEquals(Color.blue, ColorUtils.getColor("0000ff"));
  }

  public void testGetColorDescriptionByColor() throws Exception {
    assertEquals("112233", ColorUtils.getColorDescription(new Color(0x112233)));

    assertEquals("FF0000", ColorUtils.getColorDescription(Color.RED));
    assertEquals("404040", ColorUtils.getColorDescription(Color.DARK_GRAY));
  }

  public void testGetColorDescriptionByString() throws Exception {
    assertEquals("112233", ColorUtils.getColorDescription("112233"));

    assertEquals("RED", ColorUtils.getColorDescription("red"));
    assertEquals("FF0000", ColorUtils.getColorDescription("FF0000"));
    assertEquals("0000FF", ColorUtils.getColorDescription("0000ff"));
  }

  private void checkAssertEqualsError(String messagePrefix,
                                      Object expectedColor,
                                      Color actualColor,
                                      String errorMessage) {
    try {
      ColorUtils.assertEquals(messagePrefix, expectedColor, actualColor);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals(errorMessage, e.getMessage());
    }
  }

  private void checkAssertEqualsError(String expected, Color actual, String errorMessage) {
    try {
      ColorUtils.assertEquals(expected, actual);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
      assertEquals(errorMessage, e.getMessage());
    }
  }
}

