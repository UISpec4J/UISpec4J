package org.uispec4j;

import org.uispec4j.interception.toolkit.UISpecToolkit;
import org.uispec4j.interception.ui.UISpecLF;

import java.awt.*;
import java.lang.reflect.Field;

/**
 * Facade for the initialization of the library, mainly used for the interception mechanism.
 *
 * @see <a href="http://www.uispec4j.org/intercepting-windows">Intercepting windows</a>
 */
public class UISpec4J {
  private static long windowInterceptionTimeLimit = 10000;
  public static final int DEFAULT_ASSERTION_TIME_LIMIT = 500;
  private static long assertionTimeLimit = DEFAULT_ASSERTION_TIME_LIMIT;

  /**
   * Initializes UISpec4J, for instance by setting up the interception mechanism.
   */
  public static void init() {
    if (GraphicsEnvironment.isHeadless()) {
      System.out.println("UISpec4J.init: warning: headless mode is not supported");
    }

    // Black magic - do not touch this (system.setProperty seem to load dynamic libraries)
    if ("Linux".equalsIgnoreCase(System.getProperty("os.name")) && "1.5".equals(System.getProperty("java.specification.version"))) {
      System.setProperty("awt.toolkit", "sun.awt.motif.MToolkit");
    }
    UISpecLF.init();
    initToolkit();
  }

  private static void initToolkit() {
    try {
      Field toolkitField = Toolkit.class.getDeclaredField("toolkit");
      toolkitField.setAccessible(true);
      toolkitField.set(null, new UISpecToolkit());
    }
    catch (Exception e) {
      throw new RuntimeException("Unable to initialize toolkit for interception.", e);
    }
  }

  /**
   * Sets the default timeout value (in milliseconds) used by the library for interception mechanism.
   * Default value is 10.000, i.e. 10s.
   */
  public static void setWindowInterceptionTimeLimit(long millis) {
    UISpec4J.windowInterceptionTimeLimit = millis;
  }

  /**
   * Returns the default timeout value (in milliseconds) used by the library for interception mechanism.
   * Default value is 10.000, i.e. 10s.
   */
  public static long getWindowInterceptionTimeLimit() {
    return windowInterceptionTimeLimit;
  }

  /**
   * Returns the default timeout value (in milliseconds) used when checking assertions.
   * Default value is 500, i.e. 0.5s.
   */
  public static long getAssertionTimeLimit() {
    return assertionTimeLimit;
  }

  /**
   * Sets the default timeout value (in milliseconds) used when checking assertions.
   * Default value is 500, i.e. 0.5s.
   */
  public static void setAssertionTimeLimit(long assertionTimeLimit) {
    UISpec4J.assertionTimeLimit = assertionTimeLimit;
  }
}
