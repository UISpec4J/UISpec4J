package org.uispec4j.utils;

import org.uispec4j.Trigger;

import java.lang.reflect.Method;

public class MainClassTrigger implements Trigger {
  private Method main;
  private String[] args;

  public MainClassTrigger(Class mainClass, String... args) {
    this.args = args;
    try {
      main = mainClass.getMethod("main", args.getClass());
    }
    catch (NoSuchMethodException e) {
      throw new RuntimeException("Class " + mainClass.getName() + " has no method: public static void main(String[])");
    }
  }

  public void run() throws Exception {
    main.invoke(null, new Object[]{args});
  }
}
