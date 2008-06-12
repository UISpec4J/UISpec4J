package org.uispec4j.utils;

import org.uispec4j.Button;
import org.uispec4j.*;
import org.uispec4j.MenuBar;
import org.uispec4j.MenuItem;
import org.uispec4j.Panel;
import org.uispec4j.Window;
import org.uispec4j.Desktop;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.List;

/**
 * Factory which creates UIComponent wrappers for Swing components.
 */
public final class UIComponentFactory {

  private static final Class[] COMPONENT_CLASSES = {
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

  private static List ADDITIONAL_CLASSES;
  private static Map SWING_TO_UISPEC_MAP;

  /* Warning : reflection call in the extension mechanism */
  public static void addUIComponentClass(Class uiComponentClass) {
    getAdditionalClassesList().add(uiComponentClass);
  }

  public static UIComponent createUIComponent(Component component) {
    Class swingClass = getSwingClass(component.getClass());
    if (swingClass == null) {
      return null;
    }
    Class uiSpecClass = (Class)getSwingToUISpecMap().get(swingClass);
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

  static void fillSwingToUISpecMap(Map map, Class[] uiSpecClasses) {
    for (int i = 0; i < uiSpecClasses.length; i++) {
      Class uiSpecClass = uiSpecClasses[i];
      addUIComponentClass(uiSpecClass, map);
    }
  }

  static Map initUIComponentClasses(Class[] uiComponentClasses, List classes) {
    List allClasses = new ArrayList();
    allClasses.addAll(Arrays.asList(uiComponentClasses));
    allClasses.addAll(classes);
    Class[] allClassesArray = (Class[])allClasses.toArray(new Class[allClasses.size()]);
    checkClasses(allClassesArray);

    Map result = new HashMap();
    fillSwingToUISpecMap(result, allClassesArray);
    return result;
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
    if (getSwingToUISpecMap().containsKey(swingClass)) {
      return swingClass;
    }
    return getSwingClass(swingClass.getSuperclass());
  }

  private static void checkClasses(Class[] classes) {
    for (int i = 0; i < classes.length; i++) {
      checkClass(classes[i]);
      checkTypeNameField(classes[i]);
    }
  }

  private static void checkClass(Class uiComponentClass) {
    if (!UIComponent.class.isAssignableFrom(uiComponentClass)) {
      throw new RuntimeException("Class '" + uiComponentClass + "' should implement " + UIComponent.class);
    }
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

  private static Map getSwingToUISpecMap() {
    if (SWING_TO_UISPEC_MAP == null) {
      SWING_TO_UISPEC_MAP = new HashMap();
      SWING_TO_UISPEC_MAP = initUIComponentClasses(COMPONENT_CLASSES, getAdditionalClassesList());
    }
    return SWING_TO_UISPEC_MAP;
  }

  private static List getAdditionalClassesList() {
    if (ADDITIONAL_CLASSES == null) {
      ADDITIONAL_CLASSES = new ArrayList();
    }
    return ADDITIONAL_CLASSES;
  }
}
