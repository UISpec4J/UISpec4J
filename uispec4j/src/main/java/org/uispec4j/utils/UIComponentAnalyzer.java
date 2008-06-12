package org.uispec4j.utils;

public class UIComponentAnalyzer {
  public static Class[] getSwingClasses(Class uiComponentClass) {
    Class[] classes;
    try {
      classes = (Class[])uiComponentClass.getDeclaredField("SWING_CLASSES").get(null);
      if (classes == null) {
        throw new RuntimeException("Field 'static Class[] SWING_CLASSES' in class " + uiComponentClass +
                                   " should be initialized");
      }
    }
    catch (NoSuchFieldException e) {
      throw new RuntimeException("Class " + uiComponentClass +
                                 " should have a field 'static Class[] SWING_CLASSES'");
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException("Invalid UIComponent class " + uiComponentClass, e);
    }
    return classes;
  }

  public static String getTypeName(Class uiComponentClass) {
    String typeName = null;
    try {
      typeName = (String)uiComponentClass.getDeclaredField("TYPE_NAME").get(null);
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException("Invalid UIComponent class " + uiComponentClass, e);
    }
    catch (NoSuchFieldException e) {
      throw new RuntimeException("Invalid UIComponent class " + uiComponentClass, e);
    }
    return typeName;
  }
}
