package org.uispec4j.extension;

import org.uispec4j.Panel;
import org.uispec4j.finder.ComponentMatcher;
import org.uispec4j.finder.ComponentMatchers;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GeneratedJarChecker {
  public static void main(String[] args) throws Exception {
    Class componentClass = getComponentClass(args[0]);

    JCountingButton jCountingButton = new JCountingButton("counter");
    Object newPanel = createUISpecPanel(Panel.class, jCountingButton);

    checkGetCoutingButtonMethods(newPanel, jCountingButton, componentClass);

    System.out.println("OK");
  }

  private static void checkGetCoutingButtonMethods(Object newPanel, JCountingButton jCountingButton, Class componentClass)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Method methodWithStringArg = Panel.class.getMethod("getCountingButton", String.class);
    checkGetCountingButton(methodWithStringArg, newPanel, jCountingButton, new Object[]{"counter"}, componentClass);

    Method methodWithMatcherArg = Panel.class.getMethod("getCountingButton", ComponentMatcher.class);
    checkGetCountingButton(methodWithMatcherArg, newPanel, jCountingButton,
                           new Object[]{ComponentMatchers.displayedNameIdentity("counter")}, componentClass);

    Method methodWithNoArgs = Panel.class.getMethod("getCountingButton");
    checkGetCountingButton(methodWithNoArgs, newPanel, jCountingButton, new Object[]{}, componentClass);
  }

  private static Class getComponentClass(String classname) throws ClassNotFoundException {
    return GeneratedJarChecker.class.getClassLoader().loadClass(classname);
  }

  private static void checkGetCountingButton(Method method, Object newPanel, JCountingButton jCountingButton, Object[] args, Class componentClass) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Object found = method.invoke(newPanel, args);
    check("The component should be an instance of " + componentClass.getName(), componentClass.isInstance(found));
    jCountingButton.reset();
    componentClass.getMethod("click").invoke(found);
    int count = jCountingButton.getCount();
    check("the button should have detected 1 event instead of " + count, count == 1);
  }

  private static Object createUISpecPanel(Class newPanelClass, JComponent component) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    JPanel jPanel = new JPanel();
    jPanel.add(component);
    Constructor constructor = newPanelClass.getConstructor(Container.class);
    return constructor.newInstance(jPanel);
  }

  private static void check(String s, boolean b) {
    if (!b) {
      throw new RuntimeException(s);
    }
  }

}
