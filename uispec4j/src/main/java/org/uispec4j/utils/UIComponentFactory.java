package org.uispec4j.utils;

import org.uispec4j.Button;
import org.uispec4j.*;
import org.uispec4j.MenuBar;
import org.uispec4j.MenuItem;
import org.uispec4j.Panel;
import org.uispec4j.Window;
  
import java.awt.Container;
import java.awt.Component;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Factory which creates UIComponent wrappers for Swing components.
 */
public final class UIComponentFactory {

  private static final Class[] BUILT_IN_COMPONENT_CLASSES = {
    Button.class,
    CheckBox.class,
    ComboBox.class,
    Desktop.class,
    ListBox.class,
    MenuBar.class,
    MenuItem.class,
    Panel.class,
    PasswordField.class,
    ProgressBar.class,
    RadioButton.class,
    Spinner.class,
    TabGroup.class,
    Table.class,
    TextBox.class,
    ToggleButton.class,
    Tree.class,
    Window.class,
    Slider.class
  };

  private static volatile Map<Class<? extends Container>, Class<? extends UIComponent>> SWING_TO_UISPEC_MAP = new HashMap();
  private static Set<Class<? extends UIComponent>> COMPONENT_CLASSES = new HashSet();
  private static boolean initialized;

  /* Warning : reflection call in the extension mechanism */
  public static void addUIComponentClass(Class uiComponentClass) {
    register(uiComponentClass);
  }

  public synchronized static <T extends UIComponent> void register(Class<T>... uiSpecClasses) {
    for (Class<T> uiSpecClass : uiSpecClasses) {
      if (readyToAdd(uiSpecClass)) {
        addUIComponentClass(uiSpecClass, SWING_TO_UISPEC_MAP);
        COMPONENT_CLASSES.add(uiSpecClass);
      }
    }
  }

  public static UIComponent createUIComponent(Component component) {
    Class swingClass = getSwingClass(component.getClass());
    if (swingClass == null) {
      return null;
    }
    Class uiSpecClass = getComponentMap().get(swingClass);
    try {
      Constructor constructor = getConstructor(uiSpecClass, swingClass);
      return (UIComponent)constructor.newInstance(new Object[]{component});
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static UIComponent[] createUIComponents(Component[] swingComponents) {
    UIComponent[] components = new UIComponent[swingComponents.length];
    for (int i = 0; i < swingComponents.length; i++) {
      components[i] = createUIComponent(swingComponents[i]);
    }
    return components;
  }

  private static Constructor getConstructor(Class uiSpecClass, Class swingClass) throws NoSuchMethodException {
    try {
      return uiSpecClass.getConstructor(new Class[]{swingClass});
    }
    catch (NoSuchMethodException e) {
      return getConstructor(uiSpecClass, swingClass.getSuperclass());
    }
  }

  private static void addUIComponentClass(Class uiSpecClass, Map map) {
    Class[] classes = UIComponentAnalyzer.getSwingClasses(uiSpecClass);
    for (int j = 0; j < classes.length; j++) {
      checkSwingClass(classes[j], uiSpecClass);
      map.put(classes[j], uiSpecClass);
    }
  }

  private static void checkSwingClass(Class aClass, Class uiSpecClass) {
    if (!Container.class.isAssignableFrom(aClass)) {
      throw new RuntimeException("Class '" + aClass +
                                 "' in field 'SWING_CLASSES' of class '" +
                                 uiSpecClass +
                                 "' should extend '" + Container.class + "'");
    }
  }

  private static Class getSwingClass(Class swingClass) {
    if (swingClass == null) {
      return null;
    }
    if (getComponentMap().containsKey(swingClass)) {
      return swingClass;
    }
    return getSwingClass(swingClass.getSuperclass());
  }

  private static boolean readyToAdd(Class uiComponentClass) {
    if (COMPONENT_CLASSES.contains(uiComponentClass)) {
      return false;
    }
    if (!UIComponent.class.isAssignableFrom(uiComponentClass)) {
      throw new RuntimeException("Class '" + uiComponentClass + "' should implement " + UIComponent.class);
    }
    checkTypeNameField(uiComponentClass);
    return true;
  }

  private static void checkTypeNameField(Class uiComponentClass) {
    Object value;
    try {
      value = uiComponentClass.getDeclaredField("TYPE_NAME").get(null);
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException("Field 'static String TYPE_NAME' in class " +
                                 uiComponentClass +
                                 " should be public");
    }
    catch (NoSuchFieldException e) {
      throw new RuntimeException("Class " + uiComponentClass +
                                 " should have a field 'public static String TYPE_NAME'");
    }
    if (value == null) {
      throw new RuntimeException("Field 'static String TYPE_NAME' in class " +
                                 uiComponentClass +
                                 " should be initialized");
    }
    if (!String.class.isInstance(value)) {
      throw new RuntimeException("Static field 'TYPE_NAME' in class " +
                                 uiComponentClass +
                                 " should be of type String");
    }
  }

  private synchronized static Map<Class<? extends Container>, Class<? extends UIComponent>> getComponentMap() {
    if (!initialized) {
      register(BUILT_IN_COMPONENT_CLASSES);
      initialized = true;
    }
    return SWING_TO_UISPEC_MAP;
  }

}
