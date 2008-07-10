package org.uispec4j.utils;

import org.uispec4j.UIComponent;
import org.uispec4j.assertion.Assertion;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UIComponentFactoryTest extends UnitTestCase {

  public void testInitWithDummyComponent() throws Exception {
    Map result = new HashMap();
    UIComponentFactory.fillSwingToUISpecMap(result, new Class[]{ComponentWithSwingClassesField.class});
    assertEquals(ComponentWithSwingClassesField.class, result.get(JButton.class));
  }

  public void testInitErrorForClassWithoutSwingClassesField() throws Exception {
    checkInitError(ComponentWithoutSwingClassesField.class,
                   "Class " + ComponentWithoutSwingClassesField.class +
                   " should have a field 'static Class[] SWING_CLASSES'");
  }

  public void testInitErrorForClassWithUninitialisedSwingClassesField() throws Exception {
    checkInitError(ComponentWithUninitializedSwingClassesField.class,
                   "Field 'static Class[] SWING_CLASSES' in class " +
                   ComponentWithUninitializedSwingClassesField.class +
                   " should be initialized");
  }

  public void testNonSwingClassInSwingClassesField() throws Exception {
    checkInitError(ComponentWithInvalidSwingClass.class,
                   "Class '" + String.class + "' in field 'SWING_CLASSES' of class '" +
                   ComponentWithInvalidSwingClass.class +
                   "' should extend '" + Container.class + "'");
  }

  public void testInitErrorForClassWithoutTypeName() throws Exception {
    checkInitError(ComponentWithoutTypeName.class,
                   "Class " + ComponentWithoutTypeName.class +
                   " should have a field 'public static String TYPE_NAME'");
  }

  public void testInitErrorForClassWithUninitialisedTypeName() throws Exception {
    checkInitError(ComponentWithUninitializedTypeNameField.class,
                   "Field 'static String TYPE_NAME' in class " +
                   ComponentWithUninitializedTypeNameField.class +
                   " should be initialized");
  }

  public void testInitErrorForClassWithPrivateTypeName() throws Exception {
    checkInitError(ComponentWithPrivateTypeNameField.class,
                   "Field 'static String TYPE_NAME' in class " +
                   ComponentWithPrivateTypeNameField.class +
                   " should be public");
  }

  public void testInitErrorForClassWithUnexpectedTypeNameClass() throws Exception {
    checkInitError(ComponentWithoutUnexpectedTypeNameClass.class,
                   "Static field 'TYPE_NAME' in class " +
                   ComponentWithoutUnexpectedTypeNameClass.class +
                   " should be of type String");
  }

  public void testComponentClassesMustImplementUIComponent() throws Exception {
    checkInitError(String.class,
                   "Class '" + String.class + "' should implement " + UIComponent.class);
  }

  private void checkInitError(Class swingClass, String message) {
    try {
      UIComponentFactory.initUIComponentClasses(new Class[]{swingClass}, new ArrayList());
      fail();
    }
    catch (RuntimeException e) {
      assertEquals(message, e.getMessage());
    }
  }

  private static class ComponentWithSwingClassesField extends DummyUIComponent {
    public static Class[] SWING_CLASSES = {JButton.class};
    public static String TYPE_NAME = "Type";
  }

  private static class ComponentWithoutSwingClassesField extends DummyUIComponent {
    public static String TYPE_NAME = "Type";
  }

  private static class ComponentWithUninitializedSwingClassesField extends DummyUIComponent {
    static Class[] SWING_CLASSES;
    public static String TYPE_NAME = "Type";
  }

  private static class ComponentWithInvalidSwingClass extends DummyUIComponent {
    static Class[] SWING_CLASSES = {String.class};
    public static String TYPE_NAME = "Type";
  }

  private static class ComponentWithoutTypeName extends DummyUIComponent {
    public static Class[] SWING_CLASSES = {org.uispec4j.AbstractButton.class};
  }

  private static class ComponentWithUninitializedTypeNameField extends DummyUIComponent {
    public static Class[] SWING_CLASSES = {org.uispec4j.AbstractButton.class};
    public static String TYPE_NAME;
  }

  private static class ComponentWithPrivateTypeNameField extends DummyUIComponent {
    public static Class[] SWING_CLASSES = {org.uispec4j.AbstractButton.class};
    private static String TYPE_NAME = "Type";
  }

  private static class ComponentWithoutUnexpectedTypeNameClass extends DummyUIComponent {
    public static Class[] SWING_CLASSES = {org.uispec4j.AbstractButton.class};
    public static Object TYPE_NAME = new Integer(3);
  }

  private static class DummyUIComponent implements UIComponent {

    public Component getAwtComponent() {
      return null;
    }

    public String getDescription() {
      return null;
    }

    public String getDescriptionTypeName() {
      return null;
    }

    public Assertion isEnabled() {
      return new Assertion() {
        public void check() {
        }
      };
    }

    public Assertion isVisible() {
      return new Assertion() {
        public void check() throws Exception {
        }
      };
    }

    public String getName() {
      return null;
    }

    public org.uispec4j.Panel getContainer(String parentName) {
      return null;
    }

    public String getLabel() {
      return null;
    }
  }
}
