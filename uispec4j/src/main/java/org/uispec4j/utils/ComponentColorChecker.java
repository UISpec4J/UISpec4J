package org.uispec4j.utils;

import java.awt.*;

public abstract class ComponentColorChecker {
  public final static ComponentColorChecker FOREGROUND = new ComponentColorChecker() {
    public Color getColor(Component component) {
      return component.getForeground();
    }
  };

  public final static ComponentColorChecker BACKGROUND = new ComponentColorChecker() {
    public Color getColor(Component component) {
      return component.getBackground();
    }
  };

  public abstract Color getColor(Component component);

  public void check(String errorMessage, Object expectedColor, Component component) {
    ColorUtils.assertEquals(errorMessage, expectedColor, getColor(component));

  }
}
