package org.uispec4j;

import org.uispec4j.utils.ComponentUtils;
import org.uispec4j.utils.Stringifier;

import java.awt.*;

public class SwingComponentStringifier implements Stringifier {
  private static final SwingComponentStringifier INSTANCE = new SwingComponentStringifier();

  private SwingComponentStringifier() {
  }

  public String toString(Object obj) {
    Component component = ((Component) obj);
    return component.getClass().getName() + " (" + ComponentUtils.getDisplayedName(component) + ")";
  }

  public static Stringifier instance() {
    return INSTANCE;
  }
}
