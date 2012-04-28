package org.uispec4j;

import java.awt.AWTError;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.lang.reflect.Field;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import org.uispec4j.interception.toolkit.ToolkitHandler;
import org.uispec4j.interception.ui.UISpecLF;

/**
 * Facade for the initialization of the library, mainly used for the interception mechanism.
 * 
 * @see <a href="http://www.uispec4j.org/intercepting-windows">Intercepting windows</a>
 */
public class UISpec4J {
  public static final int DEFAULT_ASSERTION_TIME_LIMIT = 500;
  private static final String SYSTEM_PROPERTY = "awt.toolkit";
  private static final String UNIX_SYSTEM_DEFAULT_VALUE = "sun.awt.motif.MToolkit";
  private static final String WINDOWS_SYSTEM_DEFAULT_VALUE = "sun.awt.windows.WToolkit";
  private static long windowInterceptionTimeLimit = 10000;
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
      System.setProperty(SYSTEM_PROPERTY, "sun.awt.motif.MToolkit");
    }
    initToolkit();
    UISpecLF.init();
  }

  private static void initToolkit() {
    try {
      ToolkitHandler toolkitHandler = new ToolkitHandler();
      Toolkit awtToolkit = createProxy(toolkitHandler, toolkitHandler, resolveAwtToolkit());

      Field toolkitField = Toolkit.class.getDeclaredField("toolkit");
      toolkitField.setAccessible(true);
      toolkitField.set(null, awtToolkit);
    } catch (Exception e) {
      throw new RuntimeException("Unable to initialize toolkit for interception.", e);
    }
  }

  private static <T> T createProxy(MethodFilter filter, MethodHandler handler, Class<T> klass) {
    try {
      ProxyFactory factory = new ProxyFactory();
      factory.setSuperclass(klass);
      factory.setFilter(filter);
      ProxyObject proxy = (ProxyObject) factory.createClass().newInstance();
      proxy.setHandler(handler);
      return (T) proxy;
    } catch (InstantiationException e) {
      throw new RuntimeException("Unable to initialize toolkit for interception.", e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Unable to initialize toolkit for interception.", e);
    }
  }

  private static Class<Toolkit> resolveAwtToolkit() {
    String awtToolkit = System.getProperty(SYSTEM_PROPERTY);
    if (awtToolkit == null) {
      try {
        Class.forName(WINDOWS_SYSTEM_DEFAULT_VALUE);
        awtToolkit = WINDOWS_SYSTEM_DEFAULT_VALUE;
      } catch (ClassNotFoundException e) {
        try {
          Class.forName(UNIX_SYSTEM_DEFAULT_VALUE);
          awtToolkit = UNIX_SYSTEM_DEFAULT_VALUE;
        } catch (ClassNotFoundException e1) {
          throw new AWTError("Unable to locate AWT Toolkit");
        }
      }
    }
    try {
      return (Class<Toolkit>) Class.forName(awtToolkit);
    } catch (ClassNotFoundException e) {
      throw new AWTError("Unable to load AWT Toolkit");
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
